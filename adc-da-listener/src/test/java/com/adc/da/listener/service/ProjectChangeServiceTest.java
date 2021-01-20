package com.adc.da.listener.service;

import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.UserWithProjects;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.form.dao.AdcFormDao;
import com.adc.da.listener.entity.MilestoneChangeEO;
import com.adc.da.listener.utils.FormEOInit;
import com.adc.da.progress.dao.ProjectBudgetChangeEODao;
import com.adc.da.progress.dao.ProjectMilepostEODao;
import com.adc.da.progress.dao.ProjectMilepostResultEODao;
import com.adc.da.progress.entity.ProjectBudgetChangeEO;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.progress.entity.ProjectMilepostResultEO;
import com.adc.da.progress.service.ProjectBudgetChangeEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.adc.da.listener.utils.FormKeyWord.ID;
import static com.adc.da.listener.utils.FormKeyWord.UNDERLINE;
import static com.adc.da.listener.utils.FormKeyWord.US_ID;
import static com.adc.da.listener.utils.FormKeyWord.US_NAME;
import static com.adc.da.listener.utils.FormKeyWord.YES;
import static com.adc.da.listener.utils.FormType.END_TIME;
import static com.adc.da.listener.utils.FormType.INT_ID;
import static com.adc.da.listener.utils.FormType.INT_NAME;
import static com.adc.da.listener.utils.FormType.INT_NEW;
import static com.adc.da.listener.utils.FormType.INT_OLD;
import static com.adc.da.listener.utils.FormType.PROJECT_ID;
import static com.adc.da.listener.utils.FormType.PROJECT_NAME;
import static com.adc.da.listener.utils.FormType.BEGIN_TIME;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-08-02
 */
public class ProjectChangeServiceTest {

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

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Test
    public void a() throws Exception {
        Gson gson = new Gson();

        Type type = new TypeToken<LinkedTreeMap<String, Object>>() {
        }.getType();

        jsonMap = gson.fromJson(jsonData, type);

        projectInfo = FormEOInit.baseFormDataAnalysis(jsonMap);

        Project project;
        // todo project=projectRepository.findById(projectInfo[PROJECT_ID]);
        project = new Project();

        project.setModifyTime(new Date());
        /*  部门变更      */
        if (changeCheck("项目承担部门变更")) {
            String deptId = (String) jsonMap.get(formMap.get("新承担部门") + UNDERLINE + ID);
            project.setDeptId(deptId);
        }

        /*基本情况*/
        if (changeCheck("项目基本情况变更")) {
            String description = (String) jsonMap.get(formMap.get("新项目基本情况"));
            project.setProjectDescription(description);
        }

        /*起止时间*/
        if (changeCheck("项目起止时间变更")) {
            Date[] date = setProjectDate();
            project.setProjectBeginTime(date[BEGIN_TIME]);
            project.setProjectEndTime(date[END_TIME]);
        }


        /*  组员变更  */
        if (changeCheck("项目组成员变更")) {
            setProjectMember(project);
        }

        //更新project
        //todo projectRepository.save(project);

        /*         * 预算成本         */
        if (changeCheck("项目成本预算变更")) {
            setBudgetChange();
        }
        /*          * 里程碑变更相关结果集          */
        if (changeCheck("项目里程碑变更")) {
            String jsonString = gson.toJson(jsonMap.get("proMilepostArr"), ArrayList.class);

            Type typeArrayList = new TypeToken<ArrayList<MilestoneChangeEO>>() {
            }.getType();

            List<MilestoneChangeEO> milestoneEOList = gson.fromJson(jsonString, typeArrayList);

            /* 需要存储的结果集 */
            List<ProjectMilepostEO> milepostEOList = new ArrayList<>(milestoneData(milestoneEOList));

            //todo 里程碑存储
            for (ProjectMilepostEO eo : milepostEOList) {
//                milepostEODao.updateByPrimaryKey(eo);
            }
            //todo 交付物删除
//            milepostResultEODao.deleteByPrimaryKey()
            //todo 交付物存储
//            milepostResultEODao.insertList(milestoneResultEOList);

        }

    }

    @Autowired
    private AdcFormDao adcFormDao;

    @Autowired
    private ProjectBudgetChangeEODao dao;

    @Autowired
    private ProjectBudgetChangeEOService budgetChangeService;

    @Autowired
    private ProjectMilepostEODao milepostEODao;

    @Autowired
    private ProjectMilepostResultEODao milepostResultEODao;

