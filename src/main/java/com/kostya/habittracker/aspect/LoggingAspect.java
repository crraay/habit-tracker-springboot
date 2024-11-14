package com.kostya.habittracker.aspect;

import com.kostya.habittracker.annotation.LogExecution;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@within(logExecution) || @annotation(logExecution)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecution logExecution) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        String argsString = getArgumentsString(parameterNames, parameters, args);

        logger.info("Method {} of {} called with arguments: {}", methodName, className, argsString);

        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            logger.info("Method {} of {} finished in {} ms", methodName, className, executionTime);
        }

        return result;
    }

    private String getArgumentsString(String[] parameterNames, Parameter[] parameters, Object[] args) {
        return Arrays.stream(args)
                .map(arg -> {
                    int index = Arrays.asList(args).indexOf(arg);
                    if (parameters[index].isAnnotationPresent(AuthenticationPrincipal.class)) {
                        return null;
                    } else {
                        return parameterNames[index] + "=" + arg;
                    }
                })
                .filter(arg -> arg != null)
                .collect(Collectors.joining(", "));
    }
}