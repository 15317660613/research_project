package com.adc.da.research.personnel.vo;

import com.adc.da.base.entity.BaseEntity;

/**
 * @description 专家人员-专家组关联表VO
 * @date 2020/11/13
 * @auth wcj
 */
public class ExpertGroupUserVO extends BaseEntity {

    /**
     * 关联表ID
     */
    private String id;

    /**
     * 专家组id
     */
    private String groupId;

    /**
     * 专家人员id
     */
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
