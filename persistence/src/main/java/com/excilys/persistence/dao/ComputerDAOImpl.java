package com.excilys.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.core.om.Company;
import com.excilys.core.om.Computer;
import com.excilys.persistence.connection.ConnectionFactory;
import com.excilys.persistence.connection.JdbcDatasource;
import com.jolbox.bonecp.BoneCPDataSource;

@Repository
public class ComputerDAOImpl implements ComputerDAO{

//	@Autowired
//	@Qualifier("dataSource")
//	private BoneCPDataSource datasource;
	@Autowired
	DataSource datasource;
	
//	@Autowired
//	private ConnectionFactory connectionFactory;
	
	private ComputerDAOImpl() {
	}
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Retourne l'unique instance de ComputerDAO
	 * @return
	 */
	public ComputerDAOImpl getInstance(){
		return this;
	}


	/**
	 * find a Computer by id
	 * @param paramId l'id du Computer rechercher
	 * @return l'instance de la Computer
	 */
	public Computer findById(Long paramId){
		Connection connection = DataSourceUtils.getConnection(datasource);
		
		Computer computer = new Computer();

		// Company company = new Company();
		Company company = Company.builder().build();

		// requete de recuperation des companies répertorié dans la base
		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id WHERE pc.id=?;";
		ResultSet results = null;
		PreparedStatement pstmt = null;

		if(connection != null){

			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setLong(1, paramId);
				results = pstmt.executeQuery();

				while(results.next()){
					// Recuperation des donnéees de la ligne
					Long computerId = results.getLong("pc.id");
					String computerName = results.getString("pc.name");
					DateTime computerIntroD = new DateTime(results.getDate("pc.introduced"));
					DateTime computerDiscD = new DateTime(results.getDate("pc.discontinued"));
					String companyName = results.getString("comp.name");
					Long companyId = results.getLong("comp.id");

					computer.setId(computerId);
					computer.setName(computerName);
					computer.setIntroducedDate(computerIntroD);
					computer.setDiscontinuedDate(computerDiscD);
					company.setId(companyId);
					company.setName(companyName);
					computer.setCompany(company);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				log.error("Problem during company search...");
				throw new RuntimeException("Problem during company search...");
			} finally{
				JdbcDatasource.closeObject(results, pstmt);
			}
		}
		else{
			log.error("The connection is null...");
		}

		return computer;
	}

	/**
	 * Get all the computers
	 * @return
	 */
	public List<Computer> retrieveAll() {
		
		Connection connection = DataSourceUtils.getConnection(datasource);
		
		ArrayList<Computer> al = new ArrayList<Computer>();

		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id ORDER BY pc.name;";
		ResultSet results = null;
		Statement stmt = null;

		
		if(connection != null){

			try {
				stmt = connection.createStatement();
				results = stmt.executeQuery(query);

				while(results.next()){
					Long id = results.getLong("id");
					String name = results.getString("name");
					DateTime introduced = null;
					DateTime discontinued = null;

					if(results.getTimestamp("introduced")!=null)
						introduced = new DateTime(results.getTimestamp("introduced"));
					if(results.getTimestamp("discontinued")!=null)
						discontinued = new DateTime(results.getTimestamp("discontinued"));

					Company cpy = Company.builder().build();

					cpy.setId(results.getLong("comp.id"));
					cpy.setName(results.getString("comp.name"));

					al.add(new Computer(id, name, introduced, discontinued, cpy));

				}

			} catch (SQLException e) {
				e.printStackTrace();
				log.error("Problem during retrieve");
				throw new RuntimeException("Problem during retrieve DAO..");
			} finally{
				JdbcDatasource.closeObject(results, stmt);
			}
		}
		else{
			log.error("The connection is null...");
		}

		return al;
	}


	/**
	 * Count computers in the database
	 * @return
	 */
	public int count(){
		
		Connection connection = DataSourceUtils.getConnection(datasource);
		
		// number of computers
		int nb = -99;

		String query = "SELECT COUNT(*) AS nb FROM computer;";
		ResultSet results = null;
		Statement stmt = null;

		if(connection != null){

			try {
				stmt = connection.createStatement();
				results = stmt.executeQuery(query);

				while(results.next()){
					nb = results.getInt("nb");					
				}

			} catch (SQLException e) {
				
				e.printStackTrace();
				log.error("Problem during count()");
				throw new RuntimeException("Problem during count()");
			} finally{
				JdbcDatasource.closeObject(results, stmt);
			}
		}
		else{
			log.error("The connection is null...");
		}

		return nb;
	}

