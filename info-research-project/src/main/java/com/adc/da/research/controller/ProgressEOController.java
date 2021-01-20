package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.entity.ProgressEO;
import com.adc.da.research.page.ProgressEOPage;
import com.adc.da.research.service.ProgressEOService;
import com.adc.da.research.vo.ImageVO;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.FileUtil;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/progress")
@Api(tags = "|科研类项目模块|")
@Slf4j
public class ProgressEOController extends BaseController<ProgressEO> {

    @Autowired
    private ProgressEOService progressEOService;

    @Autowired
    private FileEOService fileEOService;

    @Autowired
    private IFileStore iFileStore;

    @ApiOperation(value = "|ProgressEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions"research:progress:page")
    public ResponseMessage<PageInfo<ProgressEO>> page(ProgressEOPage page) throws Exception {
        List<ProgressEO> rows = progressEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProgressEO|查询")
    @GetMapping("")
    //@RequiresPermissions"research:progress:list")
    public ResponseMessage<List<ProgressEO>> list(ProgressEOPage page) throws Exception {
        return Result.success(progressEOService.queryByList(page));
    }

     
    @ApiOperation(value = "|ProgressEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions"research:progress:save")
    public ResponseMessage<ProgressEO> create(@RequestBody ProgressEO progressEO) throws Exception {
        progressEO.setId(UUID.randomUUID10());
        ProgressEOPage progressEOPage = new ProgressEOPage();
        progressEOPage.setCheckDetail(progressEO.getCheckDetail());
        progressEOPage.setResearchProjectId(progressEO.getResearchProjectId());
        if (progressEOService.queryByCount(progressEOPage) > 0) {
            return Result.error("1", "该检查内容已存在", progressEO);
        }
        progressEOService.insertSelective(progressEO);
        return Result.success(progressEO);
    }

    @ApiOperation(value = "|ProgressEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions"research:progress:update")
    public ResponseMessage<ProgressEO> update(@RequestBody ProgressEO progressEO) throws Exception {

        if (-1 == progressEOService.updateByPrimaryKeySelective(progressEO)) {
            return Result.error("1", "该检查内容已存在", progressEO);
        } else {
            return Result.success(progressEO);
        }
    }

    @ApiOperation(value = "|ProgressEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions"research:progress:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        progressEOService.deleteByPrimaryKey(id);
        log.info("delete from RS_PROJECT_PROGRESS where id = {}", id);
        return Result.success();
    }

    /**
     * 图片上传
     * 权限字段：
     *
     * @param filePath 指定文件上传路径，空则在根路径
     * @return ImageVO
     * @throws Exception
     */
    @ApiOperation(value = "|ProgressEO|上传图片")
    @PostMapping("/uploadImage")
    public ResponseMessage<ImageVO> uploadImage(String filePath,
        @RequestParam("file") MultipartFile file) throws Exception {
        String userId = UserUtils.getUserId();
        if (null == userId) {
            return Result.error("登陆可能过期，或没有登陆！");
        }
        FileEO fileEO;
        InputStream is = null;
        try {
            /* 获取文件后缀 */
            String fileExtension = FileUtil.getFileExtension(file.getOriginalFilename());

            is = file.getInputStream();
            /* 设置路径*/
            String path = iFileStore.storeFile(is, fileExtension, filePath);

            /* 设置文件属性 */
            fileEO = new FileEO();
            fileEO.setFileId(UUID.randomUUID());
            fileEO.setFileName(FileUtil.getFileName(file.getOriginalFilename()));
            fileEO.setFileType(fileExtension);
            fileEO.setContentType(file.getContentType());
            fileEO.setSavePath(path);
            fileEO.setCreateTime(new Date());
            fileEO.setUserId(userId);
            fileEO.setFileSize(String.valueOf(file.getSize()));

            fileEOService.insertSelective(fileEO);
        } catch (IOException e) {
            return Result.error("r0072", "文件存储失败，请重试");
        } finally {
            IOUtils.closeQuietly(is);
        }
        ImageVO imageVO = new ImageVO();
        imageVO.setSrc(fileEO.getSavePath());
        imageVO.setTitle(fileEO.getFileName());
        return Result.success(imageVO);
    }

}
