package io.github.shshdxk.dm.datatype.core;


import io.github.shshdxk.liquibase.database.Database;
import io.github.shshdxk.liquibase.datatype.DataTypeInfo;
import io.github.shshdxk.liquibase.datatype.DatabaseDataType;
import io.github.shshdxk.liquibase.datatype.LiquibaseDataType;

@DataTypeInfo(name="varchar", aliases = {"java.sql.Types.VARCHAR", "java.lang.String", "varchar2", "character varying"}, minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class VarcharType extends io.github.shshdxk.liquibase.datatype.core.VarcharType {
    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
//        String originalDefinition = StringUtil.trimToEmpty(getRawDefinition());
//        if ((database instanceof DmDatabase)) {
//            if (originalDefinition.toLowerCase(Locale.US).startsWith("text")) {
//                return new DatabaseDataType("TEXT");
//            }
//        }
        return super.toDatabaseDataType(database);
    }

}
