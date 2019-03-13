package com.ltc.redis.entity;

import java.time.LocalDateTime;
import com.ltc.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 浮标与用户等级关联表
 * </p>
 *
 * @author ltc
 * @since 2019-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AkcBuoyLevel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 浮标id
     */
    private Integer buoyId;

    /**
     * 用户等级:vip(0-6)
     */
    private Integer level;

    /**
     * 创建时间
     */
    private LocalDateTime createdDate;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastModifiedDate;


}
