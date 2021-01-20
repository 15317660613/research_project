package com.adc.da.main.advice;

import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.ResponseMessageCodeEnum;
import com.adc.da.util.http.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 异常处理
 */
@ControllerAdvice
@Order(value = 4)
public class AdcDaBaseExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(AdcDaBaseExceptionAdvice.class);

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseMessage ParamIllegalException(IllegalArgumentException exception) {
        logger.warn(exception.getMessage(), exception);
        //return Result.error(exception.getMessage());
        return Result.error("接口调用失败");
    }

    /**
     * 处理所有adc异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(AdcDaBaseException.class)
    public ResponseEntity<ResponseMessage> handlerAdcDaBaseException(AdcDaBaseException exception) {
        logger.warn(exception.getMessage(), exception);
        if (exception.getMessage().contains("登录")) {
            //登录异常 返回401
            return new ResponseEntity<>(
                Result.error(exception.getErrorCode(), exception.getMessage()),
                HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(Result.error(exception.getErrorCode(), exception.getMessage()), HttpStatus.OK);

        }

    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseMessage handlerAdcDaBaseException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        //在数据库中记录程序异常，这个地方的异常是未处理的异常，需要管理员查看并进行处理以防重复出现
        //   异常信息：" + exception.getMessage()
        return Result.error(
            ResponseMessageCodeEnum.ERROR.getCode(),
            "程序异常，请重试。如果重复出现请联系管理员处理！");
    }

}
