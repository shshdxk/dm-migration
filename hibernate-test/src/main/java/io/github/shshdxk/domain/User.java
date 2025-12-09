package io.github.shshdxk.domain;

import io.github.shshdxk.enums.Gender;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.util.List;

/**
 *
 */
@Entity
@Table(name = User.TABLE_NAME, comment = "用户表", uniqueConstraints = {
        @UniqueConstraint(name = "U_USERS_USERNAME", columnNames = {"username"}),
        @UniqueConstraint(name = "U_USERS_MOBILE", columnNames = {"mobile"})})
public class User extends UpdatableEntity {

    public static final String TABLE_NAME = "ih_users";
    @Column(name = "username", nullable = false, length = 32, comment = "用户名")
    private String username;

    @Column(name = "mobile", comment = "联系方式")
    @ColumnDefault("-1")
    private String mobile;

    @Column(name = "full_name", nullable = false, comment = "全称")
    @JdbcTypeCode(SqlTypes.CLOB)
    private String fullName;

    @Column(name = "password_hash", comment = "密码")
    private String password;

    @Column(name = "is_locked", nullable = false, comment = "是否被锁定")
    @ColumnDefault("true")
    private boolean locked = false;

    @Column(name = "gender", nullable = false, comment = "性别")
    private Gender gender = Gender.UNKNOWN;

    @Column(name = "avatar_url", length = Integer.MAX_VALUE, comment = "头像URL")
    @Convert(converter = StringListConvert.class)
    @JdbcTypeCode(SqlTypes.NCLOB)
    private List<String> avatarUrl;

    @Column(name = "avatar_url1", comment = "头像URL1")
    @JdbcTypeCode(SqlTypes.CLOB)
    private String avatarUrl1;

    @Column(name = "avatar_url2", length = Integer.MAX_VALUE, comment = "头像URL2")
    @JdbcTypeCode(SqlTypes.CLOB)
    private String avatarUrl2;

    @Column(name = "avatar_url3", comment = "头像URL3")
    @JdbcTypeCode(SqlTypes.NCLOB)
    private String avatarUrl3;

    @Column(name = "is_deleted", comment = "是否删除")
    @ColumnDefault("false")
    private boolean deleted = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
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

    public List<String> getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(List<String> avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getAvatarUrl1() {
        return avatarUrl1;
    }

    public void setAvatarUrl1(String avatarUrl1) {
        this.avatarUrl1 = avatarUrl1;
    }

    public String getAvatarUrl2() {
        return avatarUrl2;
    }

    public void setAvatarUrl2(String avatarUrl2) {
        this.avatarUrl2 = avatarUrl2;
    }

    public String getAvatarUrl3() {
        return avatarUrl3;
    }

    public void setAvatarUrl3(String avatarUrl3) {
        this.avatarUrl3 = avatarUrl3;
    }
}