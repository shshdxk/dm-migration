package io.github.shshdxk.domain;

import io.github.shshdxk.enums.Gender;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = User.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(name = "U_USERS_USERNAME", columnNames = {"username"}),
        @UniqueConstraint(name = "U_USERS_MOBILE", columnNames = {"mobile"})})
@org.hibernate.annotations.Table(appliesTo = User.TABLE_NAME, comment = "用户表")
public class User extends UpdatableEntity {

    public static final String TABLE_NAME = "ih_users";
    @Comment("名称")
    @Column(name = "username", nullable = false, length = 32)
    private String username;

    @Comment("联系方式")
    @Column(name = "mobile")
    @ColumnDefault("-1")
    private String mobile;

    @Comment("全称")
    @Column(name = "full_name", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String fullName;

    @Comment(" 密码")
    @Column(name = "password_hash")
    private String password;

    @Comment("是否被锁定")
    @Column(name = "is_locked", nullable = false)
    @ColumnDefault("true")
    private boolean locked = false;

    @Comment("性别")
    @Column(name = "gender", nullable = false)
    private Gender gender = Gender.UNKNOWN;

    @Comment("头像URL")
    @Column(name = "avatar_url", length = Integer.MAX_VALUE)
    @Convert(converter = StringListConvert.class)
    private List<String> avatarUrl;

    @Comment("头像URL1")
    @Column(name = "avatar_url1")
    @Type(type = "org.hibernate.type.TextType")
    private String avatarUrl1;

    @Comment("头像URL2")
    @Column(name = "avatar_url2")
    @Convert(converter = StringListConvert.class)
    private String avatarUrl2;

    @Comment("头像URL3")
    @Column(name = "avatar_url3", length = Integer.MAX_VALUE)
    @Convert(converter = StringListConvert.class)
    private String avatarUrl3;

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