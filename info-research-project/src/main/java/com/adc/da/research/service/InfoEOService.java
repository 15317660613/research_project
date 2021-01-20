package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.page.BudgetEOPage;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.dao.HiBaseEODao;
import com.adc.da.research.dao.InfoEODao;
import com.adc.da.research.entity.HiBaseEO;
import com.adc.da.research.entity.HiBaseInterface;
import com.adc.da.research.entity.InfoEO;
import com.adc.da.research.page.InfoEOPage;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.adc.da.workflow.entity.ActCommittedTaskEO;
import com.adc.da.workflow.service.ActCommittedTaskEOService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.adc.da.research.utils.RSPendingStatus.RS_PENDING_UPDATE;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_INFO InfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("infoEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class InfoEOService extends BaseService<InfoEO, String> {

    @Autowired
    private InfoEODao dao;

    @Autowired
    private BudgetEODao budgetEODao;

    @Autowired
    private ActCommittedTaskEOService actCommittedTaskEOService;

    public List<InfoEO> getPendingProcess(InfoEOPage queryPage) {
        Integer rowCount = dao.countPendingProcess(queryPage);
        queryPage.getPager().setRowCount(rowCount);
        return dao.queryPendingProcess(queryPage);
    }

    public InfoEODao getDao() {
        return dao;
    }

    /**
     * 存储科研项目基本信息，需要添加 所属业务信息
     *
     * @param infoEO
     * @return
     */
    public InfoEO save(InfoEO infoEO) throws Exception{
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        BudgetEOPage page = new BudgetEOPage();
        Calendar cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);

        page.setProjectName(String.valueOf(year) + '%');
        page.setProjectNameOperator("like");
        page.setProperty("2");
        List<BudgetEO> budgetEOList = budgetEODao.queryByList(page);
        if (CollectionUtils.isEmpty(budgetEOList)) {
            throw new AdcDaBaseException("科研业务信息获取失败");
        }
        BudgetEO budgetEO = budgetEOList.get(0);
        infoEO.setBusinessId(budgetEO.getId());
        infoEO.setBusinessName(budgetEO.getProjectName());
        infoEO.setResearchProjectId(UUID.randomUUID10());
        infoEO.setExtInfo5(String.valueOf(new Date().getTime()));
        this.getDao().insertSelective(infoEO);
        // 将暂存待发信息存入acr_comminted_task表中
        ActCommittedTaskEO actCommittedTaskEO = new ActCommittedTaskEO();
        // 把科研项目id放到businessKey中，科研项目的存待发特征是 表单id不存在，bussinessKey中存的是科研项目的的id
        actCommittedTaskEO.setBusinessKey(infoEO.getResearchProjectId());
        actCommittedTaskEO.setExt1(infoEO.getResearchProjectName());
        actCommittedTaskEO.setCreateTime(new Date());
        actCommittedTaskEO.setCreateUserId(userEO.getUsid());
        actCommittedTaskEO.setUsname(userEO.getUsname());
        JSONObject json = new JSONObject();
        json.put("orgId", infoEO.getOwnDepartmentId());
        json.put("orgName", infoEO.getOwnDepartmentName());
        actCommittedTaskEO.setExt2(json.toJSONString());
        actCommittedTaskEOService.saveOrUpdate(actCommittedTaskEO);
        return infoEO;
    }

    /**
     * 加入重名校验
     *
     * @param infoEO
     * @return
     * @throws Exception
     */
    @Override
    public int updateByPrimaryKeySelective(InfoEO infoEO) throws Exception {

        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        InfoEO oldInfoEO = dao.selectByPrimaryKey(infoEO.getResearchProjectId());

        if (!StringUtils.equals(oldInfoEO.getResearchProjectName(), infoEO.getResearchProjectName())) {
            InfoEOPage infoEOPage = new InfoEOPage();
            infoEOPage.setResearchProjectName(infoEO.getResearchProjectName());
            infoEOPage.setBusinessId(infoEO.getBusinessId());
            if (dao.queryByCount(infoEOPage) > 0) {
                return -1;
            }
        }
        List<ActCommittedTaskEO> actCommittedTaskEOList = actCommittedTaskEOService.selectByBusinessKey(infoEO.getResearchProjectId());
        ActCommittedTaskEO actCommittedTaskEO = null;
        if (CollectionUtils.isEmpty(actCommittedTaskEOList)) {
            actCommittedTaskEO = new ActCommittedTaskEO();
        }else {
            actCommittedTaskEO = actCommittedTaskEOList.get(0);
        }
        // 把科研项目id放到businessKey中，科研项目的存待发特征是 表单id不存在，bussinessKey中存的是科研项目的的id
        actCommittedTaskEO.setBusinessKey(infoEO.getResearchProjectId());
        actCommittedTaskEO.setExt1(infoEO.getResearchProjectName());
        actCommittedTaskEO.setCreateTime(new Date());
        actCommittedTaskEO.setCreateUserId(userEO.getUsid());
        actCommittedTaskEO.setUsname(userEO.getUsname());
        JSONObject json = new JSONObject();
        json.put("orgId", infoEO.getOwnDepartmentId());
        json.put("orgName", infoEO.getOwnDepartmentName());
        actCommittedTaskEO.setExt2(json.toJSONString());
        actCommittedTaskEOService.saveOrUpdate(actCommittedTaskEO);
        return dao.updateByPrimaryKeySelective(infoEO);

    }

    @Override
    public void deleteByPrimaryKey(String id) throws Exception {
        InfoEO infoEO = new InfoEO();
        infoEO.setResearchProjectId(id);
        infoEO.setExtInfo4("-1");
        dao.updateByPrimaryKeySelective(infoEO);
        List<ActCommittedTaskEO> actCommittedTaskEOList = actCommittedTaskEOService.selectByBusinessKey(id);
        if (CollectionUtils.isNotEmpty(actCommittedTaskEOList)) {
            ActCommittedTaskEO actCommittedTaskEO = actCommittedTaskEOList.get(0);
            actCommittedTaskEOService.deleteByPrimaryKey(actCommittedTaskEO.getId());
        }
    }

    /**
     * 对项目状态进行更新
     *
     * @param infoEO
     */
    private void updateInfoEOStatus(InfoEO infoEO, String businessKey, String expectedValue) {

        /*
         * 3 为 流程中
         * 2 为 未发起（变更）
         * 0 为 未发起（新建）
         */
        String status = infoEO.getExtInfo4();

        if (!StringUtils.equals(status, expectedValue)) {
            infoEO.setExtInfo6(businessKey);
            infoEO.setExtInfo4(expectedValue);
            dao.updateByPrimaryKeySelective(infoEO);
        }
    }

    @Autowired
    private HiBaseEODao hiBaseEODao;

    /**
     * 校验状态，并且项目更新状态
     *
     * @param vo
     * @param <T>
     * @return
     */
    public <T extends HiBaseInterface> InfoEO checkAndUpdateStatus(T vo) {

        String key = vo.getProcBusinessKey();

        HiBaseEO hiBaseEO = getHiBaseEOByBusinessKey(key);
        String projectId = hiBaseEO.getResearchProjectId();
        InfoEO infoEO = getInfoEOByHiBaseEO(projectId);
        updateInfoEOStatus(infoEO, key, RS_PENDING_UPDATE);
        return infoEO;
    }

    /**
     * @param businessKey
     * @return
     */
    public InfoEO checkAndUpdateStatus(String businessKey, String projectId, String expectedValue) {

        InfoEO infoEO = getInfoEOByHiBaseEO(projectId);
        updateInfoEOStatus(infoEO, businessKey, expectedValue);
        return infoEO;
    }

    /**
     * 获取基本信息
     *
     * @param businessKey
     * @return
     */
    private HiBaseEO getHiBaseEOByBusinessKey(String businessKey) {

        HiBaseEO hiBaseEO = hiBaseEODao.selectByPrimaryKey(businessKey);
        if (hiBaseEO == null) {
            throw new AdcDaBaseException("修改操作非法，不存在相关Key值");
        }

        return hiBaseEO;
    }

    /**
     * 获取基本信息
     *
     * @param hiBaseEO
     * @return
     */
    private InfoEO getInfoEOByHiBaseEO(String projectId) {

        InfoEO infoEO = dao.selectByPrimaryKey(projectId);
        if (infoEO == null) {
            throw new AdcDaBaseException("修改操作非法，基本信息缺失");
        }

        return infoEO;
    }
}
