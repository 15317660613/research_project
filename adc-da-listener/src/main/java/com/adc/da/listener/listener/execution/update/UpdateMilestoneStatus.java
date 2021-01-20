package com.adc.da.listener.listener.execution.update;

import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.progress.dao.ProjectMilepostEODao;
import com.adc.da.progress.entity.ProjectMilepostEO;
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
 * * ${updateMilestoneStatus}
 *
 * @author 李坤澔
 *     date 2019-08-12
 */
@Component("updateMilestoneStatus")
public class UpdateMilestoneStatus implements ExecutionListener {

    @Autowired
    private transient ProjectMilepostEODao dao;

    /**
     * 流程编辑器中设置 1 表示更新删除 ext_info_1 字段
     */
    private FixedValue delFlag;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        /*
         * 获取表单数据
         */
        Map<String, Object> jsonMap = ListenerUtils
            .initGlobalFormDataMap((String) delegateExecution.getVariable(Constants.GLOBAL_FORM_DATA));
        String milestoneId = (String) jsonMap.get("milestoneInfo_id");

        ProjectMilepostEO eo = new ProjectMilepostEO();
        eo.setId(milestoneId);

        if (null != delFlag && "1".equals(delFlag.getExpressionText())) {
            eo.setExtInfo1("");

        } else {
            eo.setExtInfo1(delegateExecution.getProcessInstanceId());

        }

        dao.updateByPrimaryKeySelective(eo);
    }
}
