package com.adc.da.budget.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.query.budget.BudgetQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <br>
 * <b>功能：</b>TS_BUDGET BudgetEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-10-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BudgetEODao extends BaseDao<BudgetEO> {

    /**
     * 批量删除
     *
     * @param ids id组
     */
    void deleteInBatch(List<String> ids);

    void deleteLogicInBatch(@Param("budgetIds") List<String> budgetIds);

    List<BudgetEO> selectByPrimaryKeys(@Param("budgetIds") List<String> budgetIds);
    List<BudgetEO> selectByIdList(@Param("budgetIds") List<String> budgetIds);

    List<BudgetEO> selectByPrimaryKeysNotOld(@Param("budgetIds") List<String> budgetIds);

    List<BudgetEO> selectByCreateUserId(String userId);

    //只根据PM查询业务
    List<BudgetEO> selectByPM(String pm);

    //根据PM与BudgetName查询业务
    List<BudgetEO> selectByPmAndBudgetName(@Param("id") String id, @Param("userId") String userId, @Param("budgetName") String budgetName);

    List<BudgetEO> selectByDeptIds(@Param("deptIds") List<String> deptIds,@Param("property") String property);

    //查询所有的业务  如果property==null，那么查询全部，否则根据属性查询业务
    List<BudgetEO> findAll(@Param("property") String property);

    List<BudgetEO> findByDomainIdNotNull();
    List<String> queryAllBudgetByType(@Param("property") String property);

    List<String> findBudgetIdsByNameLike(@Param("args") String args);

    List<String> findBudgetIdsByNameEquals(@Param("args") String args);

    List<String> findAllBudgetIdByNameNotLike(@Param("args") String args);

    List<String> findAllBudgetIdByNameNotLikeAndPropertyArr(@Param("args") String args, @Param("property") String[] property);

    List<BudgetEO> findAllBudgetNameNotLike(@Param("args") String args, @Param("property") String[] property);

    /**
     * 根据业务名称模糊查询Budget List
     *
     * @author Wei Jinjin
     * @date 2020-04-08
     */
    List<BudgetEO> findAllBudgetNameLikeAndByType(@Param("args") String args, @Param("property") String property);

    List<BudgetEO> findAllBudgetIdByDomainId(@Param("domainId") String domainId);

    List<BudgetEO> findAllBudgetNameNotLikeAndKeys
        (@Param("budgetIds") List<String> budgetIds,
            @Param("args") String args,
            @Param("property") String[] property);

    // 业务查询页
    List<BudgetEO> findByPage(BudgetQuery page);

    List<BudgetEO>  findAllBudget();

    int findByCount(BudgetQuery page);
 // 加注释  这里property 类型可为null 是查所有类型
    List<BudgetEO> findByIds(@Param("ids") Set<String> ids,@Param("property") String property);

    /**
     * 根据组织机构以及项目名称查询项目信息
     *
     * @param deptIds
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-05-27
     **/
    List<BudgetEO> selectByDeptIdsAndBudgetName(@Param("deptIds") List<String> deptIds,
        @Param("budgetName") String budgetName);

    List<BudgetEO> selectByBudgetName(
        @Param("budgetName") String budgetName);

    List<Map<String, Object>> selectEveryMonthInvoiceDataByBudgetName
        (@Param("budgetName") String budgetName, @Param("year") String year);

    int updateByDomainId(BudgetEO var1);

    List<BudgetEO> selectByAdminId(@Param("adminId") String adminId);

    BudgetEO selectById(@Param("id") String id);

    void deleteLogicInBatchById(@Param("id")String id);

    List<String> queryAllBudgetByTypeName(String property, String name);

    /**
     * 仅限 领导视角使用

     * @param budgetName
     * @param operate
     * @return
     */
    List<String> selectBudgetIdAndBudgetNameByBudgetName(
        @Param("budgetName") String budgetName,
        @Param("operate") String operate);
}
