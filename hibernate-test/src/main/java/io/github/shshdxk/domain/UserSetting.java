package io.github.shshdxk.domain;

import io.github.shshdxk.enums.UserSettingKey;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = UserSetting.TABLE_NAME,
    uniqueConstraints = {
//        @UniqueConstraint(name = "U_USER_SETTINGS_KEY", columnNames = { "user_id", "setting_key" }),
            @UniqueConstraint(name = "U_USER_SETTINGS_USE_ID", columnNames = { "user_id" })
    }
)
@org.hibernate.annotations.Table(appliesTo = UserSetting.TABLE_NAME, comment = "用户设置表")
public class UserSetting extends UpdatableEntity {

    public static final String TABLE_NAME = "ih_user_settings";

    @Comment("用户")
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_SETTING_USER_ID1"))
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Comment("参数描述")
    @Column(name = "setting_key", length = 128)
    @Enumerated(EnumType.STRING)
    private UserSettingKey key;

    @Comment("值")
    @Column(name = "setting_value", length = 2000)
    private String value;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserSettingKey getKey() {
        return key;
    }

    public void setKey(UserSettingKey key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
