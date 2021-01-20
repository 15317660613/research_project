package com.adc.da.ext.sys.log.controller;

import com.adc.da.base.page.Pager;
import com.adc.da.base.web.BaseController;
import com.adc.da.ext.sys.log.entity.LogEOExt;
import com.adc.da.ext.sys.log.page.LogEOExtPage;
import com.adc.da.ext.sys.log.service.LogEOExtService;
import com.adc.da.ext.sys.log.vo.LogVOExt;
import com.adc.da.log.entity.LogEO;
import com.adc.da.log.page.LogEOPage;
import com.adc.da.log.service.LogEOService;
import com.adc.da.log.vo.LogVO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 日志管理模块
 * 1.日志分页查询 @author 李坤澔
 * 2.日志详情
 * date 2018-08-16
 *
 * @author comments created by Lee Kwanho
 * @see LogEOService
 * @see LogEO
 * @see LogVO
 * @see LogEOPage
 * @see mybatis.mapper.log
 */
@RestController
@RequestMapping("/${restPath}/logExt/log")
@Api(tags = "Sys-日志管理")
public class LogEOExtController extends BaseController<LogEOExt> {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(com.adc.da.log.controller.LogEOController.class);

    /**
     * 统一标识日志异常
     */
    private static final String ERROR_INFO = "日志管理异常";

    /**
     * @see LogEOService
     */
    @Autowired
    private LogEOExtService logEOExtService;

    @Autowired
    private UserEOService userEOService;

    /**
     * dozer相关，EO间VO转换
     *
     * @see dozer
     */
    @Autowired
    private BeanMapper beanMapper;

    /**
     * new paging query, using GetMapping.
     * total 5 variable: pageNo,pageSize,account,startTime,endTime
     * date 2018/03/26
     * 按照时间节点
     * 权限字段：sys:log:list
     *
     * @param pageNo    页码
     * @param pageSize  分页大小
     * @param account   账户
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 查询结果
     * @author Lee Kwanho 李坤澔
     */
    @ApiOperation(value = "|LogEO|分页查询")
    @GetMapping("")
    @RequiresPermissions("sys:log:list")
    public ResponseMessage<PageInfo<LogVOExt>> page(Integer pageNo, Integer pageSize, String account,
                                                    String startTime, String endTime, String userName) {

        LogEOExtPage page = new LogEOExtPage();
        //设置排序字段，对应是数据库中的字段非EO中的字段 倒叙排序
        page.setOrderBy("start_time DESC");
        if (pageNo != null) {
            page.setPage(pageNo);
        }
        if (pageSize != null) {
            page.setPageSize(pageSize);
        }

        /* 日志相关账户查询 */
        if (StringUtils.isNotEmpty(account)) {
            page.setAccount(account);
            page.setAccountOperator("LIKE");
        }

        if (StringUtils.isNotEmpty(userName)) {
            page.setUserName(userName);
        }

        /* 设置查询起始时间 */
        if (StringUtils.isNotEmpty(startTime)) {
            page.setStartTime1(startTime);
        }

        /* 设置查询终止时间 */
        if (StringUtils.isNotEmpty(endTime)) {
            page.setStartTime2(endTime);
        }
        page.setPager(new Pager());

        try {
            List<LogEOExt> rows = logEOExtService.queryByPage(page);
            PageInfo<LogVOExt> logVOExtPageInfo = beanMapper.mapPage(getPageInfo(page.getPager(), rows), LogVOExt.class);
            List<String> userIdList = new ArrayList<>();
            for (LogEOExt logEOExt : rows){
                if(StringUtils.isNotEmpty(logEOExt.getUsid())) {
                    userIdList.add(logEOExt.getUsid());
                }
            }
            if (CollectionUtils.isNotEmpty(userIdList)) {
                List<UserEO> userEOList = userEOService.getDao().getUserWithRolesByUserIds(userIdList);
                Map<String, String> userIdDeptNamesMap = userIdDeptNamesMap(userEOList);
                for (LogVOExt logVOExt : logVOExtPageInfo.getList()) {
                    logVOExt.setUserDeptNames(userIdDeptNamesMap.get(logVOExt.getUsid()));
                }
            }
            return Result.success(logVOExtPageInfo);
        } catch (Exception e) {
            logger.error(ERROR_INFO, e);
            return Result.error(ERROR_INFO + e.getMessage());
        }

    }

    private Map<String,String> userIdDeptNamesMap(List<UserEO> userEOList){
        Map<String,String> map = new HashMap<>();
        for (UserEO userEO : userEOList){
            List<OrgEO> orgEOList  =  userEO.getOrgEOList();
            Collections.sort(orgEOList, new Comparator<OrgEO>() {
                @Override
                public int compare(OrgEO o1, OrgEO o2) {
                    //当为null 的情况 this返回-1 所有null在前面 ，反之，在后面
                    if ( o1.getParentIds() == null) {
                        return -1;
                    }
                    if (o2.getParentIds() == null) {
                        return 0;
                    }
                    int plen1 = StringUtils.split(o1.getParentIds(),",").length;
                    int plen2 = StringUtils.split(o2.getParentIds(),",").length;

                    if (plen1 - plen2 < 0){
                        return 1;
                    }else {
                        return -1 ;
                    }
                }
            });
            List<String> orgNameList = new ArrayList<>();
            for (OrgEO orgEO : orgEOList){
                orgNameList.add(orgEO.getName());
            }
            map.put(userEO.getUsid(),StringUtils.join(orgNameList,","));
        }
        return map;
    }

    /**
     * 日志详情
     * 权限字段：sys:log:get
     *
     * @param id 日志id
     * @return 日志详情
     */
    @ApiOperation(value = "|LogEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("sys:log:get")
    public ResponseMessage<LogVOExt> find(@PathVariable String id) {
        try {
            LogVOExt logVO = beanMapper.map(logEOExtService.selectByPrimaryKey(id), LogVOExt.class);
            return Result.success(logVO);
        } catch (Exception e) {
            logger.error(ERROR_INFO, e);
            return Result.error(ERROR_INFO + e.getMessage());
        }

    }

}

