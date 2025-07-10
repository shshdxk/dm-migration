package io.github.shshdxk.dm.datatype.core;

import io.github.shshdxk.dm.database.core.DmDatabase;
import io.github.shshdxk.liquibase.database.Database;
import io.github.shshdxk.liquibase.datatype.DataTypeInfo;
import io.github.shshdxk.liquibase.datatype.DatabaseDataType;
import io.github.shshdxk.liquibase.datatype.LiquibaseDataType;
import io.github.shshdxk.liquibase.util.StringUtil;

import java.util.Locale;

@DataTypeInfo(name = "boolean", aliases = {"java.sql.Types.BOOLEAN", "java.lang.Boolean", "bit", "bool"}, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class BooleanType extends io.github.shshdxk.liquibase.datatype.core.BooleanType {

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        String originalDefinition = StringUtil.trimToEmpty(getRawDefinition());
        if ((database instanceof DmDatabase)) {
            if (originalDefinition.toLowerCase(Locale.US).startsWith("bit")) {
                return new DatabaseDataType("BIT", getParameters());
            }
            return new DatabaseDataType("TINYINT");
        }
        return super.toDatabaseDataType(database);
    }

    @Override
    public String objectToSql(Object value, Database database) {
        if ((value == null) || "null".equals(value.toString().toLowerCase(Locale.US))) {
            return null;
        }
        if (database instanceof DmDatabase) {
            if (value instanceof Boolean) {
                if (((Boolean) value)) {
                    return "1";
                } else {
                    return "0";
                }
            }
        }
        return super.objectToSql(value, database);
    }


}
