package com.adc.da.event.vo;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>WR_EVENT_RECEIVE EventReceiveEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class EventReceiveVO extends BaseEntity {

    private String id;
    private String receiveUserId;
    private String receiveUserName;
    private Integer state;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
    private String eventId;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String extInfo6;
    private Integer delFlag;
    private Integer NumOfStateIs1;
    private Integer NumOfStateIs0;
    private Integer sum;
    private String fileId;
    private String createUser;
    private String eventTitle;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getNumOfStateIs0() {
        return NumOfStateIs0;
    }

    public void setNumOfStateIs0(Integer numOfStateIs0) {
        NumOfStateIs0 = numOfStateIs0;
    }

    public Integer getNumOfStateIs1() {
        return NumOfStateIs1;
    }

    public void setNumOfStateIs1(Integer numOfStateIs1) {
        NumOfStateIs1 = numOfStateIs1;
    }




    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>receiveUserId -> receive_user_id_</li>
     * <li>receiveUserName -> receive_user_name_</li>
     * <li>state -> state_</li>
     * <li>receiveTime -> receive_time_</li>
     * <li>finishTime -> finish_time_</li>
     * <li>eventId -> event_id_</li>
     * <li>extInfo1 -> ext_info1_</li>
     * <li>extInfo2 -> ext_info2_</li>
     * <li>extInfo3 -> ext_info3_</li>
     * <li>extInfo4 -> ext_info4_</li>
     * <li>extInfo5 -> ext_info5_</li>
     * <li>extInfo6 -> ext_info6_</li>
     * <li>delFlag -> del_flag_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id_";
            case "receiveUserId": return "receive_user_id_";
            case "receiveUserName": return "receive_user_name_";
            case "state": return "state_";
            case "receiveTime": return "receive_time_";
            case "finishTime": return "finish_time_";
            case "eventId": return "event_id_";
            case "extInfo1": return "ext_info1_";
            case "extInfo2": return "ext_info2_";
            case "extInfo3": return "ext_info3_";
            case "extInfo4": return "ext_info4_";
            case "extInfo5": return "ext_info5_";
            case "extInfo6": return "ext_info6_";
            case "delFlag": return "del_flag_";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id_ -> id</li>
     * <li>receive_user_id_ -> receiveUserId</li>
     * <li>receive_user_name_ -> receiveUserName</li>
     * <li>state_ -> state</li>
     * <li>receive_time_ -> receiveTime</li>
     * <li>finish_time_ -> finishTime</li>
     * <li>event_id_ -> eventId</li>
     * <li>ext_info1_ -> extInfo1</li>
     * <li>ext_info2_ -> extInfo2</li>
     * <li>ext_info3_ -> extInfo3</li>
     * <li>ext_info4_ -> extInfo4</li>
     * <li>ext_info5_ -> extInfo5</li>
     * <li>ext_info6_ -> extInfo6</li>
     * <li>del_flag_ -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id_": return "id";
            case "receive_user_id_": return "receiveUserId";
            case "receive_user_name_": return "receiveUserName";
            case "state_": return "state";
            case "receive_time_": return "receiveTime";
            case "finish_time_": return "finishTime";
            case "event_id_": return "eventId";
            case "ext_info1_": return "extInfo1";
            case "ext_info2_": return "extInfo2";
            case "ext_info3_": return "extInfo3";
            case "ext_info4_": return "extInfo4";
            case "ext_info5_": return "extInfo5";
            case "ext_info6_": return "extInfo6";
            case "del_flag_": return "delFlag";
            default: return null;
        }
    }

    /**  **/
    public String getId() {
        return this.id;
    }

    /**  **/
    public void setId(String id) {
        this.id = id;
    }

    /**  **/
    public String getReceiveUserId() {
        return this.receiveUserId;
    }

    /**  **/
    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    /**  **/
    public String getReceiveUserName() {
        return this.receiveUserName;
    }

    /**  **/
    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    /**  **/
    public Integer getState() {
        return this.state;
    }

    /**  **/
    public void setState(Integer state) {
        this.state = state;
    }

    /**  **/
    public Date getReceiveTime() {
        return this.receiveTime;
    }

    /**  **/
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    /**  **/
    public Date getFinishTime() {
        return this.finishTime;
    }

    /**  **/
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    /**  **/
    public String getEventId() {
        return this.eventId;
    }

    /**  **/
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**  **/
    public String getExtInfo1() {
        return this.extInfo1;
    }

    /**  **/
    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    /**  **/
    public String getExtInfo2() {
        return this.extInfo2;
    }

    /**  **/
    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    /**  **/
    public String getExtInfo3() {
        return this.extInfo3;
    }

    /**  **/
    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    /**  **/
    public String getExtInfo4() {
        return this.extInfo4;
    }

    /**  **/
    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }

    /**  **/
    public String getExtInfo5() {
        return this.extInfo5;
    }

    /**  **/
    public void setExtInfo5(String extInfo5) {
        this.extInfo5 = extInfo5;
    }

    /**  **/
    public String getExtInfo6() {
        return this.extInfo6;
    }

    /**  **/
    public void setExtInfo6(String extInfo6) {
        this.extInfo6 = extInfo6;
    }

    /**  **/
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

}
