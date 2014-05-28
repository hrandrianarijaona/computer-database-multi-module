package com.excilys.persistence.dao;

import java.sql.Connection;
import java.util.List;

import com.excilys.core.om.Company;

public interface CompanyDAO {

// 	public static CompanyDAO cdao = null;

	/**
	 * get all Companies
	 * @return
	 */
	public List<Company> getListCompany();

	/**
	 * Insert a Company
	 * @param cp
	 */
	public Long insertCompany(Company cp);

	/**
	 * Search a Company
	 * @param paramId l'id à rechercher
	 * @return L'objet Company
	 */
	public Company findCompanyById(Long paramId);

}
