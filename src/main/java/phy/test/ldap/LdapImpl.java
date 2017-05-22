package phy.test.ldap;

import org.springframework.ldap.query.SearchScope;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

@Component
public class LdapImpl implements Ldap {
    private LdapContext ds;

	@Override
	public void search(String uid) throws NamingException {
		System.out.println("Searching...");
		SearchControls searchCtls = new SearchControls();

		// Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		// specify the LDAP search filter
		String searchFilter = "uid=" + uid;

		// Specify the Base for the search
		String searchBase = "dc=haima,dc=com";

		// Specify the attributes to return
		String returnedAtts[] = { "cn","sn","userPassword" };
		searchCtls.setReturningAttributes(returnedAtts);

		// Search for objects using the filter
//		NamingEnumeration<SearchResult> entries = ds.search(searchBase,
//				searchFilter, searchCtls);
//
//		// Loop through the search results
//		while (entries.hasMoreElements()) {
//			SearchResult entry = entries.next();
//			System.out.println(">>>" + entry.getName());
//			// Print out the groups
//			Attributes attrs = entry.getAttributes();
//			if (attrs != null) {
//				for (NamingEnumeration<? extends Attribute> names = attrs
//						.getAll(); names.hasMore();) {
//					Attribute attr = names.next();
//					System.out.println("AttributeID: " + attr.getID());
//					for (NamingEnumeration<?> e = attr.getAll(); e.hasMore();) {
//						System.out.println("Attributes:" + e.next());
//					}
//				}
//			}
//		}

		NamingEnumeration<SearchResult> entries = ds.search(searchBase, "uniqueMember=uid=" + uid + ",ou=itsection,dc=haima,dc=com", searchCtls);

		// Loop through the search results
		while (entries.hasMoreElements()) {
			SearchResult entry = entries.next();
			System.out.println(">>>" + entry.getName());
			// Print out the groups
			Attributes attrs = entry.getAttributes();
			if (attrs != null) {
				for (NamingEnumeration<? extends Attribute> names = attrs
						.getAll(); names.hasMore();) {
					Attribute attr = names.next();
					System.out.println("AttributeID: " + attr.getID());
					for (NamingEnumeration<?> e = attr.getAll(); e.hasMore();) {
						System.out.println("Attributes:" + e.next());
					}
				}
			}
		}
		System.out.println("Search complete.");
	}

	@Override
	public void listGroups() throws NamingException {
		System.out.println("listing group");
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String searchBase = "ou=groups,dc=haima,dc=com";
		String[] returnAttrs = {"cn"};
		searchControls.setReturningAttributes(returnAttrs);
		NamingEnumeration<SearchResult> entries = ds.search(searchBase, "objectClass=*", searchControls);
		while (entries.hasMoreElements()) {
			SearchResult entry = entries.next();
			System.out.println(">>>" + entry.getName());
			// Print out the groups
			Attributes attrs = entry.getAttributes();
			if (attrs != null) {
				for (NamingEnumeration<? extends Attribute> names = attrs
						.getAll(); names.hasMore();) {
					Attribute attr = names.next();
					System.out.println(attr.getID() + ":");
					for (NamingEnumeration<?> e = attr.getAll(); e.hasMore();) {
						System.out.println("    " + e.next());
					}
				}
			}
		}
		System.out.println("listed group");
	}

	@Override
	public void update() throws NamingException {
		System.out.println("Updating...");
		 ModificationItem[] mods = new ModificationItem[1];
         Attribute attr = new BasicAttribute("cn", "changed value");
         
         // Support add, replace and remove an attribute.
         mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
         ds.modifyAttributes("uid=test,ou=tester,dc=haima,dc=com", mods);
		System.out.println("Updated.");
	}

	@Override
	public void add() throws NamingException {
		System.out.println("Adding...");
		Attributes attrs = new BasicAttributes();
		attrs.put("uid", "test");
		attrs.put("sn", "test");
		attrs.put("cn", "test test");
		attrs.put("userPassword", "111111".getBytes());
		// the following attribute has two values
		Attribute objclass = new BasicAttribute("objectClass");
		objclass.add("inetOrgPerson");
		attrs.put(objclass);

		this.ds.createSubcontext("uid=test,ou=tester,dc=haima,dc=com", attrs);
		System.out.println("Add complete.");
	}

	@Override
	public void delete() throws NamingException {
		System.out.println("Deleting...");
		this.ds.destroySubcontext("uid=test,ou=tester,dc=haima,dc=com");
		System.out.println("Deleted.");
	}

	@Override
	public synchronized void connect() throws NamingException {
		System.out.println("connecting...");
		if (ds == null) {
			Hashtable<String, Object> env = new Hashtable<>(11);
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, "ldap://192.168.109.128:389");
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, "cn=root,dc=haima,dc=com");
			env.put(Context.SECURITY_CREDENTIALS, "1234");

			ds = new InitialLdapContext(env, null);
			// ds = (DirContext) initial.lookup("ldap://localhost:389");
		}
		System.out.println("connected.");
	}

	@Override
	public void close() throws NamingException {
		System.out.println("closing...");
		ds.close();
		System.out.println("closed.");
	}

	@Override
	public LdapContext getCtx() {
		return ds;
	}

}