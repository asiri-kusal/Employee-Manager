/**
 * ControllerAdvice for catch all controller level exceptions.
 *
 * @author Asiri Samaraweera
 * @version 1.0
 */
package lk.dialog.ms.exception;

import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import lk.dialog.ms.payload.response.ResponseMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String GLOBAL_ERROR_MESSAGE = " : Global Exception Occur";
    private static final String NULL_POINT_ERROR_MESSAGE = " : Null Pointer Exception Occur";
    private static final String SQL_EXCEPTION_ERROR_MESSAGE = " : SQL Exception Occur";
    private static final String NO_HANDLER_EXCEPTION_ERROR_MESSAGE = " : No Handler Found Exception Occur";

    /**
     * Catch Global Exceptions
     *
     * @param @{@link Exception}
     * @param @{@link HttpServletRequest}
     * @return @{@link ResponseMessage}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> globleExceptionHandler(Exception ex, HttpServletRequest request) {
        return getResponseMessage(ex, request.getRequestURI() + GLOBAL_ERROR_MESSAGE, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Catch NullPointerException Exceptions
     *
     * @param @{@link Exception}
     * @param @{@link HttpServletRequest}
     * @return @{@link ResponseMessage}
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResponseMessage> nullPointerExceptionHandler(Exception ex, HttpServletRequest request) {
        return getResponseMessage(ex, request.getRequestURI() + NULL_POINT_ERROR_MESSAGE,
                                  HttpStatus.UNPROCESSABLE_ENTITY,
                                  request);
    }

    /**
     * Catch SQLException Exceptions
     *
     * @param @{@link Exception}
     * @param @{@link HttpServletRequest}
     * @return @{@link ResponseMessage}
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseMessage> sqlExceptionHandler(Exception ex, HttpServletRequest request) {
        return getResponseMessage(ex, request.getRequestURI() + SQL_EXCEPTION_ERROR_MESSAGE,
                                  HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    /**
     * Catch NoHandlerFoundException Exceptions
     *
     * @param @{@link Exception}
     * @param @{@link HttpServletRequest}
     * @return @{@link ResponseMessage}
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseMessage> noHandlerFoundExceptionExceptionHandler(Exception ex,
                                                                                   HttpServletRequest request) {
        return getResponseMessage(ex, request.getRequestURI() + NO_HANDLER_EXCEPTION_ERROR_MESSAGE,
                                  HttpStatus.NOT_FOUND, request);
    }

    private ResponseEntity<ResponseMessage> getResponseMessage(Exception ex, Object value, HttpStatus status,
                                                               HttpServletRequest request) {
        ResponseMessage message = new ResponseMessage.ResponseBuilder()
            .setCode(status.value())
            .setOriginalMessage(ex.getMessage())
            .setMessage(value)
            .setTimeStamp(new Date())
            .setTraceId(request.getHeader("dialog-trace-id"))
            .build();
        return new ResponseEntity<>(message, status);
    }

}
