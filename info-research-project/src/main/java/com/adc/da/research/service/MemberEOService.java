package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.MemberEODao;
import com.adc.da.research.entity.MemberEO;
import com.adc.da.research.page.MemberEOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_MEMBER MemberEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("memberEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class MemberEOService extends BaseService<MemberEO, String> {

    @Autowired
    private MemberEODao dao;

    public MemberEODao getDao() {
        return dao;
    }

    /**
     * 插入前，进行用户id校验
     *
     * @param t
     * @return
     * @throws Exception
     */
    @Override
    public int insertSelective(MemberEO t) throws Exception {
        MemberEOPage queryPage = new MemberEOPage();
        queryPage.setMemberNameId(t.getMemberNameId());
        queryPage.setResearchProjectId(t.getResearchProjectId());
        if (dao.queryByCount(queryPage) == 0) {
            return this.getDao().insertSelective(t);
        } else {
            return -1;

        }
    }

    /**
     * 修改，进行用户id校验
     *
     * @param t
     * @return
     * @throws Exception
     */
    @Override
    public int updateByPrimaryKeySelective(MemberEO t) throws Exception {
        MemberEOPage queryPage = new MemberEOPage();
        queryPage.setMemberNameId(t.getMemberNameId());
        queryPage.setResearchProjectId(t.getResearchProjectId());

        List<MemberEO> queryList = dao.queryByList(queryPage);
        if (CollectionUtils.isNotEmpty(queryList)) {
            if (1 == queryList.size() && t.getId().equals(queryList.get(0).getId())) {
                return this.getDao().updateByPrimaryKeySelective(t);
            } else {
                return -1;
            }
        } else {
            return this.getDao().updateByPrimaryKeySelective(t);
        }
    }

    /**
     * 获取项目人员信息，包含排序
     *
     * @param projectId
     * @return
     */
    public List<MemberEO> getMemberInfoWithSort(String projectId) {
        MemberEOPage queryPage = new MemberEOPage();
        queryPage.setResearchProjectId(projectId);
        queryPage.setOrderBy("LEADER_FLAG_ DESC , EXT_INFO_1_");
        return dao.queryByList(queryPage);
    }

    /**
     * 获取项目人员信息，包含排序
     *
     * @param projectId
     * @return
     */
    public MemberEO getLeaderInfo(String projectId) {
        MemberEOPage queryPage = new MemberEOPage();
        queryPage.setResearchProjectId(projectId);
        queryPage.setLeaderFlag(1);

        List<MemberEO> list = dao.queryByList(queryPage);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }

        throw new AdcDaBaseException("项目信息异常");
    }
}
