package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserAccountAndManagerTest {

    /**
     * Test whether the UserAccount can make new instances and change parameters.
     */
    @Test
    public void testCreateNewUser() {
        Factory f = new Factory();
        UserAccount newUser = f.createUserAccount("aaa", "b");
        assertEquals("aaa", newUser.getName());
        assertEquals("b", newUser.getPassword());
        newUser.setPassword("1a2c");
        assertEquals("1a2c", newUser.getPassword());
        newUser.setName("abcabc");
        assertEquals("abcabc", newUser.getName());
        newUser.setEmail("abc@gmail.com");
        assertEquals("abc@gmail.com", newUser.getEmail());
        newUser.setAge(3);
        assertEquals(3, (int) newUser.getAge());
    }

    /**
     * Test whether the UserAccountManager can initialize a new instance and add user into the list.
     */
    @Test
    public void testUserAccountManager() {
        Factory f = new Factory();
        UserAccountManager m = f.createUserManager();
        assertEquals(5, m.getGlobalScoreBoard().size());
        m.addUser("aaa");
        assertEquals(1, m.getUserList().size());
    }
}