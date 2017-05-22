package phy.test.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import phy.test.ldap.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.LdapName;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * Created by phy on 2017/4/27.
 */
@Controller
@RequestMapping(value = "mg")
@SuppressWarnings("unchecked")
public class ManageController {

    @Autowired
    private Ldap ldap;

    @PostConstruct
    public void init() {
        try {
            ldap.connect();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            ldap.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "addRole")
    public String preAddRole() {
        return "/ldap/addRole";
    }

    @RequestMapping(value = "doAddRole")
    public String doAddRole(HttpServletRequest req) {
        String role = req.getParameter("role");
        Attributes attributes = new BasicAttributes();
        attributes.put("cn", role);
        attributes.put("uniqueMember", "cn=spaceholder");
        Attribute attribute = new BasicAttribute("objectClass");
        attribute.add("groupOfUniqueNames");
        attributes.put(attribute);
        StringBuffer sb = new StringBuffer();
        sb.append("cn=").append(role).append(",").append("ou=").append("角色").append(",").append(Constants.BASE);
        LdapContext ctx = ldap.getCtx();
        try {
            ctx.createSubcontext(sb.toString(), attributes);
        } catch (NamingException e) {
            e.printStackTrace();
            req.setAttribute("msg", e.getMessage());
            return "/ldap/addRole";
        }
        return "redirect:/mg/menu";
    }

    @RequestMapping(value = "addDept")
    public String preAddDept() {
        return "/ldap/addDept";
    }

    @RequestMapping(value = "doAddDept")
    public String doAddDept(HttpServletRequest req) {
        String dept = req.getParameter("dept");
        Attributes attributes = new BasicAttributes();
        attributes.put("ou", dept);
        Attribute attribute = new BasicAttribute("objectClass");
        attribute.add("organizationalUnit");
        attributes.put(attribute);
        StringBuffer sb = new StringBuffer();
        sb.append("ou=").append(dept).append(",").append(Constants.BASE);
        LdapContext ctx = ldap.getCtx();
        try {
            ctx.createSubcontext(sb.toString(), attributes);
        } catch (NamingException e) {
            e.printStackTrace();
            req.setAttribute("msg", e.getMessage());
            return "/ldap/addDept";
        }
        return "redirect:/mg/dept";
    }

    @RequestMapping(value = "addUser")
    public String preAddUser() {
        return "/ldap/addUser";
    }

    @RequestMapping(value = "doAddUser")
    public String doAddUser(HttpServletRequest req, HttpServletResponse rep) {
        String uid = req.getParameter("uid");
        String sn = req.getParameter("sn");
        String cn = req.getParameter("cn");
        String mail = req.getParameter("email");
        String pwd = req.getParameter("pwd");
        String deptName = req.getParameter("dept");
        Attributes attributes = new BasicAttributes();
        attributes.put("uid", uid);
        attributes.put("sn", sn);
        Attribute mailAttr = new BasicAttribute("mail");
        mailAttr.add(mail);
        attributes.put(mailAttr);
        attributes.put("cn", cn);
        attributes.put("userPassword",pwd);
        Attribute attribute = new BasicAttribute("objectClass");
        attribute.add("inetOrgPerson");
        attributes.put(attribute);
        LdapContext ctx = ldap.getCtx();
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("uid=").append(uid).append(",ou=").append(deptName).append(",").append(Constants.BASE);
            ctx.createSubcontext(sb.toString(), attributes);
        } catch (NamingException e) {
            e.printStackTrace();
            req.setAttribute("msg",e.getMessage());
            return "/ldap/addUser";
        }
        return "redirect:/mg/dept";
    }

