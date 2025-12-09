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
@Table(name = UserB.TABLE_NAME, comment = "用户表B")
public class UserB implements Persistable<Long>, Serializable {

    public static final String TABLE_NAME = "ih_users_b";

    @Id
    @Column(name = "id", nullable = false, updatable = false, comment = "主键")
    @NativeGenerator(sequenceForm = @SequenceGenerator(
            name = "user_b_id_seq_gen",
            sequenceName = "user_b_id_seq",
            allocationSize = 22,
            initialValue = 3
    ))
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_b_id_seq_gen")
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