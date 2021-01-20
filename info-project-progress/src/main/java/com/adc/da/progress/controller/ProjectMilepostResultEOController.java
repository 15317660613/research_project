package com.adc.da.progress.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.file.controller.FileUploadRestController;
import com.adc.da.file.entity.MyFileEO;
import com.adc.da.file.service.MyFileEOService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.login.util.CacheUtils;
import com.adc.da.login.util.UserUtils;
import com.adc.da.progress.entity.ProjectMilepostResultEO;
import com.adc.da.progress.page.ProjectMilepostResultEOPage;
import com.adc.da.progress.service.ProjectMilepostResultEOService;
import com.adc.da.progress.vo.ProjectMilepostResultVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.FileUtil;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.subject.Subject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/progress/projectMilepostResult")
@Api(description = "|ProjectMilepostResultEO|")
public class ProjectMilepostResultEOController extends BaseController<ProjectMilepostResultEO> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectMilepostResultEOController.class);

    @Autowired
    private ProjectMilepostResultEOService projectMilepostResultEOService;

    /**
     * @see MyFileEOService
     */
    @Autowired
    private MyFileEOService fileEOService;

    /**
     * @see IFileStore
     */
    @Autowired
    private IFileStore iFileStore;

    @Autowired
    private UserEOService userEOService;

    /**
     * 上传文件类型允许白名单
     */
    private List<String> whiteUrls = new ArrayList<>();

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

    /**
     * 文件上传
     * 权限字段：sys:file:upload
     *
     * @param milepostResultId 指定文件上传路径，空则在根路径
     * @return MyFileEO
     * @throws Exception
     */
    @ApiOperation(value = "|MyFileEO|上传")
    @PostMapping("/upload")
//    @RequiresPermissions("sys:file:upload")
    public ResponseMessage<ProjectMilepostResultVO> upload(String remark, String milepostResultId,
        @RequestParam("file") MultipartFile file) throws Exception {
        Subject subject = (Subject) CacheUtils.getSubjectCache("A6VCZ2NVHC");

        String fileSize = "0";
        long fileLength = file.getSize();
        long length = 0;
        if (fileLength >= 1000000) {
            length = fileLength >> 20;
            fileSize = String.valueOf(length) + "MB";
        } else if (fileLength < 1000000 && fileLength >= 1000) {
            length = fileLength >> 10;
            fileSize = String.valueOf(length) + "KB";
        } else {
            fileSize = String.valueOf(fileLength) + "B";
        }

        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        MyFileEO fileEO;
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
            String path = iFileStore.storeFile(is, fileExtension, "milePostFile");

            /* 设置文件属性 */
            fileEO = new MyFileEO();
            fileEO.setForeignId(milepostResultId);
            fileEO.setFileId(UUID.randomUUID10());
            fileEO.setFileName(FileUtil.getFileName(file.getOriginalFilename()));
            fileEO.setFileType(fileExtension);
            fileEO.setContentType(file.getContentType());
            fileEO.setSavePath(path);
            fileEO.setCreateTime(new Date());
            fileEO.setUserId(userId);
            if (StringUtils.isNotEmpty(remark)) {
                fileEO.setRemark(remark);
                List<MyFileEO> myFileEOList = fileEOService.selectByForeignId(milepostResultId);
                if (CollectionUtils.isNotEmpty(myFileEOList)) {
                    for (MyFileEO myFileEO : myFileEOList) {
//                myFileEO.setForeignId(myFileEO.getForeignId()+"-");
                        fileEOService.deleteByFileId(myFileEO.getFileId());
                    }
                }
            }
            fileEO.setFileSize(fileSize);
            fileEOService.insertSelective(fileEO);
            ProjectMilepostResultEO milepostResultEO = projectMilepostResultEOService
                .selectByPrimaryKey(milepostResultId);
            if (null != milepostResultEO) {
                milepostResultEO.setFileId(fileEO.getFileId());
                projectMilepostResultEOService.updateByPrimaryKeySelective(milepostResultEO);
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return Result.error("r0072", "文件存储失败，请重试");
        } finally {

            IOUtils.closeQuietly(is);
        }
        ProjectMilepostResultVO projectMilepostResultVO = new ProjectMilepostResultVO();
        UserEO userEO = userEOService.selectByPrimaryKey(fileEO.getUserId());
        String uploadUserName = "";
        if (null != userEO) {
            uploadUserName = userEO.getUsname();
        }

        projectMilepostResultVO.setFileId(fileEO.getFileId());
        projectMilepostResultVO.setFileName(fileEO.getFileName());
        projectMilepostResultVO.setUploadUserId(fileEO.getUserId());
        projectMilepostResultVO.setUploadUserName(uploadUserName);
        projectMilepostResultVO.setUploadTime(fileEO.getCreateTime());
        projectMilepostResultVO.setFileSize(fileSize);
        return Result.success(projectMilepostResultVO);
    }

    /**
     * @param fileId
     * @return MyFileEO
     * @throws Exception
     */
    @ApiOperation(value = "|MyFileEO|删除")
    @GetMapping("/deleteFile/{fileId}")
