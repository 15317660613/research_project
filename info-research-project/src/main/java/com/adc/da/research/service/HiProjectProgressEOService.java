package com.adc.da.research.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.HiProjectProgressEODao;
import com.adc.da.research.entity.HiProjectProgressEO;
import com.adc.da.research.entity.ProgressEO;
import com.adc.da.research.page.HiProjectProgressEOPage;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adc.da.research.utils.CompareObject.READ_ONLY;
import static com.adc.da.research.utils.CompareObject.WRITE_DIFFERENT;

/**
 * <br>
 * <b>功能：</b>RS_HI_PROJECT_PROGRESS HiProjectProgressEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("hiProjectProgressEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class HiProjectProgressEOService extends BaseService<HiProjectProgressEO, String> {

    @Autowired
    private HiProjectProgressEODao dao;

    public HiProjectProgressEODao getDao() {
        return dao;
    }

    @Autowired
    private InfoEOService infoEOService;

    @Autowired
    private ProgressEOService progressEOService;

    @Override
    public List<HiProjectProgressEO> queryByPage(BasePage page) throws Exception {
        int rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);

        String projectId = ((HiProjectProgressEOPage) page).getResearchProjectId();

        List<ProgressEO> sourceProgressEOList = progressEOService.getDao().getRunningProjectInfo(projectId);

        Map<String, ProgressEO> sourceMap = new HashMap<>();

        sourceProgressEOList.forEach(progressEO -> sourceMap.put(progressEO.getId(), progressEO));

        List<HiProjectProgressEO> resultList = this.getDao().queryByPage(page);
        resultList.forEach(hiProjectProgressEO -> {
            String id = hiProjectProgressEO.getId();
            ProgressEO sourceEO = sourceMap.get(id);
            if (null != sourceEO) {
                hiProjectProgressEO.setExtInfo1(sourceEO.getExtInfo1());
                hiProjectProgressEO.setExtInfo2(sourceEO.getExtInfo2());
            }
        });

        return resultList;
    }

    @Override
    public int insertSelective(HiProjectProgressEO eo) throws Exception {

        HiProjectProgressEOPage queryPage = new HiProjectProgressEOPage();
        queryPage.setCheckDetail(eo.getCheckDetail());
        queryPage.setResearchProjectId(eo.getResearchProjectId());
        queryPage.setProcBusinessKey(eo.getProcBusinessKey());
        if (dao.queryByCount(queryPage) > 0) {
            return -1;
        }

        eo.setId(UUID.randomUUID10());
        /*
         * 设置mask
         */
        eo.initMask(new HiProjectProgressEO(), READ_ONLY);
        return this.getDao().insertSelective(eo);
    }

    /**
     * 更新，进行校验并且设置mask
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public int updateAndSetMask(HiProjectProgressEO vo) throws Exception {
        /*
         * 优先进行重名校验
         */
        if (updateCheck(vo) == -1) {
            return -1;
        }

        infoEOService.checkAndUpdateStatus(vo);
        String id = vo.getId();

        /*
         *  获取历史数据，进行转换
         */
        ProgressEO sourceEO = progressEOService.selectByPrimaryKey(id);
        HiProjectProgressEO target = new HiProjectProgressEO();
        if (sourceEO != null) {
            /*
             * 证明为非新增数据
             */
            BeanUtils.copyProperties(sourceEO, target);
        }
        /*
         * 更新差异标识，同时选择性更改target
         * 初始化BusinessKey
         */
        target.initMask(vo, WRITE_DIFFERENT);
        target.setId(id);
        target.setProcBusinessKey(vo.getProcBusinessKey());
        target.setResearchProjectId(vo.getResearchProjectId());
        return this.getDao().updateByPrimaryKeySelective(target);
    }

    /**
     * @param id
     * @param key
     * @throws Exception
     */
    public void deleteByPrimaryKey(String id, String key) {
        HiProjectProgressEO hiMemberEO = new HiProjectProgressEO();
        hiMemberEO.setId(id);
        hiMemberEO.setProcBusinessKey(key);
        this.getDao().deleteByPrimaryKey(hiMemberEO);
    }

    /**
     * 更新操作，重名校验
     *
     * @param progressEO
     * @return
     * @throws Exception
     */
    public int updateCheck(HiProjectProgressEO progressEO) {

        /*
         * 根据id 与 proc_key 进行查询
         */
        HiProjectProgressEO oldProgressEO = dao.selectByPrimaryKey(progressEO);

        if (0 == progressEO.getExtInfo1()
            && !StringUtils.equals(oldProgressEO.getCheckDetail(), progressEO.getCheckDetail())) {
            HiProjectProgressEOPage queryPage = new HiProjectProgressEOPage();
            queryPage.setCheckDetail(progressEO.getCheckDetail());
            queryPage.setResearchProjectId(progressEO.getResearchProjectId());
            queryPage.setProcBusinessKey(progressEO.getProcBusinessKey());

            if (dao.queryByCount(queryPage) > 0) {
                return -1;
            }
        }
        return 0;
    }

}
