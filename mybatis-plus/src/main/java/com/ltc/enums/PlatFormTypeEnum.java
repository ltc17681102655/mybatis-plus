package com.ltc.enums;

public enum PlatFormTypeEnum {

    APP("APP", 1),
    VIP("VIP", 2),
    BIG_WARE("BIA_WARE", 3);

    private String key;
    private Integer value;

    PlatFormTypeEnum(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
}
