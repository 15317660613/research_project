package com.adc.da.file.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.file.entity.MyFileEO;
import com.adc.da.file.service.MyFileEOService;
import com.adc.da.file.vo.MyFileVO;
import com.adc.da.progress.page.MyFilePage;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/${restPath}/progress/myFileEO")
@Api(description = "|MyFileEO|")
public class MyFileEOController extends BaseController<MyFileVO> {
    @Autowired
    private MyFileEOService fileEOService ;

    @Autowired
    private BeanMapper beanMapper ;

    @Autowired
    private UserEOService userEOService ;

    @Value("${file.path}")
    private String basePath;


    @ApiOperation(value = "|MyFileEO|分页查询")
    @PostMapping("/myFilePage")
//    @RequiresPermissions("progress:projectMilepostResult:page")
    public ResponseMessage<PageInfo<MyFileVO>> page(@RequestBody MyFilePage page) throws Exception {
        List<MyFileEO> myFileEOList = fileEOService.queryFileByPage(page);
        List<MyFileVO> myFileVOList = new ArrayList<>();
        for (MyFileEO fileEO : myFileEOList){
            if (StringUtils.isNotEmpty(fileEO.getRemark())) {
                continue;
            }
            MyFileVO fileVO = beanMapper.map(fileEO,MyFileVO.class);
            this.fillMyFileVO(fileVO);
            myFileVOList.add(fileVO);
        }
        return Result.success(getPageInfo(page.getPager(), myFileVOList));
    }

    private void fillMyFileVO( MyFileVO fileVO ) throws Exception{
        if (!this.basePath.endsWith("\\") && !this.basePath.endsWith("/")) {
            this.basePath = this.basePath + File.separator;
        }

        UserEO userEO = userEOService.selectByPrimaryKey(fileVO.getUserId());
        String uploadUserName = "" ;
        if (null != userEO){
            uploadUserName = userEO.getUsname();
        }
        String savePath = fileVO.getSavePath();
        String fullPath = basePath + savePath;
        String fileSize = "0";
        File file = new File(fullPath);
        long fileLength = file.length();
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

        fileVO.setFileSize(fileSize);

        fileVO.setUploadUserName(uploadUserName);

    }

}
