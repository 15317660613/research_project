package com.adc.da.budget.service;

import com.adc.da.budget.constant.Import;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.PurchaseRepository;
import com.adc.da.budget.vo.PurchaseVO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

/**
 * @Auther: chenhaidong
 * @Date: 2019/1/11
 * @Description:
 */
@Service
public class PurchaseService extends BaseService<Purchase, String> {

    private Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    UserEOService userEOService;

    @Autowired
    OrgEOService orgEOService;


    /**
     * 应用于AutoJavaService
     *
     * @param purchaseEO 从表单获取数据
     * @return purchase
     * @author chenhaidong
     * date 2019-1-11
     * @see AutoJavaService
     **/
    public Purchase insert(Purchase purchaseEO) {
        if (purchaseEO == null) { purchaseEO = new Purchase();}
        /*
         * 设置日期
         */
        purchaseEO.setCreateTime(new Date());
        purchaseEO.setModifyTime(new Date());
        /*
         * 设置id
         */
        purchaseEO.setId(UUID.randomUUID10());
        String userId = UserUtils.getUserId();
        if (!StringUtils.isNotEmpty(userId)) {
            throw new AdcDaBaseException("无登录信息");
        }
        UserEO userEO = userEOService.getUserWithRoles(userId);
        purchaseEO.setPrincipal(userEO.getUsname());
        purchaseEO.setPrincipalId(userEO.getUsid());
        List<OrgEO> orgEOList = getDeptIds();
        if (StringUtils.isEmpty(orgEOList)) {
            throw new AdcDaBaseException("无部门信息");
        }
        OrgEO orgEO = orgEOList.get(0);
        purchaseEO.setOrgId(orgEO.getId());
        purchaseEO.setOrg(orgEO.getName());
        purchaseRepository.save(purchaseEO);
        return purchaseEO;
    }

    /**
     * @Description: 新增
     * @auther: chenhaidong
     * @params:
     * @return:
     * @date: 2019/1/11
     */
    public Purchase insert(PurchaseVO purchaseVO) {
        Purchase purchase = beanMapper.map(purchaseVO, Purchase.class);
        purchase.setCreateTime(new Date());
        purchase.setModifyTime(new Date());
        purchase.setId(UUID.randomUUID());
        String userId = UserUtils.getUserId();
        if (!StringUtils.isNotEmpty(userId)) {
            throw new AdcDaBaseException("无登录信息");
        }
        UserEO userEO = userEOService.getUserWithRoles(userId);
        purchase.setPrincipal(userEO.getUsname());
        purchase.setPrincipalId(userEO.getUsid());
        List<OrgEO> orgEOList = getDeptIds();
        if (StringUtils.isEmpty(orgEOList)) {
            throw new AdcDaBaseException("无部门信息");
        }
        OrgEO orgEO = orgEOList.get(0);
        purchase.setOrgId(orgEO.getId());
        purchase.setOrg(orgEO.getName());
        purchaseRepository.save(purchase);
        return purchase;
    }

