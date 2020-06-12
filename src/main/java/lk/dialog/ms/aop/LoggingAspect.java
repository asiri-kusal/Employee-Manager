/**
 * Aspect for logging execution of service and repository Spring components.
 *
 * @author Asiri Samaraweera
 * @version 1.0
 */
package lk.dialog.ms.aop;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

    @Autowired
    private HttpServletRequest request;

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
              " || within(@org.springframework.stereotype.Service *)" +
              " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(lk.dialog.ms.controllers..*)" +
              " || within(lk.dialog.ms.repository..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        commonLogs();
        LOGGER.error("Exception in {}.{}() with message = {}", joinPoint.getSignature().getDeclaringTypeName(),
                     joinPoint.getSignature().getName(), e.getMessage() != null ? e.getMessage() : "NULL");
        LOGGER.error(e);
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        commonLogs();
        LOGGER.info(" {} executed in {} ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }

    private void commonLogs() {
        LOGGER.info("URL path: {}", request.getRequestURI());
        LOGGER.info("Dialog-Trace-ID {}", request.getHeader("dialog-trace-id"));
        LOGGER.info("URI path variable {}", HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }

}