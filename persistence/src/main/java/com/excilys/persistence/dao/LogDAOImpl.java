package com.excilys.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.excilys.persistence.connection.ConnectionFactory;

@Repository
public class LogDAOImpl implements LogDAO {

	public LogDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addLog(String msg, String type, Connection c) {
		
		// TODO Auto-generated method stub
		// ajoutez ici le code d'insertion d'un produit
		String query = "INSERT INTO log(message, time, type) VALUES(?,?,?);";
		int results = 0;
		PreparedStatement pstmt = null;

		try {
			pstmt = c.prepareStatement(query);
			pstmt.setString(1, msg);

			java.util.Date now = new java.util.Date();
			pstmt.setDate(2, new java.sql.Date(now.getTime()));

			pstmt.setString(3, type);

			// System.out.println("La requete: " + pstmt.toString());

			results = pstmt.executeUpdate();

			System.out.println("Insertion de log bien effectué...");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Probleme dans la requete d'insertion d'un log...");
		}finally{
			try {

				if(pstmt != null)
					pstmt.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
