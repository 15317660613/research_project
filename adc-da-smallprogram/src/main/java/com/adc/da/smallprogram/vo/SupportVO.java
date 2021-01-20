package com.adc.da.smallprogram.vo;

import com.adc.da.smallprogram.entity.ScheduleSupportEO;
import com.adc.da.smallprogram.entity.ScheduleSupportUserEO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SupportVO
 * @Description: TODO
 * @Author 丁强
 * @Date 2019/12/5
 * @Version V1.0
 **/
@Data
public class SupportVO {
    private ScheduleSupportEO scheduleSupportEO ;

    private List<ScheduleSupportUserEO> scheduleSupportUserEOList = new ArrayList<>();
}
