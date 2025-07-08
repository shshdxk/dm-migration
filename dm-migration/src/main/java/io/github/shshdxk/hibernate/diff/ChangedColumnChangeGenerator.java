package io.github.shshdxk.hibernate.diff;

import io.github.shshdxk.hibernate.database.HibernateDatabase;
import io.github.shshdxk.liquibase.change.Change;
import io.github.shshdxk.liquibase.database.Database;
import io.github.shshdxk.liquibase.diff.Difference;
import io.github.shshdxk.liquibase.diff.ObjectDifferences;
import io.github.shshdxk.liquibase.diff.output.DiffOutputControl;
import io.github.shshdxk.liquibase.statement.DatabaseFunction;
import io.github.shshdxk.liquibase.structure.DatabaseObject;
import io.github.shshdxk.liquibase.structure.core.Column;

import java.util.List;

/**
 * Hibernate and database types tend to look different even though they are not.
 * There are enough false positives that it works much better to suppress all column changes based on types.
 */
public class ChangedColumnChangeGenerator extends io.github.shshdxk.liquibase.diff.output.changelog.core.ChangedColumnChangeGenerator {

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (Column.class.isAssignableFrom(objectType)) {
            return PRIORITY_ADDITIONAL;
        }
        return PRIORITY_NONE;
    }

    @Override
    protected void handleTypeDifferences(Column column, ObjectDifferences differences, DiffOutputControl control, List<Change> changes, Database referenceDatabase, Database comparisonDatabase) {
        if (referenceDatabase instanceof HibernateDatabase && comparisonDatabase instanceof HibernateDatabase) {
            // do nothing, types tend to not match with hibernate
        } else {
            super.handleTypeDifferences(column, differences, control, changes, referenceDatabase, comparisonDatabase);
        }
    }

    @Override
    protected void handleDefaultValueDifferences(Column column, ObjectDifferences differences, DiffOutputControl control, List<Change> changes, Database referenceDatabase, Database comparisonDatabase) {
        if (referenceDatabase instanceof HibernateDatabase || comparisonDatabase instanceof HibernateDatabase) {
            Difference difference = differences.getDifference("defaultValue");
            if (difference != null) {
                if (difference.getReferenceValue() == null && difference.getComparedValue() instanceof DatabaseFunction) {
                    //database sometimes adds a function default value, like for timestamp columns
                    return;
                }
            }
            // do nothing, types tend to not match with hibernate
        }
            super.handleDefaultValueDifferences(column, differences, control, changes, referenceDatabase, comparisonDatabase);
    }
}
