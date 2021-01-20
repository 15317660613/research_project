package com.adc.da.event.controller;

import com.adc.da.base.page.Pager;
import com.adc.da.base.web.BaseController;
import com.adc.da.event.entity.EventEO;
import com.adc.da.event.entity.EventReceiveEO;
import com.adc.da.event.page.EventReceiveEOPage;
import com.adc.da.event.page.MyEventReceiveEOPage;
import com.adc.da.event.page.SearchPage;
import com.adc.da.event.service.EventEOService;
import com.adc.da.event.service.EventFileEOService;
import com.adc.da.event.service.EventReceiveEOService;
import com.adc.da.event.vo.EventProcessVO;
import com.adc.da.event.vo.EventReceiveVO;
import com.adc.da.event.vo.ParmVO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;


@RestController
@RequestMapping("/${restPath}/event/eventReceive")
@Api(tags= "工作简报|EventReceiveEO|")
public class EventReceiveEOController extends BaseController<EventReceiveEO>{

    private static final Logger logger = LoggerFactory.getLogger(EventReceiveEOController.class);

    @Autowired
    private EventReceiveEOService eventReceiveEOService;
    @Autowired
    private EventFileEOService eventFileEOService;
    @Autowired
    private EventEOService eventEOService;
    @Autowired
    private UserEOService userEOService;
    @Autowired
    private FileEOService fileEOService ;

    @Autowired
    BeanMapper beanMapper;

