package io.github.shshdxk.domain;

import io.github.shshdxk.enums.Gender;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 */
@Entity
@Table(name = User.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(name = "U_USERS_USERNAME", columnNames = {"username"}),
        @UniqueConstraint(name = "U_USERS_MOBILE", columnNames = {"mobile"}),})
@org.hibernate.annotations.Table(appliesTo = User.TABLE_NAME, comment = "用户表")
public class User extends UpdatableEntity {

    public static final String TABLE_NAME = "ih_users";
    @Comment("名称")
    @Column(name = "username", nullable = false, length = 32)
    private String username;

    @Comment("联系方式")
    @Column(name = "mobile")
    @ColumnDefault("-1")
    private int mobile;

    @Comment("全称")
    @Column(name = "full_name")
    @Type(type = "org.hibernate.type.TextType")
    private String fullName;

    @Comment("密码")
    @Column(name = "password_hash")
    private String password;

    @Comment("是否锁定")
    @Column(name = "is_locked")
    @ColumnDefault("false")
    private boolean locked = false;

    @Comment("性别")
    @Column(name = "gender", nullable = false)
    private Gender gender = Gender.UNKNOWN;

    @Comment("头像URL")
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Comment("是否删除")
    @Column(name = "is_deleted")
    @ColumnDefault("0")
    private boolean deleted = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}