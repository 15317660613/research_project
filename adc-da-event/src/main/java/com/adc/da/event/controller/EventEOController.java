package com.adc.da.event.controller;

import com.adc.da.base.page.Pager;
import com.adc.da.base.web.BaseController;
import com.adc.da.event.dao.EventEODao;
import com.adc.da.event.entity.EventEO;
import com.adc.da.event.entity.EventFileEO;
import com.adc.da.event.entity.EventReceiveEO;
import com.adc.da.event.page.EventEOPage;
import com.adc.da.event.page.MyEventEOPage;
import com.adc.da.event.page.SearchPage;
import com.adc.da.event.service.EventEOService;
import com.adc.da.event.service.EventFileEOService;
import com.adc.da.event.service.EventReceiveEOService;
import com.adc.da.event.vo.EventVO;
import com.adc.da.event.vo.MyEventVO;
import com.adc.da.file.controller.FileUploadRestController;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.store.IFileStore;
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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/event/event")
@Api(tags = "工作简报|EventEO|")
public class EventEOController extends BaseController<EventEO> {

    private static final Logger logger = LoggerFactory.getLogger(EventEOController.class);

    /**
     * 上传文件类型允许白名单
     */
    private static List<String> whiteUrls = new ArrayList<>();

    @Autowired
    BeanMapper beanMapper;

    @Autowired
    private EventEOService eventEOService;

    @Autowired
    private EventEODao eventEODao;

    @Autowired
    private EventReceiveEOService eventReceiveEOService;

    @Autowired
    private EventFileEOService eventFileEOService;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private FileEOService fileEOService;
//    @Autowired
//    private

    @Autowired
    private IFileStore iFileStore;

    @Value("${file.path}")
    private String filepath;

