package io.github.shshdxk.hibernate.diff;

import io.github.shshdxk.liquibase.change.Change;
import io.github.shshdxk.liquibase.database.Database;
import io.github.shshdxk.hibernate.database.HibernateDatabase;
import io.github.shshdxk.liquibase.diff.output.DiffOutputControl;
import io.github.shshdxk.liquibase.diff.output.changelog.ChangeGeneratorChain;
import io.github.shshdxk.liquibase.structure.DatabaseObject;
import io.github.shshdxk.liquibase.structure.core.Sequence;

public class MissingSequenceChangeGenerator extends io.github.shshdxk.liquibase.diff.output.changelog.core.MissingSequenceChangeGenerator {

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (Sequence.class.isAssignableFrom(objectType)) {
            return PRIORITY_ADDITIONAL;
        }
        return PRIORITY_NONE;
    }

    @Override
    public Change[] fixMissing(DatabaseObject missingObject, DiffOutputControl control, Database referenceDatabase, Database comparisonDatabase, ChangeGeneratorChain chain) {
        if (referenceDatabase instanceof HibernateDatabase && !comparisonDatabase.supportsSequences()) {
            return null;
        } else if (comparisonDatabase instanceof HibernateDatabase && !referenceDatabase.supportsSequences()) {
            return null;
        }else {
            return super.fixMissing(missingObject, control, referenceDatabase, comparisonDatabase, chain);
        }
    }
}
