package com.adc.da.research.project.vo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/12/04/13:44
 * @Description:
 */
public class ChangeProjectVO {
    //变更属性
    private String changeProperty;

    //变更人
    private String changeUser;

    //变更内容
    private String changeContext;

    private String changeTime;

    public String getChangeProperty() {
        return changeProperty;
    }

    public void setChangeProperty(String changeProperty) {
        this.changeProperty = changeProperty;
    }

    public String getChangeUser() {
        return changeUser;
    }

    public void setChangeUser(String changeUser) {
        this.changeUser = changeUser;
    }

    public String getChangeContext() {
        return changeContext;
    }

    public void setChangeContext(String changeContext) {
        this.changeContext = changeContext;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }
}
