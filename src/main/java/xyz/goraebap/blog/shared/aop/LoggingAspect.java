package xyz.goraebap.blog.shared.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * Controller 메서드 실행 로깅
     * - within()으로 클래스 레벨 필터링 먼저 수행 (AOP 성능 최적화)
     */
    @Around("within(xyz.goraebap.blog.app.*.web..*) && execution(* *Controller.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.info("[Controller] {}.{} called", className, methodName);
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            log.info("[Controller] {}.{} completed - {}ms",
                    className,
                    methodName,
                    executionTime);

            return result;
        } catch (Exception e) {
            log.error("[Controller] {}.{} failed - {}: {}",
                    className,
                    methodName,
                    e.getClass().getSimpleName(),
                    e.getMessage());
            throw e;
        }
    }

    /**
     * Service 메서드 실행 로깅
     * - within()으로 클래스 레벨 필터링 먼저 수행 (AOP 성능 최적화)
     */
    @Around("within(xyz.goraebap.blog.app.*.service..*) && execution(* *Service.*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.debug("[Service] {}.{} started - args: {}",
                className,
                methodName,
                Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();

            log.debug("[Service] {}.{} completed",
                    className,
                    methodName);

            return result;
        } catch (Exception e) {
            log.error("[Service] {}.{} failed - {}: {}",
                    className,
                    methodName,
                    e.getClass().getSimpleName(),
                    e.getMessage());
            throw e;
        }
    }
}
