package com.adc.da.crm.vo;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import com.adc.da.crm.annotation.MatchFieldCollection;
import com.adc.da.crm.entity.CustomerContactEO;
import com.adc.da.crm.entity.CustomerVisitRecordEO;
import com.adc.da.util.http.PageInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <b>功能：</b>CUSTOMER_RECORD CustomerRecordEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class PageVO<T> extends BaseEntity {

    private PageInfo<T> pageInfo;
    private List<Map<String, String>> tableHeadList;

    public PageInfo<T> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<T> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Map<String, String>> getTableHeadList() {
        return tableHeadList;
    }

    public void setTableHeadList(List<Map<String, String>> tableHeadList) {
        this.tableHeadList = tableHeadList;
    }
}
