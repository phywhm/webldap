package phy.test;

import phy.test.ldap.Factory;
import phy.test.ldap.Ldap;

import javax.naming.NamingException;

/**
 * Created by phy on 2017/4/24.
 */
public class LdapTest {

    public static void main(String[] args) throws NamingException {
        Ldap ldap = Factory.createInstance();

        try {
            ldap.connect();
//            // add uid=test,ou=tester,dc=ibm,dc=com
//            ldap.add();
//            // search uid=test
//            ldap.search();
//            // update cn with new value of "changed name"
//            ldap.update();
//            // search uid=test to see cn value.
//            ldap.search();
//            // delete uid=test,ou=tester,dc=ibm,dc=com
//            ldap.delete();
//            // search again.
//            ldap.search();
        } catch (NamingException ne) {
            System.out.println(ne.getMessage());
            ne.printStackTrace();
        } finally {
            ldap.close();
        }
    }
}
