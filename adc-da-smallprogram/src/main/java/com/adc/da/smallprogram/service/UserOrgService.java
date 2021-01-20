package com.adc.da.smallprogram.service;

import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.entity.UserEPEntity;
import com.adc.da.crm.dao.UserDao;
import com.adc.da.crm.entity.UserEOCompare;
import com.adc.da.smallprogram.dao.UserOpenidEODao;
import com.adc.da.smallprogram.dao.UserOrgDao;
import com.adc.da.smallprogram.entity.UserOrgEO;
import com.adc.da.smallprogram.vo.OpenIdResVO;
import com.adc.da.smallprogram.vo.SmallUserVO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.page.UserEOPage;
import com.adc.da.sys.vo.UserVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("userOrgService")
public class UserOrgService {

    @Autowired
    private UserOrgDao userOrgDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private UserEODao userEODao;

    @Autowired
    private UserOpenidEODao userOpenidEODao;

    public ResponseMessage queryUser(String orgId) {
        List<UserOrgEO> list = new ArrayList<>();
        UserOrgEO bean;
        UserEOPage page = new UserEOPage();
        page.setPage(1);
        page.setPageSize(100);
        if ("USW7ASDVED".equals(orgId)) {
            List<UserEO> userEOS = userDao.queryUserEOByUserIds();
            //String[] fakeArr = {"郑继虎", "郑乃金", "惠怡静", "陈平", "朱向雷", "赵冬昶", "张鹏", "杜志彬", "孟菲", "陈辰"};
            for (UserEO userEO : userEOS) {
                bean = beanMapper.map(userEO,UserOrgEO.class);
                list.add(bean);

            }
        } else {
            List<UserOrgEO> list1 = userOrgDao.queryDepLeader(orgId);
            List<UserOrgEO> listFirst = new ArrayList<>();
            List<UserOrgEO> listSecond = new ArrayList<>();
            List<UserOrgEO> listThird = new ArrayList<>();
            //需求变更：一人有多个角色时取级别最低的角色。
            if (CollectionUtils.isNotEmpty(list1)) {
                for (int i = 0; i < list1.size(); i++) {
                    String userId = list1.get(i).getUsid();
                    List<String> roleNames = userOrgDao.queryRoleByUser(userId);
                    if (CollectionUtils.isEmpty(roleNames)) {
                        continue;
                    }
                    if (roleNames.contains("员工")) {
                        list1.remove(i);
                        continue;
                    } else if (roleNames.contains("部长")) {
                        listFirst.add(list1.get(i));
                        continue;
                    } else if (roleNames.contains("副部长")) {
                        listSecond.add(list1.get(i));
                        continue;
                    } else if (roleNames.contains("部长助理")) {
                        listThird.add(list1.get(i));
                        continue;
                    }
                }
                //控制角色之后，改变排序条件 按照部长-》副部长-》部长助理
                listFirst.addAll(listSecond);
                listFirst.addAll(listThird);
                list1 = listFirst;
            } else {

                list1 = userOrgDao.queryAllUserByOrgId(orgId);
            }
            //添加同等权重时按照姓名拼音排序
            sortUserList( list1 , list);
            return Result.success(list);
        }
        return Result.success(list);
    }

    public ResponseMessage queryUserNew(String orgId) {
        List<UserOrgEO> list = new ArrayList<>();
        String parentIds = userOrgDao.queryOrgParentIdsByOrgId(orgId);
        if (StringUtils.isEmpty(parentIds)){
            parentIds = "" ;
        }
        UserEOPage page = new UserEOPage();
        page.setPage(1);
        page.setPageSize(100);
        if ("USW7ASDVED".equals(orgId)) {
            List<UserOrgEO> list1 = userOrgDao.findUserByKeywordADCLeader(orgId);
            //String[] fakeArr = {"郑继虎", "郑乃金", "惠怡静", "陈平", "朱向雷", "赵冬昶", "张鹏", "杜志彬", "孟菲", "陈辰"};
            sortUserList( list1 , list);
        } else if (StringUtils.isNotEmpty(parentIds)&&parentIds.indexOf(",")>-1&&parentIds.split(",").length==3){
            List<UserOrgEO> list1 = userOrgDao.findUserByKeywordBigDeptLeader(orgId);
            //添加同等权重时按照姓名拼音排序
            sortUserList( list1 , list);
        }else {
            List<UserOrgEO> list1 = userOrgDao.findUserByKeywordSmallDeptLeader(orgId);
            //添加同等权重时按照姓名拼音排序
            sortUserList( list1 , list);
        }
        return Result.success(list);
    }

