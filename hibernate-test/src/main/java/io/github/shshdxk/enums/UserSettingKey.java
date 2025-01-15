package io.github.shshdxk.enums;

public enum UserSettingKey {

    DEMO("demo")
    ;

    private final String name;

    UserSettingKey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

