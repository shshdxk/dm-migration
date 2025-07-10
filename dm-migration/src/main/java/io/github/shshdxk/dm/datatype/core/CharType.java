package io.github.shshdxk.dm.datatype.core;


import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;

@DataTypeInfo(name="char", aliases = {"java.sql.Types.CHAR", "bpchar", "character"}, minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class CharType extends liquibase.datatype.core.CharType {
    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        return super.toDatabaseDataType(database);
    }

}