    @RequestMapping(value = "dept")
    public String dept(HttpServletRequest req) {
        try {
            Enumeration<NetworkInterface> nets =  NetworkInterface.getNetworkInterfaces();
            System.out.println(req.getLocalPort());
            while (nets.hasMoreElements()) {
                NetworkInterface net = nets.nextElement();
                Enumeration<InetAddress> ips = net.getInetAddresses();
                if (ips.hasMoreElements()) {
                    System.out.println(net.getDisplayName() + "===" + net.getName());
                    while (ips.hasMoreElements()) {
                        InetAddress ip = ips.nextElement();
                        System.out.println(ip.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        LdapContext ctx = ldap.getCtx();
        SearchControls sctl = new SearchControls();
        sctl.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        sctl.setReturningAttributes(new String[]{"ou"});
        try {
            NamingEnumeration<SearchResult> res = ctx.search(Constants.BASE, "(&(!(ou=角色))(objectClass=organizationalUnit))", sctl);
            if (res != null) {
                List<Dept> list = new ArrayList<>();
                while (res.hasMore()) {
                    SearchResult sr = res.next();
                    Attributes attributes = sr.getAttributes();
                    if (attributes != null) {
                        Dept dept = new Dept();
                        for (NamingEnumeration<? extends Attribute> attrs = attributes.getAll(); attrs.hasMore(); ) {
                            Attribute attr = attrs.next();
                            if ("ou".equals(attr.getID())) {
                                dept.setOu((String) attr.get(0));
                                fetchUsers(dept);
                            }
                        }
                        list.add(dept);
                    }
                }
                req.setAttribute("list", list);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "ldap/dept";
    }

    private void fetchUsers(Dept dept) {
        LdapContext ctx = ldap.getCtx();
        SearchControls sctl = new SearchControls();
        sctl.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sctl.setReturningAttributes(new String[]{"uid","sn","cn","mail"});
        try {
            NamingEnumeration<SearchResult> res = ctx.search("ou="+dept.getOu()+"," + Constants.BASE, "(objectClass=inetOrgPerson)", sctl);
            List<User> users = null;
            while (res.hasMore()) {
                if (users == null)
                    users = new ArrayList<>();
                SearchResult sr = res.next();
                Attributes attributes = sr.getAttributes();
                if (attributes != null) {
                    User u = new User();
                    u.setDn(sr.getNameInNamespace());
                    sctl.setSearchScope(SearchControls.ONELEVEL_SCOPE);
                    sctl.setReturningAttributes(new String[]{"cn"});
                    NamingEnumeration<SearchResult> resRole = ctx.search("ou=角色," + Constants.BASE, "(uniqueMember=" + u.getDn() + ")", sctl);
                    while (resRole.hasMore()) {
                        SearchResult srRole = resRole.next();
                        String rn = (String) srRole.getAttributes().get("cn").get(0);
                        u.setRoleName(rn);
                    }
                    buildUser(u, attributes);
                    users.add(u);
                }
            }
            dept.setUsers(users);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "menu")
    public String menu(HttpServletRequest req) {
        LdapContext ctx = ldap.getCtx();
        SearchControls sctl = new SearchControls();
        sctl.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sctl.setReturningAttributes(new String[]{"cn", "uniqueMember"});
        try {
            NamingEnumeration<SearchResult> res = ctx.search("ou=角色," + Constants.BASE, "(objectClass=groupOfUniqueNames)", sctl);
            if (res != null) {
                List<Role> list = new ArrayList<>();
                while (res.hasMore()) {
                    SearchResult sr = res.next();
                    Attributes attributes = sr.getAttributes();
                    if (attributes != null) {
                        Role role = new Role();
                        role.setDn(sr.getNameInNamespace());
                        for (NamingEnumeration<? extends Attribute> attrs = attributes.getAll(); attrs.hasMore(); ) {
                            Attribute attr = attrs.next();
                            if ("cn".equals(attr.getID())) {
                                role.setCn((String) attr.get(0));
                            } else if ("uniqueMember".equals(attr.getID())) {
                                List<User> users = new ArrayList<>();
                                for (NamingEnumeration<String> members = (NamingEnumeration<String>) attr.getAll();members.hasMore();) {
                                    User u = findUser(members.next());
                                    if (u != null) users.add(u);
                                }
                                role.setUsers(users);
                            }
                        }
                        list.add(role);
                    }
                }
                req.setAttribute("list", list);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "/ldap/menu";
    }

    private User findUser(String dn) {
        User u = null;
        SearchControls sctl = new SearchControls();
        sctl.setSearchScope(SearchControls.OBJECT_SCOPE);
        sctl.setReturningAttributes(new String[]{"cn", "sn", "mail", "uid"});
        String filter = "objectClass=*";
        LdapContext ctx = ldap.getCtx();
        try {

            NamingEnumeration<SearchResult> res = ctx.search(dn, filter, sctl);
            while (res.hasMore()) {
                SearchResult sr = res.next();
                Attributes attributes = sr.getAttributes();
                if (attributes != null) {
                    u = new User();
                    u.setDn(sr.getNameInNamespace());
                    buildUser(u, attributes);
                }
            }
        } catch (NamingException e) {
//            e.printStackTrace();
        }
        return u;
    }

    private void buildUser(User u, Attributes attributes) throws NamingException {
        for (NamingEnumeration<? extends Attribute> attrs = attributes.getAll(); attrs.hasMore(); ) {
            Attribute attr = attrs.next();
            if ("cn".equals(attr.getID())) {
                u.setCn((String) attr.get(0));
            } else if ("sn".equals(attr.getID())) {
                u.setSn((String) attr.get(0));
            } else if ("uid".equals(attr.getID())) {
                u.setUid((String) attr.get(0));
            } else if ("mail".equals(attr.getID())) {
                List<String> mails = new ArrayList<>();
                for (NamingEnumeration<String> vals = (NamingEnumeration<String>) attr.getAll(); vals.hasMore();) {
                    mails.add(vals.next());
                }
                u.setEmails(mails);
            }
        }
    }

    @RequestMapping(value = "setRole")
    public String preBindRole(HttpServletRequest req) {
        LdapContext ctx = ldap.getCtx();
        SearchControls sctl = new SearchControls();
        sctl.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sctl.setReturningAttributes(new String[]{"cn"});
        try {
            NamingEnumeration<SearchResult> res = ctx.search("ou=角色," + Constants.BASE, "(objectClass=groupOfUniqueNames)", sctl);
            if (res != null) {
                List<Role> list = new ArrayList<>();
                while (res.hasMore()) {
                    SearchResult sr = res.next();
                    Attributes attributes = sr.getAttributes();
                    if (attributes != null) {
                        Role role = new Role();
                        role.setDn(sr.getNameInNamespace());
                        for (NamingEnumeration<? extends Attribute> attrs = attributes.getAll(); attrs.hasMore(); ) {
                            Attribute attr = attrs.next();
                            if ("cn".equals(attr.getID())) {
                                role.setCn((String) attr.get(0));
                            }
                        }
                        list.add(role);
                    }
                }
                req.setAttribute("list", list);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "/ldap/listRole";
    }

    @RequestMapping(value = "doSetRole")
    public String doBindRole(HttpServletRequest req) {
        String dn = req.getParameter("dn");
        String role = req.getParameter("role");
        if (!StringUtils.isEmpty(dn) && !StringUtils.isEmpty(role)) {
            LdapContext ctx = ldap.getCtx();
            Attributes attributes = new BasicAttributes();
            attributes.put("uniqueMember", dn);
            try {
                ctx.modifyAttributes(role, LdapContext.ADD_ATTRIBUTE, attributes);
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/mg/dept";
    }

    @RequestMapping(value = "fire")
    public String unBindRole(HttpServletRequest req) {
        String dn = req.getParameter("dn");
        String role = req.getParameter("role");
        if (!StringUtils.isEmpty(dn) && !StringUtils.isEmpty(role)) {
            LdapContext ctx = ldap.getCtx();
            Attributes attributes = new BasicAttributes();
            attributes.put("uniqueMember", dn);
            try {
                ctx.modifyAttributes(role, LdapContext.REMOVE_ATTRIBUTE, attributes);
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/mg/menu";
    }
}
