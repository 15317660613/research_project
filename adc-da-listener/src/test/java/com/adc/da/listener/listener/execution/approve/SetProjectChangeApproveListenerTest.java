package com.adc.da.listener.listener.execution.approve;

import com.adc.da.budget.entity.Project;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.adc.da.listener.utils.FormKeyWord.YES;
import static com.adc.da.listener.utils.FormType.INT_NEW;
import static com.adc.da.listener.utils.FormType.INT_OLD;

public class SetProjectChangeApproveListenerTest {

    @Before
    public void before() throws Exception {

        jsonData = "{\n"
            + "    \"select_1564650327815\": \"(卡达克)力帆集团ELV法规年度运维服务合同-CAMDS\",\n"
            + "    \"radio_1564650341241\": \"否\",\n"
            + "    \"inputorgselect_1564650454975\": \"回收利用部\",\n"
            + "    \"inputorgselect_1564650466799\": \"软件开发部\",\n"
            + "    \"radio_1564650489079\": \"否\",\n"
            + "    \"inputuserselect_1564650566623\": \"宁淼,庄梦梦,侯猛,邱婧,李甜\",\n"
            + "    \"inputuserselect_1564650579087\": \"管理员1号\",\n"
            + "    \"radio_1564650583053\": \"否\",\n"
            + "    \"textarea_1564650624351\": \"\",\n"
            + "    \"textarea_1564650636191\": \"\",\n"
            + "    \"radio_1564650640720\": \"否\",\n"
            + "    \"datetime_1564650740415\": \"\",\n"
            + "    \"datetime_1564650777127\": \"\",\n"
            + "    \"datetime_1564650870327\": \"2019-08-06\",\n"
            + "    \"datetime_1564650880639\": \"2019-08-06\",\n"
            + "    \"radio_1564650885130\": \"否\",\n"
            + "    \"input_1564710566075\": \"\",\n"
            + "    \"input_1564710583091\": \"\",\n"
            + "    \"input_1564710600443\": \"\",\n"
            + "    \"input_1564710613971\": \"\",\n"
            + "    \"input_1564710627277\": \"\",\n"
            + "    \"input_1564710692811\": \"\",\n"
            + "    \"input_1564710703274\": \"\",\n"
            + "    \"input_1564710716131\": \"\",\n"
            + "    \"input_1564710727691\": \"\",\n"
            + "    \"input_1564710738283\": \"\",\n"
            + "    \"radio_1564711119020\": \"是\",\n"
            + "    \"select_1564714706165_milepost\": \"里程碑13\",\n"
            + "    \"inputuserselect_1564714739876\": \"\",\n"
            + "    \"textarea_1564714818173\": \"1234\",\n"
            + "    \"datetime_1564714842260\": \"2019-08-07\",\n"
            + "    \"datetime_1564714856573\": \"2019-09-27\",\n"
            + "    \"input_156505469450070\": \"12\",\n"
            + "    \"input_1564715083563\": \"新里程碑名称1\",\n"
            + "    \"inputuserselect_1564715107124\": \"管理员1号\",\n"
            + "    \"textarea_1564715119788\": \"新里程碑目标1\",\n"
            + "    \"datetime_1564715133524\": \"2019-08-06\",\n"
            + "    \"datetime_1564715146244\": \"2019-08-06\",\n"
            + "    \"input_1564715168604\": \"新预计起止时间1\",\n"
            + "    \"input_1565054740609\": \"新预计起止时间12\",\n"
            + "    \"select_1565054697418_milepost_1\": \"里程碑13\",\n"
            + "    \"inputuserselect_15650546974182\": \"\",\n"
            + "    \"textarea_15650546974183\": \"1234\",\n"
            + "    \"datetime_15650546974184\": \"2019-08-07\",\n"
            + "    \"datetime_15650546974185\": \"2019-09-27\",\n"
            + "    \"input_156505470892970\": \"12\",\n"
            + "    \"input_15650546974187\": \"新里程碑名称2\",\n"
            + "    \"inputuserselect_15650546974188\": \"管理员1号\",\n"
            + "    \"textarea_15650546974189\": \"新里程碑名称2\",\n"
            + "    \"datetime_156505469741810\": \"2019-08-04\",\n"
            + "    \"datetime_156505469741811\": \"2019-08-06\",\n"
            + "    \"input_156505469741812\": \"新预计成果物2\",\n"
            + "    \"input_1565054736338\": \"新预计成果物23\",\n"
            + "    \"proMilepostArr\": [\n"
            + "        {\n"
            + "            \"id\": \"NXRD854E2T\",\n"
            + "            \"oldName\": {\n"
            + "                \"value\": \"里程碑13\",\n"
            + "                \"name\": \"select_1564714706165_milepost\"\n"
            + "            },\n"
            + "            \"newName\": {\n"
            + "                \"value\": \"新里程碑名称1\",\n"
            + "                \"name\": \"input_1564715083563\"\n"
            + "            },\n"
            + "            \"oldUser\": {\n"
            + "                \"usid\": \"GHVRTMA9H2\",\n"
            + "                \"value\": \"\",\n"
            + "                \"name\": \"inputuserselect_1564714739876\"\n"
            + "            },\n"
            + "            \"newUser\": {\n"
            + "                \"usid\": \"GHVRTMA9H2\",\n"
            + "                \"usname\": \"管理员1号\",\n"
            + "                \"name\": \"inputuserselect_1564715107124\"\n"
            + "            },\n"
            + "            \"oldTarget\": {\n"
            + "                \"value\": \"1234\",\n"
            + "                \"name\": \"textarea_1564714818173\"\n"
            + "            },\n"
            + "            \"newTarget\": {\n"
            + "                \"value\": \"新里程碑目标1\",\n"
            + "                \"name\": \"textarea_1564715119788\"\n"
            + "            },\n"
            + "            \"oldDate\": {\n"
            + "                \"startValue\": \"2019-08-07\",\n"
            + "                \"startName\": \"datetime_1564714842260\",\n"
            + "                \"endName\": \"datetime_1564714856573\",\n"
            + "                \"endValue\": \"2019-09-27\"\n"
            + "            },\n"
            + "            \"newDate\": {\n"
            + "                \"startValue\": \"2019-08-06\",\n"
            + "                \"startName\": \"datetime_1564715133524\",\n"
            + "                \"endName\": \"datetime_1564715146244\",\n"
            + "                \"endValue\": \"2019-08-06\"\n"
            + "            },\n"
            + "            \"newOutcomes\": [\n"
            + "                {\n"
            + "                    \"value\": \"新预计起止时间1\",\n"
            + "                    \"name\": \"input_1564715168604\"\n"
            + "                },\n"
            + "                {\n"
            + "                    \"value\": \"新预计起止时间12\",\n"
            + "                    \"name\": \"input_1565054740609\"\n"
            + "                }\n"
            + "            ],\n"
            + "            \"oldOutcomes\": [\n"
            + "                {\n"
            + "                    \"id\": \"BTFPN4H7X6\",\n"
            + "                    \"value\": \"12\",\n"
            + "                    \"name\": \"input_156505469450070\"\n"
            + "                }\n"
            + "            ]\n"
            + "        },\n"
            + "        {\n"
            + "            \"id\": \"NXRD854E2T\",\n"
            + "            \"oldName\": {\n"
            + "                \"value\": \"里程碑13\",\n"
            + "                \"name\": \"select_1565054697418_milepost_1\"\n"
            + "            },\n"
            + "            \"newName\": {\n"
            + "                \"value\": \"新里程碑名称2\",\n"
            + "                \"name\": \"input_15650546974187\"\n"
            + "            },\n"
            + "            \"oldUser\": {\n"
            + "                \"usid\": \"GHVRTMA9H2\",\n"
            + "                \"value\": \"\",\n"
            + "                \"name\": \"inputuserselect_15650546974182\"\n"
            + "            },\n"
            + "            \"newUser\": {\n"
            + "                \"usid\": \"GHVRTMA9H2\",\n"
            + "                \"value\": \"管理员1号\",\n"
            + "                \"name\": \"inputuserselect_15650546974188\"\n"
            + "            },\n"
            + "            \"oldTarget\": {\n"
            + "                \"value\": \"1234\",\n"
            + "                \"name\": \"textarea_15650546974183\"\n"
            + "            },\n"
            + "            \"newTarget\": {\n"
            + "                \"value\": \"新里程碑名称2\",\n"
            + "                \"name\": \"textarea_15650546974189\"\n"
            + "            },\n"
            + "            \"oldDate\": {\n"
            + "                \"startValue\": \"2019-08-07\",\n"
            + "                \"startName\": \"datetime_15650546974184\",\n"
            + "                \"endName\": \"datetime_15650546974185\",\n"
            + "                \"endValue\": \"2019-09-27\"\n"
            + "            },\n"
            + "            \"newDate\": {\n"
            + "                \"startValue\": \"2019-08-04\",\n"
            + "                \"startName\": \"datetime_156505469741810\",\n"
            + "                \"endName\": \"datetime_156505469741811\",\n"
            + "                \"endValue\": \"2019-08-06\"\n"
            + "            },\n"
            + "            \"newOutcomes\": [\n"
            + "                {\n"
            + "                    \"value\": \"新预计成果物2\",\n"
            + "                    \"name\": \"input_156505469741812\"\n"
            + "                },\n"
            + "                {\n"
            + "                    \"value\": \"新预计成果物23\",\n"
            + "                    \"name\": \"input_1565054736338\"\n"
            + "                }\n"
            + "            ],\n"
            + "            \"oldOutcomes\": [\n"
            + "                {\n"
            + "                    \"id\": \"BTFPN4H7X6\",\n"
            + "                    \"value\": \"12\",\n"
            + "                    \"name\": \"input_156505470892970\"\n"
            + "                }\n"
            + "            ]\n"
            + "        }\n"
            + "    ],\n"
            + "    \"select_1564650327815_proid\": \"TXYFPTP8E5\",\n"
            + "    \"select_1564650327815_proname\": \"(卡达克)力帆集团ELV法规年度运维服务合同-CAMDS\",\n"
            + "    \"inputorgselect_1564650454975_id\": \"R7YQVKUNPP\",\n"
            + "    \"inputorgselect_1564650454975_name\": \"回收利用部\",\n"
            + "    \"inputorgselect_1564650466799_id\": \"MEGMW98MVJ\",\n"
            + "    \"inputorgselect_1564650466799_name\": \"软件开发部\",\n"
            + "    \"inputuserselect_1564650566623_usid\": \"宁淼,庄梦梦,侯猛,邱婧,李甜\",\n"
            + "    \"inputuserselect_1564650566623_usname\": \"宁淼,庄梦梦,侯猛,邱婧,李甜\",\n"
            + "    \"inputuserselect_1564650579087_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_1564650579087_usname\": \"管理员1号\",\n"
            + "    \"select_1564714706165_milepost_proid\": \"NXRD854E2T\",\n"
            + "    \"select_1564714706165_milepost_proname\": \"里程碑13\",\n"
            + "    \"inputuserselect_1564714739876_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_1564714739876_usname\": null,\n"
            + "    \"inputuserselect_1564715107124_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_1564715107124_usname\": \"管理员1号\",\n"
            + "    \"select_1565054697418_milepost_1_proid\": \"NXRD854E2T\",\n"
            + "    \"select_1565054697418_milepost_1_proname\": \"里程碑13\",\n"
            + "    \"inputuserselect_15650546974182_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_15650546974182_usname\": null,\n"
            + "    \"inputuserselect_15650546974188_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_15650546974188_usname\": \"管理员1号\"\n"
            + "}";

        jsonData = "{\n"
            + "    \"select_1564650327815\": \"(卡达克)力帆集团ELV法规年度运维服务合同-CAMDS\",\n"
            + "    \"radio_1564650341241\": \"否\",\n"
            + "    \"inputorgselect_1564650454975\": \"回收利用部\",\n"
            + "    \"inputorgselect_1564650466799\": \"软件开发部\",\n"
            + "    \"radio_1564650489079\": \"否\",\n"
            + "    \"inputuserselect_1564650566623\": \"宁淼,庄梦梦,侯猛,邱婧,李甜\",\n"
            + "    \"inputuserselect_1564650579087\": \"管理员1号\",\n"
            + "    \"radio_1564650583053\": \"否\",\n"
            + "    \"textarea_1564650624351\": \"\",\n"
            + "    \"textarea_1564650636191\": \"\",\n"
            + "    \"radio_1564650640720\": \"否\",\n"
            + "    \"datetime_1564650740415\": \"\",\n"
            + "    \"datetime_1564650777127\": \"\",\n"
            + "    \"datetime_1564650870327\": \"2019-08-06\",\n"
            + "    \"datetime_1564650880639\": \"2019-08-06\",\n"
            + "    \"radio_1564650885130\": \"是\",\n"
            + "    \"input_1564710566075\": \"1.00\",\n"
            + "    \"input_1564710583091\": \"2.00\",\n"
            + "    \"input_1564710600443\": \"3.00\",\n"
            + "    \"input_1564710613971\": \"4.00\",\n"
            + "    \"input_1564710627277\": \"10.00\",\n"
            + "    \"input_1564710692811\": \"\",\n"
            + "    \"input_1564710703274\": \"\",\n"
            + "    \"input_1564710716131\": \"\",\n"
            + "    \"input_1564710727691\": \"\",\n"
            + "    \"input_1564710738283\": \"\",\n"
            + "    \"radio_1564711119020\": \"是\",\n"
            + "    \"select_1564714706165_milepost\": \"\",\n"
            + "    \"inputuserselect_1564714739876\": \"\",\n"
            + "    \"textarea_1564714818173\": \"\",\n"
            + "    \"datetime_1564714842260\": \"\",\n"
            + "    \"datetime_1564714856573\": \"\",\n"
            + "    \"input_1564714869133\": \"\",\n"
            + "    \"input_1564715083563\": \"\",\n"
            + "    \"inputuserselect_1564715107124\": \"管理员1号\",\n"
            + "    \"textarea_1564715119788\": \"\",\n"
            + "    \"datetime_1564715133524\": \"2019-08-06\",\n"
            + "    \"datetime_1564715146244\": \"2019-08-06\",\n"
            + "    \"input_1564715168604\": \"\",\n"
            + "    \"proMilepostArr\": [\n"
            + "        {\n"
            + "            \"oldName\": {\n"
            + "                \"value\": \"\",\n"
            + "                \"name\": \"select_1564714706165_milepost\"\n"
            + "            },\n"
            + "            \"newName\": {\n"
            + "                \"value\": \"\",\n"
            + "                \"name\": \"input_1564715083563\"\n"
            + "            },\n"
            + "            \"oldUser\": {\n"
            + "                \"usid\": \"GHVRTMA9H2\",\n"
            + "                \"value\": \"\",\n"
            + "                \"name\": \"inputuserselect_1564714739876\"\n"
            + "            },\n"
            + "            \"newUser\": {\n"
            + "                \"usid\": \"GHVRTMA9H2\",\n"
            + "                \"value\": \"管理员1号\",\n"
            + "                \"name\": \"inputuserselect_1564715107124\"\n"
            + "            },\n"
            + "            \"oldTarget\": {\n"
            + "                \"value\": \"\",\n"
            + "                \"name\": \"textarea_1564714818173\"\n"
            + "            },\n"
            + "            \"newTarget\": {\n"
            + "                \"value\": \"\",\n"
            + "                \"name\": \"textarea_1564715119788\"\n"
            + "            },\n"
            + "            \"oldDate\": {\n"
            + "                \"startValue\": \"\",\n"
            + "                \"startName\": \"datetime_1564714842260\",\n"
            + "                \"endName\": \"datetime_1564714856573\",\n"
            + "                \"endValue\": \"\"\n"
            + "            },\n"
            + "            \"newDate\": {\n"
            + "                \"startValue\": \"2019-08-06\",\n"
            + "                \"startName\": \"datetime_1564715133524\",\n"
            + "                \"endName\": \"datetime_1564715146244\",\n"
            + "                \"endValue\": \"2019-08-06\"\n"
            + "            },\n"
            + "            \"newOutcomes\": [\n"
            + "                {\n"
            + "                    \"value\": \"\",\n"
            + "                    \"name\": \"input_1564715168604\"\n"
            + "                }\n"
            + "            ],\n"
            + "            \"oldOutcomes\": [\n"
            + "                {\n"
            + "                    \"value\": \"\",\n"
            + "                    \"name\": \"input_1564714869133\"\n"
            + "                }\n"
            + "            ]\n"
            + "        }\n"
            + "    ],\n"
            + "    \"costId\": \"IDIDIDIDI\",\n"
            + "    \"select_1564650327815_proid\": \"TXYFPTP8E5\",\n"
            + "    \"select_1564650327815_proname\": \"(卡达克)力帆集团ELV法规年度运维服务合同-CAMDS\",\n"
            + "    \"inputorgselect_1564650454975_id\": \"R7YQVKUNPP\",\n"
            + "    \"inputorgselect_1564650454975_name\": \"回收利用部\",\n"
            + "    \"inputorgselect_1564650466799_id\": \"MEGMW98MVJ\",\n"
            + "    \"inputorgselect_1564650466799_name\": \"软件开发部\",\n"
            + "    \"inputuserselect_1564650566623_usid\": \"宁淼,庄梦梦,侯猛,邱婧,李甜\",\n"
            + "    \"inputuserselect_1564650566623_usname\": \"宁淼,庄梦梦,侯猛,邱婧,李甜\",\n"
            + "    \"inputuserselect_1564650579087_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_1564650579087_usname\": \"管理员1号\",\n"
            + "    \"inputuserselect_1564714739876_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_1564714739876_usname\": \"\",\n"
            + "    \"inputuserselect_1564715107124_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_1564715107124_usname\": \"管理员1号\"\n"
            + "}";

        formMap = new HashMap<>();
        formMap.put("项目承担部门变更", "radio_1564650341241");
        formMap.put("项目组成员变更", "radio_1564650489079");
        formMap.put("项目基本情况变更", "radio_1564650583053");
        formMap.put("项目起止时间变更", "radio_1564650640720");
        formMap.put("项目成本预算变更", "radio_1564650885130");
        formMap.put("项目里程碑变更", "radio_1564711119020");
        formMap.put("原人工成本", "input_1564710566075");
        formMap.put("原采购成本", "input_1564710583091");
        formMap.put("原跨部门协作成本", "input_1564710600443");
        formMap.put("原其他费用", "input_1564710613971");
        formMap.put("原成本预算总金额", "input_1564710627277");
        formMap.put("新人工成本", "input_1564710692811");
        formMap.put("新采购成本", "input_1564710703274");
        formMap.put("新跨部门协作成本", "input_1564710716131");
        formMap.put("新其他费用", "input_1564710727691");
        formMap.put("新成本预算总金额", "input_1564710738283");
    }

