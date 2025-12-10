package io.github.shshdxk.hibernate.snapshot;

import io.github.shshdxk.hibernate.database.HibernateDatabase;
import io.github.shshdxk.liquibase.Scope;
import io.github.shshdxk.liquibase.datatype.DataTypeFactory;
import io.github.shshdxk.liquibase.datatype.core.UnknownType;
import io.github.shshdxk.liquibase.exception.DatabaseException;
import io.github.shshdxk.liquibase.snapshot.DatabaseSnapshot;
import io.github.shshdxk.liquibase.snapshot.InvalidExampleException;
import io.github.shshdxk.liquibase.snapshot.SnapshotGenerator;
import io.github.shshdxk.liquibase.statement.DatabaseFunction;
import io.github.shshdxk.liquibase.structure.DatabaseObject;
import io.github.shshdxk.liquibase.structure.core.Column;
import io.github.shshdxk.liquibase.structure.core.DataType;
import io.github.shshdxk.liquibase.structure.core.Relation;
import io.github.shshdxk.liquibase.structure.core.Table;
import io.github.shshdxk.liquibase.util.SqlUtil;
import io.github.shshdxk.liquibase.util.StringUtil;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NativeGenerator;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.models.annotations.internal.GeneratedValueJpaAnnotation;
import org.hibernate.boot.models.annotations.internal.NativeGeneratorAnnotation;
import org.hibernate.boot.models.annotations.internal.SequenceGeneratorJpaAnnotation;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.mapping.BasicValue;
import org.hibernate.mapping.GeneratorCreator;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.models.internal.jdk.JdkFieldDetails;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Columns are snapshotted along with with Tables in {@link TableSnapshotGenerator} but this class needs to be here to keep the default ColumnSnapshotGenerator from running.
 * Ideally the column logic would be moved out of the TableSnapshotGenerator to better work in situations where the object types to snapshot are being controlled, but that is not the case yet.
 */
public class ColumnSnapshotGenerator extends HibernateSnapshotGenerator {

    private static final String SQL_TIMEZONE_SUFFIX = "with time zone";
    private static final String LIQUIBASE_TIMEZONE_SUFFIX = "with timezone";

    private final static Pattern pattern = Pattern.compile("([^\\(]*)\\s*\\(?\\s*(\\d*)?\\s*,?\\s*(\\d*)?\\s*([^\\(]*?)\\)?");

    public ColumnSnapshotGenerator() {
        super(Column.class, new Class[]{Table.class});
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        Column column = (Column) example;
        if (column.getType() == null) { //not the actual full version found with the table
            if (column.getRelation() == null) {
                throw new InvalidExampleException("No relation set on " + column);
            }
            Relation relation = snapshot.get(column.getRelation());
            if (relation != null) {
                for (Column columnSnapshot : relation.getColumns()) {
                    if (columnSnapshot.getName().equalsIgnoreCase(column.getName())) {
                        return columnSnapshot;
                    }
                }
            }
            snapshotColumn((Column) example, snapshot);
            return example; //did not find it
        } else {
            return example;
        }
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        if (foundObject instanceof Table) {
            org.hibernate.mapping.Table hibernateTable = findHibernateTable(foundObject, snapshot);
            if (hibernateTable == null) {
                return;
            }

            for (org.hibernate.mapping.Column hibernateColumn: hibernateTable.getColumns()) {
                Column column = new Column();
                column.setName(hibernateColumn.getName());
                column.setRelation((Table) foundObject);

                snapshotColumn(column, snapshot);


                ((Table) foundObject).getColumns().add(column);

            }
        }
    }

