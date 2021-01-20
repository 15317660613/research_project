package com.adc.da.smallprogram.service;

import com.adc.da.smallprogram.constant.Role;
import com.adc.da.smallprogram.dao.UserOpenidEODao;
import com.adc.da.smallprogram.entity.UserOpenidEO;
import com.adc.da.smallprogram.enums.DeleteCodeEnum;
import com.adc.da.smallprogram.page.UserOpenidEOPage;
import com.adc.da.smallprogram.vo.SmallLoginVO;
import com.adc.da.smallprogram.vo.SmallUserVO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.RoleEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.RoleEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service("smallUserService")
@Slf4j
public class SmallUserService {

    @Autowired
    private UserEOService userService;
    @Autowired
    private UserOpenidEODao userOpenidEODao;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private RoleEOService roleEOService;
    @Autowired
    private UserEODao userEODao ;

    /**
     * 根据openId 获取用户信息
     *
     * @param openId
     * @return
     */
    public ResponseMessage getUserByOpenId(String openId) {
        if (StringUtils.isBlank(openId)) {
            return Result.error("openId为空!");
        }
//        UserEO userEO = userOpenidEODao.getUserByOpenId(openId);

        String userId = userOpenidEODao.getUserIdByOpenId(openId);
        UserEO userEO = userEODao.getUserWithRoles(userId);
        if (userEO == null) {
            return Result.error("未查询到用户信息");
        }
        //校验绑定用户角色，部长级及以上可以导入，
        userEO.setExtInfo3("0");
        List<RoleEO> roleEOList = roleEOService.getSysRoleListByUserId(userEO.getUsid());

        if (!judgePermission(roleEOList,userEO)) {
            return Result.error("该账号无权限，请更换账号!");
        }

        SmallUserVO smallUserVO = beanMapper.map(userEO, SmallUserVO.class);
        smallUserVO.setOpenId(openId);
        return Result.success(smallUserVO);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage checkUser(SmallLoginVO smallLoginVO) {
        //用户名不为空，获取用户信息
        if (StringUtils.isBlank(smallLoginVO.getUsername())) {
            return Result.error("用户名为空!");
        }
        if (StringUtils.isBlank(smallLoginVO.getPassword())) {
            return Result.error("密码为空!");
        }
        if (StringUtils.isBlank(smallLoginVO.getOpenId())) {
            return Result.error("用户openId为空!");
        }
        //校验openId是否已经绑定了用户
        int result = userOpenidEODao.checkUserByOpenId(smallLoginVO.getOpenId());
        if (result > 0){
            return Result.error("该账号已被绑定！");
        }
            UserEO userEO = userService.getUserByLoginNameNotDeleted(smallLoginVO.getUsername());
        //获取用户信息不为空 且密码不为空
        if (userEO == null) {
            return Result.error("未查询到此用户!");
        }
        //校验密码正确性
        if (!PasswordUtils.validatePassword(smallLoginVO.getPassword(), userEO.getPassword())) {
            return Result.error("密码校验失败!");
        }
        //校验绑定用户角色，部长级及以上可以绑定成功，部长级以下员工不可绑定，不可进入小程序主界面
        List<RoleEO> roleEOList = roleEOService.getSysRoleListByUserId(userEO.getUsid());
        userEO.setRoleEOList(roleEOList);
        List<String> roleIdList = getRoleIdList(roleEOList);
        userEO.setRoleIdList(roleIdList);

        if (!judgePermission(roleEOList,userEO)) {
            return Result.error("该账号无权限，请更换账号!");
        }
        UserOpenidEOPage userOpenidEOPage = new UserOpenidEOPage();
        userOpenidEOPage.setUserId(userEO.getUsid());
        List<UserOpenidEO> userOpenidEOList = userOpenidEODao.queryByList(userOpenidEOPage);
        if (CollectionUtils.isNotEmpty(userOpenidEOList)) {
            return Result.error("该用户已被绑定,请更换用户!");
        }
        //密码校验成功，进行绑定openID和userId操作
        UserOpenidEO userOpenidEO = new UserOpenidEO();
        userOpenidEO.setDelFlag(Long.valueOf(DeleteCodeEnum.NORMAL.getCode()));
        userOpenidEO.setOpenId(smallLoginVO.getOpenId());
        userOpenidEO.setUserId(userEO.getUsid());
        userOpenidEODao.insertSelective(userOpenidEO);
        SmallUserVO smallUserVO = beanMapper.map(userEO, SmallUserVO.class);
        smallUserVO.setOpenId(smallLoginVO.getOpenId());

        return Result.success(smallUserVO);
    }

    private Boolean judgePermission(List<RoleEO> roleEOList,UserEO userEO){
        boolean flag = false;
        userEO.setExtInfo3("0");
        for (RoleEO roleEO : roleEOList) {
            if (!StringUtils.isEmpty(roleEO.getExtInfo())&&
                    (StringUtils.equals(roleEO.getName(), Role.ZHU_REN)
                            ||StringUtils.equals(roleEO.getName(),Role.BEN_BU_ZHANG))){
                userEO.setExtInfo3("1");
            }
            if (!StringUtils.isEmpty(roleEO.getExtInfo())&&Role.ROLE_CODE.contains(roleEO.getExtInfo())){
                flag = true;
            }
        }
        List<String> roleCodeList = new ArrayList<>();
        roleCodeList.add("SCHEDULE_SENIOR_LEADER");
        List<UserEO> userEOList = userEODao.getAllUserEOsByRoleCode(roleCodeList);
        for (UserEO user : userEOList){
            if (StringUtils.equals(user.getUsid(),userEO.getUsid())){
                userEO.setExtInfo4("1");
                break;
            }
        }

        return flag ;
    }

    private List<String> getRoleIdList(List<RoleEO> roleEOList){
        List<String> roleIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roleEOList)){
            for (RoleEO roleEO : roleEOList){
                roleIdList.add(roleEO.getId());
            }
        }
        return roleIdList ;
    }

}