    @Autowired
    private ProjectBudgetChangeEODao projectBudgetChangeEODao;

    /**
     * 组装项目组成员变更
     */
    private void setProjectMember(Project project) {

        String[] projectLeader = new String[2];
        projectLeader[INT_ID] = project.getProjectLeaderId();
        projectLeader[INT_NAME] = project.getProjectLeader();

        String[][] membersForm = new String[2][2];

        /* 记录已经被删除的用户
         */
        Set<String> deletedId = new TreeSet<>();

        if (StringUtils.isNotEmpty((String) jsonMap.get(formMap.get("新项目组成员")))) {
            //新数据
            membersForm[INT_NEW][INT_ID] = (String) jsonMap.get(formMap.get("新项目组成员") + UNDERLINE + US_ID);
            membersForm[INT_NEW][INT_NAME] = (String) jsonMap.get(formMap.get("新项目组成员") + UNDERLINE + US_NAME);

            //旧数据
            membersForm[INT_OLD][INT_ID] = (String) jsonMap.get(formMap.get("原项目组成员") + UNDERLINE + US_ID);
            membersForm[INT_OLD][INT_NAME] = (String) jsonMap.get(formMap.get("原项目组成员") + UNDERLINE + US_NAME);

            for (String id : membersForm[INT_OLD][INT_ID].split(",")) {
                if (!membersForm[INT_NEW][INT_ID].contains(id)) {
                    deletedId.add(id);
                }
            }
        } else {
            throw new AdcDaBaseException("项目组成员参数异常");
        }

        //todo 针对被删除的用户进行处理
        List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(projectInfo[PROJECT_ID], Boolean.TRUE);
        Set<String> taskIdSet = new TreeSet<>();
        Set<String> childTaskIdSet = new TreeSet<>();
        List<ChildrenTask> childrenTaskList;
        for (Task task : taskList) {
            taskIdSet.add(task.getId());
            String[][] memberNames = new String[2][];
            String[][] memberIds = new String[2][];
            memberIds[INT_OLD] = task.getMemberIds();
            memberNames[INT_OLD] = task.getMemberNames();

            List<Map<String, String>> taskMapList = new ArrayList<>();
            Map<String, String> idNameMap;

            List<String> idList = new ArrayList<>();
            List<String> nameList = new ArrayList<>();
            int length = memberIds[INT_OLD].length;
            for (int i = 0; i < length; i++) {
                /*
                 * 针对非删除用户进行特殊处理
                 */
                if (!deletedId.contains(memberIds[INT_OLD][i])) {
                    idNameMap = new HashMap<>();
                    idNameMap.put("id", memberIds[INT_OLD][i]);
                    idNameMap.put("name", memberNames[INT_OLD][i]);

                    taskMapList.add(idNameMap);
                    idList.add(memberIds[INT_OLD][i]);
                    nameList.add(memberNames[INT_OLD][i]);
                }
            }
            /*
             * 若idList非空，则直接将idList转换成数组
             * 若idList为空，将负责人信息写入
             */
            if (CollectionUtils.isNotEmpty(idList)) {
                memberIds[INT_NEW] = idList.toArray(new String[0]);
                memberNames[INT_NEW] = nameList.toArray(new String[0]);
            } else {
                memberIds[INT_NEW] = new String[1];
                memberIds[INT_NEW][0] = projectLeader[INT_ID];

                memberNames[INT_NEW] = new String[1];
                memberNames[INT_NEW][0] = projectLeader[INT_NAME];

                idNameMap = new HashMap<>();
                idNameMap.put("id", projectLeader[INT_ID]);
                idNameMap.put("name", projectLeader[INT_NAME]);

                taskMapList.add(idNameMap);
            }

            task.setMemberIds(memberIds[INT_NEW]);
            task.setMemberNames(memberNames[INT_NEW]);
            task.setMapsList(taskMapList);
        }

        //存储 任务
        //  taskRepository.save(taskList);

        /*
         * 子任务  子任务只有一个人，所以直接替换创建人，所属人即可
         */
        if (CollectionUtils.isNotEmpty(taskIdSet)) {
            childrenTaskList = childTaskRepository.findByTaskIdInAndDelFlagNot(
                taskIdSet,
                Boolean.TRUE);
            for (ChildrenTask childrenTask : childrenTaskList) {
                childTaskIdSet.add(childrenTask.getId());

                String childId = childrenTask.getCreateUserId();
                if (deletedId.contains(childId)) {
                    childrenTask.setCreateUserId(projectLeader[INT_ID]);
                    childrenTask.setPersonId(projectLeader[INT_ID]);
                    childrenTask.setPersonName(projectLeader[INT_NAME]);
                }
            }
        } else {
            childrenTaskList = new ArrayList<>();
        }

        //存储 子任务
        //todo childTaskRepository.save(childrenTaskList);

        /*
         * 针对被删去的用户，同步删除项目，任务，子任务对应的记录
         *
         */
        if (CollectionUtils.isNotEmpty(deletedId)) {

            List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(deletedId);

            for (UserWithProjects userWithProjects : userWithProjectsList) {

                Set<String> projectIds = userWithProjects.getProjectIds();
                projectIds.remove(projectInfo[PROJECT_ID]);

                userWithProjects.setProjectIds(projectIds);

                if (CollectionUtils.isNotEmpty(taskList)) {
                    Set<String> taskIds = userWithProjects.getTaskIds();
                    for (String taskId : taskIdSet) {
                        taskIds.remove(taskId);
                    }
                    userWithProjects.setTaskIds(taskIds);
                }

                if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                    Set<String> childTaskIds = userWithProjects.getChildTaskIds();
                    for (String taskId : childTaskIdSet) {
                        childTaskIds.remove(taskId);
                    }
                    userWithProjects.setChildTaskIds(childTaskIds);
                }

            }
            //进行存储
            //todo   userWithProjectsRepository.save(userWithProjectsList);
        }

