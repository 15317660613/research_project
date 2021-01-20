package com.adc.da.research.project.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.config.entity.GradeRulesInfoEO;
import com.adc.da.research.config.service.GradeRulesInfoService;
import com.adc.da.research.project.dao.JudgeInfoEODao;
import com.adc.da.research.project.dao.ScoreInfoEODao;
import com.adc.da.research.project.entity.JudgeInfoEO;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.entity.ScoreInfoEO;
import com.adc.da.research.project.vo.JudgeInfoVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>RS_JUDGE_INFO JudgeInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("judgeInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class JudgeInfoEOService extends BaseService<JudgeInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(JudgeInfoEOService.class);

    @Autowired
    private JudgeInfoEODao dao;
    @Autowired
    private ProjectDataEOService projectDataEOService;
    @Autowired
    private GradeRulesInfoService gradeRulesInfoService;
    @Autowired
    private ScoreInfoEOService scoreInfoEOService;
    @Autowired
    private ScoreInfoEODao scoreInfoEODao;


    public JudgeInfoEODao getDao() {
        return dao;
    }

    /**
     * 批量新增
     *
     * @param judgeInfoVO
     */
    public void batchInsertSelective(JudgeInfoVO judgeInfoVO) throws Exception {
        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if(StringUtils.isEmpty(judgeInfoVO)){
            return;
        }
        List<GradeRulesInfoEO> gradeRulesInfoEOS = gradeRulesInfoService.selectById(judgeInfoVO.getRatingRulesId()); //查询评分细则
        List<ScoreInfoEO> scoreInfoEOList=new ArrayList<>();//评分
        List<JudgeInfoEO> judgeInfoEOList=new LinkedList<>();//评审

        String expertUserIds=judgeInfoVO.getExpertUserId();
        String expertUserNames=judgeInfoVO.getExpertUserName();
        String projectId=judgeInfoVO.getProjectId();
        //线下或者专家为空  添加管理员
        if(StringUtils.isEmpty(expertUserIds)||StringUtils.isEmpty(expertUserNames)||StringUtils.equals(judgeInfoVO.getJudgeMethodId(),"X97H5VX6MT")){
            JudgeInfoEO judgeInfoEO=new JudgeInfoEO();
            String id=UUID.randomUUID10();
            judgeInfoEO.setId(id);
            judgeInfoEO.setJudgeMethodId(judgeInfoVO.getJudgeMethodId());
            judgeInfoEO.setProjectId(judgeInfoVO.getProjectId());
            judgeInfoEO.setRatingRulesId(judgeInfoVO.getRatingRulesId());
            judgeInfoEO.setExpertUserId(user.getUsid());
            judgeInfoEO.setExpertUserName(user.getUsname());
            judgeInfoEO.setReviewRemark(judgeInfoVO.getReviewRemark());
            judgeInfoEOList.add(judgeInfoEO);
            for (GradeRulesInfoEO g:gradeRulesInfoEOS) {
                ScoreInfoEO scoreInfoEO=new ScoreInfoEO();
                scoreInfoEO.setId(UUID.randomUUID10());
                scoreInfoEO.setProjectId(projectId);
                scoreInfoEO.setExpertUserId(user.getUsid());//管理员id
                scoreInfoEO.setExpertUserName(user.getUsname());//管理员姓名
                scoreInfoEO.setGradingRulesId(g.getGradingRulesId());//评分细则id
                scoreInfoEOList.add(scoreInfoEO);
            }
        }

        //添加选择的专家人员
        if(StringUtils.isNotEmpty(expertUserIds)||StringUtils.isNotEmpty(expertUserNames)){

            String[] expertUserIdArray=expertUserIds.split(",");
            String[] expertUserNameArray=expertUserNames.split(",");

            for(int i=0;i<expertUserIdArray.length;i++){
                JudgeInfoEO judgeInfoEO=new JudgeInfoEO();
                String id=UUID.randomUUID10();
                judgeInfoEO.setId(id);
                judgeInfoEO.setJudgeMethodId(judgeInfoVO.getJudgeMethodId());
                judgeInfoEO.setProjectId(judgeInfoVO.getProjectId());
                judgeInfoEO.setRatingRulesId(judgeInfoVO.getRatingRulesId());
                judgeInfoEO.setExpertUserId(expertUserIdArray[i]);
                judgeInfoEO.setExpertUserName(expertUserNameArray[i]);
                judgeInfoEO.setReviewRemark(judgeInfoVO.getReviewRemark());
                judgeInfoEOList.add(judgeInfoEO);
                for (GradeRulesInfoEO g:gradeRulesInfoEOS) {
                    ScoreInfoEO scoreInfoEO=new ScoreInfoEO();
                    scoreInfoEO.setId(UUID.randomUUID10());
                    scoreInfoEO.setProjectId(projectId);
                    scoreInfoEO.setExpertUserId(expertUserIdArray[i]);//专家人员id
                    scoreInfoEO.setExpertUserName(expertUserNameArray[i]);//专家人员姓名
                    scoreInfoEO.setGradingRulesId(g.getGradingRulesId());//评分细则id
                    scoreInfoEOList.add(scoreInfoEO);
                }
            }
        }
        dao.deleteByProjectId(projectId);
        scoreInfoEODao.deleteByProjectId(projectId);
        //评分新增
        scoreInfoEODao.batchInsertSelective(scoreInfoEOList);

        //评审新增
        dao.batchInsertSelective(judgeInfoEOList);


        ProjectDataEO projectDataEO = new ProjectDataEO();
        projectDataEO.setId(projectId);
        //根据对应的评审方式修改状态
        if(StringUtils.equals(judgeInfoVO.getJudgeMethodId(),"SLPC99GEBW")){
            projectDataEO.setProjectStatus("评审中");
        }else {
            projectDataEO.setProjectStatus("审核中");
        }

        projectDataEOService.updateByPrimaryKeySelective(projectDataEO);


    }

    /**
     * 新增和修改都调用这个接口
     *
     * @param judgeInfoVO
     */
    public void insertJudgeInfoVO(JudgeInfoVO judgeInfoVO){
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        String expertUserIds=judgeInfoVO.getExpertUserId();
        String expertUserNames=judgeInfoVO.getExpertUserName();
        String projectId=judgeInfoVO.getProjectId();
        if(StringUtils.isEmpty(expertUserIds)||StringUtils.isEmpty(expertUserNames)){

            dao.deleteByProjectId(projectId);

            List<JudgeInfoEO> judgeInfoEOList=new LinkedList<>();

                JudgeInfoEO judgeInfoEO=new JudgeInfoEO();
                String id=UUID.randomUUID10();
                judgeInfoEO.setId(id);
                judgeInfoEO.setJudgeMethodId(judgeInfoVO.getJudgeMethodId());
                judgeInfoEO.setProjectId(judgeInfoVO.getProjectId());
                judgeInfoEO.setRatingRulesId(judgeInfoVO.getRatingRulesId());
                judgeInfoEO.setExpertUserId(user.getUsid());
                judgeInfoEO.setExpertUserName(user.getUsname());
                judgeInfoEOList.add(judgeInfoEO);


            dao.batchInsertSelective(judgeInfoEOList);

        }else{


            dao.deleteByProjectId(projectId);
            String[] expertUserIdArray=expertUserIds.split(",");
            String[] expertUserNameArray=expertUserNames.split(",");
            List<JudgeInfoEO> judgeInfoEOList=new LinkedList<>();
            for(int i=0;i<expertUserIdArray.length;i++){
                JudgeInfoEO judgeInfoEO=new JudgeInfoEO();
                String id=UUID.randomUUID10();
                judgeInfoEO.setId(id);
                judgeInfoEO.setJudgeMethodId(judgeInfoVO.getJudgeMethodId());
                judgeInfoEO.setProjectId(judgeInfoVO.getProjectId());
                judgeInfoEO.setRatingRulesId(judgeInfoVO.getRatingRulesId());
                judgeInfoEO.setExpertUserId(expertUserIdArray[i]);
                judgeInfoEO.setExpertUserName(expertUserNameArray[i]);
                judgeInfoEOList.add(judgeInfoEO);
            }


            dao.batchInsertSelective(judgeInfoEOList);
        }


        //修改rs_project_data 中相应id对应的项目状态为 '评审中'

        try{
            projectDataEOService.updateProjectStatusByProjectId(projectId,"评审中");
        }catch (Exception ex)
        {
            logger.info(ex.getMessage());
        }
    }

    /**
     * 根据ProjectId修改
     * @param judgeInfoEO
     */
    public void updateByProjectId( JudgeInfoEO judgeInfoEO){
        dao.updateByProjectId(judgeInfoEO);
    }

    /**
     * 根据ProjectId和专家id修改
     * @param judgeInfoEO
     */
    public void updateByProjectIdAndExpertUserId( JudgeInfoEO judgeInfoEO){
        dao.updateByProjectIdAndExpertUserId(judgeInfoEO);
    }
    /**
     * 根据project id 删除
     */
    public void  deleteByProjectId( String projectId){

        dao.deleteByProjectId(projectId);
    }
    /**
     * 根据project id 查询
     */
    public List<JudgeInfoEO> queryListByProjectId( String projectId){
        return dao.queryListByProjectId(projectId);
    }

}
