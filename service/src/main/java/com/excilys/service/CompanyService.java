package com.excilys.service;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.core.om.Company;
import com.excilys.persistence.dao.CompanyDAOImpl;
import com.excilys.persistence.dao.HibernateCompanyDAOImpl;
import com.excilys.persistence.dao.JdbcTemplateCompanyDAO;

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
	
	@Autowired
	private HibernateCompanyDAOImpl companyDAO;
	
	
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
	public List<Company> getListCompany() {
		List<Company> lc = null;
		log.info("getListCompany... ");
		lc = companyDAO.getListCompany();
		
		return lc;
	}
	
	/**
	 * Insert a Company
	 * @param cp
	 */
	@Transactional
	public void insertCompany(Company cp) {
		Long id = null;
		//			connection.setAutoCommit(false);
					id = companyDAO.insertCompany(cp);
					log.info("insertCompany(" + id + ")");
		//			logService.addLog("insertCompany(" + id + ")", TypeLog.INFOS, connection);
		
	}
	
	/**
	 * Search Company
	 * @param paramId l'id à rechercher
	 * @return L'objet Company
	 */
	@Transactional(readOnly=true)
	public Company findCompanyById(Long paramId){
		Company cpy = null;
		log.info("findCompanyById(" + paramId + ") ");
		cpy = companyDAO.findCompanyById(paramId);
		
		return cpy;
	}
	
	
}