        String[] memberStr = new String[2];

        if (membersForm[INT_NEW][INT_ID].contains(projectLeader[INT_ID])) {
            memberStr[INT_ID] = (membersForm[INT_NEW][INT_ID]);
            memberStr[INT_NAME] = (membersForm[INT_NEW][INT_NAME]);

        } else {
            memberStr[INT_ID] = (projectLeader[INT_ID] + "," + membersForm[INT_NEW][INT_ID]);
            memberStr[INT_NAME] = (projectLeader[INT_NAME] + "," + membersForm[INT_NEW][INT_NAME]);
        }

        /*
         * 用于 组装 mapList 以及 project 所需的String[]
         * 二维数组
         */
        String[][] memberMapList = new String[2][];

        memberMapList[INT_ID] = memberStr[INT_ID].split(",");
        memberMapList[INT_NAME] = memberStr[INT_NAME].split(",");

        project.setProjectMemberIds(memberMapList[INT_ID]);
        project.setMemberNames(memberMapList[INT_NAME]);
        project.setProjectMemberNames(memberStr[INT_NAME]);

        //todo  project.getMapList();
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> map;

        int length = memberMapList[INT_ID].length;

        for (int i = 0; i < length; i++) {
            map = new HashMap<>();
            map.put("name", memberMapList[INT_NAME][i]);
            map.put("id", memberMapList[INT_ID][i]);
            mapList.add(map);
        }