    private String jsonData;

    private Map<String, String> formMap;

    private Map<String, Object> jsonMap;

    @Test
    public void a() throws Exception {
        Gson gson = new Gson();

        Type type = new TypeToken<LinkedTreeMap<String, Object>>() {
        }.getType();

        jsonMap = gson.fromJson(jsonData, type);

        Project project;
        project = new Project();

        project.setModifyTime(new Date());
        /*  部门变更      */
        if (changeCheck("项目承担部门变更")) {
            return;
        }

        /*起止时间*/
        if (changeCheck("项目起止时间变更")) {
            if (setProjectDate()) {
                return;
            }
        }

        /*         * 预算成本         */
        if (changeCheck("项目成本预算变更")) {
            if (checkBudget()) {
                return;
            }
        }

        /*
         * 都未超额，则完成流程
         */
        //todo    delegateExecution.setVariable("approve", "hide_a");
    }

    /**
     * 进行成本预算的判断，如果新总额大于原，返回true
     */
    private boolean checkBudget() {

        String[] costStr = new String[2];
        float[] cost = new float[2];

        costStr[INT_NEW] = "新成本预算总金额";
        costStr[INT_OLD] = "原成本预算总金额";

        String[] s = new String[2];

        s[INT_NEW] = (String) jsonMap.get(formMap.get(costStr[INT_NEW]));
        s[INT_OLD] = (String) jsonMap.get(formMap.get(costStr[INT_OLD]));

        if (StringUtils.isNotEmpty(s[INT_NEW])) {
            cost[INT_NEW] = Float.parseFloat(s[INT_NEW]);
        } else {
            cost[INT_NEW] = 0.0F;
        }

        if (StringUtils.isNotEmpty(s[INT_OLD])) {
            cost[INT_OLD] = Float.parseFloat(s[INT_OLD]);
        } else {
            cost[INT_OLD] = 0.0F;
        }

        return cost[INT_NEW] > cost[INT_OLD];

    }

