package com.excilys.persistence.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ProjetConnection {

	private static Connection cn = null;
	
	private ProjetConnection() {
		// TODO Auto-generated constructor stub
		cn = null;
		Context ctx;
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/computer-database");

			cn = ds.getConnection();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Recupère une connection JNDI
	 * @return
	 */
	public static Connection getInstance(){
		// Si c'est la première utilisation, alors on instancie
		if(cn==null)
			new ProjetConnection(); 
		return cn;
	}
	
	public static void disconnect(){
		try {
			if(!cn.isClosed())
				cn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Tentative de fermeture de la connection qui a déjà été fermé auparavant...");
		}
	}

}
