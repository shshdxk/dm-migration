package io.github.shshdxk.domain;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 *
 */
@Entity
@Table(name = UserD.TABLE_NAME, comment = "用户表D")
public class UserD implements Persistable<Long>, Serializable {

    public static final String TABLE_NAME = "ih_users_d";

    @Id
    @Column(name = "id", nullable = false, updatable = false, comment = "主键")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_d_seq")
    @TableGenerator(name = "user_d_seq", table = "SeqTable", pkColumnName = "pk", pkColumnValue = "user_d", valueColumnName = "seq", initialValue = 12, allocationSize = 31)
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