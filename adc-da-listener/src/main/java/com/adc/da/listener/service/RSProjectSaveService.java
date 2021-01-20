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
 * 科研变更-保存 服务
 *
 * @author 李坤澔
 *     date 2019-08-27
 */
@Component("rSProjectSaveService")
@Slf4j
public class RSProjectSaveService implements JavaDelegate {

    @Autowired
    private ProjectSaveService projectSaveService;

    /**
     * 项目创建人（默认给 刘复星）
     */
    private FixedValue pm;

    @Autowired
    private HiSyncNewDataService hiSyncNewDataService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String[] key = RSProjectBusinessKey.spitBusinessKey(execution.getProcessBusinessKey());
        /*
         * 刘复星 id
         */
        String createUID;
        if (pm != null && StringUtils.isNotEmpty(pm.getExpressionText())) {
            createUID = pm.getExpressionText();
        } else {
            createUID = "JC9TFGZDEC";
        }
        /*
         * 将数据同步到base表
         */
        hiSyncNewDataService.syncNewData(key[RS_PROJECT_ID], key[RS_BUSINESS_KEY]);



        /*
         * 更新es
         */
        projectSaveService.save(key[RS_PROJECT_ID], createUID, false);
    }

}