	/**
	 * Insert a computer to database
	 */
	public Long create(Computer cp) {
		
		Connection connection = DataSourceUtils.getConnection(datasource);

		Long id = null;

		String query = "INSERT INTO computer(name,introduced,discontinued,company_id) VALUES(?,?,?,?);";
		int results = 0;
		PreparedStatement pstmt = null;
		
		

		try {
			pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, cp.getName());
			//pstmt.setTimestamp(2, new Timestamp(cp.getIntroducedDate().getMillis()));
			pstmt.setDate(2, new java.sql.Date(cp.getIntroducedDate().getMillis()));

			//pstmt.setTimestamp(3, new Timestamp(cp.getDiscontinuedDate().getMillis()));
			pstmt.setDate(3, new java.sql.Date(cp.getDiscontinuedDate().getMillis()));

			if(cp.getCompany().getId()!=null)
				pstmt.setLong(4, cp.getCompany().getId());
			else
				pstmt.setNull(4, Types.NULL);
			

			results = pstmt.executeUpdate();

			log.info("Insertion done...");
			
			try {
				// we take the generate id
				ResultSet rsId = pstmt.getGeneratedKeys();
				while(rsId.next()){
					id = rsId.getLong(1);
				}
				
				// closing of rsId
				//ConnectionFactory.closeObject(rsId);
				JdbcDatasource.closeObject(rsId);
				
			} catch (SQLException e1) {
				
				e1.printStackTrace();
				log.error("Insert() => Problem during id generation");
				throw new RuntimeException("Insert() => Problem during id generation");
			}

		} catch (SQLException e) {
			
			e.printStackTrace();
			log.error("Problem during insertion...");
		}finally{
			JdbcDatasource.closeObject(results, pstmt);
		}
		return id;

	}

	/**
	 * Delete a computer from database
	 * @param id
	 */
	public void delete(Long id){
		
		
		Connection connection = DataSourceUtils.getConnection(datasource);
		
		String query = "DELETE FROM computer WHERE id=?;";
		int results = 0;
		PreparedStatement pstmt = null;

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setLong(1, id);

			results = pstmt.executeUpdate();

			log.info("Deleting done...");

		} catch (SQLException e) {
			
			e.printStackTrace();
			log.error("Problem during deletion");
			throw new RuntimeException("Problem during deletion in DAO..");
		}finally{
			JdbcDatasource.closeObject(results, pstmt);
		}
	}


	/**
	 * Retrieve computer with param given
	 * @param word the word to search in the name
	 * @param rang the page
	 * @param interval the interval of result
	 * @param filter the sort mode (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	@Transactional
	public List<Computer> retrieve(String word, int rang, int interval, int filter, boolean isAsc) {
		
		
		Connection connection = DataSourceUtils.getConnection(datasource);
		
		
		ArrayList<Computer> al = new ArrayList<Computer>();

		String sFilter;
		switch(filter){
		case 0: // by Computer name
			sFilter = "pc.name"; break;
		case 1: // by introducedDate
			sFilter = "pc.introduced"; break;
		case 2: // by discontinuedDate
			sFilter = "pc.discontinued"; break;
		case 3: // by Company name
			sFilter = "comp.name"; break;
		default:
			sFilter = "pc.name"; break;
		}

		if(!isAsc)
			sFilter = sFilter + " DESC";

		sFilter = sFilter + ", pc.name ASC ";
		
		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id WHERE pc.name LIKE ? ORDER BY " + sFilter + " LIMIT ?, ?;";



		ResultSet results = null;
		PreparedStatement pstmt = null;

		
		if(connection != null){

			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setString(1, "%"+ word + "%");
				pstmt.setInt(2, rang*interval);
				pstmt.setInt(3, interval);

				results = pstmt.executeQuery();


				while(results.next()){
					Long id = results.getLong("id");
					String name = results.getString("name");
					DateTime introduced = null;
					DateTime discontinued = null;

					if(results.getTimestamp("introduced")!=null)
						introduced = new DateTime(results.getTimestamp("introduced"));
					if(results.getTimestamp("discontinued")!=null)
						discontinued = new DateTime(results.getTimestamp("discontinued"));

					// Creation de la company à associer
					// Company cpy = new Company();
					Company cpy = Company.builder().build();
					cpy.setId(results.getLong("comp.id"));
					cpy.setName(results.getString("comp.name"));

					al.add(new Computer(id, name, introduced, discontinued, cpy));

				}

			} catch (SQLException e) {
				
				e.printStackTrace();
				log.error("Problem in retrieve");
				throw new RuntimeException("Problem in retrieve in DAO..");
			} finally{
				JdbcDatasource.closeObject(results, pstmt);
			}
		}
		else{
			log.error("The connection is null...");
		}

		return al;
	}


	/**
	 * Update a computer in the database
	 * @param comp the Computer to update
	 */
	public void update(Computer comp){
		
		
		Connection connection = DataSourceUtils.getConnection(datasource);
		
		String query = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
		int results = 0;
		PreparedStatement pstmt = null;

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, comp.getName());
			pstmt.setTimestamp(2, new Timestamp(comp.getIntroducedDate().getMillis()));
			pstmt.setTimestamp(3, new Timestamp(comp.getDiscontinuedDate().getMillis()));
			if(comp.getCompany().getId()!=null)
				pstmt.setLong(4, comp.getCompany().getId());
			else
				pstmt.setNull(4, Types.NULL);
			pstmt.setLong(5, comp.getId());

			results = pstmt.executeUpdate();

			log.error("Update done...");

		} catch (SQLException e) {
			
			e.printStackTrace();
			log.error("Problem during update...");
			throw new RuntimeException("Problem during update in DAO..");
		}finally{
			JdbcDatasource.closeObject(results, pstmt);
		}
	}

	
	/**
	 * count computer in database with the pattern
	 * @param filter the pattern
	 * @return
	 */
	public int countWithFilter(String filter) {
		
		
		Connection connection = DataSourceUtils.getConnection(datasource);
		
		String query = "SELECT COUNT(*) AS nb FROM computer WHERE name LIKE ?;";
		PreparedStatement pstmt = null;
		ResultSet results = null;
		int nb = -1;
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, "%"+filter+"%");

			results = pstmt.executeQuery();

			while(results.next()){				
				nb = results.getInt(1);
			}

		} catch (SQLException e) {
			
			e.printStackTrace();
			log.error("Problem during countWithFilter...");
			throw new RuntimeException("Problem during countWithFilter in DAO..");
		}finally{
			JdbcDatasource.closeObject(results, pstmt);
		}
		
		return nb;
	}

}
