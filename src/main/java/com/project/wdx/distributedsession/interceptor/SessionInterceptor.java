package com.project.wdx.distributedsession.interceptor;

import com.project.wdx.distributedsession.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class SessionInterceptor  implements HandlerInterceptor {

    private static String SESSION_ID = "DISTRIBUTED_SESSION_ID";

    public static ThreadLocal<String> threadLocal = new ThreadLocal<String>();


    private List<String> unLoginUris = new ArrayList<>();

    @Value("${spring.distributed.session.login.name}")
    private String loginNameLable ;
    @Value("${spring.distributed.session.login.uri}")
    private String loginUri;

    @Autowired
    private DataRepository dataRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String requestURI = request.getRequestURI();

        if(unLoginUris.contains(requestURI)){
            return true;
        }

        Cookie cookie =request.getCookies()[0];
        if(!StringUtils.pathEquals(cookie.getName(),SESSION_ID)){
            return false;
        }
        String value = cookie.getValue();
        if(StringUtils.isEmpty(value)){
            return false;
        }
        threadLocal.set(value);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(request.getRequestURI().equals(loginUri)){
            String loginName = request.getParameter(loginNameLable);
            Cookie cookie = new Cookie(SESSION_ID, UUID.randomUUID().toString());
            response.addCookie(cookie);
            Object obj = this.queryUser(loginName);
            if(obj != null) {
                dataRepository.save(cookie.getValue(), obj);
            }
            threadLocal.set(cookie.getValue());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 根据登录名，查询用户信息，需要使用者实现
     * @param loginName
     * @return
     */
    protected abstract Object queryUser(String loginName);


    public void addUri(String uri){
        unLoginUris.add(uri);
    }
}
