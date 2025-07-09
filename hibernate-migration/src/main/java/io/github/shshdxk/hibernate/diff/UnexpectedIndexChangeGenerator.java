package io.github.shshdxk.hibernate.diff;

import io.github.shshdxk.hibernate.database.HibernateDatabase;
import io.github.shshdxk.liquibase.change.Change;
import io.github.shshdxk.liquibase.database.Database;
import io.github.shshdxk.liquibase.diff.output.DiffOutputControl;
import io.github.shshdxk.liquibase.diff.output.changelog.ChangeGeneratorChain;
import io.github.shshdxk.liquibase.structure.DatabaseObject;
import io.github.shshdxk.liquibase.structure.core.Index;

/**
 * Indexes tend to be added in the database that don't correspond to what is in Hibernate, so we suppress all dropIndex changes
 * based on indexes defined in the database but not in hibernate.
 */
public class UnexpectedIndexChangeGenerator extends io.github.shshdxk.liquibase.diff.output.changelog.core.UnexpectedIndexChangeGenerator {

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (Index.class.isAssignableFrom(objectType)) {
            return PRIORITY_ADDITIONAL;
        }
        return PRIORITY_NONE;
    }

    @Override
    public Change[] fixUnexpected(DatabaseObject unexpectedObject, DiffOutputControl control, Database referenceDatabase, Database comparisonDatabase, ChangeGeneratorChain chain) {
        if (referenceDatabase instanceof HibernateDatabase || comparisonDatabase instanceof HibernateDatabase) {
            return null;
        } else {
            return super.fixUnexpected(unexpectedObject, control, referenceDatabase, comparisonDatabase, chain);
        }
    }
}
