package com.adc.da.processform.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.processform.dao.ProjectContractInvoiceEODao;
import com.adc.da.processform.entity.ProjectContractInvoiceEO;
import com.adc.da.processform.vo.ContractInvoiceVO;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <br>
 * <b>功能：</b>PF_PROJECT_CONTRACT_INVOICE ProjectContractInvoiceEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectContractInvoiceEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectContractInvoiceEOService extends BaseService<ProjectContractInvoiceEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectContractInvoiceEOService.class);

    @Autowired
    private ProjectContractInvoiceEODao dao;

    public ProjectContractInvoiceEODao getDao() {
        return dao;
    }

    public List<ContractInvoiceVO> check(InputStream is, ImportParams params) throws Exception {
        List<ContractInvoiceVO> datas = ExcelImportUtil.importExcel(is, ContractInvoiceVO.class, params);
        List<ProjectContractInvoiceEO> projectContractInvoiceEOList = dao.queryAll();
        Map<String, Integer> getContractNOCountMap = getContractNOCountMap(datas);
        Map<String, List<ContractInvoiceVO>> getContractNOContractInvoiceVOListMap = getContractNOContractInvoiceVOListMap(datas);
        Map<String, Integer> getProjectContractNOCountMap = getProjectContractNOCountMap(projectContractInvoiceEOList);

        List<ContractInvoiceVO> resList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : getContractNOCountMap.entrySet()) {
            Integer excelCount = entry.getValue();
            Integer dataBaseCount = getProjectContractNOCountMap.get(entry.getKey());
            if (null == dataBaseCount || dataBaseCount < excelCount) {
                if (null != getContractNOContractInvoiceVOListMap.get(entry.getKey())) {
                    resList.addAll(getContractNOContractInvoiceVOListMap.get(entry.getKey()));
                }
            }

        }

        return resList;
    }

    public Map<String, Integer> getContractNOCountMap(List<ContractInvoiceVO> contractInvoiceVOList) {
        Map<String, Integer> map = new HashMap<>();
        for (ContractInvoiceVO contractInvoiceVO : contractInvoiceVOList) {
            if (null != map.get(contractInvoiceVO.getContractNO())) {
                map.put(contractInvoiceVO.getContractNO(), map.get(contractInvoiceVO.getContractNO()) + 1);
            } else {
                map.put(contractInvoiceVO.getContractNO(), 1);
            }
        }
        return map;
    }

    public Map<String, List<ContractInvoiceVO>> getContractNOContractInvoiceVOListMap(List<ContractInvoiceVO> contractInvoiceVOList) {
        Map<String, List<ContractInvoiceVO>> map = new HashMap<>();
        for (ContractInvoiceVO contractInvoiceVO : contractInvoiceVOList) {
            List<ContractInvoiceVO> invoiceVOList = map.get(contractInvoiceVO.getContractNO());
            if (CollectionUtils.isNotEmpty(invoiceVOList)) {
                invoiceVOList.add(contractInvoiceVO);
                map.put(contractInvoiceVO.getContractNO(), invoiceVOList);
            } else {
                invoiceVOList = new ArrayList<>();
                invoiceVOList.add(contractInvoiceVO);
                map.put(contractInvoiceVO.getContractNO(), invoiceVOList);
            }
        }
        return map;
    }

    public Map<String, Integer> getProjectContractNOCountMap(List<ProjectContractInvoiceEO> contractInvoiceVOList) {
        Map<String, Integer> map = new HashMap<>();
        for (ProjectContractInvoiceEO contractInvoiceVO : contractInvoiceVOList) {
            if (null != map.get(contractInvoiceVO.getContractId())) {
                map.put(contractInvoiceVO.getContractId(), map.get(contractInvoiceVO.getContractId()) + 1);
            } else {
                map.put(contractInvoiceVO.getContractId(), 1);
            }
        }
        return map;
    }



}
