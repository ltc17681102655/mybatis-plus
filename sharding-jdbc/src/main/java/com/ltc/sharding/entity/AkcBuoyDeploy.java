package com.ltc.sharding.entity;

import java.time.LocalDateTime;
import com.ltc.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * cms_配置_浮标
 * </p>
 *
 * @author ltc
 * @since 2019-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AkcBuoyDeploy extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片url
     */
    private String imgUrl;

    /**
     * 链接url
     */
    private String linkUrl;

    /**
     * 图片尺寸
     */
    private String imgSize;

    /**
     * 越小越在前
     */
    private Integer sort;

    /**
     * 展示类型
     */
    private Integer showType;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdDate;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastModifiedDate;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 删除 0:未删除 1:删除
     */
    private Integer deleted;


}
