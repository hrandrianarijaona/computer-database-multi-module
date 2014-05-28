package com.excilys.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;








//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.excilys.core.om.Company;
import com.excilys.persistence.connection.ConnectionFactory;
import com.excilys.persistence.connection.JdbcDatasource;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * Classe de DAO pour Company
 * @author hrandr
 *
 */
@Repository
public class CompanyDAOImpl implements CompanyDAO{
	
//	@Autowired
//	ConnectionFactory connectionFactory;
	
//	@Autowired
//	private BoneCPDataSource datasource;
	@Autowired
	DataSource datasource;
	
	private CompanyDAOImpl() {
	}
	//private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Give an instance
	 * @return
	 */
	public CompanyDAOImpl getInstance(){
		return this;
	}

	/**
	 * get all Company
	 * @return
	 */
	public List<Company> getListCompany() {
		
		
		Connection connection = null;
//		try {
//			connection = connectionFactory.getConnection();
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		connection = DataSourceUtils.getConnection(datasource);
		
		ArrayList<Company> al = new ArrayList<Company>();

		String query = "SELECT * FROM company;";
		ResultSet results = null;
		Statement stmt = null;

		if(connection != null){

			try {
				stmt = connection.createStatement();
				results = stmt.executeQuery(query);

				while(results.next()){
					Long id = results.getLong("id");
					String name = results.getString("name");

					al.add(Company.builder().id(id).name(name).build()); // Company créer avec le pattern Builder
					// al.add(new Company(id, name));

				}

			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println("Problème dans la requete de listing...");
				throw new RuntimeException("Problème dans la requete de listing niveau DAO..");
			} finally{
				JdbcDatasource.closeObject(results, stmt);
				
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return al;
	}

	/**
	 * Insert a Company
	 * @param cp
	 */
	public Long insertCompany(Company cp) {

		
		Connection connection = DataSourceUtils.getConnection(datasource);
		
		Long id = null;
		String query = "INSERT INTO company(name) VALUES(?);";
		int results = 0;
		PreparedStatement pstmt = null;
		

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, cp.getName());
			System.out.println("La requete: " + pstmt.toString());

			results = pstmt.executeUpdate();

			System.out.println("Insertion bien effectué...");
			
			try {
				// Generate id
				ResultSet rsId = pstmt.getGeneratedKeys();
				while(rsId.next()){
					id = rsId.getLong(1);
				}
				
			
//				ConnectionFactory.closeObject(rsId);
				JdbcDatasource.closeObject(rsId);
				
			} catch (SQLException e1) {
				
				e1.printStackTrace();
				System.out.println("Probleme dans la génération des id Company...");
				throw new RuntimeException("Problème dans la requete de listing niveau DAO..");
			}

		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("Probleme dans la requete d'insertion...");
			throw new RuntimeException("Problème dans la requete de listing niveau DAO..");
		}finally{
			JdbcDatasource.closeObject(results, pstmt);
		}

		return id;
	}

	/**
	 * Search a Company
	 * @param paramId l'id à rechercher
	 * @return L'objet Company
	 */
	public Company findCompanyById(Long paramId){
		
		
		Connection connection = DataSourceUtils.getConnection(datasource);
		
		// Company company = new Company();
		Company company = Company.builder().build(); // Builder pattern

		String query = "SELECT * FROM company WHERE id=?;";
		ResultSet results = null;
		PreparedStatement pstmt = null;

		if(connection != null){

			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setLong(1, paramId);
				results = pstmt.executeQuery();

				while(results.next()){
					
					Long id = results.getLong("id");
					String name = results.getString("name");
					company.setId(id);
					company.setName(name);
				}

			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println("Problème dans la requete de recherche de company...");
				throw new RuntimeException("Problème dans la requete de listing niveau DAO..");
			} finally{
				JdbcDatasource.closeObject(results, pstmt, connection);
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return company;
	}

}
