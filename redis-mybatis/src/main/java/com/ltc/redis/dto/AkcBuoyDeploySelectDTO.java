package com.ltc.redis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Auther: ltc
 * @Date: 2019/3/4 17:16
 * @Description: 浮标查询DTO
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "浮标分页查询")
public class AkcBuoyDeploySelectDTO {

    @ApiModelProperty("主键id")
    @NotNull(message = "pageIndex不能为null")
    private Integer pageIndex;

    @ApiModelProperty("主键id")
    @NotNull(message = "pageSize不能为null")
    private Integer pageSize;

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "浮标url")
    private String linkUrl;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "尺寸")
    private String imgSize;

    @ApiModelProperty(value = "用户等级:vip(0-6) list多选")
    private List<Integer> vipLevels;

    @ApiModelProperty(value = "浮标状态: 0 已下线 , 1 上线 ,  2 未上线")
    private Integer status;

//    @ApiModelProperty(value = "排序:越小越在前")
//    private Integer sort;

//    @ApiModelProperty(value = "展示类型")
//    private String showType;
//
//    @ApiModelProperty(value = "渠道")
//    private String channel;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;


//    @ApiModelProperty(value = "创建时间")
//    private Date createdDate;
//
//    @ApiModelProperty(value = "最后修改时间")
//    private Date lastModifiedDate;
//
//    @ApiModelProperty(value = "版本")
//    private Integer version;

    @ApiModelProperty(value = "删除 0:未删除 1:删除")
    private Byte deleted;

}
