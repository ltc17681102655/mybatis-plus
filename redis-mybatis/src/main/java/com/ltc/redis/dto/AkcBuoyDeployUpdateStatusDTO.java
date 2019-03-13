package com.ltc.redis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: ltc
 * @Date: 2019/3/4 17:16
 * @Description: 浮标修改DTO
 */
@Data
@ApiModel(value = "浮标批量修改状态")
public class AkcBuoyDeployUpdateStatusDTO {

    @ApiModelProperty(value = "ids")
    @NotEmpty(message = "浮标批量修改状态ids不能为空")
    private List<Integer> ids;

    @ApiModelProperty(value = "浮标状态: 0 下架 , 1 上架")
    @NotNull(message = "浮标批量下线status不能为null")
    private Integer status;

}
