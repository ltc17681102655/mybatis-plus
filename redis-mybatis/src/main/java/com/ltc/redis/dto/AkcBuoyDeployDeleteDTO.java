package com.ltc.redis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @Auther: ltc
 * @Date: 2019/3/9 17:30
 * @Description:
 */
@Data
@ApiModel(value = "浮标批量删除")
public class AkcBuoyDeployDeleteDTO {

    @ApiModelProperty(value = "ids")
    @NotEmpty(message = "浮标批量删除ids不能为空")
    private List<Integer> ids;

}
