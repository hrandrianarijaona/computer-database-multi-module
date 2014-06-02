package com.excilys.persistence.dao;

import java.sql.Connection;
import java.util.List;

import com.excilys.core.om.Computer;

public interface ComputerDAO {

	/**
	 * Cherche un Computer par son Id
	 * @param paramId
	 * @return
	 */
	public Computer findById(Long paramId);
	
	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base
	 * @return
	 */
	public List<Computer> retrieveAll();
	
	/**
	 * retourne le nombre de computer/ordinateur dans la base
	 * @return
	 */
	public int count();
	
	/**
	 * retourne le nombre de computer/ordinateur dans la base contenant le motif filter
	 * @param filter le motif
	 * @param connection la connection
	 * @return
	 */
	public int countWithFilter(String filter);

	/**
	 * Insert un ordinateur/computer dans la base
	 */
	public Long create(Computer cp);

	/**
	 * Supprime l'ordinateur identifié en paramètre de la base de donnée
	 * @param id
	 */
	public void delete(Long id);
	
	/**
	 * Met à jour un computer
	 * @param computer
	 */
	public void update(Computer computer);
	
	
	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base correspondant au motif avec intervalle de resultat et les critères de triage et d'ordre
	 * @param word le motif à chercher
	 * @param rang la page
	 * @param interval le nombre d'element à afficher
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	public List<Computer> retrieve(String word, int rang, int interval, int filter, boolean isAsc);
	
	
}
