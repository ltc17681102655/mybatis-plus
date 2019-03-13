package com.ltc.redis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

/**
 * @Auther: ltc
 * @Date: 2019/3/4 17:16
 * @Description: 浮标修改DTO
 */
@Data
@ApiModel(value = "浮标编辑修改")
public class AkcBuoyDeployUpdateDTO {

    @ApiModelProperty("主键id")
    private int id;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "浮标名称不能为空")
    private String name;

    @ApiModelProperty(value = "浮标url")
    @NotBlank(message = "浮标链接地址为空")
    private String linkUrl;

    @ApiModelProperty(value = "浮标图片地址")
    @NotBlank(message = "浮标图片地址为空")
    private String imgUrl;

    @ApiModelProperty(value = "浮标图片尺寸")
    @NotBlank(message = "浮标图片尺寸为空")
    private String imgSize;

    @ApiModelProperty(value = "用户等级:vip(0-6) list多选")
    @NotEmpty(message = "会员levels必填")
    private List<Integer> vipLevels;

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

//    @ApiModelProperty(value = "版本")
//    private Integer version;
//
//    @ApiModelProperty(value = "删除 0:未删除 1:删除")
//    private Byte deleted;

}
