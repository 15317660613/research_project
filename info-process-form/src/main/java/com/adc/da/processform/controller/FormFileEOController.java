package com.adc.da.processform.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.processform.entity.FormFileEO;
import com.adc.da.processform.page.FormFileEOPage;
import com.adc.da.processform.service.FormFileEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/${restPath}/processform/formFile")
@Api(description = "|FormFileEO|")
public class FormFileEOController extends BaseController<FormFileEO>{

    private static final Logger logger = LoggerFactory.getLogger(FormFileEOController.class);

    @Autowired
    private FormFileEOService formFileEOService;

    /**
     * @see FileEOService
     */
    @Autowired
    private FileEOService fileEOService;

    /**
     * @see IFileStore
     */
    @Autowired
    private IFileStore iFileStore;

    /**
     * 上传文件类型允许白名单
     */
    private List<String> whiteUrls = new ArrayList<>();

    /**
     * 初始化白名单，改用try-with-resources 写法
     *
     * @author Lee Kwanho 李坤澔
     * date 2018-08-28
     **/
    @PostConstruct
    public void init() {
        /* 读取白名单文件，路径/adc-da-main/src/main/resources/white
        改为try-with-resources 写法，不用不用手动关闭is和reader */
        try (InputStream is = FormFileEOController.class
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


    @ApiOperation(value = "|FormFileEO|查询")
    @GetMapping("/selectByProcessAndBelong")
//    @RequiresPermissions("processform:formFile:list")
    public ResponseMessage selectByProcessAndBelong(String processinstid , String   filebelong) throws Exception {
        FormFileEO formFile = formFileEOService.selectByProcessAndBelong(processinstid,filebelong);

        if (null != formFile) {
            return Result.success();
        }else {
            return Result.error();
        }

    }




    /**
     * 文件上传
     * 权限字段：sys:file:upload
     *
     * @param filePath 指定文件上传路径，空则在根路径
     * @return FileEO
     * @throws Exception
     */
    @ApiOperation(value = "|FileEO|上传")
    @PostMapping("/upload")
    @RequiresPermissions("sys:file:upload")
    public ResponseMessage<FileEO> upload(String filePath, String userId ,
            String processinstid , String   filebelong ,
            @RequestParam("file") MultipartFile file) throws Exception {

        FormFileEO formFile = formFileEOService.selectByProcessAndBelong(processinstid,filebelong);

        if (null != formFile) {
            return Result.error("当前表单文件已存在，请与管理员联系！");
        }


        FileEO fileEO;
        InputStream is = null;
        try {
            /* 获取文件后缀 */
            String fileExtension = FileUtil.getFileExtension(file.getOriginalFilename());
            /* 校验文件类型 */
            if (!whiteUrls.contains(fileExtension)) {
                logger.error("上传文件类型不允许，请重试");
                return Result.error("r0071", "上传文件类型不允许，请重试");
            }

            is = file.getInputStream();
            /* 设置路径*/
            String path = iFileStore.storeFile(is, fileExtension, filePath);

            /* 设置文件属性 */
            fileEO = new FileEO();
            String fileId = UUID.randomUUID() ;
            fileEO.setFileId(fileId);
            fileEO.setFileName(FileUtil.getFileName(file.getOriginalFilename()));
            fileEO.setFileType(fileExtension);
            fileEO.setContentType(file.getContentType());
            fileEO.setSavePath(path);
            fileEO.setCreateTime(new Date());
            fileEO.setUserId(userId);
            fileEOService.insertSelective(fileEO);
            FormFileEO formFileEO = new FormFileEO();

            formFileEO.setId(UUID.randomUUID());
            formFileEO.setFileid(fileId);
            formFileEO.setProcessinstid(processinstid);
            formFileEO.setFilebelong(filebelong);

            formFileEOService.insert(formFileEO);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return Result.error("r0072", "文件存储失败，请重试");
        } finally {
            IOUtils.closeQuietly(is);
        }
        return Result.success("200","上传成功！",fileEO);
    }





    @ApiOperation("|FileEO|下载")
    @GetMapping({"/download"})
    @RequiresPermissions({"sys:file:download"})
    public void downFile(HttpServletResponse response, String fileName ,  String processinstid , String   filebelong ) throws Exception {
        if (StringUtils.isEmpty(processinstid) || StringUtils.isEmpty(filebelong)) {
            throw new AdcDaBaseException("processinstid不能为空");
        } else {

            FormFileEO formFileEO = formFileEOService.selectByProcessAndBelong(processinstid,filebelong);

            if (null == formFileEO) {
                throw new AdcDaBaseException("当前表单没有上传文件或已被删除,如有疑问请联系管理员!");
            }

            FileEO sysFileEO = (FileEO)this.fileEOService.selectByPrimaryKey(formFileEO.getFileid());
            if (sysFileEO == null) {
                throw new AdcDaBaseException("FileId[" + formFileEO.getFileid() + "]不存在");
            } else {
                InputStream is = null;
                ServletOutputStream os = null;

                try {
                    if (StringUtils.isEmpty(fileName)) {
                        fileName = sysFileEO.getFileName() + "." + sysFileEO.getFileType();
                    }

                    response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
                    response.setContentType(sysFileEO.getContentType());
                    is = this.iFileStore.loadFile(sysFileEO.getSavePath());
                    os = response.getOutputStream();
                    IOUtils.copy(is, os);
                    os.flush();
                } catch (IOException var11) {
                    logger.error(var11.getMessage(), var11);
                    throw new AdcDaBaseException("下载文件失败，请重试");
                } finally {
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(os);
                }

            }
        }
    }


    @ApiOperation(value = "|FormFileEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("processform:formFile:page")
    public ResponseMessage<PageInfo<FormFileEO>> page(FormFileEOPage page) throws Exception {
        List<FormFileEO> rows = formFileEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|FormFileEO|查询")
    @GetMapping("")
    @RequiresPermissions("processform:formFile:list")
    public ResponseMessage<List<FormFileEO>> list(FormFileEOPage page) throws Exception {
        return Result.success(formFileEOService.queryByList(page));
	}

    @ApiOperation(value = "|FormFileEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("processform:formFile:get")
    public ResponseMessage<FormFileEO> find(@PathVariable String id) throws Exception {
        return Result.success(formFileEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|FormFileEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("processform:formFile:save")
    public ResponseMessage<FormFileEO> create(@RequestBody FormFileEO formFileEO) throws Exception {
        formFileEOService.insertSelective(formFileEO);
        return Result.success(formFileEO);
    }

    @ApiOperation(value = "|FormFileEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("processform:formFile:update")
    public ResponseMessage<FormFileEO> update(@RequestBody FormFileEO formFileEO) throws Exception {
        formFileEOService.updateByPrimaryKeySelective(formFileEO);
        return Result.success(formFileEO);
    }

    @ApiOperation(value = "|FormFileEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("processform:formFile:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        formFileEOService.deleteByPrimaryKey(id);
        logger.info("delete from PF_FORM_FILE where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|FormFileEO|软删除")
    @GetMapping ("/softDelete")
//    @RequiresPermissions("processform:formFile:delete")
    public ResponseMessage softDelete( String processinstid, String filebelong) throws Exception {
        formFileEOService.softDelete(processinstid,filebelong);
        logger.info("delete from PF_FORM_FILE where processinstid = {}", processinstid);
        return Result.success();
    }







}
