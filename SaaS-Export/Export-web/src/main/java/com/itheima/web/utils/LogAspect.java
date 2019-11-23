package com.itheima.web.utils;

import com.itheima.domain.system.SysLog;
import com.itheima.domain.system.User;
import com.itheima.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;


    @Around(value = "execution( * com.itheima.web.controller.*.*.*(..))")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        // 1、提取数据，从session, request
        User user = (User) session.getAttribute("loginUser");
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        if (user!=null&&requestMapping!=null){
            // 2、构造日志实体类
            SysLog log = new SysLog();
            log.setTime(new Date());
            log.setUserName(user.getUserName());
            log.setCompanyId(user.getCompanyId());
            log.setCompanyName(user.getCompanyName());

            log.setIp(request.getLocalAddr());  //0.0.0.0

            log.setMethod(method.getName());  //方法名，list, toAdd, toUpdate, edit
            log.setAction(requestMapping.name());  //方法的注释，RequestMapping.name

            // 3、写入日志
            sysLogService.save(log);

        }

        // 4、返回执行被代理的方法
        return pjp.proceed();
    }
}
