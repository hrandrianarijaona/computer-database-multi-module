package com.excilys.binding.mapper;

import com.excilys.binding.dto.CompanyDTO;
import com.excilys.core.om.Company;

public class CompanyMapper {

	private CompanyMapper() {
		// TODO Auto-generated constructor stub
	}
	
	public static Company mapCompany(CompanyDTO cdto){
		Long id = Long.parseLong(cdto.getId());
		Company company = Company.builder().id(id).name(cdto.getName()).build();
		return company;
	}

}