	@ApiOperation(value = "|EventReceiveEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("event:eventReceive:page")
    public ResponseMessage<PageInfo<EventReceiveEO>> page(EventReceiveEOPage page) throws Exception {
        List<EventReceiveEO> rows = eventReceiveEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }


    @ApiOperation(value = "|EventEO|分页查询")
    @GetMapping("/querySearchPage")
    //@RequiresPermissions("event:event:get")
    public ResponseMessage <PageInfo<EventReceiveEO>> querySearchPage( Integer pageNo , Integer pageSize,String queryFlag) throws Exception {

        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        SearchPage page = new SearchPage() ;
        page.setUserId(userId);
        if (pageNo != null) {
            page.setPage(pageNo);
        }
        if (pageSize != null) {
            page.setPageSize(pageSize);
        }
        if (queryFlag != null) {
            page.setQueryFlag(queryFlag);
        }
        List<EventReceiveEO> eventReceiveEOList = eventReceiveEOService.querySearchPage(page);

        for (EventReceiveEO eventReceiveEO : eventReceiveEOList){
            if (StringUtils.isEmpty(eventReceiveEO.getEventEO().getExtInfo6())){
                eventReceiveEO.getEventEO().setExtInfo6(eventReceiveEO.getFileEO().getFileId());
            }
        }
        return Result.success(getPageInfo(page.getPager(),eventReceiveEOList));
    }




    @ApiOperation(value = "|EventReceiveEO|自定义分页查询")
    @PostMapping("/myPage")
    //@RequiresPermissions("event:eventReceive:page")
    public ResponseMessage<PageInfo<EventReceiveVO>> myPage(MyEventReceiveEOPage page) throws Exception {
        String eventId =null;
        String fileId = null;
        EventEO eventEO =null;
	    List<EventReceiveEO> rows = eventReceiveEOService.queryByMyPage(page);
        List<EventReceiveVO> eventReceiveVOList = beanMapper.mapList(rows, EventReceiveVO.class);
        for(EventReceiveVO eventReceiveVO:eventReceiveVOList){
            eventId = eventReceiveVO.getEventId();

            eventEO = eventEOService.selectByPrimaryKey(eventId);
            fileId = eventFileEOService.selectByEventId(eventId);
            if (null == fileId){
                throw new AdcDaBaseException("所选文件不存在");
            }
            eventReceiveVO.setFileId(fileId);
            eventReceiveVO.setCreateUser(eventEO.getCreateUserId());
            eventReceiveVO.setEventTitle(eventEO.getEventTitle());
        }
        return Result.success(getMyPageInfo(page.getPager(), eventReceiveVOList));
    }
    public PageInfo<EventReceiveVO> getMyPageInfo(Pager pager, List<EventReceiveVO> rows) {
        PageInfo<EventReceiveVO> pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount((long)pager.getRowCount());
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setPageCount((long)pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo;
    }

	@ApiOperation(value = "|EventReceiveEO|查询")
    @GetMapping("")
    //@RequiresPermissions("event:eventReceive:list")
    public ResponseMessage<List<EventReceiveEO>> list(EventReceiveEOPage page) throws Exception {
        return Result.success(eventReceiveEOService.queryByList(page));
	}

    @ApiOperation(value = "|EventReceiveEO|查询已完成和未完d成人数")
    @PutMapping("/selectNumOfReceiveByState/{eventId}")
    //@RequiresPermissions("event:eventReceive:list")
    public ResponseMessage<EventReceiveVO> selectNumOfReceive(@PathVariable("eventId") String eventId) throws Exception {
	    EventReceiveVO eventReceiveVO = new EventReceiveVO();
	    EventReceiveEO eventReceiveEO =new EventReceiveEO();
	    eventReceiveVO.setEventId(eventId);
	    eventReceiveEO.setEventId(eventId);
	    eventReceiveEO.setState(1);
        int numOfStateIs1 = eventReceiveEOService.selectNumOfState(eventReceiveEO);
        eventReceiveEO.setState(0);
        int numOfStateIs0 = eventReceiveEOService.selectNumOfState(eventReceiveEO);
        eventReceiveVO.setNumOfStateIs0(numOfStateIs0);
        eventReceiveVO.setNumOfStateIs1(numOfStateIs1);
        eventReceiveVO.setSum(numOfStateIs0 + numOfStateIs1);
	    return Result.success(eventReceiveVO);
    }



    @ApiOperation(value = "|EventReceiveEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("event:eventReceive:get")
    public ResponseMessage<EventReceiveEO> find(@PathVariable String id) throws Exception {
        return Result.success(eventReceiveEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|EventReceiveEO|详情 根据事件id查询")
    @GetMapping("/selectByEvent/{id}")
    //@RequiresPermissions("event:eventReceive:get")
    public ResponseMessage <List<EventProcessVO>> findByEventId(@PathVariable("id") String eventId) throws Exception {
        return Result.success(eventReceiveEOService.getEventState(eventId));
    }

    @ApiOperation(value = "|EventReceiveEO|详情 根据当前用户id查询")
    @GetMapping("/selectByLoginUser")
    //@RequiresPermissions("event:eventReceive:get")
    public ResponseMessage <List<EventReceiveVO>> findByByLoginUser() throws Exception {
        String eventId = null;
        String fileId = null;
        EventEO eventEO =null;
	    String userId = UserUtils.getUserId();
        List<EventReceiveEO>  eventReceiveEOList= eventReceiveEOService.selectByReceiveUserId(userId);
        List<EventReceiveVO>  eventReceiveVOList = beanMapper.mapList(eventReceiveEOList,EventReceiveVO.class);
        for(EventReceiveVO eventReceiveVO:eventReceiveVOList){
            eventId = eventReceiveVO.getEventId();
            fileId = eventFileEOService.selectByEventId(eventId);
            eventEO = eventEOService.selectByPrimaryKey(eventId);
            eventReceiveVO.setFileId(fileId);
            eventReceiveVO.setCreateUser(eventEO.getCreateUserId());
            eventReceiveVO.setEventTitle(eventEO.getEventTitle());
        }
	    return Result.success(eventReceiveVOList);
    }

   /* @ApiOperation(value = "|EventReceiveEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("event:eventReceive:save")
    public ResponseMessage<EventReceiveEO> create(@RequestBody EventReceiveEO eventReceiveEO) throws Exception {
        eventReceiveEO.setId(UUID.randomUUID10());
	    eventReceiveEOService.insertSelective(eventReceiveEO);
        return Result.success(eventReceiveEO);
    }*/

    @ApiOperation(value = "|EventReceiveEO|新增")
    @PostMapping(value = {"create"})
    //@RequiresPermissions("event:eventReceive:save")
    public ResponseMessage<List<EventReceiveEO>> create(@RequestBody ParmVO parmVO) throws Exception {
        Date now = null;
        String receiveUserName = null;
        String eventId= parmVO.getEventId();
        String []receiveUsersId = parmVO.getReceiveUsersId();
        EventReceiveEO eo = eventReceiveEOService.selectByPrimaryKey(parmVO.getEventReceiveId());
//        eventEOService.setSendFlagById(eventId);
        if (eo.getState()==2){
            eventReceiveEOService.updateByReceiveUserId(3,eo.getEventId(),null);
            eo.setState(3);
        }else if(eo.getState()==1){ //文件是第二次发送,即发送给领导
            eo.setState(3);
            eventReceiveEOService.updateByReceiveUserId(3,eo.getEventId(),null);
        }else if (eo.getState()==0) {
            eo.setState(1);
            eventReceiveEOService.updateByPrimaryKeySelective(eo);
        }
        List<EventReceiveEO> eventReceiveEOList = new ArrayList<EventReceiveEO>();
        for(String receiveUserId:receiveUsersId){
            EventReceiveEO eventReceiveEO = new EventReceiveEO();
            eventReceiveEO.setId(UUID.randomUUID10());
            now = new Date();
            eventReceiveEO.setReceiveTime(now);
            eventReceiveEO.setEventId(eventId);
            eventReceiveEO.setReceiveUserId(receiveUserId);
            UserEO userEO = userEOService.selectByPrimaryKey(receiveUserId);
            if(null == userEO){
                throw new AdcDaBaseException("所选用户不存在");
            }
            receiveUserName = userEO.getUsname();
            eventReceiveEO.setReceiveUserName(receiveUserName);
            eventReceiveEO.setState(0);
            if (eo.getState()==3){
                eventReceiveEO.setState(4);
            }
            eventReceiveEO.setDelFlag(0);
            eventReceiveEO.setExtInfo1(parmVO.getQueryFlag());
            eventReceiveEOService.insertSelective(eventReceiveEO);
            eventReceiveEOList.add(eventReceiveEO);
        }
        return Result.success(eventReceiveEOList);
    }

    @ApiOperation(value = "|EventReceiveEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("event:eventReceive:update")
    public ResponseMessage<EventReceiveEO> update(@RequestBody EventReceiveEO eventReceiveEO) throws Exception {
        eventReceiveEOService.updateByPrimaryKeySelective(eventReceiveEO);
        return Result.success(eventReceiveEO);
    }

    @ApiOperation(value = "|EventReceiveEO|系统生成事件完成的时间")
    @PutMapping(value = {"/finishTime/{id}"})
    //@RequiresPermissions("event:event:update")
    public ResponseMessage<EventReceiveEO> updateCreateTime(@PathVariable("id") String eventEOId) throws Exception {
        Date now = new Date();
        EventReceiveEO eventReceiveEO = new EventReceiveEO();
        eventReceiveEO.setId(eventEOId);
        eventReceiveEO.setFinishTime(now);
        eventReceiveEO.setState(1);
        eventReceiveEOService.updateByPrimaryKeySelective(eventReceiveEO);
        return Result.success(eventReceiveEO);
    }


//    @ApiOperation(value = "|EventReceiveEO|删除")
//    @DeleteMapping("/{id}")
//    //@RequiresPermissions("event:eventReceive:delete")
//    public ResponseMessage delete(@PathVariable String id) throws Exception {
//        eventReceiveEOService.deleteByPrimaryKey(id);
//        logger.info("delete from WR_EVENT_RECEIVE where id = {}", id);
//        return Result.success();
//    }


    @ApiOperation(value = "|EventReceiveEO|软删除，根据eventId删除所有代办")
    @DeleteMapping("/delByEventId/{id}")
    //@RequiresPermissions("event:eventReceive:delete")
    public ResponseMessage delByEventId(@PathVariable String id) throws Exception {
        eventReceiveEOService.delByEventId(id);
        eventEOService.delByPrimaryKey(id);
        return Result.success();
    }

    @ApiOperation(value = "|EventReceiveEO|根据事件id查询代办情况")
    @GetMapping("/getReceiveState/{id}")
    //@RequiresPermissions("event:eventReceive:delete")
    public ResponseMessage<List<EventReceiveEO>> getReceiveState(@PathVariable String id) throws Exception {
        return Result.success( eventReceiveEOService.selectByEventId(id));
    }



}
