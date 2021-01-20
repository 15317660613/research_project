package com.adc.da.crm.controller;

import com.adc.da.base.page.Pager;
import com.adc.da.base.web.BaseController;
import com.adc.da.crm.dao.UserDao;
import com.adc.da.crm.entity.UserEOCompare;
import com.adc.da.crm.service.UserService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.page.UserEOPage;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.sys.vo.UserVO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.io.Resources;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Api(description = "|UserController|")
public class UserController extends BaseController<UserEO> {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserEOService userEOService;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    private OrgEOService orgEOService;

    @Autowired
    private DicTypeEOService dicTypeEOService;


    @ApiOperation("|UserEO|分页查询ss")
    @GetMapping("/api/user/selectUserEO")
    @RequiresPermissions({"sys:user:list"})
    public ResponseMessage<PageInfo<UserVO>> page(Integer pageNo, Integer pageSize, String usName, String roleName, String orgId) {
        UserEOPage page = new UserEOPage();
        if (pageNo != null) {
            page.setPage(pageNo);
        }
        page.setPageSize(10000);
        page.setOrderBy("to_number(u0.EXT_INFO) DESC,u0.ACCOUNT ASC");
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        Subject subject = SecurityUtils.getSubject();
        List<String> orgIds = new ArrayList<>();
        if (StringUtils.isEmpty(orgId)){
            if (subject.hasRole("组长")){
                List<OrgEO> orgEOList = userEOService.getUserWithRoles(userId).getOrgEOList();
                String ordId = "";
                for (OrgEO orgEO : orgEOList) {
                    if (orgEO.getName().contains("组")){
                        ordId = orgEO.getId();
                        orgIds.add(ordId);
                    }
                }
                if(StringUtils.isEmpty(ordId)){
                    ordId = orgEOList.get(0).getId();
                    orgIds.add(ordId);
                }
                getOrgEO(orgIds,ordId);
            } else {
                    UserEO userEO = userEOService.getUserWithRoles(userId);
                    OrgEO orgEO = userEO.getOrgEOList().get(0);
                    orgIds.add(orgEO.getId());
                    //所有子部门
                    List<OrgEO> orgEOList = orgEOService.getOrgEOByPid(orgEO.getId());
                    for (OrgEO eo : orgEOList) {
                        orgIds.add(eo.getId());
                        getOrgEO(orgIds,eo.getId());
                    }
            }
        } else {
            orgIds.add(orgId);
        }
        page.setPager(new Pager());
        try {
            List<UserEO> rows =  userDao.selectByOrgIdAndUsName(orgIds,usName);
            List<UserEOCompare> userLst = new ArrayList<>();
            List<UserEO> usersLst = new ArrayList<>();
            for (UserEO row : rows) {
                UserEOCompare userEOCompare = new UserEOCompare();
                BeanUtils.copyProperties(row,userEOCompare);
                userLst.add(userEOCompare);
            }

            Collections.sort(userLst);

            for (int i= userLst.size()-1;i>=0;i-- ) {
                UserEO userEOCompare = new UserEO();
                BeanUtils.copyProperties(userLst.get(i),userEOCompare);
                usersLst.add(userEOCompare);
            }
            Collections.sort(rows, new Comparator<UserEO>() {
                @Override
                public int compare(UserEO o1, UserEO o2) {
                    return Integer.parseInt(o2.getExtInfo())-Integer.parseInt(o1.getExtInfo());
                }
            });
            return Result.success(this.beanMapper.mapPage(this.getPageInfo(page.getPager(), rows), UserVO.class));
        } catch (Exception var8) {
            logger.error("查询用户数据异常：", var8);
            return Result.error("-1", "查询参数异常");
        }
    }
    private void getOrgEO(List<String>orgIds,String orgId){
        List<OrgEO> orgEOList = orgEOService.getOrgEOByPid(orgId);
        for (OrgEO eo : orgEOList) {
            orgIds.add(eo.getId());
            List<OrgEO> list = orgEOService.getOrgEOByPid(eo.getId());
            if (null == list){
                return ;
            }else {
                getOrgEO(orgIds,eo.getId());
            }
        }
    }

    @ApiOperation("|UserEO|查询组织机构下所有的用户")
    @GetMapping("/api/user/selectAllUserVOByOrgId")
//    @RequiresPermissions({"sys:user:list"})
    public ResponseMessage<List<UserVO>> selectAllUserByOrgId(String orgId,String usName) {
        List<UserEO> userEOList = userService.selectAllUserByOrgId(orgId,usName);
        List<UserVO> userVOList = beanMapper.mapList(userEOList,UserVO.class);
        return Result.success(userVOList);

    }

    @ApiOperation("|UserEO|分页查询中心和领导和本部领导")
    @GetMapping("/api/user/getCenterAndDeptUserEO")
    @RequiresPermissions({"sys:user:list"})
    public ResponseMessage<PageInfo<UserVO>> getCenterAndDeptUserEO() {

        Comparator<UserEO> comparator = new Comparator<UserEO>() {
            public int compare(UserEO s1, UserEO s2) {
                // 根据职级权重倒序排序
                return Integer.valueOf(s2.getExtInfo())  - Integer.valueOf(s1.getExtInfo());
            }
        };

        List<UserEO> userEOList =  userDao.queryUserEOByUserIds();

        Collections.sort(userEOList,comparator);

        return Result.success(this.beanMapper.mapPage(this.getPageInfo(new Pager(), userEOList), UserVO.class));

    }


    @ApiOperation("|UserEO|分页查询ss")
    @GetMapping("/api/user/newSelectUserEO")
    @RequiresPermissions({"sys:user:list"})
    public ResponseMessage<PageInfo<UserVO>> newSelectUserEO(String usName) {
        String userId = UserUtils.getUserId();
        List<String> chargeOrgIdList = userDao.queryOrgIdByUid(userId);
        // 先从字典找一圈
        List<DicTypeEO> dicTypeEOList = dicTypeEOService.queryByList("FAKE_BENBU");
        if(CollectionUtils.isNotEmpty(dicTypeEOList)) {
            for (DicTypeEO dicTypeEO : dicTypeEOList) {
                if (StringUtils.contains(dicTypeEO.getDicTypeName(), userId)) {
                    String orgId = dicTypeEO.getDicTypeCode();
                    chargeOrgIdList.add(orgId);
                }
            }
        }
        List<OrgEO> allOrgEOList =  userDao.listAllOrg();
        List<String> searchOrgIdList = new ArrayList<>();
        searchOrgIdList.addAll(chargeOrgIdList);

        for (OrgEO orgEO : allOrgEOList){
            for (String orgId : chargeOrgIdList) {
                if (orgEO.getParentIds().contains(orgId)) {
                    searchOrgIdList.add(orgEO.getId());
                }
            }
        }

        List<UserEO> rows =  userDao.selectByOrgIdAndUsName(searchOrgIdList,usName);
        return Result.success(this.beanMapper.mapPage(this.getPageInfo(new Pager(), rows), UserVO.class));

    }







}
