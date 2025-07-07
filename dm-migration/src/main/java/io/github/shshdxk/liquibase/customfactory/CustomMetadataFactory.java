package io.github.shshdxk.liquibase.customfactory;

import io.github.shshdxk.liquibase.database.HibernateDatabase;
import io.github.shshdxk.liquibase.database.connection.HibernateConnection;
import org.hibernate.boot.Metadata;

/**
 * Implement this interface to dynamically generate a hibernate:ejb3 configuration.
 * For example, if you create a class called com.example.hibernate.MyConfig, specify a url of hibernate:ejb3:com.example.hibernate.MyConfig.
 */
public interface CustomMetadataFactory {

    /*
     * Create a hibernate Configuration for the given database and connection.
     */
    Metadata getMetadata(HibernateDatabase hibernateDatabase, HibernateConnection connection);

}