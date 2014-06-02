package com.excilys.controller;

import java.util.List;

import javax.validation.Valid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.excilys.binding.dto.ComputerDTO;
import com.excilys.core.om.Company;
import com.excilys.core.om.Computer;
import com.excilys.core.om.Page;
import com.excilys.service.CompanyService;
import com.excilys.service.ComputerService;

@Controller
@RequestMapping("/addComputer")
public class AddComputer {

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ComputerService computerService;
	
//	@Autowired
//	@Qualifier(value="messageSource")
//	private ResourceBundleMessageSource rbms;
	
	public AddComputer() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String initComputer(ModelMap modelMap){
		List<Company> companyList = companyService.retrieveAll();
		modelMap.addAttribute("companyList", companyList);
		
		modelMap.addAttribute("computerDTO", new ComputerDTO()); // model Computer associé au formulaire
		
		return "addComputer";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String saveComputer(@Valid ComputerDTO computerDTO, BindingResult bindingResult, ModelMap modelMap){
		if (bindingResult.hasErrors()) {
			List<Company> companyList = companyService.retrieveAll();
			modelMap.addAttribute("companyList", companyList);
			return "addComputer";
		} else {
			
			int code = 0;
			int page = 0, interval = 20;
			String sFiltre = "";
			
			
//			DateTimeFormatter fmt = DateTimeFormat.forPattern(rbms.getMessage("add.date_format", null, LocaleContextHolder.getLocale()));
			
			// On crée le Computer
			Computer c = new Computer();
			c.setName(computerDTO.getName());
			c.setIntroducedDate(new DateTime(computerDTO.getIntroducedDate()));
//			c.setIntroducedDate(fmt.parseDateTime(computerDTO.getIntroducedDate()));
			c.setDiscontinuedDate(new DateTime(computerDTO.getDiscontinuedDate()));
//			c.setIntroducedDate(fmt.parseDateTime(computerDTO.getDiscontinuedDate()));
			
			// On récupère la Company correspondante
			if(!computerDTO.getIdCompany().equals("0"))
				c.setCompany(companyService.findById(Long.parseLong(computerDTO.getIdCompany())));
			else
				c.setCompany(null);

			// On insère le computer dans la base
			computerService.insert(c);

			// compte le nb de Computer dans la base
			int nbComputer = computerService.count();
			modelMap.addAttribute("nbComputer", nbComputer);

			// liste les Computers
			List<Computer> computerList = computerService.retrieve(sFiltre, page, interval, code, true);
			modelMap.addAttribute("computerList", computerList);

			int nbPage = (int) Math.ceil(computerService.retrieveAll().size()/interval);

			Page<Computer> laPage = new Page<>(nbComputer, page, interval, code, nbPage, sFiltre, computerList);
			modelMap.addAttribute("pageComputer", laPage);


			
			return "dashboard";
		}
	}

}
