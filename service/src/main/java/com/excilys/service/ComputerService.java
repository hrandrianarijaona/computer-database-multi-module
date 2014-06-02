package com.excilys.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.core.om.Computer;
import com.excilys.persistence.dao.QueryDslComputerDAOImpl;


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
//	@Autowired
//	private JdbcTemplateComputerDAO computerDAO;
	
//	@Autowired
//	private HibernateComputerDAOImpl computerDAO;
	
	@Autowired
	private QueryDslComputerDAOImpl computerDAO;
	
//	@Autowired
//	private LogServiceImpl logService;


	/**
	 * get an Instance of Computer
	 * @return
	 */
	public ComputerService getInstance(){
		return this;
	}

	/**
	 * search Computer by Id
	 * @param paramId l'id du Computer rechercher
	 * @return l'instance de la Computer
	 */
	@Transactional(readOnly=true)
	public Computer findComputerById(Long paramId){

		log.info("findComputerById... ");
		Computer computer = computerDAO.findById(paramId);

		return computer;
	}

	/**
	 * get all the Computer
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Computer> getListComputers() {
		List<Computer> lc = null;
		log.info("Listing of Computers... ");
		lc = computerDAO.retrieveAll();
		//			logService.addLog("Listing des Computers effectué...", TypeLog.INFOS, connection);

		return lc;
	}


	/**
	 * count the Computer from database
	 * @return
	 */
	@Transactional(readOnly=true)
	public int getNbComputer(){
		log.info("getNbComputer... ");
		int nbComputer = computerDAO.count();

		return nbComputer;
	}

	/**
	 * Insert a Computer
	 */
	@Transactional(readOnly = false)
	public void insertComputer(Computer cp) {
		Long id = null;
		id = computerDAO.create(cp);
	}

	/**
	 * delete a Computer
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void deleteComputer(Long id){
		log.info("deleteComputer...");

		computerDAO.delete(id);
		//			logService.addLog("Delete du computer id(" + id + ")", TypeLog.INFOS, connection);

	}



	/**
	 * search Computer by filtering and ordering with range
	 * @param word le motif à chercher
	 * @param rang la page
	 * @param interval le nombre d'element à afficher
	 * @param filter le mode de tri (0 => name, 1 => introducedDate, 2 => discontinuedDate, 3 => company)
	 * @param isAsc true => ascendant / false => descendant
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Computer> searchComputersByFilteringAndOrderingWithRange(String word, int rang, int interval, int filter, boolean isAsc) {

		List<Computer> lc = null;
		log.info("searchComputersByFilteringAndOrderingWithRange... ");
		lc = computerDAO.retrieve(word, rang, interval, filter, isAsc);

		return lc;
	}


	/**
	 * update a Computer
	 * @param comp le Computer à mettre à jour
	 */
	@Transactional(readOnly = false)
	public void updateComputer(Computer comp){
		log.info("updateComputer("+ comp.getId() +")... ");
		computerDAO.update(comp);
		//			logService.addLog("updateComputer("+ comp.getId() +")... ", TypeLog.INFOS, connection);

	}

	/**
	 * count Computer with filtering
	 * @param filter le motif
	 * @return
	 */
	@Transactional(readOnly=true)
	public int getNbComputerFilter(String filter) {
		log.info("getNbComputerFilter(" + filter + ")... ");
		int nbComputer = computerDAO.countWithFilter(filter);

		return nbComputer;
	}

}
