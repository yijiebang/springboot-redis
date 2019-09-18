package com.yjb.common.exception;

import lombok.Data;

/*
 * 功能描述: 建立自定义异常类 -- 继承运行异常最高类
 */
@Data/*省去get set*/
public class MyException extends RuntimeException{

    private int code;
    private String msg;


    public MyException(int code, String msg){
        this.code = code;
        this.msg = msg;
     }

}