//    @RequiresPermissions("sys:file:upload")
    public ResponseMessage deleteFile(@PathVariable("fileId") String fileId) {
        fileEOService.deleteByFileId(fileId);
        return Result.success("200", "删除成功！", true);
    }

    /**
     * @param fileIds
     * @return MyFileEO
     * @throws Exception
     */
    @ApiOperation(value = "|MyFileEO|移动文件到新的目录下")
    @GetMapping("/moveFile/{fileIds}/{foreignId}")
//    @RequiresPermissions("sys:file:upload")
    public ResponseMessage move(@PathVariable("fileIds") String[] fileIds,
        @PathVariable("foreignId") String foreignId) {

        List<String> fileIdList = Arrays.asList(fileIds);
        fileEOService.moveFile(fileIdList, foreignId);

        return Result.success("200", "移动成功！", true);
    }

    /**
     * 文件下载接口
     * 权限字段：sys:file:download
     *
     * @param response
     * @param fileId   文件id
     * @param fileName 文件名（下载后保存）
     * @throws Exception
     */
    @ApiOperation(value = "|MyFileEO|下载")
    @GetMapping("/{fileId}/download")
//    @RequiresPermissions("sys:file:download")
    public void downFile(HttpServletResponse response, @NotNull @PathVariable("fileId") String fileId,
        String fileName) throws Exception {
        /* 校验 */
        if (StringUtils.isEmpty(fileId)) {
            throw new AdcDaBaseException("FileId不能为空");
        }
        /* 查询文件信息 */
        MyFileEO sysFileEO = fileEOService.selectByPrimaryKey(fileId);
        if (sysFileEO == null) {
            throw new AdcDaBaseException("FileId[" + fileId + "]不存在");
        }

        /*
         * 对文件进行下载操作
         */
        InputStream is = null;
        OutputStream os = null;
        try {
            if (StringUtils.isEmpty(fileName)) {
                fileName = sysFileEO.getFileName() + "." + sysFileEO.getFileType();
            }
            /* 设置header */
            response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType(sysFileEO.getContentType());
            is = iFileStore.loadFile(sysFileEO.getSavePath());
            os = response.getOutputStream();
            IOUtils.copy(is, os);
            os.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("下载文件失败，请重试");
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    @ApiOperation(value = "|ProjectMilepostResultEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("progress:projectMilepostResult:page")
    public ResponseMessage<PageInfo<ProjectMilepostResultEO>> page(ProjectMilepostResultEOPage page) throws Exception {
        List<ProjectMilepostResultEO> rows = projectMilepostResultEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProjectMilepostResultEO|查询")
    @GetMapping("")
//    @RequiresPermissions("progress:projectMilepostResult:list")
    public ResponseMessage<List<ProjectMilepostResultEO>> list(ProjectMilepostResultEOPage page) throws Exception {
        return Result.success(projectMilepostResultEOService.queryByList(page));
    }

    @ApiOperation(value = "|ProjectMilepostResultEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("progress:projectMilepostResult:get")
    public ResponseMessage<ProjectMilepostResultEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectMilepostResultEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectMilepostResultEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:projectMilepostResult:save")
    public ResponseMessage<ProjectMilepostResultEO> create(@RequestBody ProjectMilepostResultEO projectMilepostResultEO)
        throws Exception {
        projectMilepostResultEO.setId(UUID.randomUUID10());
        projectMilepostResultEOService.insertSelective(projectMilepostResultEO);
        return Result.success(projectMilepostResultEO);
    }

    @ApiOperation(value = "|ProjectMilepostResultEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:projectMilepostResult:update")
    public ResponseMessage<ProjectMilepostResultEO> update(@RequestBody ProjectMilepostResultEO projectMilepostResultEO)
        throws Exception {
        projectMilepostResultEOService.updateByPrimaryKeySelective(projectMilepostResultEO);
        return Result.success(projectMilepostResultEO);
    }

    @ApiOperation(value = "|ProjectMilepostResultEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("progress:projectMilepostResult:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectMilepostResultEOService.deleteByPrimaryKey(id);
        logger.info("delete from PR_PROJECT_MILEPOST_RESULT where id = {}", id);
        return Result.success();
    }

}
