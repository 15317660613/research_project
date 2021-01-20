package com.adc.da.leaderview.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MilepostLeaderViewVO {
    private String projectName ;
    private String milepostName;
    private String milepostLeader ;
    private String milepostTarget ;
    private Date milepostStartTime ;
    private Date milepostEndTime ;
    private String milepostStatus;
    private String milepostResult ;

}
