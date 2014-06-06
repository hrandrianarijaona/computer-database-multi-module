package com.excilys.webService;

import java.util.List;

import com.excilys.core.om.Computer;


public interface ComputerWS {

	/**
	 * search Computer by Id
	 * @param paramId l'id du Computer rechercher
	 * @return l'instance de la Computer
	 */
	public Computer findComputerById(Long paramId);

	/**
	 * get all the Computer
	 * @return
	 */
	public List<Computer> retrieveAll();


	/**
	 * count the Computer from database
	 * @return
	 */
	public int count();

	/**
	 * Insert a Computer
	 */
	public void insert(Computer cp);

	/**
	 * delete a Computer
	 * @param id
	 */
	public void delete(Long id);



	/**
	 * search Computer by filtering and ordering with range
	 * @param word le motif à chercher
	 * @param rang la page
	 * @param interval le nombre d'element à afficher
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	public List<Computer> retrieve(String word, int rang, int interval, int filter, boolean isAsc);


	/**
	 * update a Computer
	 * @param comp le Computer à mettre à jour
	 */
	public void update(Computer comp);

	/**
	 * count Computer with filtering
	 * @param filter le motif
	 * @return
	 */
	public int countWithFilter(String filter);

}
