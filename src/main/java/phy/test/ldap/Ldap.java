package phy.test.ldap;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

public interface Ldap {
    public void connect() throws NamingException;
	public void search(String uid) throws NamingException;
	public void listGroups() throws NamingException;
	public void update() throws NamingException;
	public void add() throws NamingException;
	public void delete() throws NamingException;
	public void close() throws NamingException;

	public LdapContext getCtx();
}