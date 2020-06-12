/**
 * The TestController Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.controllers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestControllerTest {

    private static final String MESSAGE = "Result should be equal";
    private TestController controller;

    @Before
    public void init() {
        controller = new TestController();
    }

    @Test
    public void testAllAccess() {
        String result = controller.allAccess();
        assertEquals(MESSAGE, "Public Content.", result);
    }

    @Test
    public void testAdminAccess() {
        String result = controller.adminAccess();
        assertEquals(MESSAGE, "Admin Board.", result);
    }

    @Test
    public void testModeratorAccess() {
        String result = controller.moderatorAccess();
        assertEquals(MESSAGE, "Moderator Board.", result);
    }

    @Test
    public void testUserAccess() {
        String result = controller.userAccess();
        assertEquals(MESSAGE, "User Content.", result);
    }

}