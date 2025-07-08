package io.github.shshdxk.dm.datatype.core;

import io.github.shshdxk.liquibase.database.Database;
import io.github.shshdxk.dm.database.core.DmDatabase;
import io.github.shshdxk.liquibase.datatype.DataTypeInfo;
import io.github.shshdxk.liquibase.datatype.DatabaseDataType;
import io.github.shshdxk.liquibase.datatype.LiquibaseDataType;
import io.github.shshdxk.liquibase.util.StringUtil;

import java.util.Locale;

@DataTypeInfo(name = "clob", aliases = {"longvarchar", "text", "longtext", "java.sql.Types.LONGVARCHAR", "java.sql.Types.CLOB", "nclob", "longnvarchar", "ntext", "java.sql.Types.LONGNVARCHAR", "java.sql.Types.NCLOB", "tinytext", "mediumtext"}, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class ClobType extends io.github.shshdxk.liquibase.datatype.core.ClobType {

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {

        String originalDefinition = StringUtil.trimToEmpty(getRawDefinition());
        if ((database instanceof DmDatabase)) {
            if (originalDefinition.toLowerCase(Locale.US).startsWith("text")) {
                return new DatabaseDataType("TEXT");
            }
        }
        return super.toDatabaseDataType(database);
    }

}
