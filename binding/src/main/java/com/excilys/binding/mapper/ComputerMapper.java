package com.excilys.binding.mapper;

import org.joda.time.DateTime;

import com.excilys.binding.dto.ComputerDTO;
import com.excilys.core.om.Company;
import com.excilys.core.om.Computer;
import com.excilys.service.CompanyService;

public class ComputerMapper {
	
	private ComputerMapper() {
		// TODO Auto-generated constructor stub
	}
	
	public static Computer mapComputer(ComputerDTO cdto, CompanyService companyService){
		Long id = Long.parseLong(cdto.getId());
		String name = cdto.getName();
		DateTime introducedDate = null;
		DateTime discontinuedDate = null;
		Long idCompany = Long.parseLong(cdto.getIdCompany());
		Company company = companyService.findById(idCompany);
		
		if(cdto.getIntroducedDate()!=null)
			introducedDate = new DateTime(cdto.getIntroducedDate());
		
		if(cdto.getDiscontinuedDate()!=null)
			discontinuedDate = new DateTime(cdto.getDiscontinuedDate());
		
		Computer computer = Computer.builder().id(id).name(name).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(company).build();
		
		return computer;
		
	}

}
