package com.adc.da.research.config.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.config.dao.GradeRulesInfoDao;
import com.adc.da.research.config.entity.GradeRulesInfoEO;
import com.adc.da.research.config.entity.GradingRulesEO;
import com.adc.da.research.config.entity.RatingRulesEO;
import com.adc.da.research.config.vo.GradeRulesInfoVO;
import com.adc.da.research.project.dao.JudgeInfoEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @date 2020/10/23 10:06
 * @auth zhn
 */
@Service
@Slf4j
public class GradeRulesInfoService {

    @Autowired
    private GradingRulesEOService gradingRulesEOService;

    @Autowired
    private RatingRulesEOService ratingRulesEOService;

    @Autowired
    private GradeRulesInfoDao gradeRulesInfoDao;

    @Autowired
    private JudgeInfoEODao judgeInfoEODao;

    /**
     * 新增评分规则信息
     */
    public void insertOrUpdate(List<GradeRulesInfoVO> gradeRulesInfoVOS) {
        String ratingRulesId = gradeRulesInfoVOS.get(0).getRatingRulesId();
        if (ratingRulesId == null) {
            String templateName=gradeRulesInfoVOS.get(0).getRatingRulesName();
            if(StringUtils.isEmpty(templateName)){
                throw new AdcDaBaseException("空的模板名");
            }

            UserEO user = UserUtils.getUser();
            if (ObjectUtil.isNull(user)){
                throw new AdcDaBaseException("登陆可能过期，请登录！");
            }

            //新增评分规则信息
            RatingRulesEO ratingRulesEO = new RatingRulesEO();
            String userId = user.getUsid();
            String userName = user.getUsname();
            ratingRulesEO.setCreateUserName(userName);
            ratingRulesEO.setCreateUserId(userId);
            ratingRulesEO.setCreateTime(new Date());
            ratingRulesEO.setId(UUID.randomUUID10());

            //因为每个对象中的模板名称都一样,所以gradeRulesInfoVOS.get(0)就可以
            ratingRulesEO.setRatingRulesName(gradeRulesInfoVOS.get(0).getRatingRulesName());
            try {
                List<RatingRulesEO> ratingRulesEOList=ratingRulesEOService.findByName(templateName);
                if(CollectionUtils.isNotEmpty(ratingRulesEOList)){
                    throw new AdcDaBaseException("操作失败：重复的模板名称");
                }
                ratingRulesEOService.insertSelective(ratingRulesEO);
            } catch (Exception e) {
                log.error("操作失败: " +e.getMessage());
                throw new AdcDaBaseException("操作失败:"+e.getMessage());
            }

            //新增评分细则信息
            List<GradingRulesEO> gradingRulesEOS = getGradingRulesEOS(gradeRulesInfoVOS, ratingRulesEO);
            try {
                //批量新增评分细则详情
                gradingRulesEOService.batchInsertSelective(gradingRulesEOS);
            } catch (Exception e) {
                log.error("评分细则详情操作失败" + e.getMessage());
                throw new AdcDaBaseException("评分细则详情操作失败");
            }
        } else {
            //修改评分规则信息
            RatingRulesEO ratingRulesEO = new RatingRulesEO();
            ratingRulesEO.setModifyTime(gradeRulesInfoVOS.get(0).getModifyTime());
            //因为每个对象中的模板迷城都一样,所以gradeRulesInfoVOS.get(0)就可以
            ratingRulesEO.setRatingRulesName(gradeRulesInfoVOS.get(0).getRatingRulesName());
            ratingRulesEO.setId(gradeRulesInfoVOS.get(0).getRatingRulesId());
            try {
                ratingRulesEOService.updateByPrimaryKeySelective(ratingRulesEO);
            } catch (Exception e) {
                log.error("评分规则模板修改失败" + e.getMessage());
                throw new AdcDaBaseException("评分规则模板修改失败，请检查模板名是否重复");
            }

            //修改评分细则信息
            List<GradingRulesEO> gradingRulesEOS = getGradingRulesEOS(gradeRulesInfoVOS, ratingRulesEO);
            try {
                //因为编辑评分细则是既可以新增也可以编辑,所以采用现将之前保存的删除(根据模板id删除),再次重新新增
                if (ObjectUtil.isNotNull(ratingRulesId)) {
                    gradingRulesEOService.deleteByRatingRulesId(ratingRulesId);
                }
                //批量新增评分细则详情
                gradingRulesEOService.batchInsertSelective(gradingRulesEOS);
            } catch (Exception e) {
                log.error("评分细则信息修改失败" + e.getMessage());
                throw new AdcDaBaseException("评分细则信息修改失败");
            }

        }


    }

    /**
     * 评分细则信息替换
     *
     * @param gradeRulesInfoVOS
     * @param ratingRulesEO
     * @return
     */
    private List<GradingRulesEO> getGradingRulesEOS(List<GradeRulesInfoVO> gradeRulesInfoVOS, RatingRulesEO ratingRulesEO) {
        List<GradingRulesEO> gradingRulesEOS = new ArrayList<>();
        int order = 0;
        for (GradeRulesInfoVO gradeRulesInfoVO : gradeRulesInfoVOS) {
            GradingRulesEO gradingRulesEO = new GradingRulesEO();
            if (gradeRulesInfoVO.getGradingRulesId() == null) {
                gradingRulesEO.setId(UUID.randomUUID10());
            } else {
                gradingRulesEO.setId(gradeRulesInfoVO.getGradingRulesId());
            }
            gradingRulesEO.setRatingRulesId(ratingRulesEO.getId());
            gradingRulesEO.setGradingName(gradeRulesInfoVO.getGradingName());
            gradingRulesEO.setCappedPoints(gradeRulesInfoVO.getCappedPoints());
            gradingRulesEO.setLowestScore(gradeRulesInfoVO.getLowestScore());
            gradingRulesEO.setCreateTime(new Date());
            gradingRulesEO.setModifyTime(new Date());
            gradingRulesEO.setSort(order++);
            gradingRulesEOS.add(gradingRulesEO);
        }
        return gradingRulesEOS;
    }

    /**
     * 删除--评分规则
     *
     * @param ids
     */
    public void batchDeleteByIds(List<String> ids) {
        //删除RS_RATING_RULES(评分规则信息表)中信息
        try {
            ratingRulesEOService.batchDeleteByIds(ids);
        } catch (Exception e) {
            log.error("评分规则模板批量删除失败" + e.getMessage());
            throw new AdcDaBaseException("评分规则模板批量删除失败");
        }
        //删除RS_GRADING_RULES(评分细则信息表)中信息
        try {
            //根据
            gradingRulesEOService.batchDeleteByIds(ids);
        } catch (Exception e) {
            log.error("评分细则批量删除失败" + e.getMessage());
            throw new AdcDaBaseException("评分细则批量删除失败");
        }
    }


    /**
     * 根据id查询单条详情
     * @param id
     * @return
     */
    public List<GradeRulesInfoEO> selectById(String id) {
        return gradeRulesInfoDao.selectById(id);
    }

    public String getRatingRulesIdByProjectId(String projectId){
        return judgeInfoEODao.getRatingRulesIdByProjectId(projectId);
    }

    public List<GradeRulesInfoEO> selectByProjectId(String projectId){
        String ratingRulesId=getRatingRulesIdByProjectId(projectId);
        return gradeRulesInfoDao.selectById(ratingRulesId);
    }
}
