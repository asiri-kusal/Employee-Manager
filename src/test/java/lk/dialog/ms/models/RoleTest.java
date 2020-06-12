/**
 * The Role test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.models;

import java.beans.IntrospectionException;

import lk.dialog.ms.JavaBeanTester;
import org.junit.Test;

public class RoleTest {
    @Test
    public void testBeanProperties() throws IntrospectionException {
        JavaBeanTester.test(Role.class, null);
    }

    @Test
    public void testConstructor() throws IntrospectionException {
        JavaBeanTester.test(Role.class, new Role());
    }

    @Test
    public void testConstructorWithArgs() throws IntrospectionException {
        JavaBeanTester.test(Role.class, new Role(ERole.ROLE_USER));
    }
}
