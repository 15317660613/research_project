package com.adc.da.oa.service;

import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.UserWithProjects;
import com.adc.da.budget.page.BudgetEOPage;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.service.BudgetEOService;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据同步-业务
 */
@Service
public class DataSyncBudgetService {
    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private DataSyncBaseService baseService;

    /**
     * 删除
     *
     * @param domainSet
     * @return
     */
    public void deleteBudgetIdByDomainId(Set<String> domainSet) throws Exception {
        BudgetEOPage queryPage = new BudgetEOPage();
        queryPage.setDomainIds(domainSet);
        List<BudgetEO> budgetEOList = budgetEOService.queryByList(queryPage);
        List<String> ids = new ArrayList<>();
        for (BudgetEO eo : budgetEOList) {
            ids.add(eo.getId());
        }
        if (CollectionUtils.isNotEmpty(ids)) {
            budgetEOService.deleteInBatch(ids, false);
        }

    }

    /**
     * 更新操作
     *
     * @param json
     * @return
     * @throws Exception
     */
    public List<BudgetEO> updateMultiJson(String json) throws Exception {
        List<BudgetEO> budgetEOList = checkJson(json);
        String pm;
        String budgetId;
        for (BudgetEO saveEO : budgetEOList) {
            BudgetEOPage queryPage = new BudgetEOPage();
            queryPage.setDomainId(saveEO.getDomainId());
            List<BudgetEO> budgetEOFormList = budgetEOService.queryByList(queryPage);
            pm = saveEO.getPm();
            budgetId = saveEO.getId();
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(pm);
            //更新es信息
            if (userWithProjects != null) {
                Set<String> budgetIds = userWithProjects.getBusinessIds();
                if (!budgetIds.contains(saveEO.getId())) {
                    budgetIds.add(budgetId);
                    userWithProjects.setBusinessIds(budgetIds);
                    userWithProjectsRepository.save(userWithProjects);
                }
            }

            if (CollectionUtils.isEmpty(budgetEOFormList)) {
                //新增
                saveEO.setId(UUID.randomUUID10());
                budgetEOService.insertSelective(saveEO);
                budgetEOService.saveUserBudgetPM(saveEO);
                budgetEOService.saveUserBudgetBusinessAdmin(saveEO);

            } else {
                /*
                 * 修改
                 * 补充eo的id
                 */
                saveEO.setId(budgetEOFormList.get(0).getId());
                //budgetEOService.setPM(saveEO);
                budgetEOService.getDao().updateByDomainId(saveEO);
                if (StringUtils.isNotEmpty(saveEO.getBusinessAdminId())) {
                    budgetEOService.updateUserBudgetAdminNew(saveEO, budgetEOFormList.get(0));
                    budgetEOFormList.get(0).setBusinessAdminId(saveEO.getBusinessAdminId());
                }
                if (StringUtils.isNotEmpty(saveEO.getPm())) {
                    budgetEOService.updateUserBudgetPMNew(saveEO, budgetEOFormList.get(0));
                }
                budgetEOService.updateByPrimaryKeySelective(saveEO);
            }

        }

        return budgetEOList;
    }

    /**
     * 解析json信息
     *
     * @param json
     * @return
     */
    private List<BudgetEO> checkJson(String json) {
        Gson gson = new Gson();

        Type typeList = new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType();

        List<Map<String, Object>> jsonList = gson.fromJson(json, typeList);

        List<BudgetEO> budgetEOList = new ArrayList<>(jsonList.size());
        for (Map<String, Object> map : jsonList) {
            BudgetEO budgetEO = new BudgetEO();
            budgetEO.setProperty("0");
            budgetEO.setPropertyId("cx");

            /*
             * 设置部门
             */
            String deptName = (String) map.get("field2");
            budgetEO.setDeptName(deptName);

            String deptId = baseService.getDeptId(deptName);
            budgetEO.setDeptId(deptId);

            /*
             * 设置负责人
             */
            String userName = (String) map.get("field1");
            budgetEO.setCreateUserName(userName);

            String userId = baseService.getUserId(userName, deptId);
            budgetEO.setPm(userId);
            budgetEO.setCreateUserId(userId);

            /*
             * 设置周期
             */
            String baseDateStr = getCycle((String) map.get("field3"));
            budgetEO.setCycle(baseDateStr);
            if (StringUtils.isNotEmpty(baseDateStr)) {
                /*
                 *  若时间不为空，将开始时间设置为传入的时间，结束时间 为开始时间+1
                 */
                Date date = DateUtils.stringToDate(baseDateStr, DateUtils.YYYY_MM_DD_EN);
                budgetEO.setCycleBegin(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.YEAR, 1);
                budgetEO.setCycleEnd(calendar.getTime());
            }

            /*
             * 设置项目id
             */
            budgetEO.setDomainId((String) map.get("field4"));
            /*
             * 项目名称
             */
            budgetEO.setProjectName((String) map.get("field5"));
            budgetEO.setRemark((String) map.get("field7"));
            if (StringUtils.isNotEmpty(budgetEO.getProjectName())) {
                budgetEOList.add(budgetEO);
            }

        }
        return budgetEOList;

    }

    /**
     * 设置周期
     *
     * @param cycleBeginStr
     * @return
     */
    public static String getCycle(String cycleBeginStr) {

        StringBuilder cycleBuilder = new StringBuilder();
        if (cycleBeginStr != null) {
            String[] strArray = cycleBeginStr.split("-");
            int year = Integer.parseInt(strArray[0]) + 1;
            cycleBuilder.append(cycleBeginStr).append(" - ").append(year).append("-").append(strArray[1])
                        .append("-").append(strArray[2]);
        } else {
            cycleBuilder.append("null");
        }
        return cycleBuilder.toString();
    }
}