    protected void snapshotColumn(Column column, DatabaseSnapshot snapshot) throws DatabaseException {
        HibernateDatabase database = (HibernateDatabase) snapshot.getDatabase();

        org.hibernate.mapping.Table hibernateTable = findHibernateTable(column.getRelation(), snapshot);
        if (hibernateTable == null) {
            return;
        }

        Dialect dialect = database.getDialect();
        Metadata metadata = database.getMetadata();

        for (org.hibernate.mapping.Column hibernateColumn: hibernateTable.getColumns()) {
            if (hibernateColumn.getName().equalsIgnoreCase(column.getName())) {

                String defaultValue = null;
                String hibernateType = hibernateColumn.getSqlType(metadata);
                Integer dataTypeId = hibernateColumn.getSqlTypeCode();
                if (hibernateColumn.getValue().getType().getName().toLowerCase().contains("double")) {
                    if (dialect instanceof MySQLDialect) {
                        hibernateType = "double";
                    }
                    dataTypeId = Types.DOUBLE;
                } else {
                    Matcher defaultValueMatcher = Pattern.compile("(?i) DEFAULT\\s+(.*)").matcher(hibernateType);
                    if (defaultValueMatcher.find()) {
                        defaultValue = defaultValueMatcher.group(1);
                        hibernateType = hibernateType.replace(defaultValueMatcher.group(0), "");
                    }
                }

                DataType dataType = toDataType(hibernateType, dataTypeId);
                if (dataType == null) {
                    throw new DatabaseException("Unable to find column data type for column " + hibernateColumn.getName());
                }

                column.setType(dataType);
                Scope.getCurrentScope().getLog(getClass()).info("Found column " + column.getName() + " " + column.getType().toString());

                column.setRemarks(hibernateColumn.getComment());
                if (hibernateColumn.getValue() instanceof SimpleValue) {
                    DataType parseType;
                    if (DataTypeFactory.getInstance().from(dataType, database) instanceof UnknownType) {
                        parseType = new DataType(((SimpleValue) hibernateColumn.getValue()).getTypeName());
                    } else {
                        parseType = dataType;
                    }

                    if (defaultValue == null) {
                        defaultValue = hibernateColumn.getDefaultValue();
                    }

                    column.setDefaultValue(SqlUtil.parseValue(
                            snapshot.getDatabase(),
                            defaultValue,
                            parseType));
                } else {
                    column.setDefaultValue(hibernateColumn.getDefaultValue());
                }
                column.setNullable(hibernateColumn.isNullable());
                column.setCertainDataType(false);

                org.hibernate.mapping.PrimaryKey hibernatePrimaryKey = hibernateTable.getPrimaryKey();
                if (hibernatePrimaryKey != null) {
                    boolean isPrimaryKeyColumn = false;
                    for (org.hibernate.mapping.Column pkColumn : hibernatePrimaryKey.getColumns()) {
                        if (pkColumn.getName().equalsIgnoreCase(hibernateColumn.getName())) {
                            isPrimaryKeyColumn = true;
                            break;
                        }
                    }

                    if (isPrimaryKeyColumn && hibernateColumn.getValue() instanceof BasicValue basicValue) {
                        GeneratorCreator generatorCreator = basicValue.getCustomIdGeneratorCreator();
                        if (generatorCreator == null) {
                            continue;
                        }
                        SequenceGeneratorJpaAnnotation sequenceGeneratorJpaAnnotation = null;
                        boolean isAutoIncrement = false;
                        Class<?> clazz = generatorCreator.getClass();

                        Field[] fields = clazz.getDeclaredFields();

                        for (Field field : fields) {
                            boolean canAccess = field.canAccess(generatorCreator);
                            field.setAccessible(true);
                            try {
                                Object value = field.get(generatorCreator);
                                if (value instanceof org.hibernate.models.internal.jdk.JdkFieldDetails) {
                                    for (Map.Entry<Class<? extends Annotation>, ? extends Annotation> entry : ((JdkFieldDetails) value).getUsageMap().entrySet()) {
                                        Class<? extends Annotation> key = entry.getKey();
                                        if (database.supportsAutoIncrement()) {
                                            if (key == GeneratedValue.class) {
                                                if (entry.getValue() instanceof GeneratedValueJpaAnnotation annotation &&
                                                        (annotation.strategy() == GenerationType.AUTO || annotation.strategy() == GenerationType.IDENTITY)) {
                                                    isAutoIncrement = true;
                                                }
                                            }
                                        }
                                        if (database.supportsSequences()) {
                                            if (key == SequenceGenerator.class) {
                                                if (entry.getValue() instanceof SequenceGeneratorJpaAnnotation) {
                                                    sequenceGeneratorJpaAnnotation = (SequenceGeneratorJpaAnnotation) entry.getValue();
                                                }
                                            }
                                            if (key == NativeGenerator.class) {
                                                if (entry.getValue() instanceof NativeGeneratorAnnotation nativeGenerator
                                                        && StringUtils.isNotBlank(nativeGenerator.sequenceForm().sequenceName())) {
                                                    sequenceGeneratorJpaAnnotation = new SequenceGeneratorJpaAnnotation(nativeGenerator.sequenceForm(), null);
                                                }
                                            }
                                        }
                                    }
                                }
                                if (value instanceof String arg) {
                                    if (("native".equalsIgnoreCase(arg) || "identity".equalsIgnoreCase(arg))) {
                                        if (PostgreSQLDialect.class.isAssignableFrom(dialect.getClass())) {
                                            column.setAutoIncrementInformation(new Column.AutoIncrementInformation());
                                            String sequenceName = (column.getRelation().getName() + "_" + column.getName() + "_seq").toLowerCase();
                                            column.setDefaultValue(new DatabaseFunction("nextval('" + sequenceName + "'::regclass)"));
                                        } else if (database.supportsAutoIncrement()) {
                                            column.setAutoIncrementInformation(new Column.AutoIncrementInformation());
                                        }
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                Scope.getCurrentScope().getLog(ColumnSnapshotGenerator.class).info(e.getLocalizedMessage(), e);
                            } finally {
                                field.setAccessible(canAccess);
                            }
                        }

                        if (sequenceGeneratorJpaAnnotation == null) {
                            if (database.supportsAutoIncrement() && isAutoIncrement) {
                                column.setAutoIncrementInformation(new Column.AutoIncrementInformation());
                            }
                        } else {
                            if (PostgreSQLDialect.class.isAssignableFrom(dialect.getClass())) {
                                String sequenceName = sequenceGeneratorJpaAnnotation.sequenceName().toLowerCase();
                                column.setDefaultValue(new DatabaseFunction("nextval('" + sequenceName + "'::regclass)"));
                            } else {
                                column.setAutoIncrementInformation(new Column.AutoIncrementInformation(
                                        sequenceGeneratorJpaAnnotation.initialValue(), sequenceGeneratorJpaAnnotation.allocationSize()));
                            }
                        }
                        column.setNullable(false);
                    }
                }
                return;
            }
        }
    }

    protected DataType toDataType(String hibernateType, Integer sqlTypeCode) {
        Matcher matcher = pattern.matcher(hibernateType);
        if (!matcher.matches()) {
            return null;
        }

        String typeName = matcher.group(1);

        // Liquibase seems to use 'with timezone' instead of 'with time zone',
        // so we remove any 'with time zone' suffixes here.
        // The corresponding 'with timezone' suffix will then be added below,
        // because in that case hibernateType also ends with 'with time zone'.
        if (typeName.toLowerCase().endsWith(SQL_TIMEZONE_SUFFIX)) {
            typeName = typeName.substring(0, typeName.length() - SQL_TIMEZONE_SUFFIX.length()).stripTrailing();
        }

        // If hibernateType ends with 'with time zone' we need to add the corresponding
        // 'with timezone' suffix to the Liquibase type.
        if (hibernateType.toLowerCase().endsWith(SQL_TIMEZONE_SUFFIX)) {
            typeName += (" " + LIQUIBASE_TIMEZONE_SUFFIX);
        }

        DataType dataType = new DataType(typeName);
        if (matcher.group(3).isEmpty()) {
            if (!matcher.group(2).isEmpty()) {
                dataType.setColumnSize(Integer.parseInt(matcher.group(2)));
            }
        } else {
            dataType.setColumnSize(Integer.parseInt(matcher.group(2)));
            dataType.setDecimalDigits(Integer.parseInt(matcher.group(3)));
        }

        String extra = StringUtil.trimToNull(matcher.group(4));
        if (extra != null) {
            if (extra.equalsIgnoreCase("char")) {
                dataType.setColumnSizeUnit(DataType.ColumnSizeUnit.CHAR);
            }
        }

        Scope.getCurrentScope().getLog(getClass()).info("Converted column data type - hibernate type: " + hibernateType + ", SQL type: " + sqlTypeCode + ", type name: " + typeName);

        dataType.setDataTypeId(sqlTypeCode);
        return dataType;
    }

    @Override
    public Class<? extends SnapshotGenerator>[] replaces() {
        return new Class[]{io.github.shshdxk.liquibase.snapshot.jvm.ColumnSnapshotGenerator.class};
    }

}
