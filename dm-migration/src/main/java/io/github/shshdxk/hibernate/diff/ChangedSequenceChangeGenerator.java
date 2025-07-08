package io.github.shshdxk.hibernate.diff;

import io.github.shshdxk.hibernate.database.HibernateDatabase;
import io.github.shshdxk.liquibase.change.Change;
import io.github.shshdxk.liquibase.database.Database;
import io.github.shshdxk.liquibase.diff.Difference;
import io.github.shshdxk.liquibase.diff.ObjectDifferences;
import io.github.shshdxk.liquibase.diff.output.DiffOutputControl;
import io.github.shshdxk.liquibase.diff.output.changelog.ChangeGeneratorChain;
import io.github.shshdxk.liquibase.structure.DatabaseObject;
import io.github.shshdxk.liquibase.structure.core.Sequence;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Hibernate manages sequences only by the name, startValue and incrementBy fields.
 * However, non-hibernate databases might return default values for other fields triggering false positives.
 */
public class ChangedSequenceChangeGenerator extends io.github.shshdxk.liquibase.diff.output.changelog.core.ChangedSequenceChangeGenerator {

    private static final Set<String> HIBERNATE_SEQUENCE_FIELDS;

    static {
        HashSet<String> hibernateSequenceFields = new HashSet<>();
        hibernateSequenceFields.add("name");
        hibernateSequenceFields.add("startValue");
        hibernateSequenceFields.add("incrementBy");
        HIBERNATE_SEQUENCE_FIELDS = Collections.unmodifiableSet(hibernateSequenceFields);
    }

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (Sequence.class.isAssignableFrom(objectType)) {
            return PRIORITY_ADDITIONAL;
        }
        return PRIORITY_NONE;
    }

    @Override
    public Change[] fixChanged(DatabaseObject changedObject, ObjectDifferences differences, DiffOutputControl control,
            Database referenceDatabase, Database comparisonDatabase, ChangeGeneratorChain chain) {
        if (!(referenceDatabase instanceof HibernateDatabase || comparisonDatabase instanceof HibernateDatabase)) {
            return super.fixChanged(changedObject, differences, control, referenceDatabase, comparisonDatabase, chain);
        }

        // if any of the databases is a hibernate database, remove all differences that affect a field not managed by hibernate
        Set<String> ignoredDifferenceFields = differences.getDifferences().stream()
                .map(Difference::getField)
                .filter(differenceField ->  !HIBERNATE_SEQUENCE_FIELDS.contains(differenceField))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        ignoredDifferenceFields.forEach(differences::removeDifference);
        return super.fixChanged(changedObject, differences, control, referenceDatabase, comparisonDatabase, chain);
    }

}
