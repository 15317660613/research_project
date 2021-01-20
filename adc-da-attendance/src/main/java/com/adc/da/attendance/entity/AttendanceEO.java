package com.adc.da.attendance.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <b>功能：</b>ST_ATTENDANCE AttendanceEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-12-13 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 * /**
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>id -> id_</li>
 * <li>date -> date_</li>
 * <li>beginTime -> begin_time_</li>
 * <li>beginTimeInt -> begin_time_int_</li>
 * <li>endTime -> end_time_</li>
 * <li>endTimeInt -> end_time_int_</li>
 * <li>userCode -> user_code_</li>
 * <li>userId -> user_id_</li>
 * <li>userName -> user_name_</li>
 * <li>deptId -> dept_id_</li>
 * <li>deptName -> dept_name_</li>
 * <li>source -> source_</li>
 * <li>remark -> remark_</li>
 * <li>reason -> reason_</li>
 * <li>extInfo01 -> ext_info_01_</li>
 * <li>extInfo02 -> ext_info_02_</li>
 * <li>extInfo03 -> ext_info_03_</li>
 * <li>extInfo04 -> ext_info_04_</li>
 * <li>extInfo05 -> ext_info_05_</li>
 */

@Getter
@Setter
public class AttendanceEO extends BaseEntity {

    /***/
    private String id;

    /***/
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    /***/
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /***/
    private Integer beginTimeInt;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /***/
    private Integer endTimeInt;

    /***/
    private String userCode;

    /***/
    private String userId;

    /***/
    private String userName;

    /***/
    private String deptId;

    /***/
    private String deptName;

    /***/
    private String source;

    /**
     * 状态， 末 2位 表示吃到早退
     */
    private String remark;

    /***/
    private String reason;

    /***/
    private String extInfo01;

    /***/
    private String extInfo02;

    /***/
    private String extInfo03;

    /***/
    private String extInfo04;

    /***/
    private String extInfo05;

}
