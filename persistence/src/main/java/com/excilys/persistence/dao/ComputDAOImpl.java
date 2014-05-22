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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.persistence.connection.ConnectionFactory;
import com.excilys.persistence.connection.ProjetConnection;
import com.excilys.core.om.Company;
import com.excilys.core.om.Computer;

@Repository
public class ComputDAOImpl implements ComputerDAO{

	
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	private ComputDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retourne l'unique instance de ComputerDAO
	 * @return
	 */
	public ComputDAOImpl getInstance(){
		return this;
	}


	/**
	 * Recherche le Computer dans la base de donnée
	 * @param paramId l'id du Computer rechercher
	 * @return l'instance de la Computer
	 */
	public Computer findComputerById(Long paramId){
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problème dans la requete de recherche de company...");
			} finally{
				try {

					if(results != null)
						results.close();
					if(pstmt != null)
						pstmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return computer;
	}

	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base
	 * @return
	 */
	public List<Computer> getListComputers() {
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		ArrayList<Computer> al = new ArrayList<Computer>();

		// ajoutez ici le code de r�cup�ration des produits
		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id ORDER BY pc.name;";
		ResultSet results = null;
		Statement stmt = null;

		
		if(connection != null){

			try {
				stmt = connection.createStatement();
				results = stmt.executeQuery(query);

				while(results.next()){
					// Recuperation des donnéees de Computer
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problème dans la requete de listing niveau DAO...");
			} finally{
				try {

					if(results != null)
						results.close();
					if(stmt != null)
						stmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return al;
	}


	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base
	 * @param rang le rang
	 * @return
	 */
	public List<Computer> getListComputersWithRange(int rang, int interval) {
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		ArrayList<Computer> al = new ArrayList<Computer>();

		// ajoutez ici le code de r�cup�ration des produits
		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id ORDER BY pc.name LIMIT ?, ?;";
		ResultSet results = null;
		PreparedStatement pstmt = null;

		if(connection != null){

			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, rang*interval);
				pstmt.setInt(2, interval);
				results = pstmt.executeQuery();

				while(results.next()){
					// Recuperation des donnéees de Computer
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problème dans la requete de listing...");
			} finally{
				try {

					if(results != null)
						results.close();
					if(pstmt != null)
						pstmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return al;
	}


	/**
	 * retourne le nombre de computer/ordinateur dans la base
	 * @return
	 */
	public int getNbComputer(){
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		// nombre de computer
		int nb = -99;

		// requete
		String query = "SELECT COUNT(*) AS nb FROM computer;";
		ResultSet results = null;
		Statement stmt = null;

		if(connection != null){

			try {
				stmt = connection.createStatement();
				results = stmt.executeQuery(query);

				while(results.next()){
					// Recuperation des donnéees de la ligne
					nb = results.getInt("nb");					
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problème dans la requete de comptage de computer...");
			} finally{
				try {

					if(results != null)
						results.close();
					if(stmt != null)
						stmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return nb;
	}

	/**
	 * Insert un ordinateur/computer dans la base
	 */
	public Long insertComputer(Computer cp) {
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}

		Long id = null;
		
		// ajoutez ici le code d'insertion d'un produit
		String query = "INSERT INTO computer(name,introduced,discontinued,company_id) VALUES(?,?,?,?);";
		int results = 0;
		PreparedStatement pstmt = null;
		
		

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, cp.getName());
			//pstmt.setTimestamp(2, new Timestamp(cp.getIntroducedDate().getMillis()));
			pstmt.setDate(2, new java.sql.Date(cp.getIntroducedDate().getMillis()));

			//pstmt.setTimestamp(3, new Timestamp(cp.getDiscontinuedDate().getMillis()));
			pstmt.setDate(3, new java.sql.Date(cp.getDiscontinuedDate().getMillis()));

			if(cp.getCompany().getId()!=null)
				pstmt.setLong(4, cp.getCompany().getId());
			else
				pstmt.setNull(4, Types.NULL);
			
			System.out.println("La requete: " + pstmt.toString());

			results = pstmt.executeUpdate();

			System.out.println("Insertion bien effectué...");
			
			try {
				// On recupère l'id généré
				ResultSet rsId = pstmt.getGeneratedKeys();
				while(rsId.next()){
					id = rsId.getLong(1);
				}
				
				// fermeture de rsId
				ConnectionFactory.closeObject(rsId);
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("Probleme dans la génération des id Computer...");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Probleme dans la requete d'insertion...");
		}finally{
			try {

				if(pstmt != null)
					pstmt.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return id;

	}

	/**
	 * Supprime l'ordinateur identifié en paramètre de la base de donnée
	 * @param id
	 */
	public void deleteComputer(Long id){
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		// la requete
		String query = "DELETE FROM computer WHERE id=?;";
		int results = 0;
		PreparedStatement pstmt = null;

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setLong(1, id);
			System.out.println("La requete: " + pstmt.toString());

			results = pstmt.executeUpdate();

			System.out.println("Suppression bien effectué...");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Probleme dans la requete de suppression...");
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

	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base correspondant au motif
	 * @return
	 */
	public List<Computer> searchComputers(String word) {
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		ArrayList<Computer> al = new ArrayList<Computer>();

		// requete de recherche du pattern
		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id WHERE pc.name LIKE ? ;";

		ResultSet results = null;
		PreparedStatement pstmt = null;

		if(connection != null){

			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setString(1, "%"+ word + "%");

				results = pstmt.executeQuery();

				while(results.next()){
					// Recuperation des donnéees de Computer
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problème dans la requete de recherche...");
			} finally{
				try {

					if(results != null)
						results.close();
					if(pstmt != null)
						pstmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return al;
	}

	/**
	 * Fonction de recherche par filtre
	 * @param word le mot ou schema à rechercher
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	public List<Computer> searchComputersByFilteringAndOrdering(String word, int filter, boolean isAsc) {
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		ArrayList<Computer> al = new ArrayList<Computer>();

		String sFilter;
		switch(filter){
		case 0: // par nom de Computer
			sFilter = "pc.name"; break;
		case 1: // par introducedDate
			sFilter = "pc.introduced"; break;
		case 2: // par discontinuedDate
			sFilter = "pc.discontinued"; break;
		case 3: // par nom de Company
			sFilter = "comp.name"; break;
		default:
			sFilter = "pc.name"; break;
		}

		String query;

		// requete de recherche du pattern
		if(isAsc)
			query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id WHERE pc.name LIKE ? ORDER BY " + sFilter + ";";
		else
			query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id WHERE pc.name LIKE ? ORDER BY " + sFilter + " DESC;";

		ResultSet results = null;
		PreparedStatement pstmt = null;

		if(connection != null){

			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setString(1, "%"+ word + "%");

				results = pstmt.executeQuery();

				while(results.next()){
					// Recuperation des donnéees de Computer
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problème dans la requete de recherche...");
			} finally{
				try {

					if(results != null)
						results.close();
					if(pstmt != null)
						pstmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return al;
	}

	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base correspondant au motif avec intervalle de resultat
	 * @return
	 */
	public List<Computer> searchComputersWithRange(String word, int rang, int interval) {
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		ArrayList<Computer> al = new ArrayList<Computer>();

		// requete de recherche du pattern
		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id WHERE pc.name LIKE ? ORDER BY pc.name LIMIT ?, ?;";

		ResultSet results = null;
		PreparedStatement pstmt = null;

		if(connection != null){

			try {
				pstmt = connection.prepareStatement(query);
				pstmt.setString(1, "%"+ word + "%");
				pstmt.setInt(2, rang);
				pstmt.setInt(3, interval);

				results = pstmt.executeQuery();

				System.out.println(pstmt.toString());

				while(results.next()){
					// Recuperation des donnéees de Computer
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problème dans la requete de recherche...");
			} finally{
				try {

					if(results != null)
						results.close();
					if(pstmt != null)
						pstmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return al;
	}

	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base correspondant au motif avec intervalle de resultat et les critères de triage et d'ordre
	 * @param word le motif à chercher
	 * @param rang la page
	 * @param interval le nombre d'element à afficher
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	public List<Computer> searchComputersByFilteringAndOrderingWithRange(String word, int rang, int interval, int filter, boolean isAsc) {
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		ArrayList<Computer> al = new ArrayList<Computer>();

		String sFilter;
		switch(filter){
		case 0: // par nom de Computer
			sFilter = "pc.name"; break;
		case 1: // par introducedDate
			sFilter = "pc.introduced"; break;
		case 2: // par discontinuedDate
			sFilter = "pc.discontinued"; break;
		case 3: // par nom de Company
			sFilter = "comp.name"; break;
		default:
			sFilter = "pc.name"; break;
		}

		if(!isAsc)
			sFilter = sFilter + " DESC";

		sFilter = sFilter + ", pc.name ASC ";
		// requete de recherche du pattern
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

				// System.out.println(pstmt.toString());

				while(results.next()){
					// Recuperation des donnéees de Computer
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problème dans la requete de recherche...");
			} finally{
				try {

					if(results != null)
						results.close();
					if(pstmt != null)
						pstmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return al;
	}

	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base avec les critères de filtrage et d'ordre
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	public List<Computer> getListComputersByFilteringAndOrdering(int filter, boolean isAsc) {
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		ArrayList<Computer> al = new ArrayList<Computer>();

		String sFilter;
		switch(filter){
		case 0: // par nom de Computer
			sFilter = "pc.name"; break;
		case 1: // par introducedDate
			sFilter = "pc.introduced"; break;
		case 2: // par discontinuedDate
			sFilter = "pc.discontinued"; break;
		case 3: // par nom de Company
			sFilter = "comp.name"; break;
		default:
			sFilter = "pc.name"; break;
		}

		if(!isAsc)
			sFilter = sFilter + " DESC";
		
		sFilter = sFilter + ", pc.name ASC";

		// ajoutez ici le code de r�cup�ration des produits
		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id ORDER BY " + sFilter + ";";
		ResultSet results = null;
		Statement stmt = null;

		if(connection != null){

			try {
				stmt = connection.createStatement();
				results = stmt.executeQuery(query);

				System.out.println("La requete: " + query);

				while(results.next()){
					// Recuperation des donnéees de Computer
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problème dans la requete de listing...");
			} finally{
				try {

					if(results != null)
						results.close();
					if(stmt != null)
						stmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("La connection est null...");
		}


		return al;
	}

	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base avec les critères de filtrage et d'ordre
	 * @param rang la page
	 * @param interval le nombre d'element à afficher
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	public List<Computer> getListComputersByFilteringAndOrderingWithRange(int rang, int interval, int filter, boolean isAsc) {
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		ArrayList<Computer> al = new ArrayList<Computer>();

		String sFilter;
		switch(filter){
		case 0: // par nom de Computer
			sFilter = "pc.name"; break;
		case 1: // par introducedDate
			sFilter = "pc.introduced"; break;
		case 2: // par discontinuedDate
			sFilter = "pc.discontinued"; break;
		case 3: // par nom de Company
			sFilter = "comp.name"; break;
		default:
			sFilter = "pc.name"; break;
		}

		if(!isAsc)
			sFilter = sFilter + " DESC";
		
		sFilter = sFilter + ", pc.name ASC ";
		
		// ajoutez ici le code de r�cup�ration des produits
		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id ORDER BY " + sFilter + " LIMIT ?,?;";
		ResultSet results = null;
		PreparedStatement pstmt = null;

		if(connection != null){

			try {
				pstmt = connection.prepareStatement(query);


				pstmt.setInt(1, rang*interval);
				pstmt.setInt(2, interval);

				results = pstmt.executeQuery();

				while(results.next()){
					// Recuperation des donnéees de Computer
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Problème dans la requete de listing...");
			} finally{
				try {

					if(results != null)
						results.close();
					if(pstmt != null)
						pstmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("La connection est null...");
		}

		return al;
	}


	/**
	 * Met à jour un Computer de la base
	 * @param comp le Computer à mettre à jour
	 */
	public void updateComputer(Computer comp){
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
		// ajoutez ici le code d'update d'un Computer
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
			System.out.println("La requete: " + pstmt.toString());

			results = pstmt.executeUpdate();

			System.out.println("Mis à jour bien effectué...");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Probleme dans la requete de mis à jour...");
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

	
	/**
	 * retourne le nombre de computer/ordinateur dans la base contenant le motif filter
	 * @param filter le motif
	 * @param connection la connection
	 * @return
	 */
	public int getNbComputerFilter(String filter) {
		
		Logger log = LoggerFactory.getLogger(this.getClass());
		Connection connection = null;
		try {
			connection = connectionFactory.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("Erreur lors de la demande de connection.");
		}
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Probleme dans le count de resultat...");
		}finally{
			try {

				if(pstmt != null)
					pstmt.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return nb;
	}

}
