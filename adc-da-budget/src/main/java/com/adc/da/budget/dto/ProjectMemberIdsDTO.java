package com.adc.da.budget.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.util.Date;


/**
 * @author qichunxu
 */
@Data
public class ProjectMemberIdsDTO {

    @ApiModelProperty("项目组成员Ids")
    @NotEmpty(message = "项目组成员不能为空！")
    private String[] projectMemberIds;

}
