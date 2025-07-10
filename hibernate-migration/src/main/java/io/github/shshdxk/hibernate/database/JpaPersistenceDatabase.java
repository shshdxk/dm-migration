package io.github.shshdxk.hibernate.database;

import io.github.shshdxk.hibernate.database.connection.HibernateDriver;
import jakarta.persistence.spi.PersistenceUnitInfo;
import io.github.shshdxk.liquibase.database.DatabaseConnection;
import io.github.shshdxk.liquibase.exception.DatabaseException;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.spi.Bootstrap;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;

import java.util.Map;

/**
 * Database implementation for JPA configurations.
 * This supports passing a JPA persistence XML file reference.
 */
public class JpaPersistenceDatabase extends HibernateEjb3Database {

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return conn.getURL().startsWith("jpa:persistence:");
    }

    @Override
    public String getDefaultDriver(String url) {
        if (url.startsWith("jpa:persistence:")) {
            return HibernateDriver.class.getName();
        }
        return null;
    }

    @Override
    public String getShortName() {
        return "jpaPersistence";
    }

    @Override
    protected String getDefaultDatabaseProductName() {
        return "JPA Persistence";
    }

    @Override
    protected EntityManagerFactoryBuilderImpl createEntityManagerFactoryBuilder() {
        DefaultPersistenceUnitManager internalPersistenceUnitManager = new DefaultPersistenceUnitManager();

        internalPersistenceUnitManager.setPersistenceXmlLocation(getHibernateConnection().getPath());
        internalPersistenceUnitManager.setDefaultPersistenceUnitRootLocation(null);

        internalPersistenceUnitManager.preparePersistenceUnitInfos();
        PersistenceUnitInfo persistenceUnitInfo = internalPersistenceUnitManager.obtainDefaultPersistenceUnitInfo();

        EntityManagerFactoryBuilderImpl builder = (EntityManagerFactoryBuilderImpl) Bootstrap.getEntityManagerFactoryBuilder(persistenceUnitInfo, Map.of(
                HibernateDatabase.HIBERNATE_TEMP_USE_JDBC_METADATA_DEFAULTS, Boolean.FALSE.toString()));
        return builder;
    }

}
