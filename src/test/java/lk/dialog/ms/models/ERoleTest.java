/**
 * The ERole Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.models;

import java.beans.IntrospectionException;

import lk.dialog.ms.JavaBeanTester;
import org.junit.Test;

public class ERoleTest {

    @Test
    public void testBeanProperties() throws IntrospectionException {
        JavaBeanTester.test(ERole.class, null);
    }
}
