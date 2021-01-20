package com.adc.da.budget.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/22 13:55
 * @Description:
 */
@Data
public class ProjectStatusFinishedVO {
    private String id;
    private String name;
    private String projectName;
    private String priority;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date start;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date end;
    private List<Integer> schedule;
    private List<String[]> mark;
}
