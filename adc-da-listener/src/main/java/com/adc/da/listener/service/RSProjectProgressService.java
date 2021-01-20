package com.adc.da.listener.service;

import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.research.entity.ProgressEO;
import com.adc.da.research.service.ProgressEOService;
import com.adc.da.workflow.utils.contants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * describe:
 * 更新 ProgressEOService 科研项目进度（科研合同中的内容）
 * ${rSProjectProgressService}
 *
 * @author 李坤澔
 *     date 2019-08-27
 */
@Component("rSProjectProgressService")
@Slf4j
public class RSProjectProgressService implements JavaDelegate {

    @Autowired
    private ProgressEOService progressEOService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        /*
         * 获取表单数据
         */
        Map<String, Object> jsonMap = ListenerUtils
            .initGlobalFormDataMap((String) execution.getVariable(Constants.GLOBAL_FORM_DATA));
        String progressId = (String) jsonMap.get("select_1567409760846_proid");

        ProgressEO progressEO = new ProgressEO();
        progressEO.setId(progressId);
        /*
         * 完成
         */
        progressEO.setExtInfo1(1);
        progressEO.setExtInfo2(execution.getProcessInstanceId());
        /*
         * 存储完成时间
         */
        progressEO.setExtInfo3(Long.toString(new Date().getTime()));
        progressEOService.updateByPrimaryKeySelective(progressEO);

    }
}
