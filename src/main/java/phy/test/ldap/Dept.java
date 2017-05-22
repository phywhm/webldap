package phy.test.ldap;

import java.util.List;

/**
 * Created by phy on 2017/4/27.
 */
public class Dept extends LdapObject {

    private String ou;

    private List<User> users;

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
