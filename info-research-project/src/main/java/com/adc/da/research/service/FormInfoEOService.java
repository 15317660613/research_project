package com.adc.da.research.service;

import com.adc.da.research.dao.InfoEODao;
import com.adc.da.research.entity.InfoEO;
import com.adc.da.research.entity.MemberEO;
import com.adc.da.research.vo.FormInfoVO;
import com.adc.da.util.exception.AdcDaBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_INFO InfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("formInfoEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class FormInfoEOService {

    @Autowired
    private InfoEODao infoEODao;

    @Autowired
    private MemberEOService memberEOService;

    /**
     * 用于流程表单 数据的注入
     *
     * @param id
     * @return
     */
    public FormInfoVO getProjectInfo(String id) {

        InfoEO infoEO = infoEODao.selectByPrimaryKey(id);

        if (null == infoEO) {
            throw new AdcDaBaseException("项目异常");
        }

        FormInfoVO.FormInfoVOBuilder resultBuilder = FormInfoVO.builder();
        MemberEO leaderEO = memberEOService.getLeaderInfo(id);

        resultBuilder.deptId(infoEO.getOwnDepartmentId())
                     .deptName(infoEO.getOwnDepartmentName())
                     .contractNo(infoEO.getExtInfo2())
                     .projectBeginTime(infoEO.getResearchProjectBeginTime())
                     .projectEndTime(infoEO.getResearchProjectEndTime())
                     .projectId(id)
                     .projectLeaderId(leaderEO.getMemberNameId())
                     .projectLeaderName(leaderEO.getMemberName());

        return resultBuilder.build();
    }
}
