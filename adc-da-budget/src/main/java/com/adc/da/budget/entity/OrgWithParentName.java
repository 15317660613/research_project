package com.adc.da.budget.entity;

import com.adc.da.sys.entity.OrgEO;
import lombok.Data;

/**
 * 增加程序内字段：全名
 *
 * @author liuzixi
 * date 2019-03-08
 */
public class OrgWithParentName extends OrgEO {

    /**
     * 全名
     */
    private String nameWithParent;

    public OrgWithParentName() {
    }

    public OrgWithParentName(OrgEO orgEO, String nameWithParent) {
        setName(orgEO.getName());
        setId(orgEO.getId());
        setDelFlag(orgEO.getDelFlag());
        setRemarks(orgEO.getRemarks());
        setChildList(orgEO.getChildList());
        setIsShow(orgEO.getIsShow());
        setOrgCode(orgEO.getOrgCode());
        setOrgDesc(orgEO.getOrgDesc());
        setOrgType(orgEO.getOrgType());
        setParent(orgEO.getParent());
        setParentId(orgEO.getParentId());
        setParentIds(orgEO.getParentIds());
        this.nameWithParent = nameWithParent;
    }

    /**
     * Gets the value of nameWithParent.
     *
     * @return the value of nameWithParent
     */
    public String getNameWithParent() {
        return nameWithParent;
    }

    /**
     * Sets the nameWithParent.
     * <p>
     * <p>You can use getNameWithParent() to get the value of nameWithParent</p>
     *
     * @param nameWithParent nameWithParent
     */
    public void setNameWithParent(String nameWithParent) {
        this.nameWithParent = nameWithParent;
    }
}
