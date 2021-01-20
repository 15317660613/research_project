package com.adc.da.budget.controller;

import com.adc.da.budget.service.ImportUsersService;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * created by chenhaidong 2019/1/15
 */
@RestController
@RequestMapping("/${restPath}/ImportUsers")
@Api("|ImportUsers|")
@Slf4j
public class ImportUsersController {

    @Autowired
    ImportUsersService importUsersService;

    @ApiOperation(value = "|Project|无验证的Excel单sheet导入")
    @PostMapping("/excelImport")
    public ResponseMessage importUsersByExcel(@RequestParam("file") MultipartFile file){
        try(InputStream is= file.getInputStream();) {
            ImportParams params = new ImportParams();
            List<ResponseMessage> message = importUsersService.excelImportnew(is, params);
            return Result.success(message);
        } catch (IOException e) {
            log.error("获取文件失败", e);
            return Result.success("导入出错，获取文件失败");
        } catch (Exception e) {
            log.error("导入出错", e);
            return Result.success("导入出错");
        }
    }

    /**
     * 获取用户导入模版
     * @param response
     * @return
     * @author liuzixi
     * date 2019-03-08
     */
    @GetMapping("/getTemplate")
    @ApiOperation(value = "获取用户导入模版")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        // 设置response对象
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String("用户导入模版".getBytes("gbk"), "iso-8859-1") + ".xls");
        response.setCharacterEncoding("utf-8");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            Workbook workbook = importUsersService.getTemplate();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            response.setContentType("application/json; charset=utf-8");
            log.error("获取模版失败", e);
            response.getWriter().write(JSONObject.toJSONString(Result.error("程序异常，请重试。")));
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }


}