    /**
     * 针对项目 时间变更进行调整
     * 若项目延期，返回true
     *
     * @return
     */
    private boolean setProjectDate() {
        String[] time = new String[2];

        time[INT_NEW] = (String) jsonMap.get(formMap.get("新结束时间"));
        time[INT_OLD] = (String) jsonMap.get(formMap.get("原结束时间"));

        Date[] date = new Date[2];

        try {

            if (StringUtils.isNotEmpty(time[INT_NEW])) {
                date[INT_NEW] = DateUtils.stringToDate(time[INT_NEW], DateUtils.YYYY_MM_DD_EN);
            } else {
                return true;
            }
            /**
             * 原结束时间为空，判断认为不延期
             */
            if (StringUtils.isNotEmpty(time[INT_OLD])) {
                date[INT_OLD] = DateUtils.stringToDate(time[INT_OLD], DateUtils.YYYY_MM_DD_EN);
            } else {
                return false;
            }

        } catch (Exception e) {
            throw new AdcDaBaseException("项目时间变更，项目起止时间输入异常");
        }

        return date[INT_NEW].getTime() > date[INT_OLD].getTime();
    }

    /**
     * 用于判断变更的单选框
     *
     * @param field
     * @return
     */
    private boolean changeCheck(String field) {
        return YES.equals(jsonMap.get(formMap.get(field)));
    }

}