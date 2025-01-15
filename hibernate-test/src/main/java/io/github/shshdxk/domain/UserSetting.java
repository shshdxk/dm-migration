package io.github.shshdxk.domain;

import io.github.shshdxk.enums.UserSettingKey;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = UserSetting.TABLE_NAME,
    uniqueConstraints = {
        @UniqueConstraint(name = "U_USER_SETTINGS_KEY", columnNames = { "user_id", "setting_key" })
    }
)
public class UserSetting extends UpdatableEntity {

    public static final String TABLE_NAME = "ih_user_settings";

    @Comment("用户")
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_SETTING_USER_ID"))
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Comment("参数描述")
    @Column(name = "setting_key", length = 128)
    @Enumerated(EnumType.STRING)
    private UserSettingKey key;

    @Comment("值")
    @Column(name = "setting_value", length = 2000)
    private String value;

    protected UserSetting() {}

}
