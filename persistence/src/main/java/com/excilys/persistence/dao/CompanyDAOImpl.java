package com.excilys.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;










import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * DAO for Company
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
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

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
	public List<Company> retrieveAll() {
		
		
		Connection connection = null;

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

					al.add(Company.builder().id(id).name(name).build()); // Company create with pattern Builder

				}

			} catch (SQLException e) {
				
				e.printStackTrace();
				log.error("Problem during retrieveAll()...");
				throw new RuntimeException("Problem during retrieveAll()...");
			} finally{
				JdbcDatasource.closeObject(results, stmt);
				
			}
		}
		else{
			log.error("connection is null...");
		}

		return al;
	}

	/**
	 * Insert a Company
	 * @param cp
	 */
	public Long insert(Company cp) {

		
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
				log.error("Insert() => Problem during id Company generation...");
				throw new RuntimeException("Insert() => Problem during id Company generation...");
			}

		} catch (SQLException e) {
			
			e.printStackTrace();
			log.error("Problem during insertion...");
			throw new RuntimeException("Problem during insertion...");
		}finally{
			JdbcDatasource.closeObject(results, pstmt);
		}

		return id;
	}

	/**
	 * Search a Company
	 * @param paramId the id to search
	 * @return the Company
	 */
	public Company findById(Long paramId){
		
		
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
				log.error("Problem during the search of the company");
				throw new RuntimeException("Problem during the search of the company");
			} finally{
				JdbcDatasource.closeObject(results, pstmt, connection);
			}
		}
		else{
			log.error("The connection is null...");
		}

		return company;
	}

}
