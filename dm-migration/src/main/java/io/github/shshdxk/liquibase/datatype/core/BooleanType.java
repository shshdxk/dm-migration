package io.github.shshdxk.liquibase.datatype.core;

import io.github.shshdxk.liquibase.database.core.DmDatabase;
import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.util.StringUtil;

import java.util.Locale;

@DataTypeInfo(name = "boolean", aliases = {"java.sql.Types.BOOLEAN", "java.lang.Boolean", "bit", "bool"}, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class BooleanType extends liquibase.datatype.core.BooleanType {

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
}
