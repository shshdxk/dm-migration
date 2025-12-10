package io.github.shshdxk.hibernate.snapshot;

import io.github.shshdxk.hibernate.database.HibernateDatabase;
import io.github.shshdxk.liquibase.exception.DatabaseException;
import io.github.shshdxk.liquibase.snapshot.DatabaseSnapshot;
import io.github.shshdxk.liquibase.snapshot.InvalidExampleException;
import io.github.shshdxk.liquibase.snapshot.SnapshotGenerator;
import io.github.shshdxk.liquibase.structure.DatabaseObject;
import io.github.shshdxk.liquibase.structure.core.Schema;
import io.github.shshdxk.liquibase.structure.core.Sequence;

import java.math.BigInteger;

/**
 * Sequence snapshots are not yet supported, but this class needs to be implemented in order to prevent the default SequenceSnapshotGenerator from running.
 */
public class SequenceSnapshotGenerator extends HibernateSnapshotGenerator {

    public SequenceSnapshotGenerator() {
        super(Sequence.class, new Class[]{Schema.class});
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        return example;
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        if (!snapshot.getSnapshotControl().shouldInclude(Sequence.class)) {
            return;
        }

        if (foundObject instanceof Schema schema) {
            HibernateDatabase database = (HibernateDatabase) snapshot.getDatabase();
            for (org.hibernate.boot.model.relational.Namespace namespace : database.getMetadata().getDatabase().getNamespaces()) {
                for (org.hibernate.boot.model.relational.Sequence sequence : namespace.getSequences()) {
                    schema.addDatabaseObject(new Sequence()
                            .setName(sequence.getName().getSequenceName().getText())
                            .setSchema(schema)
                            .setStartValue(BigInteger.valueOf(sequence.getInitialValue()))
                            .setIncrementBy(BigInteger.valueOf(sequence.getIncrementSize()))
                    );
                }
            }
        }
    }

    @Override
    public Class<? extends SnapshotGenerator>[] replaces() {
        return new Class[]{io.github.shshdxk.liquibase.snapshot.jvm.SequenceSnapshotGenerator.class};
    }
}
