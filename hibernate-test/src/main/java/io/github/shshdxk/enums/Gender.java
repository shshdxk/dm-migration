package io.github.shshdxk.enums;

public enum Gender {
    /**
     * 未知
     */
    UNKNOWN("未知", ""),
    /**
     * 男性
     */
    MALE("男性", "男"),
    /**
     * 女性
     */
    FEMALE("女性", "女");

    private final String value;
    private final String shortValue;

    private Gender(String value, String shortValue) {
        this.value = value;
        this.shortValue = shortValue;
    }

    public String getValue() {
        return value;
    }

    public String getShortValue() {
        return shortValue;
    }

}
