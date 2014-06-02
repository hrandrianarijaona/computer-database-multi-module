package com.excilys.persistence.dao;

import java.util.List;

import com.excilys.core.om.Company;

public interface CompanyDAO {

// 	public static CompanyDAO cdao = null;

	/**
	 * get all Companies
	 * @return
	 */
	public List<Company> retrieveAll();

	/**
	 * Insert a Company
	 * @param cp
	 */
	public Long insert(Company cp);

	/**
	 * Search a Company
	 * @param paramId l'id à rechercher
	 * @return the Company
	 */
	public Company findById(Long paramId);

}