    private void sortUserList(List<UserOrgEO> list1 , List<UserOrgEO> list){
        if (CollectionUtils.isNotEmpty(list1)) {
            for (UserOrgEO sortBean : list1) {
                UserOrgEO userOrgEO = new UserOrgEO();
                UserEOCompare userEOCompare = new UserEOCompare();
                BeanUtils.copyProperties(sortBean, userEOCompare);
                userOrgEO.setUsname(userEOCompare.getUsname());
                userOrgEO.setExtInfo(userEOCompare.getExtInfo());
                userOrgEO.setUsid(userEOCompare.getUsid());
                list.add(userOrgEO);
            }
        }

        Collections.sort(list,new Comparator<UserOrgEO>(){
            public int compare(UserOrgEO arg0, UserOrgEO arg1) {
                if (null== arg1.getExtInfo()||null== arg0.getExtInfo()||StringUtils.equals(arg0.getExtInfo(), arg1.getExtInfo())){
                    return -1 ;
                }

                return Integer.valueOf(arg1.getExtInfo())- Integer.valueOf(arg0.getExtInfo());
            }
        });

    }



    public ResponseMessage queryAllUser(String orgId) {
        List<UserOrgEO> list;
        if (StringUtils.equals("USW7ASDVED", orgId)) {
            list = userOrgDao.queryAllAdcUserByOrgId(orgId);
        } else {
            list = userOrgDao.queryAllUserByOrgId(orgId);
        }
        List<UserOrgEO> sortList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (UserOrgEO sortBean : list) {
                UserOrgEO userOrgEO = new UserOrgEO();
                UserEOCompare userEOCompare = new UserEOCompare();
                BeanUtils.copyProperties(sortBean, userEOCompare);
                userOrgEO.setUsname(userEOCompare.getUsname());
                userOrgEO.setUsid(userEOCompare.getUsid());
                userOrgEO.setExtInfo(userEOCompare.getExtInfo());
                sortList.add(userOrgEO);
            }
        }
        return Result.success(sortList);
    }

    public ResponseMessage queryUserNum(String orgId) {
        List<UserOrgEO> resultList = new ArrayList<>();
        List<UserOrgEO> orgList = userOrgDao.queryChildOrg(orgId);
        int userNum;
        if (CollectionUtils.isNotEmpty(orgList)) {
            for (UserOrgEO bean : orgList) {
                UserOrgEO resultBean = new UserOrgEO();
                if ("USW7ASDVED".equals(bean.getOrgId())) {
                    userNum = userOrgDao.queryAdcNum(bean.getOrgId());
                } else {
                    userNum = userOrgDao.queryUserNumNew(bean.getOrgId());
                }

                resultBean.setUserNum(String.valueOf(userNum));
                resultBean.setOrgName(bean.getOrgABB());
                resultBean.setOrgId(bean.getOrgId());
                resultList.add(resultBean);
            }
        }
        return Result.success(resultList);
    }

    public ResponseMessage findUserByName(String userName, String queryType) {
        List<UserOrgEO> userList;
        if ("allUser".equals(queryType)) {
            userList = userOrgDao.findAllUser();
        } else {
            String usname = "%" + userName + "%";
            userList = userOrgDao.findUserByName(usname);
        }

        return Result.success(userList);
    }

    public ResponseMessage findUserByKeyword(String keyword,String orgId) {
        List<UserOrgEO> userList;

         keyword = "%" + keyword + "%";
        userList = userOrgDao.findUserByKeyword(keyword,orgId);

        Collections.sort(userList,new Comparator<UserOrgEO>(){
            public int compare(UserOrgEO arg0, UserOrgEO arg1) {
                if (null== arg1.getExtInfo()||null== arg0.getExtInfo()||StringUtils.equals(arg0.getExtInfo(), arg1.getExtInfo())){
                    return -1 ;
                }

                return Integer.valueOf(arg1.getExtInfo())- Integer.valueOf(arg0.getExtInfo());
            }
        });

        return Result.success(userList);
    }
    public SmallUserVO updateUserEOByPrimaryKeySelective(UserEO userEO){
        if (StringUtils.isNotEmpty(userEO.getPassword())) {
            String pwd = userEO.getPassword();
            pwd = PasswordUtils.encryptPassword(pwd);
            userEO.setPassword(pwd);
        }
        userEODao.updateByPrimaryKeySelective(userEO);

        UserEO user = userEODao.selectByPrimaryKey(userEO.getUsid());
        SmallUserVO smallUserVO = beanMapper.map(user, SmallUserVO.class);

        return smallUserVO ;
    }

    public ResponseMessage<String> updatePWD(String usid, String oldPWD ,String newPWD){
        UserEO user = userEODao.selectByPrimaryKey(usid);
        if (!PasswordUtils.validatePassword(oldPWD,user.getPassword())) {
            return Result.error("原密码不正确！");
        }
        String newEntrPwd = PasswordUtils.encryptPassword(newPWD);
        userEODao.updatePassword(usid,newEntrPwd);

        userOpenidEODao.deleteByUserId(usid);
        return Result.success("修改成功!" );
    }


}
