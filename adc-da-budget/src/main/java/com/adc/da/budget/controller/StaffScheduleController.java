package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.dto.StaffScheduleRequestDTO;
import com.adc.da.budget.dto.StaffScheduleResponseDTO;
import com.adc.da.budget.entity.Business;
import com.adc.da.budget.entity.UserEPEntity;
import com.adc.da.budget.service.StaffScheduleService;
import com.adc.da.budget.vo.StaffScheduleVO;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * 人员行程日历
 */
@RestController
@RequestMapping("/${restPath}/budget/staffCalendar")
@Api("|StaffSchedule|")
public class StaffScheduleController extends BaseController<Business> {

    private static final Logger logger = LoggerFactory.getLogger(StaffScheduleController.class);

    @Autowired
    private StaffScheduleService staffCalendarService;

    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private UserEODao userEODao;

    @Autowired
    private BeanMapper beanMapper;

    /**
     * 查询
     */
    @PostMapping(value = "/query", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "|StaffScheduleController|查询")
    public ResponseMessage<List<StaffScheduleResponseDTO>> query(HttpServletRequest request,
        @RequestBody StaffScheduleRequestDTO staffScheduleRequestDTO) {

        if (staffScheduleRequestDTO == null
            || staffScheduleRequestDTO.getScheduleBeginDate() == null
            || staffScheduleRequestDTO.getScheduleEndDate() == null) {
            return Result.error("参数不能为空");
        }

        //没有用户ID使用当前用户ID
        if (staffScheduleRequestDTO.getCreateUserId() == null) {
            String userId = (String) request.getSession().getAttribute(RequestUtils.LOGIN_USER_ID);
            staffScheduleRequestDTO.setCreateUserId(userId);
        }

        if (staffScheduleRequestDTO.getScheduleBeginDate().getTime() >= staffScheduleRequestDTO.getScheduleEndDate()
                                                                                               .getTime()) {
            return Result.error("开始的时间不能晚于结束时间");
        }
        return Result.success(staffCalendarService.queryStaffSchedule(staffScheduleRequestDTO.getCreateUserId(),
            staffScheduleRequestDTO.getScheduleBeginDate(), staffScheduleRequestDTO.getScheduleEndDate())
        );

    }

    /**
     * 查询
     */
    @PostMapping(value = "/newQuery", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "|StaffScheduleController|查询")
    public ResponseMessage<Map<String, Object>> newQuery(HttpServletRequest request,
        @RequestBody StaffScheduleRequestDTO staffScheduleRequestDTO) {
        if (staffScheduleRequestDTO == null
            || staffScheduleRequestDTO.getScheduleBeginDate() == null
            || staffScheduleRequestDTO.getScheduleEndDate() == null) {
            return Result.error("参数不能为空");
        }

        //没有用户ID使用当前用户ID
        if (staffScheduleRequestDTO.getCreateUserId() == null) {
            String userId = (String) request.getSession().getAttribute(RequestUtils.LOGIN_USER_ID);
            staffScheduleRequestDTO.setCreateUserId(userId);
        }

        if (staffScheduleRequestDTO.getScheduleBeginDate().getTime() >= staffScheduleRequestDTO.getScheduleEndDate()
                                                                                               .getTime()) {
            return Result.error("开始的时间不能晚于结束时间");
        }
        return Result.success(staffCalendarService.newQueryStaffSchedule(staffScheduleRequestDTO.getCreateUserId(),
            staffScheduleRequestDTO.getScheduleBeginDate(), staffScheduleRequestDTO.getScheduleEndDate())
        );

    }