    /**
     * @Description: 根据id删除 可以是ids
     * @auther: chenhaidong
     * @params:
     * @return:
     * @date: 2019/1/11
     */
    public String deleteBatch(String ids) {
        if (ids == null || ids.trim().isEmpty()) { return "删除失败"; }
        String msg = "删除成功！";
        try {
            String[] idArray = ids.split(",");
            if (null != idArray && idArray.length != 0) {
                for (String id : idArray) {
                    purchaseRepository.delete(id);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            msg = "删除失败！";
        }
        return msg;
    }

    /**
     * @Description: 修改
     * @auther: chenhaidong
     * @params:
     * @return:
     * @date: 2019/1/11
     */
    @Transactional
    public Purchase update(PurchaseVO purchaseVO) {
        try {
            Purchase purchase1 = null;
            if ((purchase1 = purchaseRepository.findOne(purchaseVO.getId())) == null) {
                throw new AdcDaBaseException("采购信息不存在");
            }
            Purchase purchase = beanMapper.map(purchaseVO, Purchase.class);
            purchase.setCreateTime(purchase1.getCreateTime());
            purchase.setModifyTime(new Date());
            purchase.setOrg(purchase1.getOrg());
            purchase.setOrgId(purchase1.getOrgId());
            purchase.setPrincipalId(purchase1.getPrincipalId());
            purchase.setPrincipal(purchase1.getPrincipal());
            // 保存
            purchaseRepository.save(purchase);
            return purchase;
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }

    /**
     * @Description: 查找
     * @auther: chenhaidong
     * @params:
     * @return:
     * @date: 2019/1/11
     */
    public Purchase querySingle(String id) {
        if (id == null) { return null; }
        Purchase purchase = purchaseRepository.findOne(id);
        String projectInfo = null;
        if (purchase != null && (projectInfo = purchase.getProjectId()) != null) {
            Project project = projectRepository.findOne(projectInfo);
            purchase.setProjectName(project == null ? null : project.getName());
        }
        return purchase;
    }

    /**
     * 导入
     *
     * @param is
     * @param params
     * @throws Exception
     */
    public String excelImport(InputStream is, ImportParams params) throws Exception {
        List<Purchase> datas = ExcelImportUtil.importExcel(is, Purchase.class, params);
        for (Purchase purchase : datas) {
            if (StringUtils.isEmpty(purchase.getId())) {
                purchase.setId(UUID.randomUUID());
            }
            purchase.setCreateTime(new Date());
            purchase.setModifyTime(new Date());
            purchaseRepository.save(purchase);
        }
        return Import.SUCCESS;
    }

    /**
     * 导出
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Workbook excelExport(ExportParams params) {
        List<Purchase> purchases = Lists.newArrayList(queryAll());
        return ExcelExportUtil.exportExcel(params, Purchase.class, purchases);
    }

    public void deleteAll() {
        purchaseRepository.deleteAll();
    }

    public List<Purchase> queryAll() {
        List<Purchase> purchaseList = Lists.newArrayList(purchaseRepository.findAll());
        if (StringUtils.isEmpty(purchaseList)) { return purchaseList; }
        fillPurchase(purchaseList);
        return purchaseList;
    }

    /**
     * 分页查询
     */
    public PageDTO queryByPage(int page, int size) {
        //分页条件判断
        if (page < 1) { page = 1; }
        if (size < 1) { size = 10; }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        List<Purchase> purchaseList = null;
        Page<Purchase> purchasePage = purchaseRepository.findAll(new PageRequest(page - 1, size, sort));
        if (purchasePage == null || (purchaseList = purchasePage.getContent()) == null) {
            return new PageDTO(0, new ArrayList(), page, size);
        }
        fillPurchase(purchaseList);
        return new PageDTO(purchasePage.getTotalElements(), purchaseList, page, size);
    }

    /**
     * 获取项目信息并填充采购信息
     *
     * @param purchaseList 采购信息列表
     */
    public void fillPurchase(List<Purchase> purchaseList) {
        Set<String> projectIdSet = new HashSet<>();
        Map<String, List<Purchase>> stringPurchaseMap = new HashMap<>();
        String projectInfo = null;
        List<Purchase> purchases = null;
        for (Purchase purchase : purchaseList) {
            if ((projectInfo = purchase.getProjectId()) != null) {
                projectIdSet.add(projectInfo);
            }
            if ((purchases = stringPurchaseMap.get(projectInfo)) == null) {
                stringPurchaseMap.put(projectInfo, purchases = new ArrayList<>());}
            purchases.add(purchase);
        }
        if (StringUtils.isNotEmpty(projectIdSet)) {
            List<Project> projectList = Lists.newArrayList(projectRepository.findAll(projectIdSet));
            for (Project project : projectList) {
                projectInfo = project.getName();
                for (Purchase purchase : stringPurchaseMap.get(project.getId())) {
                    purchase.setProjectName(projectInfo);
                }
            }
        }

    }


    /**
     * 获取当前用户所属部门列表
     *
     * @return
     */
    public List<OrgEO> getDeptIds() {
        List<OrgEO> depIds = new ArrayList<>();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) { throw new AdcDaBaseException("请登录！"); }
        if (StringUtils.isNotEmpty(userId)) {
            UserEO userEO = userEOService.getUserWithRoles(userId);
            Subject subject = SecurityUtils.getSubject();
            if (subject.hasRole("超级管理员") || subject.hasRole("管理员") || subject.hasRole("主任")) {
                return depIds;
            }
            if (StringUtils.isEmpty(userEO.getOrgEOList())) {
                return null;
            }
            return userEO.getOrgEOList();
        }
        return null;
    }
}
