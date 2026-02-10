package io.github.shshdxk.dm.database.core;

import io.github.shshdxk.liquibase.Scope;
import io.github.shshdxk.liquibase.database.DatabaseConnection;
import io.github.shshdxk.liquibase.database.core.PostgresDatabase;
import io.github.shshdxk.liquibase.exception.DatabaseException;
import io.github.shshdxk.liquibase.logging.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates Kingbase database support.
 */
public class KingbaseDatabase extends PostgresDatabase {

    public static final String PRODUCT_NAME = "KingbaseES";

    private static final Logger LOG = Scope.getCurrentScope().getLog(KingbaseDatabase.class);

    private final Set<String> systemTablesAndViews = new HashSet<>();

    private final Set<String> reservedWords = new HashSet<>();

    public KingbaseDatabase() {
        reservedWords.addAll(Arrays.asList("ALL", "ANALYSE", "ANALYZE", "AND", "ANY", "ARRAY", "AS", "ASC",
                "ASYMMETRIC", "AUTHORIZATION", "BINARY", "BOTH", "CASE", "CAST", "CHECK", "COLLATE", "COLLATION",
                "COLUMN", "CONCURRENTLY", "CONSTRAINT", "CREATE", "CROSS", "CURRENT_CATALOG", "CURRENT_DATE",
                "CURRENT_ROLE", "CURRENT_SCHEMA", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "DEFAULT",
                "DEFERRABLE", "DESC", "DISTINCT", "DO", "ELSE", "END", "EXCEPT", "FALSE", "FETCH", "FOR", "FOREIGN",
                "FREEZE", "FROM", "FULL", "GRANT", "GROUP", "HAVING", "ILIKE", "IN", "INITIALLY", "INNER", "INTERSECT",
                "INTO", "IS", "ISNULL", "JOIN", "LATERAL", "LEADING", "LEFT", "LIKE", "LIMIT", "LOCALTIME",
                "LOCALTIMESTAMP", "NATURAL", "NOT", "NOTNULL", "NULL", "OFFSET", "ON", "ONLY", "OR", "ORDER", "OUTER",
                "OVERLAPS", "PLACING", "PRIMARY", "REFERENCES", "RETURNING", "RIGHT", "SELECT", "SESSION_USER",
                "SIMILAR", "SOME", "SYMMETRIC", "TABLE", "TABLESAMPLE", "THEN", "TO", "TRAILING", "TRUE", "UNION",
                "UNIQUE", "USER", "USING", "VARIADIC", "VERBOSE", "WHEN", "WHERE", "WINDOW", "WITH", "LEVEL"));
//        super.unmodifiableDataTypes.addAll(Arrays.asList("bool", "int4", "int8", "bigserial", "serial", "oid", "bytea", "date", "timestamptz", "text", "int2[]", "int4[]", "int8[]", "float4[]", "float8[]", "bool[]", "varchar[]", "text[]", "numeric[]"));
        systemTablesAndViews.addAll(Arrays.asList("sys_stat_statements", "sys_stat_statements_all"));
    }


    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        if (!PRODUCT_NAME.equalsIgnoreCase(conn.getDatabaseProductName())) {
            return false;
        }

        int majorVersion = conn.getDatabaseMajorVersion();
        int minorVersion = conn.getDatabaseMinorVersion();

        if ((majorVersion < MINIMUM_DBMS_MAJOR_VERSION) || ((majorVersion == MINIMUM_DBMS_MAJOR_VERSION) &&
                (minorVersion < MINIMUM_DBMS_MINOR_VERSION))) {
            LOG.warning(
                    String.format(
                            "Your Kingbase software version (%d.%d) seems to indicate that your software is " +
                                    "older than %d.%d. This means that you might encounter strange behaviour and " +
                                    "incorrect error messages.", majorVersion, minorVersion, MINIMUM_DBMS_MAJOR_VERSION, MINIMUM_DBMS_MINOR_VERSION));
            return true;
        }

        return true;
    }

    @Override
    public Set<String> getSystemViews() {
        return systemTablesAndViews;
    }

    @Override
    public boolean isReservedWord(String tableName) {
        return reservedWords.contains(tableName.toUpperCase());
    }

    @Override
    public String getShortName() {
        return "kingbase";
    }

    @Override
    protected String getDefaultDatabaseProductName() {
        return "Kingbase";
    }

    @Override
    public Integer getDefaultPort() {
        return 54321;
    }

    @Override
    public String getDefaultDriver(String url) {
        if (url.startsWith("jdbc:kingbase8:")) {
            return "com.kingbase8.Driver";
        }
        return null;
    }


}
