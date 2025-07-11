package io.github.shshdxk.hibernate.snapshot;

import io.github.shshdxk.liquibase.exception.DatabaseException;
import io.github.shshdxk.liquibase.snapshot.DatabaseSnapshot;
import io.github.shshdxk.liquibase.snapshot.InvalidExampleException;
import io.github.shshdxk.liquibase.snapshot.SnapshotGenerator;
import io.github.shshdxk.liquibase.structure.DatabaseObject;
import io.github.shshdxk.liquibase.structure.core.Catalog;


/**
 * Hibernate doesn't really support Catalogs, so just return the passed example back as if it had all the info it needed.
 */
public class CatalogSnapshotGenerator extends HibernateSnapshotGenerator {

    /**
     * 构造函数，调用父类构造器并传入Catalog类作为参数。
     * 指定此生成器处理的数据库对象类型为Catalog。
     */
    public CatalogSnapshotGenerator() {
        super(Catalog.class);
    }

    /**
     * 从数据库快照中获取Catalog对象信息。
     * 由于Hibernate不支持Catalog，此方法简单地返回一个使用默认目录名称的Catalog对象并将其标记为默认。
     *
     * @param example  请求快照的数据库对象示例
     * @param snapshot 当前的数据库快照
     * @return 包含默认目录信息的Catalog对象
     * @throws DatabaseException 如果在访问数据库时发生错误
     * @throws InvalidExampleException 如果提供的示例无效
     */
    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        return new Catalog(snapshot.getDatabase().getDefaultCatalogName()).setDefault(true);
    }

    /**
     * 向找到的数据库对象添加额外信息。
     * 由于Hibernate对Catalog的有限支持，此方法不执行任何操作。
     *
     * @param foundObject 已找到的数据库对象
     * @param snapshot 当前的数据库快照
     * @throws DatabaseException 如果在访问数据库时发生错误
     * @throws InvalidExampleException 如果提供的对象无效
     */
    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        // Nothing to add to
    }

    /**
     * 指定此快照生成器替代的其他快照生成器类。
     * 该方法表明当前的Hibernate Catalog快照生成器将替代标准的JVM Catalog快照生成器。
     *
     * @return 被此生成器替代的快照生成器类数组
     */
    @Override
    public Class<? extends SnapshotGenerator>[] replaces() {
        return new Class[]{io.github.shshdxk.liquibase.snapshot.jvm.CatalogSnapshotGenerator.class};
    }
}