        project.setMapList(mapList);
    }

    /**
     * 进行成本预算的存储
     */
    private void setBudgetChange() {
        ProjectBudgetChangeEO budgetChangeEO = new ProjectBudgetChangeEO();
        budgetChangeEO.setProjectId(projectInfo[PROJECT_ID]);
        budgetChangeEO.setProjectName(projectInfo[PROJECT_NAME]);

        String[] costStr = new String[5];
        BigDecimal[] cost = new BigDecimal[5];
        /*  *  人工    */
        int labor = 0;
        /*   * 采购     */
        int purchase = 1;
        /*    * 跨部门         */
        int crossSectoral = 2;
        /*   * 其他费用         */
        int other = 3;
        /*   * 合计         */
        int amount = 4;

        costStr[labor] = "新人工成本";
        costStr[purchase] = "新采购成本";
        costStr[crossSectoral] = "新跨部门协作成本";
        costStr[other] = "新其他费用";
        costStr[amount] = "新成本预算总金额";

        int i = 0;
        String s;
        for (String str : costStr) {
            s = (String) jsonMap.get(formMap.get(str));
            if (StringUtils.isNotEmpty(s)) {
                cost[i] = new BigDecimal(s);
            } else {
                cost[i] = BigDecimal.valueOf(0.00) ;
            }
            i++;
        }
        budgetChangeEO.setAmountCount(cost[amount]);
        budgetChangeEO.setCooperationCost(cost[crossSectoral]);
        budgetChangeEO.setPersonCost(cost[labor]);
        budgetChangeEO.setPurchaseCost(cost[purchase]);
        budgetChangeEO.setOtherCost(cost[other]);

        /*
         * 根据成本id进行判断，若不存在则新增记录（历史数据）。否则进行更新操作
         */
        String costId = (String) jsonMap.get("costId");
        if (StringUtils.isNotEmpty(costId)) {
            budgetChangeEO.setId(costId);

            //  预算成本 修改
            //todo projectBudgetChangeEODao.updateByPrimaryKey(budgetChangeEO);

        } else {
            budgetChangeEO.setId(UUID.randomUUID10());

            //  预算成本 修改
            //todo  projectBudgetChangeEODao.insert(budgetChangeEO);

        }
    }

    /**
     * 针对项目 时间变更进行调整
     *
     * @return
     */
    private Date[] setProjectDate() {
        String[] time = new String[2];

        /*
         * 校验开始时间
         */
        if (StringUtils.isNotEmpty(jsonMap.get(formMap.get("新开始时间")))) {
            time[BEGIN_TIME] = (String) jsonMap.get(formMap.get("新开始时间"));
        } else {
            time[BEGIN_TIME] = (String) jsonMap.get(formMap.get("原开始时间"));

        }

        /*
         * 校验结束时间
         */
        if (StringUtils.isNotEmpty(jsonMap.get(formMap.get("新结束时间")))) {
            time[END_TIME] = (String) jsonMap.get(formMap.get("新结束时间"));

        } else {
            time[END_TIME] = (String) jsonMap.get(formMap.get("原结束时间"));

        }
        Date[] date = new Date[2];

        try {
            date[BEGIN_TIME] = DateUtils.stringToDate(time[BEGIN_TIME], DateUtils.YYYY_MM_DD_EN);
            date[END_TIME] = DateUtils.stringToDate(time[END_TIME], DateUtils.YYYY_MM_DD_EN);
        } catch (Exception e) {
            throw new AdcDaBaseException("项目时间变更，项目起止时间输入异常");
        }

        return date;
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

    /**
     * 表单基本数据，项目id与名称
     */
    private String[] projectInfo;

    /**
     * 构造里程碑数据集
     *
     * @param milestoneEOList
     * @return
     */
    private List<ProjectMilepostEO> milestoneData(List<MilestoneChangeEO> milestoneEOList) {
        String milestoneId;
        List<ProjectMilepostEO> res = new ArrayList<>(milestoneEOList.size());
        milestoneResultEOList = new ArrayList<>();

        for (MilestoneChangeEO eo : milestoneEOList) {
            milestoneId = eo.getId();

            ProjectMilepostEO resultEO = new ProjectMilepostEO();
            resultEO.setId(milestoneId);

            /* 组装时间 */
            Date[] dateArray = eo.getDateValue();
            resultEO.setMilepostBeginTime(dateArray[BEGIN_TIME]);
            resultEO.setMilepostEndTime(dateArray[END_TIME]);

            /* 组装负责人 */
            resultEO.setMilepostManagerId(eo.getUserIdValue());
            resultEO.setMilepostManagerName(eo.getUserNameValue());

            /* 目标 名称 */
            resultEO.setMilepostTarget(eo.getTargetValue());
            resultEO.setMilepostName(eo.getNameValue());

            /*项目信息*/
            resultEO.setProjectId(projectInfo[PROJECT_ID]);
            resultEO.setProjectName(projectInfo[PROJECT_NAME]);

            res.add(resultEO);
            /*
             * 成果物
             * 交付物初始化
             */
            List<String> outcomesValue = eo.getOutcomesValue();
            ProjectMilepostResultEO subBaseEO = new ProjectMilepostResultEO();
            subBaseEO.setMilepostId(milestoneId);
            milestoneResultEOList.addAll(setSubResult(subBaseEO, outcomesValue));

        }
        return res;
    }

    /**
     * 交付物结果集
     */
    private  List<ProjectMilepostResultEO> milestoneResultEOList;

    /**
     * 对交付物进行初始化,
     *
     * @param subBaseEO
     * @param outcomesValue
     */
    private List<ProjectMilepostResultEO> setSubResult(ProjectMilepostResultEO subBaseEO, List<String> outcomesValue) {
        List<ProjectMilepostResultEO> subResultList = new ArrayList<>();

        for (String outcomeValue : outcomesValue) {
            ProjectMilepostResultEO subResult = new ProjectMilepostResultEO();

            BeanUtils.copyProperties(subBaseEO, subResult);

            subResult.setId(UUID.randomUUID10());
            subResult.setFileName(outcomeValue);
            subResultList.add(subResult);
        }
        return subResultList;
    }

}
