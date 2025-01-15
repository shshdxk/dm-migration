package io.github.shshdxk;

import io.github.shshdxk.liquibase.database.core.DmDatabase;
import liquibase.database.DatabaseFactory;

public class liquibaseConfiguration {

    public liquibaseConfiguration() {
        DatabaseFactory.getInstance().register(new DmDatabase());
    }
}
