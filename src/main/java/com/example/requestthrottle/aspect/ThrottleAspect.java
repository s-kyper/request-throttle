package com.example.requestthrottle.aspect;

import com.example.requestthrottle.annotation.Throttle;
import com.example.requestthrottle.exception.ThrottleException;
import com.example.requestthrottle.model.Counter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Aspect
public class ThrottleAspect {
    @Value("${maxRequestCountInMin}")
    private Long maxRequestCountInMin;
    @Value("${timePeriodInMin}")
    private Long timePeriodInMin;

    private final ConcurrentHashMap<String, Counter> ipRequestsMap = new ConcurrentHashMap<>();

    @Before("@annotation(throttle)")
    public void throttle(JoinPoint joinPoint, Throttle throttle) {
        HttpServletRequest request = getCurrentHttpRequest();
        String ipAddress = getIpAddress(request);
        System.out.println(ipAddress);

        ipRequestsMap.compute(ipAddress, (key, val) -> {
            if (val == null) {
                Counter counter = new Counter();
                counter.visit();
                return counter;
            } else {
                val.visit();
                if (val.getVisitCount(timePeriodInMin) > maxRequestCountInMin) {
                    throw new ThrottleException();
                }
                return val;
            }
        });
    }

    private HttpServletRequest getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> new RuntimeException("No request found"));
    }

    private String getIpAddress(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-FORWARDED-FOR"))
                .orElse(request.getRemoteAddr());
    }
}
