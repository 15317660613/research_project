package com.adc.da.smallprogram.vo;

import com.adc.da.smallprogram.entity.ScheduleMeetEO;
import com.adc.da.smallprogram.entity.ScheduleMeetUserEO;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MeetVO
 * @Description: TODO
 * @Author 丁强
 * @Date 2019/11/26
 * @Version V1.0
 **/
@Data
public class MeetVO {
    private ScheduleMeetEO scheduleMeetEO ;
    private List<ScheduleMeetUserEO> scheduleMeetUserEOList ;
}
