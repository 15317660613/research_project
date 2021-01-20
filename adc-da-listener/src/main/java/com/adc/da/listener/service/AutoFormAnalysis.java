package com.adc.da.listener.service;

import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.OrgWithLevelEO;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.utils.JsonUtil;
import com.adc.da.form.dao.AdcFormDao;
import com.adc.da.form.entity.AdcFormEO;
import com.adc.da.listener.entity.InvoiceEO;
import com.adc.da.listener.utils.FormEOInit;
import com.adc.da.processform.dao.ProjectContractInvoiceEODao;
import com.adc.da.processform.entity.ProjectContractInvoiceEO;
import com.adc.da.progress.service.ProjectRateEOService;
import com.adc.da.research.service.ProjectSaveService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.adc.da.workflow.utils.contants.Constants;
import com.adc.da.workflow.utils.contants.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.FormService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.form.StartFormData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adc.da.budget.constant.ProjectType.BUSINESS_PROJECT;
import static com.adc.da.budget.constant.ProjectType.NO_BUSINESS_PROJECT;

/**
 * describe:
 * 针对经营类项目与非经营类项目 ServiceTask 执行类
 * 包含扩展表格的 解析方案
 * ${autoFormAnalysis}
 *  date 2019-11-05 该类仅用于日常事务类创建
 * @author 李坤澔
 *     date 2018-11-30
 */
@Component("autoFormAnalysis")
@Slf4j
public class AutoFormAnalysis implements JavaDelegate {

    @Autowired
    private AdcFormDao adcFormDao;

    @Autowired
    private ProjectSaveService projectSaveService;

    /**
     * 时间格式
     */
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 存入ES的数据
     */
    private Project resultEO = new Project();

    /**
     * 表单数据
     */
    private Map<String, String> jsonMap;

    /**
     * 表单内容数据
     */
    private Map<String, String> formMap;

    /**
     * 用于区分经营类与非经营类的标识
     */
    private int workflowType;

    /**
     * 扩展表格数据
     */
    private List<InvoiceEO> invoiceEOS = new ArrayList<>(1);

    @Autowired
    private FormService formService;

    @Autowired
    private ProjectContractInvoiceEODao projectContractInvoiceEODao;

    @Autowired
    private OrgListDao orgListDao;

    @Override
    public void execute(DelegateExecution execution) {

        try {
            StartFormData startFormData = formService.getStartFormData(execution.getProcessDefinitionId());
            String formKey;

            /*
             * 经营类校验
             */
            if (execution.getProcessDefinitionId().contains("p7111b710ba2d47cf9c1b87d2847060b9")) {
                workflowType = BUSINESS_PROJECT;
            } else {
                workflowType = NO_BUSINESS_PROJECT;
            }

            if (startFormData != null) {
                formKey = startFormData.getFormKey();
            } else {
                throw new AdcDaBaseException(ErrorType.TASK_GET_FORM_ERROR);
            }
            AdcFormEO adcFormEO = adcFormDao.selectByPrimaryKey(formKey);
            formMap = FormEOInit.initFormMap(adcFormEO);

            String formData = (String) execution.getVariable(Constants.GLOBAL_FORM_DATA);


            /*
             *  获取项目创建人
             */
            String initiator = (String) execution.getVariable("initiator");

            jsonMap = JsonUtil.jsonToBean(formData, HashMap.class);

            /*
             * 初始化
             */
            initProjectEO();
            //流程创建人
            String projectId = UUID.randomUUID10();

            resultEO.setCreateUserId(initiator);
            resultEO.setId(projectId);

            //  设置部门
            List<OrgWithLevelEO> orgEOList = orgListDao.getUserOrgWhitLeafAndLev(initiator);
            if (CollectionUtils.isNotEmpty(orgEOList)) {
                resultEO.setDeptId(orgEOList.get(0).getId());
            }

            if (workflowType == BUSINESS_PROJECT) {

                /*
                 * 存储合同信息
                 */
                saveInvoice();
                resultEO.setProjectType(BUSINESS_PROJECT);
                /*
                 *  经营类需要进行项目进度的存储
                 */
                projectRateEOService.initProjectProgressData(projectId, BUSINESS_PROJECT.toString());

            } else {
                resultEO.setProjectType(NO_BUSINESS_PROJECT);
            }

            /*存项目信息 */
            projectSaveService.insert(resultEO, true);

        } catch (Exception e) {
            log.error("autoFormAnalysis Exception", e);
            throw new AdcDaBaseException("表单分析失败");
        }

        log.debug("--------serviceTask已经执行已经执行！------------");

    }

