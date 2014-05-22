package com.excilys.service;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.persistence.dao.ComputerDAOImpl;
import com.excilys.persistence.dao.JdbcTemplateComputerDAO;
import com.excilys.core.om.Computer;


/**
 * Classe Service de Computer
 * @author hrandr
 *
 */
@Service
@Transactional
public class ComputerService {

	
	private Logger log = LoggerFactory.getLogger(this.getClass());
//	@Autowired
//	private ComputerDAOImpl computerDAO;
	@Autowired
	private JdbcTemplateComputerDAO computerDAO;
	
	@Autowired
	private LogServiceImpl logService;


	/**
	 * retourne l'unique instance de ComputerService
	 * @return
	 */
	public ComputerService getInstance(){
		return this;
	}

	/**
	 * Recherche le Computer dans la base de donnée
	 * @param paramId l'id du Computer rechercher
	 * @return l'instance de la Computer
	 */
	@Transactional
	public Computer findComputerById(Long paramId){

		log.info("findComputerById... ");
		Computer computer = computerDAO.findComputerById(paramId);

		return computer;
	}

	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base
	 * @return
	 */
	@Transactional
	public List<Computer> getListComputers() {
		List<Computer> lc = null;
		log.info("Listing des Computers... ");
		lc = computerDAO.getListComputers();
		//			logService.addLog("Listing des Computers effectué...", TypeLog.INFOS, connection);

		return lc;
	}

	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base avec les critères de filtrage et d'ordre
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	@Transactional
	public List<Computer> getListComputersByFilteringAndOrdering(int filter, boolean isAsc) {
		log.info("getListComputersByFilteringAndOrdering... ");
		List<Computer> lc = null;

		lc = computerDAO.getListComputersByFilteringAndOrdering(filter, isAsc);


		return lc;
	}



	/**
	 * retourne le nombre de computer/ordinateur dans la base
	 * @return
	 */
	@Transactional
	public int getNbComputer(){
		log.info("getNbComputer... ");
		int nbComputer = computerDAO.getNbComputer();

		return nbComputer;
	}

	/**
	 * Insert un ordinateur/computer dans la base
	 */
	@Transactional
	public void insertComputer(Computer cp) {
		Long id = null;
		id = computerDAO.insertComputer(cp);		
	}

	/**
	 * Supprime l'ordinateur identifié en paramètre de la base de donnée
	 * @param id
	 */
	@Transactional
	public void deleteComputer(Long id){
		log.info("deleteComputer...");

		computerDAO.deleteComputer(id);
		//			logService.addLog("Delete du computer id(" + id + ")", TypeLog.INFOS, connection);

	}

	/**
	 * Fonction de recherche par filtre
	 * @param word le mot ou schema à rechercher
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	@Transactional
	public List<Computer> searchComputersByFilteringAndOrdering(String word, int filter, boolean isAsc) {
		List<Computer> lc = null;
		log.info("searchComputersByFilteringAndOrdering... ");
		lc = computerDAO.searchComputersByFilteringAndOrdering(word, filter, isAsc);

		return lc;
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
	@Transactional
	public List<Computer> searchComputersByFilteringAndOrderingWithRange(String word, int rang, int interval, int filter, boolean isAsc) {

		List<Computer> lc = null;
		log.info("searchComputersByFilteringAndOrderingWithRange... ");
		lc = computerDAO.searchComputersByFilteringAndOrderingWithRange(word, rang, interval, filter, isAsc);

		return lc;
	}


	/**
	 * Liste tous les ordinateurs/computers repertorié dans la base avec les critères de filtrage et d'ordre
	 * @param rang la page
	 * @param interval le nombre d'element à afficher
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	@Transactional
	public List<Computer> getListComputersByFilteringAndOrderingWithRange(int rang, int interval, int filter, boolean isAsc){

		List<Computer> lc = null;
		log.info("getListComputersByFilteringAndOrderingWithRange... ");
		lc = computerDAO.getListComputersByFilteringAndOrderingWithRange(rang, interval, filter, isAsc);

		return lc;
	}

	/**
	 * Met à jour un Computer de la base
	 * @param comp le Computer à mettre à jour
	 */
	@Transactional
	public void updateComputer(Computer comp){
		log.info("updateComputer("+ comp.getId() +")... ");
		computerDAO.updateComputer(comp);
		//			logService.addLog("updateComputer("+ comp.getId() +")... ", TypeLog.INFOS, connection);

	}

	/**
	 * retourne le nombre de computer/ordinateur dans la base contenant le motif filter
	 * @param filter le motif
	 * @return
	 */
	@Transactional
	public int getNbComputerFilter(String filter) {
		log.info("getNbComputerFilter(" + filter + ")... ");
		int nbComputer = computerDAO.getNbComputerFilter(filter);

		return nbComputer;
	}

}
