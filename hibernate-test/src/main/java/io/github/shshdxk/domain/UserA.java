package io.github.shshdxk.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 *
 */
@Entity
@Table(name = UserA.TABLE_NAME, comment = "用户表A")
public class UserA implements Persistable<Long>, Serializable {

    public static final String TABLE_NAME = "ih_users_a";

    @Id
    @Column(name = "id", nullable = false, updatable = false, comment = "主键")
    @Sequence(name = "idGenerator")
    private Long id;

    @Column(name = "username", nullable = false, length = 32, comment = "用户名")
    private String username;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public @Nullable Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    public void setId(Long id) {
        this.id = id;
    }
}