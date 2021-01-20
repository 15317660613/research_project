package com.adc.da.listener.listener.execution.update;

import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.research.entity.ProgressEO;
import com.adc.da.research.service.ProgressEOService;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.workflow.utils.contants.Constants;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * describe:
 * 用于更新里程碑状态，用于里程碑完成审批
 * *
 * *  PR_PROJECT_MILEPOST
 * *
 * * ${updateRSProgressStatus}
 *
 * @author 李坤澔
 *     date 2019-08-12
 */
@Component("updateRSProgressStatus")
public class UpdateRSProgressStatus implements ExecutionListener {

    @Autowired
    private transient ProgressEOService progressEOService;

    /**
     * 流程编辑器中设置 1 表示更新删除 ext_info_1 字段
     */
    private FixedValue flag;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        /*
         * 获取表单数据
         */

        Map<String, Object> jsonMap = ListenerUtils
            .initGlobalFormDataMap((String) delegateExecution.getVariable(Constants.GLOBAL_FORM_DATA));
        String progressId = (String) jsonMap.get("select_1567409760846_proid");

        ProgressEO progressEO = new ProgressEO();
        progressEO.setId(progressId);
        /*
         * 完成
         */
        if (flag != null && StringUtils.isNotEmpty(flag.getExpressionText())) {
            if (("-1").equals(flag.getExpressionText())) {
                progressEO.setExtInfo2("-1");

            } else {
                progressEO.setExtInfo2(delegateExecution.getProcessInstanceId());

            }
        }
        progressEOService.updateByPrimaryKeySelective(progressEO, true);
    }
}
