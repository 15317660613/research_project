package com.adc.da.onlyoffice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * 该类没有任何意义，存粹是解决访问无效路径时会转发到“/” 或者“/index”路径报的异常问题
 */
@RestController
public class IndexController {
    @RequestMapping("/index")
    public void index(){
        return;
    }
    @RequestMapping("/")
    public void index1(){
        return ;
    }
}
