package com.ltc.redis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Auther: ltc
 * @Date: 2019/3/4 17:16
 * @Description: 浮标添加DTO
 */
@Data
@ApiModel(value = "浮标添加")
public class AkcBuoyDeployAddDTO {

    @ApiModelProperty(value = "浮标名称")
    @NotBlank(message = "浮标名称不能为空")
    private String name;

    @ApiModelProperty(value = "浮标图片")
    @NotBlank(message = "浮标图片地址不能为空")
    private String imgUrl;

    @ApiModelProperty(value = "浮标跳转地址")
    @NotBlank(message = "浮标跳转地址不能为空")
    private String linkUrl;

    @ApiModelProperty(value = "浮标图片尺寸")
    @NotBlank(message = "浮标图片尺寸不能为空")
    private String imgSize;

//    @ApiModelProperty(value = "排序:越小越在前")
//    private Integer sort;

//    @ApiModelProperty(value = "展示类型")
//    private String showType;
//
//    @ApiModelProperty(value = "渠道")
//    private String channel;

    @ApiModelProperty(value = "用户等级:vip(0-6) list多选")
    @NotEmpty(message = "会员levels不能不选")
    private List<Integer> vipLevels;

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间")
    private Date endTime;

}
