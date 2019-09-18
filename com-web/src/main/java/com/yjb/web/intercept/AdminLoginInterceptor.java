package com.yjb.web.intercept;

import com.alibaba.fastjson.JSONObject;
import com.yjb.common.exception.MyException;
import com.yjb.common.exception.SystemCode;
import com.yjb.common.redis.RedisUtil;
import com.yjb.web.entity.Login;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Pattern;

public class AdminLoginInterceptor implements HandlerInterceptor {
    protected List<String> patterns = new ArrayList<String>(Arrays.asList(".*?/.*/no_.*?", "/", "/error", "/login"));

    String REDIS_SESSION_KEY = "dddl";
    int SESSION_EXPIRE = 180;
    String LOGIN_TOKEN = "LOGIN_TOKEN";

    //    在请求处理之前调用,只有返回true才会执行请求
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 一些不需要过滤的方法
        String url = request.getRequestURI();
        if (isInclude(url) == true)
            return true;

        // 权限校验
        Cookie cookie = getCookieByName(request, LOGIN_TOKEN);
        Jedis jedisClient = RedisUtil.getJedis();//获取连接
        Login login = null;
        if (cookie != null && !cookie.getValue().equals("")) {

            String login2 = jedisClient.get(REDIS_SESSION_KEY + ":" + cookie.getValue());
            Object a = JSONObject.parse(login2);//redis中取出必須先轉
            login = JSONObject.parseObject(a.toString(), Login.class);

        }
        if (cookie == null || login == null) {// 判断用户是否经过了授权
            // 判断是否是AJAX访问
            if (request.getHeader("x-requested-with") != null
                    && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                response.setHeader("sessionstatus", "timeout");
                response.setStatus(403);
                throw new MyException(SystemCode.ERROR.getCode(), SystemCode.ERROR.getMessage());

            } else {
                throw new MyException(SystemCode.NOLOGIN_ERROR.getCode(), SystemCode.NOLOGIN_ERROR.getMessage());
            }
        }
        //成功之后再次更新redis tocken过期时间
        jedisClient.expire(REDIS_SESSION_KEY + ":" + cookie.getValue(), SESSION_EXPIRE);

        RedisUtil.close(jedisClient);
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