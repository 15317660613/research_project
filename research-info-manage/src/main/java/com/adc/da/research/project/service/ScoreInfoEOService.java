package com.adc.da.research.project.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.config.entity.GradingRulesEO;
import com.adc.da.research.config.service.GradingRulesEOService;
import com.adc.da.research.project.dao.ScoreInfoEODao;
import com.adc.da.research.project.entity.AnnexFileEO;
import com.adc.da.research.project.entity.JudgeInfoEO;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.entity.ScoreInfoEO;
import com.adc.da.research.project.page.AnnexFileEOPage;
import com.adc.da.research.project.page.JudgeInfoEOPage;
import com.adc.da.research.project.page.ScoreInfoEOPage;
import com.adc.da.research.project.vo.ScoreInfoVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *
 * <br>
 * <b>功能：</b>RS_SCORE_INFO ScoreInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scoreInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScoreInfoEOService extends BaseService<ScoreInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScoreInfoEOService.class);

    @Autowired
    private ScoreInfoEODao dao;
    @Autowired
    private GradingRulesEOService gradingRulesEOService;
    @Autowired
    private ProjectDataEOService projectDataEOService;
    @Autowired
    private JudgeInfoEOService judgeInfoEOService;
    @Autowired
    private AnnexFileEOService annexFileEOService;

    public ScoreInfoEODao getDao() {
        return dao;
    }


    /**
     * 表格数据处理
     * @param page
     * @return
     * @throws Exception
     */
    public  Map<String,Object> queryByArr(ScoreInfoEOPage page) throws Exception {

        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }


        page.setExpertUserId(user.getUsid());

        List<ScoreInfoEO> scoreInfoEOList = this.queryByList(page);
        List<String> rulesArrList =new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        for (ScoreInfoEO s:scoreInfoEOList) {
            GradingRulesEO gradingRulesEO = gradingRulesEOService.selectByPrimaryKey(s.getGradingRulesId());
            s.setGradingRulesName(gradingRulesEO.getGradingName());
            rulesArrList.add(gradingRulesEO.getGradingName());
            s.setLowestScore(gradingRulesEO.getLowestScore());
            s.setCappedPoints(gradingRulesEO.getCappedPoints());
            s.setRatingRulesId(gradingRulesEO.getRatingRulesId());
        }
        List<String> uniqueRules = rulesArrList.stream().distinct().collect(Collectors.toList());//去重集合-细则名称

        JudgeInfoEOPage judgeInfoEOPage=new JudgeInfoEOPage();
        judgeInfoEOPage.setProjectId(page.getProjectId());
        List<JudgeInfoEO> judgeInfoEOS = judgeInfoEOService.queryByList(judgeInfoEOPage);
        if(judgeInfoEOS.size()>0){
            map.put("reviewRemark",judgeInfoEOS.get(0).getReviewRemark());
        }
        map.put("score",uniqueRules);
        map.put("list",scoreInfoEOList);
        return map;
    }

    /**
     * 查询所有专家人员评分 列表
     * @param page
     * @return
     * @throws Exception
     */
    public  List<Map<String,Object>>  queryByUser(ScoreInfoEOPage page) throws Exception {

        List<Map<String,Object>> mapList=new ArrayList<>();
        List<String> rulesArrList =new ArrayList<>();//细则名称集合

        List<ScoreInfoEO> scoreInfoEOList = this.queryByList(page);
        for (ScoreInfoEO s:scoreInfoEOList) {
            GradingRulesEO gradingRulesEO = gradingRulesEOService.selectByPrimaryKey(s.getGradingRulesId());
            s.setGradingRulesName(gradingRulesEO.getGradingName());
            rulesArrList.add(gradingRulesEO.getGradingName());
        }
        List<String> uniqueRules = rulesArrList.stream().distinct().collect(Collectors.toList());//去重集合-细则名称
        JudgeInfoEOPage judgeInfoEOPage=new JudgeInfoEOPage();
        judgeInfoEOPage.setProjectId(page.getProjectId());
        List<JudgeInfoEO> judgeInfoEOS = judgeInfoEOService.queryByList(judgeInfoEOPage);


        for (int i = 0; i <uniqueRules.size() ; i++) {
            for (ScoreInfoEO r:scoreInfoEOList) {

                if(r.getGradingRulesName().equals(uniqueRules.get(i))){
                    if(i==0){//第一条数据生成
                        Map<String,Object> scoreMap=new HashMap<>();

                        scoreMap.put("experUserNmae",r.getExpertUserName());
                        scoreMap.put("score"+i,r.getGradingRulesScore());//对应的分值
                        scoreMap.put("scoreArr",uniqueRules);
                        scoreMap.put("projectId",r.getProjectId());
                        scoreMap.put("opinion",r.getExt1());//评分意见
                        scoreMap.put("file",r.getExt2());
                        if(judgeInfoEOS.size()>0){
                            scoreMap.put("reviewRemark",judgeInfoEOS.get(0).getReviewRemark());
                        }
                        mapList.add(scoreMap);

                    }else{

                        for (int j = 0; j <mapList.size() ; j++) {
                            if(r.getExpertUserName().equals(mapList.get(j).get("experUserNmae"))){
                                mapList.get(j).put("score"+i,r.getGradingRulesScore());
                            }
                        }
                    }
                }
            }



        }




        return mapList;
    }

    /**
     * 批量新增
     *
     * @param scoreInfoEOS
     */
    public void batchInsertSelective(List<ScoreInfoEO> scoreInfoEOS) {
        for (ScoreInfoEO m:scoreInfoEOS) {
            m.setId(UUID.randomUUID10());
            m.setState((long) 0);
        }

        dao.batchInsertSelective(scoreInfoEOS);
    }

    /**
     * 新增或修改
     * @param ScoreInfoEOS
     * @throws Exception
     */
    public void insertOrUpdate(List<ScoreInfoEO> ScoreInfoEOS) throws Exception{
            if(CollectionUtils.isEmpty(ScoreInfoEOS)){
                return;
            }
            String scoreInFoId=ScoreInfoEOS.get(0).getId();
            //如果有id为修改  没有则新增
            if(scoreInFoId!=null){
                for (ScoreInfoEO s:ScoreInfoEOS) {
                    s.setState((long) 0);
                    this.updateByPrimaryKeySelective(s);


                }



            //专家人员每次提交判断总人数和提交人数是否相等
            ScoreInfoEOPage page=new ScoreInfoEOPage();
            page.setProjectId(ScoreInfoEOS.get(0).getProjectId());
            int flag=0;//判断标识
            List<ScoreInfoEO> scoreInfoEOList = this.queryByList(page);
            for (ScoreInfoEO s:scoreInfoEOList) {

                if(StringUtils.isEmpty(s.getState())){
                    flag=1;
                }else if(s.getGradingRulesScore()==null&&s.getState()==1){//判断是否都没评分 且撤回状态为1
                    flag=1;
                }


            }
            ProjectDataEO projectDataEO = new ProjectDataEO();
            projectDataEO.setId(ScoreInfoEOS.get(0).getProjectId());
            if(flag==1){
                projectDataEO.setProjectStatus("评审中");
            }else {
                projectDataEO.setProjectStatus("审核中");
            }

            projectDataEOService.updateByPrimaryKeySelective(projectDataEO);




        }else {
            this.batchInsertSelective(ScoreInfoEOS);

        }


    }

    /**
     * 管理员线下审核
     */
    public void adminUpdate(List<ScoreInfoEO> ScoreInfoEOS)throws Exception{
        if (CollectionUtils.isEmpty(ScoreInfoEOS)){
            throw new AdcDaBaseException("审核数据为空，请检查！");
        }
        for (ScoreInfoEO s:ScoreInfoEOS) {
            s.setState((long) 0);
            this.updateByPrimaryKeySelective(s);
        }
        //修改项目状态
        if(ScoreInfoEOS.get(0).getResult()==1){
            ProjectDataEO projectDataEO = new ProjectDataEO();
            projectDataEO.setId(ScoreInfoEOS.get(0).getProjectId());
            projectDataEO.setProjectStatus("审核未通过");
            projectDataEOService.updateByPrimaryKeySelective(projectDataEO);
            dao.deleteByProjectId(ScoreInfoEOS.get(0).getProjectId());
            judgeInfoEOService.deleteByProjectId(ScoreInfoEOS.get(0).getProjectId());
        }else{
            ProjectDataEO projectDataEO = new ProjectDataEO();
            projectDataEO.setId(ScoreInfoEOS.get(0).getProjectId());
            projectDataEO.setProjectStatus("申报中");
            projectDataEOService.updateByPrimaryKeySelective(projectDataEO);

            JudgeInfoEO judgeInfoEO =new JudgeInfoEO();
            judgeInfoEO.setProjectId(ScoreInfoEOS.get(0).getProjectId());
            judgeInfoEO.setApproveComment(ScoreInfoEOS.get(0).getAuditReason());
            judgeInfoEO.setApproveResult(ScoreInfoEOS.get(0).getResult());
            judgeInfoEO.setExt1(ScoreInfoEOS.get(0).getUploadFile());
            judgeInfoEOService.updateByProjectId(judgeInfoEO);
        }






    }
    /**
     * 管理员线上审核
     */
    public void adminUpdateOnLine(List<ScoreInfoEO> ScoreInfoEOS)throws Exception{
        if (CollectionUtils.isEmpty(ScoreInfoEOS)){
            throw new AdcDaBaseException("审核数据为空，请检查！");
        }

        //修改项目状态
        if(ScoreInfoEOS.get(0).getResult()==1){
            ProjectDataEO projectDataEO = new ProjectDataEO();
            projectDataEO.setId(ScoreInfoEOS.get(0).getProjectId());
            projectDataEO.setProjectStatus("审核未通过");
            projectDataEOService.updateByPrimaryKeySelective(projectDataEO);
            dao.deleteByProjectId(ScoreInfoEOS.get(0).getProjectId());
            judgeInfoEOService.deleteByProjectId(ScoreInfoEOS.get(0).getProjectId());
        }else{
            ProjectDataEO projectDataEO = new ProjectDataEO();
            projectDataEO.setId(ScoreInfoEOS.get(0).getProjectId());
            projectDataEO.setProjectStatus("申报中");
            projectDataEOService.updateByPrimaryKeySelective(projectDataEO);
            JudgeInfoEO judgeInfoEO =new JudgeInfoEO();
            judgeInfoEO.setProjectId(ScoreInfoEOS.get(0).getProjectId());
            judgeInfoEO.setApproveComment(ScoreInfoEOS.get(0).getAuditReason());
            judgeInfoEO.setApproveResult(ScoreInfoEOS.get(0).getResult());
            judgeInfoEO.setExt1(ScoreInfoEOS.get(0).getUploadFile());
            judgeInfoEOService.updateByProjectId(judgeInfoEO);

        }






    }
    /**
     * 撤回
     */
    public void recall(ScoreInfoEO scoreInfoEO) throws Exception {
        ProjectDataEO projectDataEO = new ProjectDataEO();
        projectDataEO.setId(scoreInfoEO.getProjectId());
        projectDataEO.setProjectStatus("评审中");

        projectDataEOService.updateByPrimaryKeySelective(projectDataEO);
        ScoreInfoEO s=new ScoreInfoEO();
        s.setProjectId(scoreInfoEO.getProjectId());
        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        s.setExpertUserId(user.getUsid());
        s.setState((long) 1);
        dao.updateByProjectId(s);


    }


    /**
     * 专家评分
     * @param scoreInfoVO
     */
    public void submitScore(ScoreInfoVO scoreInfoVO) throws Exception {
        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        if(StringUtils.isEmpty(scoreInfoVO)){
            return;
        }
        int flag=0;//判断标识

        //评分
        List<ScoreInfoEO> scoreInfoEOList = scoreInfoVO.getScoreInfoEOList();
        if(StringUtils.isNotEmpty(scoreInfoEOList) && CollectionUtils.isNotEmpty(scoreInfoEOList)){
            for(ScoreInfoEO s:scoreInfoEOList){
                s.setState((long) 0);
                this.updateByPrimaryKeySelective(s);
            }
        }

        //意见
        JudgeInfoEO judgeInfoEO = scoreInfoVO.getJudgeInfoEO();
        judgeInfoEO.setExpertUserId(user.getUsid());
        String judgeInfoId = "";
        if(StringUtils.isNotEmpty(judgeInfoEO)){
            if(StringUtils.isNotEmpty(judgeInfoEO.getApproveResult())) {  //通过审核结果数据的有无来判断是否管理员审核
                //添加管理员审核数据
                judgeInfoEO.setJudgeMethodId(judgeInfoEO.getJudgeMethodId());
                judgeInfoEO.setProjectId(judgeInfoEO.getProjectId());
                judgeInfoEO.setRatingRulesId(judgeInfoEO.getRatingRulesId());
                judgeInfoEO.setExpertUserId(user.getUsid());
                judgeInfoEO.setExpertUserName(user.getUsname());
                //判断是线上还是线下  线下方式ID为X97H5VX6MT
                if(StringUtils.equals(judgeInfoEO.getJudgeMethodId(),"X97H5VX6MT")){
                    JudgeInfoEOPage judgeInfoEOPage=new JudgeInfoEOPage();
                    judgeInfoEOPage.setProjectId(scoreInfoVO.getJudgeInfoEO().getProjectId());
                    judgeInfoEOPage.setExpertUserId(user.getUsid());
                    List<JudgeInfoEO> judgeInfoEOS = judgeInfoEOService.queryByList(judgeInfoEOPage);
                    if(CollectionUtils.isNotEmpty(judgeInfoEOS)){
                        judgeInfoId = judgeInfoEOS.get(0).getId();
                        judgeInfoEO.setId(judgeInfoId);
                    }
                    judgeInfoEOService.updateByPrimaryKeySelective(judgeInfoEO);
                } else {
                    String id = UUID.randomUUID10();
                    judgeInfoEO.setId(id);
                    judgeInfoId = judgeInfoEO.getId();
                    judgeInfoEOService.insertSelective(judgeInfoEO);
                }

            }else {
                judgeInfoEOService.updateByProjectIdAndExpertUserId(judgeInfoEO);

                JudgeInfoEOPage judgeInfoEOPage=new JudgeInfoEOPage();
                judgeInfoEOPage.setProjectId(scoreInfoVO.getJudgeInfoEO().getProjectId());
                judgeInfoEOPage.setExpertUserId(user.getUsid());
                List<JudgeInfoEO> judgeInfoEOS = judgeInfoEOService.queryByList(judgeInfoEOPage);
                if (CollectionUtils.isEmpty(judgeInfoEOS)){
                    throw new AdcDaBaseException("评分数据为空，请检查！");
                }
                judgeInfoId = judgeInfoEOS.get(0).getId();
            }
        }
        //评分附件
        List<AnnexFileEO> annexFileEOS = scoreInfoVO.getAnnexFileEOList();
        if(CollectionUtils.isNotEmpty(annexFileEOS)) {
            for (AnnexFileEO a:annexFileEOS) {
                a.setExt3(judgeInfoId);
                a.setAnnexFileType("4");
            }
            annexFileEOService.batchInsertAnnexFileEO(annexFileEOS);
        }
        //专家人员每次提交判断总人数和提交人数是否相等
        ScoreInfoEOPage page=new ScoreInfoEOPage();
        page.setProjectId(scoreInfoVO.getJudgeInfoEO().getProjectId());
        List<ScoreInfoEO> scoreInfoEOS = this.queryByList(page);
        if(CollectionUtils.isNotEmpty(scoreInfoEOS)) {
            for (ScoreInfoEO s : scoreInfoEOS) {
                if (StringUtils.isEmpty(s.getState())) {
                    flag = 1;
                } else if (s.getGradingRulesScore() == null && s.getState() == 1) {//判断是否都没评分 且撤回状态为1
                    flag = 1;
                }
            }
            //只有科研管理员审核才会进入下面的逻辑
            if(0==judgeInfoEO.getApproveResult()){
                flag=2;//审核通过
            }else if(1==judgeInfoEO.getApproveResult()){
                flag=3;//审核不通过
            }
            ProjectDataEO projectDataEO = new ProjectDataEO();
            projectDataEO.setId(scoreInfoVO.getJudgeInfoEO().getProjectId());
            if (flag == 1) {
                projectDataEO.setProjectStatus("评审中");
            } else if(flag==0){
                projectDataEO.setProjectStatus("审核中");
            } else if(flag==2){
                projectDataEO.setProjectStatus("申报中");
            } else if(flag==3){
                projectDataEO.setProjectStatus("审核未通过");
            }
            projectDataEOService.updateByPrimaryKeySelective(projectDataEO);
        }
    }


    /**
     * 查询所有专家人员评分 列表
     * @param page
     * @return
     * @throws Exception
     */
    public  List<Map<String,Object>>  queryByexpertUser(ScoreInfoEOPage page) throws Exception {

        List<Map<String,Object>> mapList=new ArrayList<>();
        List<String> rulesArrList =new ArrayList<>();//细则名称集合

        List<ScoreInfoEO> scoreInfoEOList = this.queryByList(page);
        for (ScoreInfoEO s:scoreInfoEOList) {
            GradingRulesEO gradingRulesEO = gradingRulesEOService.selectByPrimaryKey(s.getGradingRulesId());
            s.setGradingRulesName(gradingRulesEO.getGradingName());
            rulesArrList.add(gradingRulesEO.getGradingName());
        }
        List<String> uniqueRules = rulesArrList.stream().distinct().collect(Collectors.toList());//去重集合-细则名称

        for (int i = 0; i <uniqueRules.size() ; i++) {
            for (ScoreInfoEO r:scoreInfoEOList) {
                if(StringUtils.isNotEmpty(r.getGradingRulesScore()) && r.getGradingRulesName().equals(uniqueRules.get(i))){
                    if(i==0){//第一条数据生成
                        Map<String,Object> scoreMap=new HashMap<>();
                        scoreMap.put("experUserNmae",r.getExpertUserName());
                        scoreMap.put("score"+i,r.getGradingRulesScore());//对应的分值
                        scoreMap.put("scoreArr",uniqueRules);
                        scoreMap.put("projectId",r.getProjectId());
                        JudgeInfoEOPage judgeInfoEOPage=new JudgeInfoEOPage();
                        judgeInfoEOPage.setProjectId(page.getProjectId());
                        judgeInfoEOPage.setExpertUserId(r.getExpertUserId());
                        List<JudgeInfoEO> judgeInfoEOS = judgeInfoEOService.queryByList(judgeInfoEOPage);
                        if(judgeInfoEOS.size()>0){
                            for(JudgeInfoEO j:judgeInfoEOS){
                                AnnexFileEOPage annexFileEO =new AnnexFileEOPage();
                                annexFileEO.setProjectId(j.getProjectId());
                                annexFileEO.setCreateUserId(r.getExpertUserId());
                                annexFileEO.setExt3(j.getId());
                                List<AnnexFileEO> annexFileEOS = annexFileEOService.queryByList(annexFileEO);
                                scoreMap.put("file",annexFileEOS);//附件
                                scoreMap.put("opinion",j.getApproveComment());//评分意见
                                if(annexFileEOS.size()>0) {
                                    scoreMap.put("time", annexFileEOS.get(0).getCreateTime());//时间
                                }

                            }
                        }
                        mapList.add(scoreMap);
                    }else{
                        for (int j = 0; j <mapList.size() ; j++) {
                            if(r.getExpertUserName().equals(mapList.get(j).get("experUserNmae"))){
                                mapList.get(j).put("score"+i,r.getGradingRulesScore());
                            }
                        }
                    }
                }
            }
        }
        return mapList;
    }


}
