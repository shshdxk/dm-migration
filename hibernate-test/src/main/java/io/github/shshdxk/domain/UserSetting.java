package io.github.shshdxk.domain;

import io.github.shshdxk.enums.UserSettingKey;
import jakarta.persistence.*;


@Entity
@Table(name = UserSetting.TABLE_NAME, comment = "用户设置表",
    uniqueConstraints = {
//        @UniqueConstraint(name = "U_USER_SETTINGS_KEY", columnNames = { "user_id", "setting_key" }),
            @UniqueConstraint(name = "U_USER_SETTINGS_USE_ID", columnNames = { "user_id" })
    }
)
public class UserSetting extends UpdatableEntity {

    public static final String TABLE_NAME = "ih_user_settings";

    @JoinColumn(name = "user_id", nullable = false, comment = "用户",
            foreignKey = @ForeignKey(name = "FK_USER_SETTING_USER_ID1"))
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "setting_key", length = 128, comment = "参数描述")
    @Enumerated(EnumType.STRING)
    private UserSettingKey key;

    @Column(name = "setting_value", length = 2000, comment = "值")
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
