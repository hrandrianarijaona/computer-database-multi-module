package com.excilys.webService;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.core.om.Company;
import com.excilys.service.CompanyService;


@WebService(endpointInterface="com.excilys.webService.CompanyWS")
public class CompanyWSImpl implements CompanyWS {
	
	@Autowired
	CompanyService companyService;
	
	public CompanyWSImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Company> retrieveAll() {
		System.out.println("coucou");
		return (ArrayList<Company>) companyService.retrieveAll();
	}

	@Override
	public void insert(Company cp) {
		companyService.insert(cp);
	}

	@Override
	public Company findById(Long paramId) {
		return findById(paramId);
	}

	@Override
	public void hello() {
		System.out.println("hello!");
	}

}
