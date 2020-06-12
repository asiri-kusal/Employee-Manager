/**
 * The User test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.models;

import java.beans.IntrospectionException;

import lk.dialog.ms.JavaBeanTester;
import org.junit.Test;

public class UserTest {

    @Test
    public void testBeanProperties() throws IntrospectionException {
        JavaBeanTester.test(User.class, null);
    }

    @Test
    public void testArgConstructor() throws IntrospectionException {
        User user = new User("Test user", "testemail@dialog.lk", "testPassword");
        JavaBeanTester.test(User.class, user);
    }
}