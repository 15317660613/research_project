package com.adc.da.smallprogram.vo;

import com.adc.da.smallprogram.entity.ScheduleResearchUserEO;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ResearchVO
 * @Description: TODO
 * @Author 丁强
 * @Date 2020/5/26
 * @Version V1.0
 **/
@Data
public class ResearchVO {
    private String id;
    private String title;
    private String content="";
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private int status;
    private int collect;
    private int top;
    //private List<ScheduleResearchUserEO> scheduleResearchUserEOList = new ArrayList<>();

}
