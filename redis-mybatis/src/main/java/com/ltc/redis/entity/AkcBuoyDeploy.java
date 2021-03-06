package com.ltc.redis.entity;

import java.time.LocalDateTime;
import java.util.Date;

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
 * @since 2019-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AkcBuoyDeploy extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private int id;
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
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createdDate;

    /**
     * 最后修改时间
     */
    private Date lastModifiedDate;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 删除 0:未删除 1:删除
     */
    private Integer deleted;


}
