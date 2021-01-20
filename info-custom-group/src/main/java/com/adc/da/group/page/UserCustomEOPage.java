package com.adc.da.group.page;

import com.adc.da.base.page.BasePage;

/**
 * <b>功能：</b>TR_USER_CUSTOM UserCustomEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class UserCustomEOPage extends BasePage {

    private String userId;

    private String userIdOperator = "=";

    private String groupId;

    private String groupIdOperator = "=";

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIdOperator() {
        return this.userIdOperator;
    }

    public void setUserIdOperator(String userIdOperator) {
        this.userIdOperator = userIdOperator;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupIdOperator() {
        return this.groupIdOperator;
    }

    public void setGroupIdOperator(String groupIdOperator) {
        this.groupIdOperator = groupIdOperator;
    }

}
