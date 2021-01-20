package com.adc.da.budget.service;

import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.ImportUser;
import com.adc.da.budget.entity.OrgWithParentName;
import com.adc.da.budget.enums.JobClassEnums;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.sys.controller.RoleEOController;
import com.adc.da.sys.controller.UserEOController;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.RoleEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.RoleEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.sys.vo.UserVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.PasswordUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * created by chenhaidong 2019/1/15
 */
@Service
public class ImportUsersService {

    /**
     * 初始密码
     */
    private static final String DEFALUT_M = "abc123";

    @Autowired
    private OrgListDao orgListDao;

    @Autowired
    UserEOController userEOController;
    @Autowired
    UserEOService userEOService;

    @Autowired
    OrgEOService orgEOService;

    @Autowired
    RoleEOService roleEOService;

    @Autowired
    RoleEOController roleEOController;

    /**
     * excel导入
     * @param is
     * @param params
     * @throws Exception
     */
    public List<ResponseMessage> excelImport(InputStream is, ImportParams params) throws Exception {
        //读取excel文件转为Javabean
        List<ImportUser> datas = ExcelImportUtil.importExcel(is, ImportUser.class, params);
        List<UserVO> userVOList = new LinkedList<>();
        //部门信息存储与查询
        Set<String> orgNameSet = new HashSet<>();
        Map<String,List<UserVO>> orgNameMap = new HashMap<>();
        //存储部门对应的用户列表
        List<UserVO> userVOS = null;
        //角色信息存储
        Map<String,List<UserVO>> roleNameMap = new HashMap<>();
        String userInfo = null;
        //新增用户对象并存储
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ImportUser user : datas) {
            UserVO userVO = new UserVO();
            userVO.setUsname(user.getName());
            userVO.setAccount(user.getAccount());
            userVO.setPassword(user.getPassword());
            userVO.setCreateTime(simpleDateFormat.format(new Date()));
            userVO.setUpdateTime(simpleDateFormat.format(new Date()));
            userVOList.add(userVO);

            if((userInfo = user.getOrg()) != null) {
                orgNameSet.add(userInfo);
                if((userVOS = orgNameMap.get(userInfo)) == null) {orgNameMap.put(userInfo,userVOS = new LinkedList<>());}
                userVOS.add(userVO);
            }

            if((userInfo = user.getRole()) != null){
                if((userVOS = roleNameMap.get(userInfo)) == null) {
                    roleNameMap.put(userInfo,userVOS = new LinkedList<>()); }
                userVOS.add(userVO);
            }
        }

        //查询部门信息
        for (String orgName:orgNameSet) {
            List<OrgEO> orgEOList = orgEOService.listOrgEOByOrgName(orgName);
            if(orgEOList != null && orgEOList.size() > 0 && orgEOList.get(0) != null){
                for(UserVO userVO:orgNameMap.get(orgName)){
                    userVO.getOrgsstr().add(orgEOList.get(0).getId());
                }
            }
        }

        //查询角色信息
        List<RoleEO> roleEOList = roleEOService.findAll();

