package com.yjb.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yjb.common.Cookie.CookieUtils;
import com.yjb.common.exception.SystemCode;
import com.yjb.common.utils.ResponseResult;
import com.yjb.web.base.BaseController;
import com.yjb.web.entity.Login;
import com.yjb.web.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class LoginController extends BaseController {

    @Value("${REDIS_SESSION_KEY}")
    String REDIS_SESSION_KEY;
    @Value("${SESSION_EXPIRE}")
    int SESSION_EXPIRE;
    @Value("${LOGIN_CookName}")
    String LOGIN_CookName;

    @Autowired
    ILoginService loginService;

    @Autowired
    private StringRedisTemplate redis;

    @ResponseBody
    @RequestMapping(value = "/login")
    public ResponseResult login(@RequestBody Login login, HttpSession session, HttpServletRequest req, HttpServletResponse response) {
        Assert.notNull(login.getLoginUsername(),"空的參數");
        String msg=SystemCode.SUCCESS.getMessage();
        int code=SystemCode.SUCCESS.getCode();
        Login user = loginService.getOne(new QueryWrapper<Login>().lambda().eq(Login::getLoginUsername, login.getLoginUsername()).eq(Login::getLoginPwd,login.getLoginPwd()));

          if (user!=null){
              //登录成功，生成token
              String token = UUID.randomUUID().toString();
              System.out.println("dddl:"+token);
             //把用户信息写入redis
              //key:REDIS_SESSION:{TOKEN}
              //value:user转成json
              user.setLoginPwd(null);
              String jsonString = JSONObject.toJSONString(user);

              redis.opsForValue().set(REDIS_SESSION_KEY+":"+token, jsonString);
              //设置session过期时间
              redis.expire(REDIS_SESSION_KEY+":"+token,  SESSION_EXPIRE  ,TimeUnit.SECONDS);//SECONDS 秒
              //写cookie
              CookieUtils.setCookie(req, response, LOGIN_CookName, token);

          }else {
              msg=SystemCode.LOGIN_ERROR.getMessage();
              code=SystemCode.LOGIN_ERROR.getCode();
          }

         return returnResu(req,"",msg,code);
    }

    @ResponseBody
    @RequestMapping(value = "/index")
    public ResponseResult index(@RequestBody Login login1, HttpSession session, HttpServletRequest req, HttpServletResponse response) {
        Login user = loginService.getOne(new QueryWrapper<Login>().lambda().eq(Login::getLoginUsername, login1.getLoginUsername()).eq(Login::getLoginPwd,login1.getLoginPwd()));

        String msg=SystemCode.SUCCESS.getMessage();
        int code=SystemCode.SUCCESS.getCode();

        return returnResu(req,user,msg,code);
    }


}
