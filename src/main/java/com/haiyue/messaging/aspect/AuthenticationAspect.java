package com.haiyue.messaging.aspect;

import com.haiyue.messaging.model.User;
import com.haiyue.messaging.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Log4j2
public class  AuthenticationAspect {

    // log controller invocations
    @Autowired
    private UserService userService;

    @Around("execution(* com.haiyue.messaging.controller.*.*(..)) && @annotation(com.haiyue.messaging.annotation.NeedAuth)")
    public Object authentication(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String loginToken = request.getHeader("Login-Token");
        User user = this.userService.authenticate(loginToken);
        try {
            var args = proceedingJoinPoint.getArgs();
            args[0] = user;
            return proceedingJoinPoint.proceed(args);
        } catch (Exception e){
            throw e;
        } finally {
            log.info("auth end");
        }
    }
}
