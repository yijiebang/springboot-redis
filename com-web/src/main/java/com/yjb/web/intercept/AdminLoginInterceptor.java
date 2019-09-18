package com.yjb.web.intercept;

import com.alibaba.fastjson.JSONObject;
import com.yjb.common.exception.MyException;
import com.yjb.common.exception.SystemCode;
import com.yjb.web.entity.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class AdminLoginInterceptor implements HandlerInterceptor {
    protected List<String> patterns = new ArrayList<String>(Arrays.asList(".*?/.*/no_.*?", "/", "/error", "/login"));

    @Value("${REDIS_SESSION_KEY}")
    String REDIS_SESSION_KEY;
    @Value("${SESSION_EXPIRE}")
    int SESSION_EXPIRE;
    @Value("${LOGIN_CookName}")
    String LOGIN_CookName;

    @Autowired
    private StringRedisTemplate redis;

    //    在请求处理之前调用,只有返回true才会执行请求
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 一些不需要过滤的方法
        String url = request.getRequestURI();
        if (isInclude(url) == true)
            return true;

        // 权限校验
        Cookie cookie = getCookieByName(request, LOGIN_CookName);

        Login login = null;
        if (cookie != null && !cookie.getValue().equals("")) {
            String login2 = redis.opsForValue().get(REDIS_SESSION_KEY + ":" + cookie.getValue());
            Object a = JSONObject.parse(login2);//redis中取出必須先轉
            if(a==null){
                throw new MyException(SystemCode.LOGINTIMER_ERROR.getCode(), SystemCode.LOGINTIMER_ERROR.getMessage());
            }
            login = JSONObject.parseObject(a.toString(), Login.class);
        }
        if (cookie == null || login == null) {// 判断用户是否经过了授权
                throw new MyException(SystemCode.NOLOGIN_ERROR.getCode(), SystemCode.NOLOGIN_ERROR.getMessage());
        }
        //成功之后再次更新redis tocken过期时间
        redis.expire(REDIS_SESSION_KEY + ":" + cookie.getValue(), SESSION_EXPIRE, TimeUnit.SECONDS);//SECONDS 秒

        return true;
    }

    //    试图渲染之后执行
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //    在请求处理之后,视图渲染之前执行
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private boolean isInclude(String url) {
        for (String pattern : patterns) {
            if (Pattern.matches(pattern, url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据名字获取cookie
     *
     * @param request
     * @param name    cookie名字
     * @return
     */
    private static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}