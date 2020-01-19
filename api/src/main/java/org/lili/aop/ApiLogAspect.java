package org.lili.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ApiLogAspect {

    private static Logger logger = LoggerFactory.getLogger(ApiLogAspect.class);
    private static ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("@annotation(requestMapping) && "+ "execution(public * org.lili.api.*.*(..))")
    public void webLog(RequestMapping requestMapping){}

    @Before("webLog(requestMapping)")
    public void doBefore(JoinPoint joinPoint, RequestMapping requestMapping) throws Throwable {
        startTime.set(System.currentTimeMillis());
        HttpServletRequest request = getRequest();
        request.setAttribute("beginTime",System.currentTimeMillis());
         logger.info("URL : " + request.getRequestURL().toString());
    }

    @AfterReturning(returning = "ret", pointcut = "webLog(requestMapping)")
    public void doAfterReturning(Object ret, RequestMapping requestMapping) throws Throwable {
        HttpServletRequest request = getRequest();
        long costTime = System.currentTimeMillis() - startTime.get();
        logger.info("requestURI : " + request.getRequestURI() + " | costTime : " + costTime);
        startTime.remove();
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

}