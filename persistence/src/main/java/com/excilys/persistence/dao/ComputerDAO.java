package com.excilys.persistence.dao;

import java.util.List;

import com.excilys.core.om.Computer;

public interface ComputerDAO {

	/**
	 * find a Computer by id
	 * @param paramId
	 * @return
	 */
	public Computer findById(Long paramId);
	
	/**
	 * get all the computers in database
	 * @return
	 */
	public List<Computer> retrieveAll();
	
	/**
	 * count all the computers
	 * @return
	 */
	public int count();
	
	/**
	 * count all the computer with name filtering
	 * @param filter le motif
	 * @param connection la connection
	 * @return
	 */
	public int countWithFilter(String filter);

	/**
	 * Insert a computer in the database
	 */
	public Long create(Computer cp);

	/**
	 * Delete the computer from database
	 * @param id
	 */
	public void delete(Long id);
	
	/**
	 * Update a computer
	 * @param computer
	 */
	public void update(Computer computer);
	
	
	/**
	 * Retrieve computers with the param given
	 * @param word le motif à chercher
	 * @param rang la page
	 * @param interval le nombre d'element à afficher
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	public List<Computer> retrieve(String word, int rang, int interval, int filter, boolean isAsc);
	
	
}
