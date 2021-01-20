package com.adc.da.listener.listener.execution.approve;

import com.adc.da.capital.dao.CapitalBudgetEODao;
import com.adc.da.capital.entity.CapitalBudgetEO;
import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.listener.utils.RSProjectBusinessKey;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.workflow.utils.contants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.adc.da.listener.utils.ListenerUtils.ACT_PROJECT_ID;
import static com.adc.da.listener.utils.ListenerUtils.ACT_SPECIAL_PROCESS_FLAG;
import static com.adc.da.listener.utils.ListenerUtils.HIDE_A;
import static com.adc.da.listener.utils.RSProjectBusinessKey.RS_PROJECT_ID;
import static com.adc.da.workflow.utils.contants.TaskCompleteType.AGREE;
import static com.adc.da.workflow.utils.contants.TaskCompleteType.APPROVE;

/**
 * 针对流程管理系列流程
 * 科研管理流程 中期检查 针对小于10万元的不需要本部长审批，走hide_a
 * 科研流程-》结项 也使用该监听
 * ${setApproveMidtermListener}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("setApproveMidtermListener")
@Slf4j
public class SetApproveMidtermListener implements ExecutionListener {

    @Autowired
    private transient CapitalBudgetEODao capitalBudgetEODao;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        /*针对同意的情况进行二次判断*/
        /*
         *  特殊流程直接进入科委会
         *  hide_a 表示进入总监审批
         */
        /*
         * 非特殊流程
         */
        if (AGREE.equals(delegateExecution.getVariable(APPROVE))
            && delegateExecution.getVariable(ACT_SPECIAL_PROCESS_FLAG) == null) {
            /*
             *  非特殊流程，进行下列处理
             */
            normalSituation(delegateExecution);
        }
    }

    /**
     * 判定 之后流程走向
     * 1. 结项
     * 2. 中期检查
     * 3. 终止 （不做金额校验）
     *
     * @param delegateExecution
     */
    private void normalSituation(DelegateExecution delegateExecution) {
        String processDefKey = delegateExecution.getProcessDefinitionId().split(":")[0];

        String projectId;
        if ("p9d12f5b27f9a482c8772de52d2825334".equals(processDefKey)) {
            /*
             * 结项
             */
            projectId = doEndingProcess(delegateExecution);
        } else if ("pcc2adec9710c4d46ac3dbeb725c80e2c".equals(processDefKey)) {
            /*
             *  中期检查
             */
            projectId = doMidTermProcess(delegateExecution);
        } else {
            /*
             *  科研-终止 流程 不做金额校验 直接进入总监审批
             */
            delegateExecution.setVariable(APPROVE, HIDE_A);
            return;
        }
        CapitalBudgetEO budgetEO = capitalBudgetEODao.selectByPrimaryKey(projectId);
        /*不符合关建指标进行本部长审批*/
        Optional<CapitalBudgetEO> opt = Optional.ofNullable(budgetEO);
        if (opt.isPresent()) {
            String centerBudget = opt.get().getCenterBudget();
            float f = Float.parseFloat(centerBudget);
            /*
             * 大于10W，进行本部长审批
             */
            if (f >= 10) {
                delegateExecution.setVariable(APPROVE, HIDE_A);
            }
        }
    }

    /**
     * 结项 进行处理
     *
     * @param delegateExecution
     */
    private String doEndingProcess(DelegateExecution delegateExecution) {
        delegateExecution.getVariable(ACT_PROJECT_ID);
        String[] key = RSProjectBusinessKey.spitBusinessKey(delegateExecution.getProcessBusinessKey());
        return key[RS_PROJECT_ID];
    }

    /**
     * 对中期检查进行处理
     *
     * @param delegateExecution
     */
    private String doMidTermProcess(DelegateExecution delegateExecution) {
        Map<String, Object> map = ListenerUtils
            .initGlobalFormDataMap((String) delegateExecution.getVariable(Constants.GLOBAL_FORM_DATA));
        if (map != null) {
            return (String) map.get("selectBusiness_1567489983968_proid");
        } else {
            throw new AdcDaBaseException("SetActDeptIDListener globalFormData   is null");
        }
    }
}