    /**
     * 按创建人ID 和 时间区间到出日报
     */
    @GetMapping(value = "/exportExcelByCreateUserIdAndTime")
    @ApiOperation(value = "|StaffScheduleController|按创建人ID 和 时间区间导出日报")
    public ResponseMessage exportExcelByCreateUserIdAndTime(HttpServletResponse response, String userId, Long beginDate,
        Long endDate) {
        String fileName = "日报数据.xls";
        OutputStream os = null;
        Workbook workbook = null;

        try {
            response.setHeader(
                "Content-Disposition",
                "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = staffCalendarService.exportExcelByCreateUserIdAndTime(userId, beginDate, endDate);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }

    public ResponseMessage<Map<String, Object>> exportExcelByCreateUserIdAndTime(HttpServletRequest request,
        @RequestBody StaffScheduleRequestDTO staffScheduleRequestDTO) {
        if (staffScheduleRequestDTO == null
            || staffScheduleRequestDTO.getScheduleBeginDate() == null
            || staffScheduleRequestDTO.getScheduleEndDate() == null) {
            return Result.error("参数不能为空");
        }

        //没有用户ID使用当前用户ID
        if (staffScheduleRequestDTO.getCreateUserId() == null) {
            String userId = (String) request.getSession().getAttribute(RequestUtils.LOGIN_USER_ID);
            staffScheduleRequestDTO.setCreateUserId(userId);
        }

        if (staffScheduleRequestDTO.getScheduleBeginDate().getTime() >= staffScheduleRequestDTO.getScheduleEndDate()
                                                                                               .getTime()) {
            return Result.error("开始的时间不能晚于结束时间");
        }
        return Result.success(staffCalendarService.newQueryStaffSchedule(staffScheduleRequestDTO.getCreateUserId(),
            staffScheduleRequestDTO.getScheduleBeginDate(), staffScheduleRequestDTO.getScheduleEndDate())
        );

    }

    /**
     * 查询
     */
    @PostMapping(value = "/newQueryWithOrgId", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "|StaffScheduleController|查询组织机构下所有人的已审批工时")
    public ResponseMessage<List<UserEPEntity>> newQueryWithOrgId(
        @RequestBody StaffScheduleRequestDTO staffScheduleRequestDTO) {
        if (staffScheduleRequestDTO == null
            || staffScheduleRequestDTO.getScheduleBeginDate() == null
            || staffScheduleRequestDTO.getScheduleEndDate() == null) {
            return Result.error("参数不能为空");
        }
        if (staffScheduleRequestDTO.getScheduleBeginDate().getTime() >= staffScheduleRequestDTO.getScheduleEndDate()
                                                                                               .getTime()) {
            return Result.error("开始的时间不能晚于结束时间");
        }
        if (StringUtils.equals(staffScheduleRequestDTO.getOrgId(), "000000")) {
            UserEO userEO = UserUtils.getUser();
            List<OrgEO> orgEOList = userEO.getOrgEOList();
            if (CollectionUtils.isNotEmpty(orgEOList)) {
                String orgId = orgEOList.get(0).getId();
                if (StringUtils.equals(orgId, "USW7ASDVED")) {
                    staffScheduleRequestDTO.setOrgId("MH8JQV5TSN");
                } else {
                    staffScheduleRequestDTO.setOrgId(orgId);
                }
            }
        }
        List<String> orgIdList = orgEODao.getSubOrgId(staffScheduleRequestDTO.getOrgId());
        orgIdList.add(staffScheduleRequestDTO.getOrgId());

        List<UserEO> userEOList = userEODao.getAllUserEOsByOrgIds(orgIdList);
        List<UserEPEntity> userEPEntityList = beanMapper.mapList(userEOList, UserEPEntity.class);

        return Result.success(staffCalendarService.newQueryStaffScheduleWithUserList(userEPEntityList,
            staffScheduleRequestDTO.getScheduleBeginDate(), staffScheduleRequestDTO.getScheduleEndDate())
        );

    }

    @ApiOperation("|StaffSchedule|查询当前用户当前周的日报")
    @GetMapping("/getWeekDays")
    public ResponseMessage<List<StaffScheduleVO>> getWeekDays() throws ParseException {
        return Result.success(staffCalendarService.getWeekView());
    }
}
