package com.adc.da.event.controller;

import com.adc.da.base.page.Pager;
import com.adc.da.base.web.BaseController;
import com.adc.da.event.entity.HistoryFileEO;
import com.adc.da.event.page.HistoryFileEOPage;
import com.adc.da.event.service.HistoryFileEOService;
import com.adc.da.event.vo.HistoryFileVO;
import com.adc.da.file.entity.FileEO;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/event/historyFile")
@Api(tags = "工作简报|HistoryFileEO|")
public class HistoryFileEOController extends BaseController<HistoryFileEO>{

    private static final Logger logger = LoggerFactory.getLogger(HistoryFileEOController.class);

    @Autowired
    private HistoryFileEOService historyFileEOService;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private FileEOService fileEOService;
    @Autowired
    private UserEOService userEOService;

/*
	@ApiOperation(value = "|HistoryFileEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("event:historyFile:page")
    public ResponseMessage<PageInfo<HistoryFileEO>> page(HistoryFileEOPage page) throws Exception {
        List<HistoryFileEO> rows = historyFileEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }
*/
    @ApiOperation(value = "|HistoryFileEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("event:historyFile:page")
    public ResponseMessage<PageInfo<HistoryFileVO>> page(HistoryFileEOPage page) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        List<HistoryFileEO> rows = historyFileEOService.queryByPage(page);
        List<HistoryFileVO> historyFileVOList = beanMapper.mapList(rows, HistoryFileVO.class);

        String fileId = null;
        FileEO fileEO = null;
        UserEO userEO = null;
        for(HistoryFileVO historyFileVO:historyFileVOList){
            fileId = historyFileVO.getFileId();
            if (null == fileId){
                throw new AdcDaBaseException("所选文件不存在");
            }
            fileEO = fileEOService.selectByPrimaryKey(fileId);
            userEO = userEOService.selectByPrimaryKey(fileEO.getUserId());
            historyFileVO.setEventCreateUserId(userEO.getUsid());
            historyFileVO.setEventCreateUserName(userEO.getUsname());
        }

        return Result.success(getMyPageInfo(page.getPager(), historyFileVOList));
    }

    public PageInfo<HistoryFileVO> getMyPageInfo(Pager pager, List<HistoryFileVO> rows) {
        PageInfo<HistoryFileVO> pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount((long)pager.getRowCount());
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setPageCount((long)pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo;
    }

    @ApiOperation(value = "|HistoryFileEO|回置到最新版本的历史文件")
    @GetMapping("/file/{id}")
    //@RequiresPermissions("event:historyFile:get")
    public ResponseMessage<List<HistoryFileEO>> list(@PathVariable("id") String fileId) throws Exception {
        FileEO fileEO = null;
        fileEO = fileEOService.selectByPrimaryKey(fileId);
        String outPath = fileEO.getSavePath();
        File outFile = new File(outPath);
        String s = outFile.getAbsolutePath();
        s= s.substring(0, s.indexOf("\\"));
        outPath = s+"\\Files\\"+outPath;
        outPath =  Pattern.compile("\\\\").matcher(outPath).replaceAll("\\\\\\\\");

        if(outPath == null){
            throw new AdcDaBaseException("文件路径不存在，可能该文件已经被删除");
        }
        HistoryFileEOPage page = new HistoryFileEOPage();
        page.setFileId(fileId);
        page.setOrderBy("edit_time_");
        List<HistoryFileEO> historyFileEOList = historyFileEOService.queryByList(page);
        String inPath = historyFileEOList.get(historyFileEOList.size()-1).getFilePath();

        int len;
        byte[] temp = new byte[1024];
        try (
                FileInputStream fis = new FileInputStream(new File(inPath));
                FileOutputStream fos = new FileOutputStream(outPath);
        ){

            while ((len = fis.read(temp)) != -1) {
                fos.write(temp, 0, len);
            }
            fos.flush();
        }catch (Exception e){
//            e.printStackTrace();
            logger.error(e.getMessage());

        }

        return Result.success();
    }


	@ApiOperation(value = "|HistoryFileEO|查询")
    @GetMapping("")
    //@RequiresPermissions("event:historyFile:list")
    public ResponseMessage<List<HistoryFileEO>> list(HistoryFileEOPage page) throws Exception {
        return Result.success(historyFileEOService.queryByList(page));
	}

    @ApiOperation(value = "|HistoryFileEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("event:historyFile:get")
    public ResponseMessage<HistoryFileEO> find(@PathVariable String id) throws Exception {
        return Result.success(historyFileEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|HistoryFileEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("event:historyFile:save")
    public ResponseMessage<HistoryFileEO> create(@RequestBody HistoryFileEO historyFileEO) throws Exception {
        historyFileEOService.insertSelective(historyFileEO);
        return Result.success(historyFileEO);
    }

    @ApiOperation(value = "|HistoryFileEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("event:historyFile:update")
    public ResponseMessage<HistoryFileEO> update(@RequestBody HistoryFileEO historyFileEO) throws Exception {
        historyFileEOService.updateByPrimaryKeySelective(historyFileEO);
        return Result.success(historyFileEO);
    }

    @ApiOperation(value = "|HistoryFileEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("event:historyFile:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        historyFileEOService.deleteByPrimaryKey(id);
        logger.info("delete from WR_HISTORY_FILE where id = {}", id);
        return Result.success();
    }
}
