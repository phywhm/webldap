package phy.test.ldap;

import java.util.List;

/**
 * Created by phy on 2017/4/27.
 */
public class Role extends LdapObject {

    private String cn;

    private List<User> users;

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
