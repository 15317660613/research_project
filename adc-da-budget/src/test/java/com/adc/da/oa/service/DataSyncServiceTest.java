package com.adc.da.oa.service;

import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.elasticsearch.common.recycler.Recycler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class DataSyncServiceTest {

    private String json;

    @Before
    public void before() throws Exception {
        json = "[\n"
            + "\t{\n"
            + "\t\t\"field1\":\"刘洋\",\n"
            + "\t\t\"field2\":\"研发信息系统部\",\n"
            + "\t\t\"field3\":\"2019-06-21\",\n"
            + "\t\t\"field4\":\"201902270001\",\n"
            + "\t\t\"field5\":\"绿色供应链管理系统\",\n"
            + "\t\t\"field7\":\"项目描述\",\n"
            + "\t\t\n"
            + "\n"
            + "\n"
            + "\t\t\"field8\":\"其他\",\n"
            + "\t\t\"field9\":\"软件板块\",\n"
            + "\t\t\"field10\":\"\",\n"
            + "\t\t\"field11\":\"\",\n"
            + "\t\t\"field12\":\"\",\n"
            + "\t\t\"field13\":\"\",\n"
            + "\t\t\"field14\":\"\",\n"
            + "\t\t\"field15\":\"\",\n"
            + "\t\t\"field16\":\"\"\n"
            + "\t},{\n"
            + "\t\t\"field1\":\"孟鑫\",\n"
            + "\t\t\"field2\":\"研发信息系统部\",\n"
            + "\t\t\"field3\":\"2019-02-27\",\n"
            + "\t\t\"field4\":\"201902270002\",\n"
            + "\t\t\"field5\":\"关键件溯源管理系统\",\n"
            + "\t\t\"field6\":\"项目进行\",\n"
            + "\t\t\"field7\":\"项目描述\",\n"
            + "\t\t\"field8\":\"其他\",\n"
            + "\t\t\"field9\":\"软件板块\",\n"
            + "\t\t\"field10\":\"\",\n"
            + "\t\t\"field11\":\"\",\n"
            + "\t\t\"field12\":\"\",\n"
            + "\t\t\"field13\":\"\",\n"
            + "\t\t\"field14\":\"\",\n"
            + "\t\t\"field15\":\"\",\n"
            + "\t\t\"field16\":\"\"\n"
            + "\t}\n"
            + "]";
    }

    @Test
    public void checkJson() {
        Gson gson = new Gson();

        Type typeList = new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType();

        List<Map<String, Object>> jsonList = gson.fromJson(json, typeList);

        BudgetEO budgetEO;
        List<BudgetEO> budgetEOList = new ArrayList<>(jsonList.size());
        for (Map map : jsonList) {
            budgetEO = new BudgetEO();
            budgetEO.setProperty("0");
            budgetEO.setPropertyId("cx");


            String deptName=(String) map.get("field2");
            budgetEO.setDeptName(deptName);

            //todo
//            String deptId= getDeptId(deptName);
//            budgetEO.setDeptId(deptId);

            String userName=(String) map.get("field1");
            budgetEO.setCreateUserName(userName);

            //todo
            //String userId=getUserId(userName,deptId);
            //budgetEO.setPm(userId);
            //budgetEO.setCreateUserId(userId);

            /*
             * 设置周期
             */
            budgetEO.setCycle(getCycle((String) map.get("field3")));

            budgetEO.setDomainId((String) map.get("field4"));
            /*
             * 项目名称
             */
            budgetEO.setProjectName((String) map.get("field5"));
            budgetEO.setRemark((String) map.get("field7"));
            if (StringUtils.isNotEmpty(budgetEO.getProjectName())) { budgetEOList.add(budgetEO); }

        }
        log.println(budgetEOList);
    }

    @Autowired
    private OrgEODao orgEODao;

    private String getDeptId(String deptName) {
        List<OrgEO> orgEOList = orgEODao.getOrgEOByOrgName(deptName);
        if (CollectionUtils.isNotEmpty(orgEOList)) {
            return orgEOList.get(0).getId();
        } else {
            return null;
        }
    }

    @Autowired
    private UserEPDao userEPDao;

    private String getUserId(String userName, String deptId) {
        List<String> userIdList = userEPDao.queryUserIdByNameAndOrgId(userName, deptId);
        if(CollectionUtils.isNotEmpty(userIdList)){
            return  userIdList.get(0);
        }else{
            return null;
        }
    }

    private String getCycle(String cycleBeginStr ){

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