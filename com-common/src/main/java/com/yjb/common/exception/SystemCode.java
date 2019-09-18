package com.yjb.common.exception;


import lombok.Getter;
import lombok.Setter;

/**
 * 错误代码定义
 */

public enum SystemCode {

    SUCCESS(1000,"成功执行"),
    NO_DATA(1260,"查询数据为空"),
    LOGIN_ERROR(1001,"用户名或密码错误"),
    LOGINTIMER_ERROR(1003,"登录过期，请重新登录"),
    NOLOGIN_ERROR(1002,"暂未登录，无权限访问"),
    ERROR_CODEDING(1111,"代码执行出错"),
    ERROR(2222,"未知异常");

    @Getter@Setter
    private int code;
    @Getter@Setter
    private String message;

    SystemCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
