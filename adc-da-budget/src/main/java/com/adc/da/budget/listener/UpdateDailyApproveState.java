package com.adc.da.budget.listener;

import com.adc.da.budget.entity.Daily;
import com.adc.da.file.entity.FileEO;
import com.adc.da.budget.entity.TaskResultEO;
import com.adc.da.budget.repository.DailyRepository;
import com.adc.da.budget.service.DailyService;
import com.adc.da.budget.service.TaskResultEOService;
import com.adc.da.budget.utils.ApplicationContextUtil;
import com.adc.da.file.service.FileEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("updateDailyApproveState")
@Slf4j
public class UpdateDailyApproveState implements ExecutionListener {

    @Autowired
    private transient DailyService dailyService;
    @Autowired
    private transient FileEOService fileEOService ;
    @Autowired
    private transient TaskResultEOService taskResultEOService ;
    @Autowired
    private transient DailyRepository dailyRepository;
    /**
     * 先获取流程设计时设置的受理组（即角色）信息，并删除该设置，
     * 然后查询和流程发起人同组织机构的该受理组人员，并设置为候选人
     */
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception{

        DailyRepository dailyRepository = ApplicationContextUtil.getBean(DailyRepository.class);
        String testValue = (String) delegateExecution.getVariable("approve");
        String dailyId = delegateExecution.getProcessBusinessKey();
        if (StringUtils.equals(testValue, "agreeView")) {
            //已阅
            Daily daily = dailyRepository.findOne(dailyId);
            daily.setApproveState(1);
            daily.setModifyTime(new Date());
            dailyRepository.save(daily);
            if(StringUtils.isNotEmpty(daily.getTaskResultFileId())){
                addTaskResult(daily);
            }
        } else if (StringUtils.equals(testValue, "100")) {
            //撤回置成草稿状态
            Daily daily = dailyRepository.findOne(dailyId);
            daily.setApproveState(4);
            daily.setModifyTime(null);
            //daily.setApproveUserId(null);
            //daily.setApproveUserName(null);

            /*dailyRepository.delete(dailyId);
            daily.setId(UUID.randomUUID().toString());*/

            List<Daily> childDailyList = dailyService.findByDailyParentIdAndAndApproveStateNotEqual(daily.getId(), 6);
            // 如果有人用认领 ，就不能删除了，否者按照parentId 把子日报全部干掉，重新生成
            if (CollectionUtils.isNotEmpty(childDailyList)) {
                //List<String> delUserIdList = ArrayUtils.compare(oldDaily.getChildrenDailyCreateUserIds(),daily.getChildrenDailyCreateUserIds());
                List<String> claimUserNameList = new ArrayList<>();
                for (Daily d : childDailyList) {
                    claimUserNameList.add(d.getCreateUserName());
                }
                throw new AdcDaBaseException(StringUtils.join(claimUserNameList, "、") + "已确认，不可进行撤回操作。");
            }else {
                dailyRepository.deleteByDailyParentId(daily.getId());
            }
            dailyRepository.save(daily);
        } else if (StringUtils.equals(testValue, "reject")) {
            //撤回置成草稿状态
            Daily daily = dailyRepository.findOne(dailyId);
            daily.setApproveState(5);
            //daily.setModifyTime(null);
            /*dailyRepository.delete(dailyId);
            daily.setId(UUID.randomUUID().toString());*/
            dailyRepository.deleteByDailyParentIdAndAndApproveState(daily.getId(),6); //如果是被驳回，那么只删除未被确认的日报
            dailyRepository.save(daily);
        } else {
            //发起提交
            Daily daily = dailyRepository.findOne(dailyId);
            daily.setApproveState(2);
            /* 更新审批人信息*/
            dailyService.updateDailyApproveUserInfo(daily);
            daily.setCreateTime(new Date());
            if(CollectionUtils.isNotEmpty(daily.getChildrenDailyCreateUserIds())) { // 是提交状态，就新增协作人
                dailyService.addAssistDaily(daily);
            }
            dailyRepository.save(daily);
        }
    }


    private void addTaskResult(Daily daily) throws Exception{

        FileEO fileEO = fileEOService.selectByPrimaryKey(daily.getTaskResultFileId());
        daily.setResultFileName(fileEO.getFileName());
        if (CollectionUtils.isNotEmpty(daily.getTaskIdArray())) {
            for (String taskId:daily.getTaskIdArray()) {
                TaskResultEO taskResultEO = new TaskResultEO();
                taskResultEO.setId(UUID.randomUUID10());
                taskResultEO.setTaskId(taskId);
                taskResultEO.setResultType("日报成果物");
                taskResultEO.setCreateTime(daily.getModifyTime());
                taskResultEO.setResultName(daily.getResultFileName());
                taskResultEOService.insertSelective(taskResultEO);
                if (null != fileEO){
                    fileEO.setForeignId(taskResultEO.getId());
                    fileEOService.updateByPrimaryKeySelective(fileEO);
                }else {
                    throw new AdcDaBaseException("当前日报关联的文件未找到，请联系日报填写者或管理员！");
                }
            }
        }
    }
}
