/**
 * The GlobalExceptionHandlerTest.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.exception;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.NoHandlerFoundException;

import lk.dialog.ms.BaseTestUtil;
import lk.dialog.ms.payload.response.ResponseMessage;

public class GlobalExceptionHandlerTest extends BaseTestUtil {

    private GlobalExceptionHandler globalExceptionHandler;
    private HttpServletRequest request;

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        globalExceptionHandler = new GlobalExceptionHandler();
        request = mockHttpServletRequest(globalExceptionHandler, false);
    }

    @Test
    public void testGlobleExceptionHandler() {
        String exceptionMessage = "test ArithmeticException";
        ResponseEntity responseEntity = globalExceptionHandler
            .globleExceptionHandler(new ArithmeticException(exceptionMessage), request);
        ResponseMessage message = (ResponseMessage) responseEntity.getBody();
        assertEquals("Original message should be equal", exceptionMessage, message.getOriginalMessage());
    }

    @Test
    public void testNoHandlerFoundExceptionExceptionHandler() {
        String url = "http://localhost:8080/api/test/";
        String method = "Get";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Dialog-Trace-ID", "trace-id-123424");
        ResponseEntity responseEntity = globalExceptionHandler
            .noHandlerFoundExceptionExceptionHandler(new NoHandlerFoundException(method, url, httpHeaders), request);
        ResponseMessage message = (ResponseMessage) responseEntity.getBody();
        assertEquals("Original message should be equal", "No handler found for " + method + " " + url,
                     message.getOriginalMessage());
    }

    @Test
    public void testSqlExceptionHandler() {
        String exceptionMessage = "test sql exception";
        ResponseEntity responseEntity = globalExceptionHandler
            .sqlExceptionHandler(new SQLException(exceptionMessage), request);
        ResponseMessage message = (ResponseMessage) responseEntity.getBody();
        assertEquals("Original message should be equal", exceptionMessage, message.getOriginalMessage());
    }

    @Test
    public void testNullPointerExceptionHandler() {
        String exceptionMessage = "test NullPointerException";
        ResponseEntity responseEntity = globalExceptionHandler
            .nullPointerExceptionHandler(new NullPointerException(exceptionMessage), request);
        ResponseMessage message = (ResponseMessage) responseEntity.getBody();
        assertEquals("Original message should be equal", exceptionMessage, message.getOriginalMessage());
    }

}