        if(roleEOList != null && roleEOList.size() > 0){
            for (RoleEO roleEO:roleEOList) {
                if((userVOS = roleNameMap.get(roleEO.getName())) != null){
                    for(UserVO userVO:userVOS) {userVO.getRolesstr().add(roleEO.getId());}
                }

            }
        }
        ResponseMessage responseMessage = null;
        List<ResponseMessage> responseMessages = new LinkedList<>();
        for(UserVO userVO:userVOList) {
            //保存用户
            if(!(responseMessage = userEOController.create(userVO)).isOk()){
                responseMessage.setMessage(userVO.getUsname() + ","+responseMessage.getMessage());
                responseMessages.add(responseMessage);
            };
        }
        return responseMessages;
    }

    /**
     * excel导入
     * @param is
     * @param params
     * @throws Exception
     */
    public List<ResponseMessage> excelImportnew(InputStream is, ImportParams params) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //读取excel文件转为Javabean
        List<ImportUser> datas = ExcelImportUtil.importExcel(is, ImportUser.class, params);
        List<UserVO> userVOList = new LinkedList<>();
        //部门信息存储与查询
        Set<String> orgNameSet = new HashSet<>();
        Map<String, List<UserVO>> orgNameMap = new HashMap<>();
        //存储部门对应的用户列表
        List<UserVO> userVOS = null;
        //角色信息存储
        Map<String, List<UserVO>> roleNameMap = new HashMap<>();
        String userInfo = null;
        //新增用户对象并存储
        for (ImportUser user : datas) {
            UserVO userVO = new UserVO();
            userVO.setUsname(user.getName());
            userVO.setAccount(user.getAccount());
            userVO.setContactAddress(user.getRank());
            userVO.setExtInfo(String.valueOf(JobClassEnums.getFromName(userVO.getContactAddress()))); // 职级权重
            String pwd = user.getPassword();
            userVO.setPassword(StringUtils.isBlank(pwd) ? DEFALUT_M : pwd); // 默认密码
            userVO.setCreateTime(simpleDateFormat.format(new Date()));
            userVO.setUpdateTime(simpleDateFormat.format(new Date()));
            userVOList.add(userVO);

            if ((userInfo = user.getOrg()) != null) {
                orgNameSet.add(userInfo);
                if ((userVOS = orgNameMap.get(userInfo)) == null){
                    orgNameMap.put(userInfo, userVOS = new LinkedList<UserVO>());}
                userVOS.add(userVO);
            }

            if ((userInfo = user.getRole()) != null) {
                //将角色名称作为键，用户作为链表添加
                if ((userVOS = roleNameMap.get(userInfo)) == null){
                    roleNameMap.put(userInfo, userVOS = new LinkedList<UserVO>());}
                userVOS.add(userVO);
            }
        }

        //查询部门信息
        // 先查询所有部门
        List<OrgWithParentName> allOrgs = getAll(orgListDao.listAllOrg());
        for (String orgName : orgNameSet) {
            OrgWithParentName thisOrg = null;
            for (OrgWithParentName org : allOrgs) {
                if (StringUtils.equals(orgName, org.getNameWithParent())) {
                    thisOrg = org;
                    break;
                }
            }
            if (null != thisOrg) {
                for (UserVO userVO : orgNameMap.get(orgName)) {
                    userVO.getOrgsstr().add(thisOrg.getId());
                }
            }
//            List<OrgEO> orgEOList = orgEOService.listOrgEOByOrgName(orgName);
//            if (orgEOList != null && orgEOList.size() > 0 && orgEOList.get(0) != null) {
//                for (UserVO userVO : orgNameMap.get(orgName)) {
//                    userVO.getOrgsstr().add(orgEOList.get(0).getId());
//                }
//            }
        }

        //查询角色信息
        List<RoleEO> roleEOList = roleEOService.findAll();

        if (roleEOList != null && roleEOList.size() > 0) {
            Map<String, RoleEO> roleEOMap = new HashMap<>();
            for (RoleEO roleEO : roleEOList) {
//                if((userVOS = roleNameMap.get(roleEO.getName())) != null){
//                    for(UserVO userVO:userVOS) userVO.getRolesstr().add(roleEO.getId());
//                }
                roleEOMap.put(roleEO.getName(), roleEO);
            }
            for (Map.Entry<String, List<UserVO>> stringRoleEOEntry : roleNameMap.entrySet()) {
                String[] roleNameArr = stringRoleEOEntry.getKey().split(",");
                for (String s : roleNameArr) {
                    RoleEO roleEO = roleEOMap.get(s);
                    List<UserVO> value = stringRoleEOEntry.getValue();
                    for (UserVO userVO : value) {
                        userVO.getRolesstr().add(roleEO.getId());
                    }
                }
            }

        }
        ResponseMessage responseMessage = null;
        List<ResponseMessage> responseMessages = new LinkedList<>();
        for (UserVO userVO : userVOList) {
//            //保存用户
            // 1. 试图创建用户
            ResponseMessage createResp = userEOController.create(userVO);
            if (!createResp.isOk()) {
                // 2. 根据返回错误码，判断新增失败类型
                if (StringUtils.equals("r0015", createResp.getRespCode())) {
                    // 2.1 如果是r0015，说明此账户的数据已存在，用本次导入的新数据，更新库里的旧数据
                    UserEO oldUserEO = userEOService.getUserByLoginName(userVO.getAccount()); // 获取库里的旧数据
                    userVO.setUsid(oldUserEO.getUsid());
                    userVO.setPassword(PasswordUtils.encryptPassword(userVO.getPassword()));
                    responseMessage = userEOController.update(userVO);
                    if (!responseMessage.isOk()) {
                        responseMessage.setMessage(userVO.getUsname() + "," + responseMessage.getMessage());
                        responseMessages.add(responseMessage);
                    }
                }
            }
//            if (!(userEOController.create(userVO)).isOk()) {
//                if((userEOController.create(userVO)).getRespCode().equals("r0015")){
//                    UserEO userByLoginName = userEOService.getUserByLoginName(userVO.getAccount());
//                    userVO.setUsid(userByLoginName.getUsid());
//                    if (!(responseMessage = userEOController.update(userVO)).isOk()) {
//                        responseMessage.setMessage(userVO.getUsname() + "," + responseMessage.getMessage());
//                        responseMessages.add(responseMessage);
//                    }
//                }
//
//            }
        }
        return responseMessages;
    }

    /**
     * 获取模版excel
     * @return
     * @author liuzixi
     * date 2019-03-07
     */
    public Workbook getTemplate() {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("用户导入模版");
        Sheet typeSheet = workbook.createSheet("typeSheet");
        // 第一行输入表头，包括姓名、部门、角色、职级、用户名
        Row titleRow = sheet.createRow(0);
        Cell nameTitle = titleRow.createCell(0);
        nameTitle.setCellValue("姓名");
        Cell deptTitle = titleRow.createCell(1);
        deptTitle.setCellValue("部门");
        Cell roleTitle = titleRow.createCell(2);
        roleTitle.setCellValue("角色");
        Cell classTitle = titleRow.createCell(3);
        classTitle.setCellValue("职级");
        Cell accTitle = titleRow.createCell(4);
        accTitle.setCellValue("用户名");

        // 部门、角色、职级做筛选框
        // 所有部门
        List<OrgEO> allOrgs = orgListDao.listAllOrg();
        if (CollectionUtils.isNotEmpty(allOrgs)) {
            // 设置部门的筛选框
            int orgNum = allOrgs.size();
            String[] orgNames = new String[orgNum];
            for (int i = 0; i < orgNum; i++) {
                orgNames[i] = getOrgWithParentName(allOrgs.get(i), allOrgs).getNameWithParent();
            }
            // 第二列：第二行到第3000行
            setRangeValues(orgNames, typeSheet, 1);
            sheet.addValidationData(setDataValidation("typeSheet!$B$1:$B$" + orgNames.length,
                    1, 1, 3000, 1));
        }
        // 所有角色
        List<RoleEO> roleEOList = roleEOService.findAll();
        if (CollectionUtils.isNotEmpty(roleEOList)) {
            // 设置角色的筛选框
            int roleNum = roleEOList.size();
            String[] roleNames = new String[roleNum];
            for (int i = 0; i< roleNum; i++) {
                roleNames[i] = roleEOList.get(i).getName();
            }
            // 第三列：第二行到第3000行
            setRangeValues(roleNames, typeSheet, 2);
            sheet.addValidationData(setDataValidation("typeSheet!$C$1:$C$" + roleNames.length,
                    1, 2, 3000, 2));
        }
        // 所有职级
        int jobClassNum = JobClassEnums.values().length;
        String[] jobClassNames = new String[jobClassNum];
        for (int i = 0; i < jobClassNum; i++) {
            jobClassNames[i] = JobClassEnums.values()[i].getName();
        }
        // 第三列：第二行到第3000行
        setRangeValues(jobClassNames, typeSheet, 3);
        sheet.addValidationData(setDataValidation("typeSheet!$D$1:$D$" + jobClassNames.length,
                1, 3, 3000, 3));

        // 隐藏typeSheet
        workbook.setSheetHidden(workbook.getSheetIndex("typeSheet"), 1);

        return workbook;
    }

    /**
     * 所有部门
     * @param allOrgs
     * @return
     * @author liuzixi
     * date 2019-03-08
     */
    List<OrgWithParentName> getAll(List<OrgEO> allOrgs) {
        List<OrgWithParentName> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(allOrgs)) {
            for (OrgEO orgEO : allOrgs) {
                list.add(getOrgWithParentName(orgEO, allOrgs));
            }
        }
        return list;
    }

    /**
     * 展开后名称
     * @param orgEO
     * @param allOrgs
     * @return
     * @author liuzixi
     * date 2019-03-08
     */
    OrgWithParentName getOrgWithParentName(OrgEO orgEO, List<OrgEO> allOrgs) {
        String orgName = orgEO.getName();
        String parentId = orgEO.getParentId();
        StringBuilder builder = new StringBuilder(orgName);
        /*
         * 循环判断条件：找到不含上级id的部门为止
         */
        while (StringUtils.isNotBlank(parentId) && !StringUtils.equals("0", parentId)) {
            OrgEO parent = getParent(parentId, allOrgs);
            if (parent == null) {
                // 内层跳出条件：当上级组织为唯一的最上级组织时，返回null，此时直接跳出字符串拼接
                break;
            }
            builder.insert(0, "-");
            builder.insert(0, parent.getName());

            parentId = parent.getParentId();
        }
        return new OrgWithParentName(orgEO, builder.toString());
    }

    /**
     * 上级
     * @param parentId
     * @param allOrgs
     * @return
     * @author liuzixi
     * date 2019-03-08
     */
    private OrgEO getParent(String parentId, List<OrgEO> allOrgs) {
        for (OrgEO org : allOrgs) {
            if (StringUtils.equals(parentId, org.getId())
                    && StringUtils.isNotBlank(org.getParentId())
                    && !StringUtils.equals("0", org.getParentId())) {
                return org;
            }
        }
        return null;
    }

    /**
     * 在用于存储下拉俩表值的sheet中赋值
     * @param values
     * @param typeSheet
     * @param colNum
     * @return
     * @author liuzixi
     * date 2019-03-08
     */
    private void setRangeValues(String[] values, Sheet typeSheet, int colNum) {
        for (int i = 0, len = values.length; i < len; i++) {
            Row row = typeSheet.getRow(i);
            if (null == row) {
                row = typeSheet.createRow(i);
            }
            Cell cell = row.createCell(colNum);
            cell.setCellValue(values[i]);
        }
    }

    /**
     * 生成筛选框
     * @param strFormula
     * @param firstRow
     * @param firstCol
     * @param endRow
     * @param endCol
     * @return
     * @author liuzixi
     * date 2019-03-08
     */
    private static HSSFDataValidation setDataValidation(String strFormula, int firstRow, int firstCol, int endRow, int endCol) {
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // add
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
        // add
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);

        return dataValidation;
    }
}
