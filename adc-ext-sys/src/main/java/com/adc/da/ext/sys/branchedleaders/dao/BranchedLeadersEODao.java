package com.adc.da.ext.sys.branchedleaders.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.ext.sys.branchedleaders.entity.BranchedLeadersEO;
import com.adc.da.ext.sys.branchedleaders.page.BranchedLeadersEOPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;
import java.util.Map;

/**
 *
 * <br>
 * <b>功能：</b>TS_BRANCHED_LEADERS BranchedLeadersEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BranchedLeadersEODao extends BaseDao<BranchedLeadersEO> {

    List<BranchedLeadersEO> getPage(BranchedLeadersEOPage page);
    String selectIdByOrgId(String orgId);
    String queryOrgIdByName(String orgName);
    String selectParentOrgNameByOrgId(String orgId);
    void deleteByOrgName(String orgName);
    Map<String,Object> getEmployeeInfoByNameAndOrgId(@Param("usname")String usname, @Param("oid")String oid);

}