    @Autowired
    private ProjectRateEOService projectRateEOService;

    /**
     * 保存合同信息
     *
     * @throws ParseException
     */
    private void saveInvoice() throws ParseException {
        /*
         * 初始化扩展表格数据
         */
        initMap();
        /*
         * 组装基础合同数据
         */
        ProjectContractInvoiceEO baseContractEO = new ProjectContractInvoiceEO();
        baseContractEO.setProjectId(resultEO.getId());
        baseContractEO.setContractId(resultEO.getContractNo());

        if (StringUtils.isNotEmpty(resultEO.getContractAmountStr())) {
            baseContractEO.setContractAmount(new BigDecimal(resultEO.getContractAmountStr()));

        }

        List<ProjectContractInvoiceEO> projectContractInvoiceEOS = new ArrayList<>();
        for (InvoiceEO eo : invoiceEOS) {
            ProjectContractInvoiceEO contractInvoiceEO = new ProjectContractInvoiceEO();
            /*复制基础合同*/
            BeanUtils.copyProperties(baseContractEO, contractInvoiceEO);
            /*只更新开票金额不为空的数据*/
            if (null != eo.getInvoiceAmount()) {
                contractInvoiceEO.setInvoiceAmount(eo.getInvoiceAmount());
                contractInvoiceEO.setInvoiceDate(eo.getDateInvoice());
                contractInvoiceEO.setRecieveMoneyDate(eo.getDateReceive());
                contractInvoiceEO.setId(UUID.randomUUID10());
                projectContractInvoiceEOS.add(contractInvoiceEO);
            }
        }

        /*存合同信息  */
        if (CollectionUtils.isNotEmpty(projectContractInvoiceEOS)) {
            projectContractInvoiceEODao.insertSelectiveAll(projectContractInvoiceEOS);
        }
    }

    /**
     * 组装EO数据，分为 经营类与非经营类
     *
     * @throws ParseException
     */
    private void initProjectEO() throws ParseException {

        String personInput;
        String timeStr;

        //业务方 与 人力投入 开始时间
        if (workflowType == BUSINESS_PROJECT) {
            resultEO.setProjectOwner(jsonMap.get(formMap.get("合同企业名称")));
            personInput = jsonMap.get(formMap.get("预计人力投入"));
            timeStr = formMap.get("合同签订时间");
            //合同金额
            if (null != formMap.get("合同金额")) {
                String amount = jsonMap.get(formMap.get("合同金额"));
                resultEO.setContractAmount(Float.parseFloat(amount));
                resultEO.setContractAmountStr(amount);
            }

            if (StringUtils.isNotEmpty(formMap.get("开始时间"))) {
                Date beginDate = formatter.parse(jsonMap.get(formMap.get("开始时间")));
                resultEO.setProjectBeginTime(beginDate);

            }

            if (StringUtils.isNotEmpty(formMap.get("结束时间"))) {
                Date endTime = formatter.parse(jsonMap.get(formMap.get("结束时间")));
                resultEO.setProjectEndTime(endTime);

            }

        } else {
            resultEO.setProjectOwner(jsonMap.get(formMap.get("业务方")));

            personInput = jsonMap.get(formMap.get("预计人力投入"));
            timeStr = formMap.get("创建时间");
        }

        //人力投入
        if (StringUtils.isNotEmpty(personInput)) {
            resultEO.setPersonInput(Integer.parseInt(personInput));
        }

        //开始时间
        if (StringUtils.isNotEmpty(timeStr)) {
            Date date = formatter.parse(jsonMap.get(timeStr));
            resultEO.setStartTime(date);
        }

        //项目名称
        resultEO.setName(jsonMap.get(formMap.get("项目名称")));
        //合同编号
        resultEO.setContractNo(jsonMap.get(formMap.get("合同编号")));

        //设置负责人以及组员
        setPmAndMembers();

        //业务类型
        resultEO.setBusinessId(jsonMap.get(formMap.get("业务类型ID")));
        //业务id
        resultEO.setBudgetId(jsonMap.get(formMap.get("所属业务ID")));

        //描述   项目描述
        resultEO.setProjectDescription(jsonMap.get(formMap.get("项目描述")));

    }

