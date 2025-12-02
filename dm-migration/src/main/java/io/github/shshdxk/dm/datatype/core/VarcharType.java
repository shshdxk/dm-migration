package io.github.shshdxk.dm.datatype.core;


import io.github.shshdxk.dm.database.core.DmDatabase;
import io.github.shshdxk.liquibase.database.Database;
import io.github.shshdxk.liquibase.datatype.DataTypeInfo;
import io.github.shshdxk.liquibase.datatype.DatabaseDataType;
import io.github.shshdxk.liquibase.datatype.LiquibaseDataType;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

@DataTypeInfo(name="varchar", aliases = {"java.sql.Types.VARCHAR", "java.lang.String", "varchar2", "character varying"}, minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class VarcharType extends io.github.shshdxk.liquibase.datatype.core.VarcharType {
    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        String originalDefinition = StringUtils.trimToEmpty(getRawDefinition());
        if ((database instanceof DmDatabase)) {
            int size = getSize();
            if (originalDefinition.toLowerCase(Locale.US).startsWith("varchar") && size > 0) {
                return new DatabaseDataType("VARCHAR", size + " CHAR");
            }
        }
        return super.toDatabaseDataType(database);
    }

    @Override
    protected int getSize() {
        if (this.getParameters().length == 0) {
            return -1;
        } else if (this.getParameters()[0] instanceof String) {
            return Integer.parseInt(((String) this.getParameters()[0]).split(" ")[0]);
        } else {
            return this.getParameters()[0] instanceof Number ? ((Number)this.getParameters()[0]).intValue() : -1;
        }
    }

}
