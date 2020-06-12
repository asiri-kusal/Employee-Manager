/**
 * The LoggingAspectTest.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.aop;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Before;
import org.junit.Test;

import lk.dialog.ms.BaseTestUtil;

public class LoggingAspectTest extends BaseTestUtil {

    private LoggingAspect loggingAspect;
    private JoinPoint joinPoint;

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        loggingAspect = new LoggingAspect();
        joinPoint = getMockJoinPoint();
        mockHttpServletRequest(loggingAspect, true);
    }

    @Test
    public void testLoggingAspect() throws Throwable {
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        when(proceedingJoinPoint.proceed()).thenReturn(new Object());
        loggingAspect.springBeanPointcut();
        loggingAspect.applicationPackagePointcut();
        loggingAspect.logAfterThrowing(joinPoint, new RuntimeException());
        loggingAspect.logExecutionTime(proceedingJoinPoint);
        verify(joinPoint, atLeast(1)).getSignature();
    }

    private JoinPoint getMockJoinPoint() {
        joinPoint = mock(JoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.getDeclaringTypeName()).thenReturn("test declaringTypeName");
        when(signature.getName()).thenReturn("test signature name");
        when(joinPoint.getSignature()).thenReturn(signature);
        return joinPoint;
    }
}
