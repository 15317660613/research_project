package com.adc.da.smallprogram.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

/**
 * @ClassName MeetPage
 * @Description: TODO
 * @Author 丁强
 * @Date 2019/11/26
 * @Version V1.0
 **/

public class MeetPage extends BasePage {
    private String receiveUserId ;

    private String title ;

    private Integer meetType ;


    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMeetType() {
        return meetType;
    }

    public void setMeetType(Integer meetType) {
        this.meetType = meetType;
    }
}
