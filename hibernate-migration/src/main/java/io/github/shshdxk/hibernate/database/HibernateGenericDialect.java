package io.github.shshdxk.hibernate.database;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;

import io.github.shshdxk.liquibase.exception.DatabaseException;

/**
 * Generic hibernate dialect used when an actual dialect cannot be determined.
 */
public class HibernateGenericDialect extends Dialect {
    public HibernateGenericDialect() throws DatabaseException {
        super(DatabaseVersion.make( 6, 1 ));
    }
}
