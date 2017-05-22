package phy.test.ldap;

public class Factory {
    private static Ldap instance;
	public synchronized static Ldap createInstance() {
		if (instance == null) {
			try {
				instance = (Ldap) Class.forName("phy.test.ldap.LdapImpl").newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
		return instance;
	}
}