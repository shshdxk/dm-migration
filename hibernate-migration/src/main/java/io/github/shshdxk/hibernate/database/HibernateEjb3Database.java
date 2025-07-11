package io.github.shshdxk.hibernate.database;

import io.github.shshdxk.liquibase.Scope;
import io.github.shshdxk.liquibase.database.DatabaseConnection;
import io.github.shshdxk.liquibase.exception.DatabaseException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Database implementation for "ejb3" hibernate configurations.
 */
public class HibernateEjb3Database extends HibernateDatabase {

    /**
     * 实体管理器工厂实例，用于管理实体和查询数据库。
     */
    protected EntityManagerFactory entityManagerFactory;

    /**
     * 获取数据库的短名称标识符。
     * 
     * @return 返回表示Hibernate EJB3数据库的短名称"hibernateEjb3"
     */
    @Override
    public String getShortName() {
        return "hibernateEjb3";
    }

    /**
     * 获取默认的数据库产品名称。
     * 
     * @return 返回默认的数据库产品名称"Hibernate EJB3"
     */
    @Override
    protected String getDefaultDatabaseProductName() {
        return "Hibernate EJB3";
    }


    /**
     * 判断给定的数据库连接是否适用于当前的Hibernate EJB3数据库实现。
     * 
     * @param conn 要检查的数据库连接
     * @return 如果连接URL以"hibernate:ejb3:"开头则返回true，否则返回false
     * @throws DatabaseException 如果检查过程中发生数据库错误
     */
    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return conn.getURL().startsWith("hibernate:ejb3:");
    }

    /**
     * Calls {@link #createEntityManagerFactoryBuilder()} to create and save the entity manager factory.
     */
    @Override
    protected Metadata buildMetadataFromPath() throws DatabaseException {
        
        EntityManagerFactoryBuilderImpl builder = createEntityManagerFactoryBuilder();

        this.entityManagerFactory = builder.build();

        Metadata metadata = builder.getMetadata();
        
        String dialectString = findDialectName();
        if (dialectString != null) {
            try {
                dialect = (Dialect) Class.forName(dialectString).newInstance();
                Scope.getCurrentScope().getLog(getClass()).info("Using dialect " + dialectString);
            } catch (Exception e) {
                throw new DatabaseException(e);
            }
        } else {
            Scope.getCurrentScope().getLog(getClass()).info("Could not determine hibernate dialect, using HibernateGenericDialect");
            dialect = new HibernateGenericDialect();
        }

        return metadata;
    }

    /**
     * 创建EntityManagerFactoryBuilder实例。
     * 此方法配置必要的Hibernate属性并使用自定义的持久化提供者。
     * 
     * @return 配置好的EntityManagerFactoryBuilderImpl实例
     */
    protected EntityManagerFactoryBuilderImpl createEntityManagerFactoryBuilder() {
        MyHibernatePersistenceProvider persistenceProvider = new MyHibernatePersistenceProvider();

        Map<String, Object> properties = new HashMap<>();
        properties.put(HibernateDatabase.HIBERNATE_TEMP_USE_JDBC_METADATA_DEFAULTS, Boolean.FALSE.toString());
        properties.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, Boolean.FALSE.toString());
        properties.put(AvailableSettings.USE_NATIONALIZED_CHARACTER_DATA, getProperty(AvailableSettings.USE_NATIONALIZED_CHARACTER_DATA));

        final EntityManagerFactoryBuilderImpl builder = (EntityManagerFactoryBuilderImpl) persistenceProvider.getEntityManagerFactoryBuilderOrNull(getHibernateConnection().getPath(), properties, null);
        return builder;
    }

    /**
     * 获取指定名称的属性值。
     * 首先尝试从EntityManagerFactory中获取属性，如果不存在则回退到父类的获取方法。
     * 
     * @param name 要获取的属性名称
     * @return 属性值，如果属性不存在则返回父类获取的值
     */
    @Override
    public String getProperty(String name) {
        String property = null;
        if (entityManagerFactory != null) {
            property = (String) entityManagerFactory.getProperties().get(name);
        }

        if (property == null) {
            return super.getProperty(name);
        } else {
            return property;
        }

    }

    /**
     * 查找Hibernate方言的名称。
     * 首先尝试通过父类方法获取方言名称，如果获取不到则从EntityManagerFactory的属性中获取。
     * 
     * @return Hibernate方言的完全限定类名，如果未找到则返回null
     */
    @Override
    protected String findDialectName() {
        String dialectName = super.findDialectName();
        if (dialectName != null) {
            return dialectName;
        }

        return (String) entityManagerFactory.getProperties().get(AvailableSettings.DIALECT);
    }

    /**
     * Adds sources based on what is in the saved entityManagerFactory
     */
    @Override
    protected void configureSources(MetadataSources sources) throws DatabaseException {
        for (ManagedType<?> managedType : entityManagerFactory.getMetamodel().getManagedTypes()) {
            Class<?> javaType = managedType.getJavaType();
            if (javaType == null) {
                continue;
            }
            sources.addAnnotatedClass(javaType);
        }

        Package[] packages = Package.getPackages();
        for (Package p : packages) {
            sources.addPackage(p);
        }
    }

    /**
     * 自定义的Hibernate持久化提供者实现。
     * 主要用于修改持久化单元描述符的特定属性，以适应特定的配置需求。
     */
    private static class MyHibernatePersistenceProvider extends HibernatePersistenceProvider {

        /**
         * 使用反射机制设置对象的字段值。
         * 此方法会保存字段的原始访问状态，设置字段值后恢复原来的访问状态。
         * 
         * @param obj 要修改的对象
         * @param fieldName 要设置的字段名称
         * @param value 要设置的新值
         * @throws Exception 如果反射操作失败
         */
        private void setField(final Object obj, String fieldName, final Object value) throws Exception {
            final Field declaredField;

            declaredField = obj.getClass().getDeclaredField(fieldName);
            // 保存字段的原始访问状态
            boolean canAccess = declaredField.canAccess(obj);
            try {
                declaredField.setAccessible(true);
                declaredField.set(obj, value);
            } catch (Exception ex) {
                throw new IllegalStateException("Cannot invoke method get", ex);
            } finally {
                // 恢复字段的原始访问状态
                declaredField.setAccessible(canAccess);
            }
        }

        /**
         * 获取EntityManagerFactoryBuilder实例，如果无法创建则返回null。
         * 此方法重写父类方法但保持原有行为。
         * 
         * @param persistenceUnitName 持久化单元名称
         * @param properties 配置属性
         * @param providedClassLoader 提供的类加载器
         * @return EntityManagerFactoryBuilder实例，如果无法创建则返回null
         */
        @Override
        protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilderOrNull(String persistenceUnitName, Map properties, ClassLoader providedClassLoader) {
            return super.getEntityManagerFactoryBuilderOrNull(persistenceUnitName, properties, providedClassLoader);
        }

        /**
         * 获取EntityManagerFactoryBuilder实例。
         * 在调用父类方法前，修改持久化单元描述符以禁用JTA数据源并将事务类型设置为RESOURCE_LOCAL。
         * 
         * @param persistenceUnitDescriptor 持久化单元描述符
         * @param integration 集成配置
         * @param providedClassLoader 提供的类加载器
         * @return 配置好的EntityManagerFactoryBuilder实例
         */
        @Override
        protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilder(PersistenceUnitDescriptor persistenceUnitDescriptor, Map integration, ClassLoader providedClassLoader) {
            try {
                setField(persistenceUnitDescriptor, "jtaDataSource", null);
                setField(persistenceUnitDescriptor, "transactionType", PersistenceUnitTransactionType.RESOURCE_LOCAL);
            } catch (Exception ex) {
                Scope.getCurrentScope().getLog(getClass()).severe(null, ex);
            }
            return super.getEntityManagerFactoryBuilder(persistenceUnitDescriptor, integration, providedClassLoader);
        }
    }
}
