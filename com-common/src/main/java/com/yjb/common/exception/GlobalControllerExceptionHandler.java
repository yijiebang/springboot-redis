package com.yjb.common.exception;

import com.yjb.common.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/***
 * @ControllerAdvice
 * 如果是返回json数据 则用 RestControllerAdvice,就可以不加 @ResponseBody
 * 捕获全局异常,处理所有不可知的异常
 * @ExceptionHandler(value=Exception.class)
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value=Exception.class)//处理所有异常
    public ResponseResult exceptionGet(Exception e, HttpServletRequest request) {
        ResponseResult result=new ResponseResult();

        if (e instanceof MyException) { // 项目中用到的自定义异常

            MyException applicationException = (MyException) e;

            result.setResponseCode(applicationException.getCode());
            result.setResponseMessage(applicationException.getMsg());
            result.setUrl(request.getRequestURL());

        }else{ // 未捕获的异常 ,如发生算数异常 1/0

            result.setResponseCode(500);
            result.setResponseMessage(e.getMessage());
            result.setUrl(request.getRequestURL());
        }
        log.error(result.toString());
        return result;
    }

     //#########################################################################################################################################################//
    //####### 建立自定义异常类 MyException，继承于运行时异常：RuntimeException。如果出异常，
    // 在@RestControllerAdvice注释下的CustomExtHandler里面捕获，根据异常种类进行处理。
    // ##########################################################################################################################################################//
}