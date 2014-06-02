package com.excilys.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.core.om.Company;
import com.excilys.persistence.dao.QueryDslCompanyDAOImpl;

/**
 * Classe singleton de service pour Company
 * @author hrandr
 *
 */
@Service
@Transactional
public class CompanyService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
//	@Autowired
//	private LogServiceImpl logService;
//	@Autowired
//	private CompanyDAOImpl companyDAO;
//	@Autowired
//	private JdbcTemplateCompanyDAO companyDAO;
	
//	@Autowired
//	private HibernateCompanyDAOImpl companyDAO;
	
	@Autowired
	private QueryDslCompanyDAOImpl companyDAO;
	
	/**
	 * get an Instance of Company
	 * @return
	 */
	public CompanyService getInstance(){
		return this;
	}
	
	/**
	 * get all the Company
	 * @return
	 */
	@Transactional
	public List<Company> retrieveAll() {
		List<Company> lc = null;
		log.info("getListCompany... ");
		lc = companyDAO.retrieveAll();
		
		return lc;
	}
	
	/**
	 * Insert a Company
	 * @param cp
	 */
	@Transactional
	public void insert(Company cp) {
		Long id = null;
		//			connection.setAutoCommit(false);
					id = companyDAO.insert(cp);
					log.info("insertCompany(" + id + ")");
		//			logService.addLog("insertCompany(" + id + ")", TypeLog.INFOS, connection);
		
	}
	
	/**
	 * Search Company
	 * @param paramId l'id à rechercher
	 * @return L'objet Company
	 */
	@Transactional(readOnly=true)
	public Company findById(Long paramId){
		Company cpy = null;
		log.info("findCompanyById(" + paramId + ") ");
		cpy = companyDAO.findById(paramId);
		
		return cpy;
	}
	
	
}
