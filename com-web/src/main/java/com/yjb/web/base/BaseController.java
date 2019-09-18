package com.yjb.web.base;

import com.yjb.common.exception.SystemCode;
import com.yjb.common.utils.ResponseResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 成功之后返回
 */
public class BaseController {

    public ResponseResult returnResu(  HttpServletRequest req , Object body,String msg,int code){
        ResponseResult result=new ResponseResult();
        result.setUrl(req.getRequestURL());
        result.setResponseCode(code);
        result.setResponseMessage(msg);
        result.setData(body);
        return result;
    }

}
