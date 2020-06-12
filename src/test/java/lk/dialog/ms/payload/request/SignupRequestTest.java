/**
 * The SignupRequest Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.payload.request;

import java.beans.IntrospectionException;

import lk.dialog.ms.JavaBeanTester;
import org.junit.Test;

public class SignupRequestTest {

    @Test
    public void testBeanProperties() throws IntrospectionException {
        JavaBeanTester.test(SignupRequest.class, null);
    }
}