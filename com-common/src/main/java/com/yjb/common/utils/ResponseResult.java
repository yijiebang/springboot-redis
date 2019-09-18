package com.yjb.common.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回的实体类
 */
@Data/*省去get set */
public class ResponseResult implements Serializable  {
    private int ResponseCode;
    private String ResponseMessage;
    private Object data;
     private StringBuffer url;
}
