/**
 * The JwtResponse Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.payload.response;

import java.beans.IntrospectionException;
import java.util.Arrays;

import lk.dialog.ms.BaseTestUtil;
import lk.dialog.ms.JavaBeanTester;
import org.junit.Test;

public class JwtResponseTest extends BaseTestUtil {

    @Test
    public void testBeanProperties() throws IntrospectionException {
        JwtResponse jwtResponse = new JwtResponse("Access token", 10l, "User name", "Email", Arrays.asList("admin"));
        JavaBeanTester.test(JwtResponse.class, jwtResponse);
    }
}