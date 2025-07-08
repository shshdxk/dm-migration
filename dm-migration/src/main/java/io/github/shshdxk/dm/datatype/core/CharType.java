package io.github.shshdxk.dm.datatype.core;

import io.github.shshdxk.liquibase.database.Database;
import io.github.shshdxk.liquibase.datatype.DataTypeInfo;
import io.github.shshdxk.liquibase.datatype.DatabaseDataType;
import io.github.shshdxk.liquibase.datatype.LiquibaseDataType;

@DataTypeInfo(name="char", aliases = {"java.sql.Types.CHAR", "bpchar", "character"}, minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class CharType extends io.github.shshdxk.liquibase.datatype.core.CharType {
    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        return super.toDatabaseDataType(database);
    }

}
