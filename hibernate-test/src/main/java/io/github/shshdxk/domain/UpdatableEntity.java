package io.github.shshdxk.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class UpdatableEntity extends AbstractEntity {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
//    @Comment("创建时间")
    private Date createdDate = new Date();

    @LastModifiedDate
    @Column(name = "updated_date", nullable = false)
//    @Comment("最后一次更新时间")
    private Date updatedDate = new Date();

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