    /**
     * 初始化白名单，改用try-with-resources 写法
     *
     * @author Lee Kwanho 李坤澔
     *     date 2018-08-28
     **/
    @PostConstruct
    public void init() {
        /* 读取白名单文件，路径/adc-da-main/src/main/resources/white
        改为try-with-resources 写法，不用不用手动关闭is和reader */
        try (InputStream is = FileUploadRestController.class
            .getClassLoader()
            .getResourceAsStream("white/uploadWhite.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            /*
             * 添加白名单内容
             */
            String line;
            while ((line = reader.readLine()) != null) {
                if (!"".equals(line)) {
                    whiteUrls.add(line);
                }
            }
        } catch (Exception e) {
            logger.error("读取文件上传白名单异常", e);
        }

    }

    @ApiOperation(value = "|EventEO|分页查询")
    @PostMapping("/page")
    //@RequiresPermissions("event:event:page")
    public ResponseMessage<PageInfo<EventEO>> page(EventEOPage page) throws Exception {
        List<EventEO> rows = eventEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|EventEO|自定义分页查询")
    @GetMapping("/myPage")
    //@RequiresPermissions("event:event:page")
    public ResponseMessage<PageInfo<EventVO>> myPage(MyEventEOPage page) throws Exception {
        String eventId = null;
        String fileId = null;
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        List<EventEO> rows = eventEOService.queryByMyPage(page);
        List<EventVO> eventVOList = beanMapper.mapList(rows, EventVO.class);
        for (EventVO eventVO : eventVOList) {
            eventId = eventVO.getId();
            fileId = eventFileEOService.selectByEventId(eventId);
            if (null == fileId) {
                throw new AdcDaBaseException("所选文件不存在");
            }
            FileEO fileEO = fileEOService.selectByPrimaryKey(fileId);
            eventVO.setSavePath(fileEO.getSavePath());
            eventVO.setFileType(fileEO.getFileType());
            if (StringUtils.equals(userId, fileEO.getUserId())) {
                eventVO.setPermissionState(1);
            } else {
                eventVO.setPermissionState(0);
            }

            eventVO.setFileId(fileId);
        }
        return Result.success(getMyPageInfo(page.getPager(), eventVOList));
    }

    public PageInfo<EventVO> getMyPageInfo(Pager pager, List<EventVO> rows) {
        PageInfo<EventVO> pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount((long) pager.getRowCount());
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setPageCount((long) pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo;
    }

    @ApiOperation(value = "|EventEO|查询")
    @GetMapping("")
    //@RequiresPermissions("event:event:list")
    public ResponseMessage<List<EventEO>> list(EventEOPage page) throws Exception {
        return Result.success(eventEOService.queryByList(page));
    }

    @ApiOperation(value = "|EventEO|详情 根据Id查询")
    @GetMapping("/{id}")
    //@RequiresPermissions("event:event:get")
    public ResponseMessage<EventVO> find(@PathVariable("id") String id) throws Exception {
        String eventId = null;
        String fileId = null;
        EventEO eventEO = eventEOService.selectByPrimaryKey(id);
        EventVO eventVO = beanMapper.map(eventEO, EventVO.class);
        eventId = eventVO.getId();
        fileId = eventFileEOService.selectByEventId(eventId);
        if (null == fileId) {
            throw new AdcDaBaseException("所选文件不存在");
        }
        eventVO.setFileId(fileId);
        return Result.success(eventVO);
        //return Result.success(eventEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|EventEO|详情 根据创建者id查询")
    @GetMapping("/getByCreateUser/{id}")
    //@RequiresPermissions("event:event:get")
    public ResponseMessage<List<EventVO>> findByCreateUserId(@PathVariable("id") String createUserId) throws Exception {
        String eventId = null;
        String fileId = null;
        List<EventEO> eventEOList = eventEOService.selectByCreateUserId(createUserId);
        List<EventVO> eventVOList = beanMapper.mapList(eventEOList, EventVO.class);
        for (EventVO eventVO : eventVOList) {
            eventId = eventVO.getId();
            fileId = eventFileEOService.selectByEventId(eventId);
            if (null == fileId) {
                throw new AdcDaBaseException("所选文件不存在");
            }
            eventVO.setFileId(fileId);
        }
        return Result.success(eventVOList);
    }

    @ApiOperation(value = "|EventEO|查询已办")
    @GetMapping("/querySearchPage")
    //@RequiresPermissions("event:event:get")
    public ResponseMessage<PageInfo<EventVO>> querySearchPage(Integer pageNo, Integer pageSize) throws Exception {

        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        SearchPage page = new SearchPage();
        page.setUserId(userId);
        if (pageNo != null) {
            page.setPage(pageNo);
        }

        if (pageSize != null) {
            page.setPageSize(pageSize);
        }

        List<EventEO> eventEOList = eventEOService.querySearchPage(page);
        List<EventVO> eventVOList = beanMapper.mapList(eventEOList, EventVO.class);
        for (EventVO eventVO : eventVOList) {
            String fileId = eventFileEOService.selectByEventId(eventVO.getId());
            if (null == fileId) {
                throw new AdcDaBaseException("所选文件不存在");
            }
            FileEO fileEO = fileEOService.selectByPrimaryKey(fileId);
            eventVO.setSavePath(fileEO.getSavePath());
            eventVO.setFileType(fileEO.getFileType());
            if (StringUtils.equals(userId, fileEO.getUserId()) && eventVO.getSendFlag() < 4) {
                eventVO.setPermissionState(1);
            } else if (eventVO.getSendFlag() < 3) {
                eventVO.setPermissionState(1);
            } else {
                eventVO.setPermissionState(0);
            }
            eventVO.setFileId(fileId);
        }
        return Result.success(getMyPageInfo(page.getPager(), eventVOList));
    }

//
//    @ApiOperation(value = "|EventEO|查询代办")
//    @GetMapping("/findDoneEventByLoginUser")
//    //@RequiresPermissions("event:event:get")
//    public ResponseMessage <List<EventVO>> findDoneEventByLoginUser(@RequestBody SearchPage page) throws Exception {
//
//        String userId = UserUtils.getUserId();
//        if (StringUtils.isEmpty(userId)) {
//            throw new AdcDaBaseException("登陆可能过期，请登录！");
//        }
//        page.setUserId(userId);
//        List<EventEO> eventEOList = eventEOService.queryDoneBySearchPage(page);
//        List<EventVO>  eventVOList = beanMapper.mapList(eventEOList,EventVO.class);
//        for(EventVO eventVO:eventVOList){
//            String fileId = eventFileEOService.selectByEventId(eventVO.getId());
//            if (null == fileId){
//                throw new AdcDaBaseException("所选文件不存在");
//            }
//            FileEO fileEO = fileEOService.selectByPrimaryKey(fileId);
//            eventVO.setSavePath(fileEO.getSavePath());
//            eventVO.setFileType(fileEO.getFileType());
//            if (StringUtils.equals(userId,fileEO.getUserId())&&eventVO.getSendFlag()<4){
//                eventVO.setPermissionState(1);
//            }else if (eventVO.getSendFlag()<3){
//                eventVO.setPermissionState(1);
//            }else {
//                eventVO.setPermissionState(0);
//            }
//            eventVO.setFileId(fileId);
//        }
//        return Result.success(eventVOList);
//    }
//

    @ApiOperation(value = "|EventEO|新增")
    @PostMapping(value = "/create")
    //@RequiresPermissions("event:event:save")
    public ResponseMessage<EventReceiveEO> create(@RequestBody MyEventVO myVO) throws Exception {
        int sum = eventEOService.selectCountByEventName(myVO.getEventName(), myVO.getQueryFlag());
        if (sum > 0) {
            throw new AdcDaBaseException("名称重复！");
        }
        EventEO eventEO = new EventEO();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        UserEO userEO = userEOService.selectByPrimaryKey(userId);
        /*创建事件*/
        eventEO.setId(UUID.randomUUID10());
        eventEO.setEventTitle(myVO.getEventName());
        eventEO.setCreateUserId(userId);
        eventEO.setCreateUserName(userEO.getUsname());
        eventEO.setCreateTime(new Date());
        eventEO.setSendFlag(0);
        eventEO.setDelFlag(0);
        eventEOService.insertSelective(eventEO);
        /*创建eventFileEO事件*/
        EventFileEO eventFileEO = new EventFileEO();
        eventFileEO.setEventId(eventEO.getId());
        eventFileEO.setFileId(myVO.getFileId());
        eventFileEOService.insert(eventFileEO);
        EventReceiveEO eventReceiveEO = new EventReceiveEO();
        eventReceiveEO.setId(UUID.randomUUID10());
        eventReceiveEO.setEventId(eventEO.getId());
        eventReceiveEO.setState(0);
        eventReceiveEO.setReceiveUserName(userEO.getUsname());
        eventReceiveEO.setReceiveUserId(userId);
        eventReceiveEO.setReceiveTime(new Date());
        eventReceiveEO.setDelFlag(0);
        eventReceiveEO.setExtInfo1(myVO.getQueryFlag());
        eventReceiveEOService.insertSelective(eventReceiveEO);

        return Result.success(eventReceiveEO);
    }

    @ApiOperation("|EventEO|文件编辑保存")
    @RequestMapping(
        value = {"/save"},
        method = {RequestMethod.POST},
        produces = {"application/json;charset=UTF-8"}
    )
    @ResponseBody
    public void saveWord(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            PrintWriter writer = response.getWriter();
            String body = "";

            try {
                Scanner scanner = new Scanner(request.getInputStream());
                scanner.useDelimiter("\\A");
                body = scanner.hasNext() ? scanner.next() : "";
                scanner.close();
            } catch (Exception var41) {
                writer.write("get request.getInputStream error:" + var41.getMessage());
                return;
            }

            if (body.isEmpty()) {
                writer.write("empty request.getInputStream");
                return;
            }

            JSONObject jsonObj = JSON.parseObject(body);
            System.out.println(body);
            int status = (Integer) jsonObj.get("status");
            JSONArray jsonArray = (JSONArray) jsonObj.get("users");
            Object obj = jsonArray.get(0);
            String users = String.valueOf(obj);
            int saved = 0;
            if (status == 6 || status == 2) {

                String downloadUri = (String) jsonObj.get("url");

                try {
                    URL url = new URL(downloadUri);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream stream = connection.getInputStream();
                    if (stream == null) {
                        throw new Exception("Stream is null");
                    }
                    String eventReceiveId = request.getParameter("eventReceiveId");
                    String userId = request.getParameter("loginUserId");
                    if (StringUtils.isNotEmpty(eventReceiveId)) {
                        EventReceiveEO eventReceiveEO = eventReceiveEOService.selectByPrimaryKey(users);

                        if (status == 2) {
                            EventEO eventEO = eventEOService.selectByPrimaryKey(eventReceiveEO.getEventId());
                            eventEO.setExtInfo6(UUID.randomUUID10());
                            eventEOService.updateByPrimaryKeySelective(eventEO);
                        }

                        if (eventReceiveEO.getState() == 2) {
                            eventReceiveEOService.updateByReceiveUserId(3, eventReceiveEO.getEventId(), null);
                        } else if (eventReceiveEO.getState() != 3) {
                            eventReceiveEO.setState(1);
                            eventReceiveEOService.updateByPrimaryKeySelective(eventReceiveEO);
                            List<EventReceiveEO> eventReceiveEOList = eventReceiveEOService
                                .selectByEventId(eventReceiveEO.getEventId());
                            boolean flag = true;
                            Iterator var18 = eventReceiveEOList.iterator();

                            while (var18.hasNext()) {
                                EventReceiveEO eo = (EventReceiveEO) var18.next();
                                if (eo.getState() != 1) {
                                    flag = false;
                                    break;
                                }
                            }

                            if (flag) {
                                String eventId = eventReceiveEOList.get(0).getEventId();
                                EventEO eventEO = eventEOService.selectByPrimaryKey(eventId);
                                String createUserId = eventEO.getCreateUserId();
                                eventReceiveEOService.updateByReceiveUserId(2, eventId, createUserId);
                            }
                        }
                    }

                    String path = this.filepath + request.getParameter("path");
                    /*String historyPath = forSave.substring(0, forSave.lastIndexOf("."));*/
                    String suffix = path.substring(path.lastIndexOf(".") + 1);
                   /* new Date();
                    historyPath = historyPath + "_" + System.currentTimeMillis() + "." + suffix;*/
                    /*String forRename = this.filepath + historyPath;
                    File file = new File(path);
                    FileInputStream fis = new FileInputStream(file);
                    FileOutputStream fos = new FileOutputStream(forRename);
                    byte[] temp = new byte[fis.available()];

                    for (int i = 0; i < temp.length; ++i) {
                        int b = fis.read();
                        temp[i] = (byte) b;
                    }

                    fos.write(temp);
                    fis.close();
                    fos.close();*/
                    File savedFile = new File(path);
                    FileOutputStream out = new FileOutputStream(savedFile);
                    Throwable var27 = null;

                    try {
                        byte[] bytes = new byte[1024];

                        while (true) {
                            int read;
                            if ((read = stream.read(bytes)) == -1) {
                                out.flush();
                                break;
                            }

                            out.write(bytes, 0, read);
                        }
                    } catch (Throwable var42) {
                        var27 = var42;
                        throw var42;
                    } finally {
                        if (out != null) {
                            if (var27 != null) {
                                try {
                                    out.close();
                                } catch (Throwable var40) {
                                    var27.addSuppressed(var40);
                                }
                            } else {
                                out.close();
                            }
                        }

                    }

                    connection.disconnect();
                } catch (Exception var44) {
                    saved = 1;
                    logger.error(var44.getMessage());
//                    var44.printStackTrace();
                }
            }

            System.out.print("编辑完成--------------11111");
            writer.write("{\"error\":" + saved + "}");
        } catch (IOException var45) {
            logger.error(var45.getMessage());
//            var45.printStackTrace();
        } catch (Exception var46) {
            logger.error(var46.getMessage());
        }

    }

    @ApiOperation(value = "|EventEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("event:event:update")
    public ResponseMessage<EventEO> update(@RequestBody EventEO eventEO) throws Exception {
        eventEOService.updateByPrimaryKeySelective(eventEO);
        return Result.success(eventEO);
    }

    @ApiOperation(value = "|EventEO|系统生成事件保存的时间")
    @PutMapping(value = {"/createTime/event/{Id}"})
    //@RequiresPermissions("event:event:update")
    public ResponseMessage<EventEO> updateCreateTime(@PathVariable("Id") String eventEOId) throws Exception {
        Date now = new Date();
        EventEO eventEO = new EventEO();
        eventEO.setId(eventEOId);
        eventEO.setCreateTime(now);
        eventEOService.updateByPrimaryKeySelective(eventEO);
        return Result.success(eventEO);
    }


    @ApiOperation(value = "|EventEO|删除,将删除标志位置为1")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("event:event:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        eventEOService.delByPrimaryKey(id);
        return Result.success();
    }

}
