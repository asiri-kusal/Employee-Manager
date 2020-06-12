/**
 * The Auditable Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.audit;

import static org.mockito.Mockito.spy;

import java.beans.IntrospectionException;

import lk.dialog.ms.JavaBeanTester;
import org.junit.Test;

public class AuditableTest {
    @Test
    public void testBeanProperties() throws IntrospectionException {
        Auditable auditable = spy(Auditable.class);
        JavaBeanTester.test(Auditable.class, auditable);
    }
}