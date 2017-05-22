import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import phy.test.ldap.Factory;
import phy.test.ldap.Ldap;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * Created by phy on 2017/4/24.
 */
@RunWith(JUnit4.class)
public class LdapTest {

    private Ldap ldap;

    @Before
    public void init() throws NamingException {
        System.out.println(1);
        ldap = Factory.createInstance();
        ldap.connect();
    }

    @Test
    public void test() throws NamingException {
        ldap.search("pan4");
//        ldap.listGroups();
    }

    @After
    public void destroy() throws NamingException {
        ldap.close();
    }
}
