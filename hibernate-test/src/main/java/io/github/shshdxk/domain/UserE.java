package io.github.shshdxk.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.NativeGenerator;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 *
 */
@Entity
@Table(name = UserE.TABLE_NAME, comment = "用户表E")
public class UserE implements Persistable<Long>, Serializable {

    public static final String TABLE_NAME = "ih_users_e";

    @Id
    @Column(name = "id", nullable = false, updatable = false, comment = "主键")
    @NativeGenerator(tableForm = @TableGenerator(
            name = "user_e_seq",
            table = "SeqTable",
            pkColumnName = "pk",
            pkColumnValue = "user_e",
            valueColumnName = "seq",
            initialValue = 12,
            allocationSize = 31
    )
    )
//    @TableGenerator(
//            name = "user_e_seq",
//            table = "SeqTable",
//            pkColumnName = "pk",
//            pkColumnValue = "user_e",
//            valueColumnName = "seq",
//            initialValue = 12,
//            allocationSize = 31
//    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_e_seq")
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