    /**
     * 对负责人以及组员进行初始化
     */
    private void setPmAndMembers() {

        String pmKey;
        String memberKey;
        if (workflowType == BUSINESS_PROJECT) {
            memberKey = formMap.get("项目组成员");
            pmKey = formMap.get("合同内部联系人");
        } else {
            pmKey = formMap.get("项目负责人");
            memberKey = formMap.get("人员");
        }
        String pmName = jsonMap.get(pmKey + "_usname");
        String pmId = jsonMap.get(pmKey + "_usid");

        //负责人
        resultEO.setProjectLeaderId(pmId.trim());
        resultEO.setProjectLeader(pmName);

        String memberIdIndex = memberKey + "_usid";
        String memberNameIndex = memberKey + "_usname";
        String memberIdString = jsonMap.get(memberIdIndex);

        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> memberMap;
        //成员   添加负责人到成员中
        if (StringUtils.isNotEmpty(memberIdString)) {
            String[] memberIds = jsonMap.get(memberIdIndex).split(",");
            String[] memberNames = jsonMap.get(memberNameIndex).split(",");
            StringBuilder nameBuilder = new StringBuilder();
            int len = memberIds.length;
            List<String> idList = new ArrayList<>(Arrays.asList(memberIds));
            List<String> memberNameList = new ArrayList<>(Arrays.asList(memberNames));
            if (!idList.contains(pmId)) {
                Map<String, String> pmMap = new HashMap<>();
                pmMap.put("id", pmId);
                pmMap.put("name", pmName);
                mapList.add(pmMap);
                nameBuilder.append(pmName).append(',');
                idList.add(pmId);
                memberNameList.add(pmName);
            }

            for (int i = 0; i < len; i++) {
                memberMap = new HashMap<>();
                memberMap.put("id", memberIds[i]);
                memberMap.put("name", memberNames[i]);
                mapList.add(memberMap);
                nameBuilder.append(memberNames[i]).append(',');
            }

            resultEO.setProjectMemberNames(nameBuilder.substring(0, nameBuilder.length() - 1));
            resultEO.setMemberNames(memberNameList.toArray(new String[0]));
            resultEO.setProjectMemberIds(idList.toArray(new String[0]));

        } else {
            memberMap = new HashMap<>();
            mapList.add(memberMap);
            memberMap.put("id", pmId);
            memberMap.put("name", pmName);

            resultEO.setProjectMemberNames(pmName);
            resultEO.setProjectMemberIds(new String[] {pmId});
            resultEO.setMemberNames(new String[] {pmName});
        }

        // 设置mapList
        resultEO.setMapList(mapList);

    }

    /**
     * 初始化结果集
     *
     * @throws ParseException
     */
    private void initMap() throws ParseException {
        invoiceEOS.clear();
        InvoiceEO invoiceEO;

        Map<String, String> listCtrl = new HashMap<>();
        Map<String, Integer> indexMap = new HashMap<>();
        int i = 0;
        /* 先初始化行数*/
        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(value) && key.contains("_listctrl_")) {

                if (key.contains("input_")) {
                    /*
                     *  序号
                     */
                    invoiceEO = new InvoiceEO();
                    invoiceEOS.add(invoiceEO);
                    /* 序号索引 */
                    indexMap.put(key.split("_")[4], i++);
                } else {
                    listCtrl.put(key, value);
                }
            }
        }

        /* 注入数据*/
        for (Map.Entry<String, String> entry : listCtrl.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(value)) {
                String[] indexString = key.split("_");

                /*第二位位行号*/
                int index = indexMap.get(indexString[4]);
                invoiceEO = invoiceEOS.get(index);
                if (key.contains("1_listctrl_")) {

                    /*
                     *  时间框 开票时间
                     */
                    invoiceEO.setDateInvoice(formatter.parse(value));
                } else if (key.contains("3_listctrl_")) {
                    /*
                     * 时间框  收款时间
                     */
                    invoiceEO.setDateReceive(formatter.parse(value));
                } else if (key.contains("2_listctrl_")) {
                    /*
                     * 开票金额
                     */
                    invoiceEO.setInvoiceAmount(new BigDecimal(value));
                }
                /*更新*/
                invoiceEOS.set(index, invoiceEO);
            }

        }

    }
}
