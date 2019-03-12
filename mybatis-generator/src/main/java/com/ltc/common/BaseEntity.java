package com.ltc.common;

import com.baomidou.mybatisplus.annotation.TableId;

public abstract class BaseEntity {

    @TableId("id")
    protected String id;


}
