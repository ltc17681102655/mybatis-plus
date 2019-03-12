package com.ltc.mybatis.plus.entity;

import com.ltc.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author ltc
 * @since 2019-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TestEnum extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 1:app 2:vip 3:big_ware
     */
    private Integer type;


}
