package io.github.shshdxk.hibernate.snapshot;

import io.github.shshdxk.liquibase.exception.DatabaseException;
import io.github.shshdxk.liquibase.snapshot.DatabaseSnapshot;
import io.github.shshdxk.liquibase.snapshot.InvalidExampleException;
import io.github.shshdxk.liquibase.structure.DatabaseObject;
import io.github.shshdxk.liquibase.structure.core.Catalog;


/**
 * Hibernate doesn't really support Catalogs, so just return the passed example back as if it had all the info it needed.
 */
public class CatalogSnapshotGenerator extends HibernateSnapshotGenerator {

    public CatalogSnapshotGenerator() {
        super(Catalog.class);
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        return new Catalog(snapshot.getDatabase().getDefaultCatalogName()).setDefault(true);
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        // Nothing to add to
    }

}
