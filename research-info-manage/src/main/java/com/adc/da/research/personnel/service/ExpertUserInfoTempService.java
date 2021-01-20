package com.adc.da.research.personnel.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.service.BaseService;
import com.adc.da.research.personnel.dao.ExpertUserInfoTempDao;
import com.adc.da.research.personnel.dto.ExpertUserInfoEODto;
import com.adc.da.research.personnel.entity.ExpertGroupInfoEO;
import com.adc.da.research.personnel.entity.ExpertUserInfoListEO;
import com.adc.da.research.personnel.page.ExpertUserInfoVOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @date 2020/10/27 17:05
 * @auth zhn
 */
@Service
@Slf4j
public class ExpertUserInfoTempService extends BaseService<ExpertUserInfoListEO, String> {
    @Autowired
    private ExpertUserInfoTempDao dao;

    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private ExpertGroupInfoEOService expertGroupInfoEOService;

    @Override
    public BaseDao<ExpertUserInfoListEO> getDao() {
        return dao;
    }

    public Workbook exportUserInfo(ExportParams exportParams, ExpertUserInfoVOPage eoPage) throws Exception {
        List<ExpertUserInfoListEO> expertUserInfoListEOS = new ArrayList<>();
        String[] ids = eoPage.getIdS();
        if (CollectionUtils.isNotEmpty(ids)) {
            expertUserInfoListEOS = dao.selectByIdS(ids);
        } else {
            expertUserInfoListEOS = dao.queryByList(eoPage);
        }
        List<ExpertUserInfoEODto> eoDtos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(expertUserInfoListEOS)) {
            eoDtos = beanMapper.mapList(expertUserInfoListEOS, ExpertUserInfoEODto.class);
        }
        return ExcelExportUtil.exportExcel(exportParams, ExpertUserInfoEODto.class, eoDtos);
    }

    public List<ExpertUserInfoListEO> queryByPageUnion(ExpertUserInfoVOPage page) {

        page.setUserNameOperator("LIKE");
        page.setCompanyNameOperator("LIKE");
        page.setLastDegreeOperator("LIKE");
        List<ExpertUserInfoListEO> result;
        try {
            result = this.queryByPage(page);
        } catch (Exception e) {
            throw new AdcDaBaseException("专家人员列表查询失败");
        }

        if(CollectionUtils.isNotEmpty(result)){
            for (ExpertUserInfoListEO expertUserInfoListEO : result) {
                //获取专家组集合
                try {
                    List<ExpertGroupInfoEO> expertGroupList = expertGroupInfoEOService.getExpertGroupList(expertUserInfoListEO.getId());
                    if(CollectionUtils.isNotEmpty(expertGroupList)){
                        //专家组id集合
                        List<String> expertGroupIdList = new ArrayList<>();
                        //专家组名称集合
                        List<String> expertGroupNameList = new ArrayList<>();
                        for (ExpertGroupInfoEO expertGroupInfoEO : expertGroupList) {
                            expertGroupIdList.add(expertGroupInfoEO.getId());
                            expertGroupNameList.add(expertGroupInfoEO.getExpertGroupName());
                        }

                        expertUserInfoListEO.setExpertGroupId(expertGroupIdList);
                        expertUserInfoListEO.setExpertGroupName(expertGroupNameList);
                    }
                } catch (Exception e) {
                    log.error("专家组信息查询失败");
                }
            }
        }
        return result;
    }
}
