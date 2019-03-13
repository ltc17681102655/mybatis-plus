package com.ltc.redis.enums;

/**
 * @Auther: ltc
 * @Date: 2019/3/13 10:02
 * @Description:
 */
public enum RedisKeyEnum {

    BUOY("buoy", "浮标key");
    private String key;
    private String desc;

    RedisKeyEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }}
