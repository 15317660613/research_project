package com.adc.da.listener.service;

import com.adc.da.listener.utils.RSProjectBusinessKey;
import com.adc.da.research.service.HiSyncNewDataService;
import com.adc.da.research.service.ProjectSaveService;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.adc.da.listener.utils.RSProjectBusinessKey.RS_BUSINESS_KEY;
import static com.adc.da.listener.utils.RSProjectBusinessKey.RS_PROJECT_ID;

/**
 * describe:
 * 科研变更-结项 -处理
 *
 * @author 李坤澔
 *     date 2019-08-27
 */
@Component("rSProjectEndService")
@Slf4j
public class RSProjectEndService implements JavaDelegate {

    @Autowired
    private ProjectSaveService projectSaveService;



    @Autowired
    private HiSyncNewDataService hiSyncNewDataService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String[] key = RSProjectBusinessKey.spitBusinessKey(execution.getProcessBusinessKey());




    }

}
