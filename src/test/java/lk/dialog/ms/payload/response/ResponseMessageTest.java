/**
 * The ResponseMessageTest.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.payload.response;

import java.beans.IntrospectionException;
import java.util.Date;

import org.junit.Test;

import lk.dialog.ms.JavaBeanTester;

public class ResponseMessageTest {

    @Test
    public void testBeanProperties() throws IntrospectionException {
        JavaBeanTester.test(ResponseMessage.class, null);
    }

    @Test
    public void testBeanPropertiesBuilder() throws IntrospectionException {
        ResponseMessage responseMessage = new ResponseMessage.ResponseBuilder()
            .setHttpStatus(405)
            .setMessage("test message")
            .setCode(405)
            .setType("test type")
            .setOrigin("test origin")
            .setOriginalMessage("test original message")
            .setDetails("test details")
            .setTraceId("test trace id")
            .setTimeStamp(new Date())
            .build();
        JavaBeanTester.test(ResponseMessage.class, responseMessage);
    }
}