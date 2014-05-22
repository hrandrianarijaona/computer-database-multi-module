package com.excilys.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.binding.dto.ComputerDTO;
import com.excilys.core.om.Company;
import com.excilys.core.om.Computer;
import com.excilys.core.om.Page;
import com.excilys.service.CompanyService;
import com.excilys.service.ComputerService;
import com.excilys.binding.validator.ComputerValidator;

@Controller
@RequestMapping("/editComputer")
public class EditComputer {

	@Autowired
	ComputerService computerService;
	
	@Autowired
	CompanyService companyService;
	
	public EditComputer() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(method = RequestMethod.GET)
	public String bindComputer(ModelMap modelMap, @RequestParam(value="id", required=true) String paramId){
		
		// On recupère le Computer à éditer en le passant par un computerDTO
		Computer computer = computerService.findComputerById(Long.parseLong(paramId));
		ComputerDTO computerDTO = new ComputerDTO(computer);
		modelMap.addAttribute("computerDTO", computerDTO);
		System.out.println(computer);
		System.out.println(computerDTO);
		
		// On recupere la liste des Company
		List<Company> companyList = companyService.getListCompany();		
		modelMap.addAttribute("companyList", companyList);

		return "editComputer";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String updateComputer(@Valid ComputerDTO computerDTO, BindingResult bindingResult, ModelMap modelMap){
		if (bindingResult.hasErrors()) {
			System.out.println("FAUX!!!");
			// on créer un objet computer contenant les champs précédent pour les retransmettre dans le formulaire
			// On recupère le Computer à éditer
			Computer computer = computerService.findComputerById(Long.parseLong(computerDTO.getId()));
			
			// on réenvoi l'ancienne version valide
			ComputerDTO oldComputerDTO = new ComputerDTO(computer);
			modelMap.addAttribute("computerDTO", oldComputerDTO);

			List<Company> companyList = companyService.getListCompany();
			modelMap.addAttribute("companyList", companyList);

			return "editComputer";
		} else {
			
//			System.out.println("paramId côté Controlleur: " + computerDTO.getId());
//			System.out.println("idCompany côté Controlleur: " + computerDTO.getIdCompany());
			
			int code = 0;
			int page = 0, interval = 20;
			String sFiltre = "";
			
			// On crée le Computer
			Computer c = new Computer();
			c.setId(Long.parseLong(computerDTO.getId()));
			c.setName(computerDTO.getName());
			c.setIntroducedDate(new DateTime(computerDTO.getIntroducedDate()));
			c.setDiscontinuedDate(new DateTime(computerDTO.getDiscontinuedDate()));
			// On récupère la Company correspondante
			c.setCompany(companyService.findCompanyById(Long.parseLong(computerDTO.getIdCompany())));

			// On update le computer dans la base 
			computerService.updateComputer(c);

			// compte le nb de Computer dans la base
			int nbComputer = computerService.getNbComputer();
			//			request.setAttribute("nbComputer", nbComputer);

			// liste les Computers
			List<Computer> computerList = computerService.searchComputersByFilteringAndOrderingWithRange(sFiltre, page, interval, code, true);
			//			request.setAttribute("computerList", computerList);

			int nbPage = (int) Math.ceil(computerService.getListComputers().size()/interval);

			Page<Computer> laPage = new Page<>(nbComputer, page, interval, code, nbPage, sFiltre, computerList);
			modelMap.addAttribute("pageComputer", laPage);
			
			return "dashboard";
		}
	}
	
	
//	@RequestMapping(method = RequestMethod.POST)
//	public String updateComputerOld(ComputerDTO computerDTO, BindingResult bindingResult, ModelMap modelMap){
//
//		HashMap<String, Integer> errorList = ComputerValidator.validateField(computerDTO);
//
//		// empty si aucune erreur
//		if(errorList.isEmpty()){
//			int code = 0;
//			int page = 0, interval = 20;
//			String sFiltre = "";
//
//			// On crée le Computer
//			Computer c = new Computer();
//			c.setId(Long.parseLong(computerDTO.getId()));
//			c.setName(computerDTO.getName());
//			c.setIntroducedDate(new DateTime(computerDTO.getIntroducedDate()));
//			c.setDiscontinuedDate(new DateTime(computerDTO.getDiscontinuedDate()));
//			// On récupère la Company correspondante
//			c.setCompany(companyService.findCompanyById(Long.parseLong(computerDTO.getIdCompany())));
//
//			// On update le computer dans la base 
//			computerService.updateComputer(c);
//
//			// compte le nb de Computer dans la base
//			int nbComputer = computerService.getNbComputer();
//			//			request.setAttribute("nbComputer", nbComputer);
//
//			// liste les Computers
//			List<Computer> computerList = computerService.searchComputersByFilteringAndOrderingWithRange(sFiltre, page, interval, code, true);
//			//			request.setAttribute("computerList", computerList);
//
//			int nbPage = (int) Math.ceil(computerService.getListComputers().size()/interval);
//
//			Page<Computer> laPage = new Page<>(nbComputer, page, interval, code, nbPage, sFiltre, computerList);
//			modelMap.addAttribute("pageComputer", laPage);
//
//			//					this.getServletContext().getRequestDispatcher( "/WEB-INF/dashboard.jsp" ).forward( request, response );
//
//			return "dashboard";
//
//		}
//		else{
//
//			// on envoi directement la map d'erreur
//			modelMap.addAttribute("errorList", errorList);
//
//			// on créer un objet computer contenant les champs précédent pour les retransmettre dans le formulaire
//			// On recupère le Computer à éditer
//			Computer computer = computerService.findComputerById(Long.parseLong(computerDTO.getId()));
//			
//			// on réenvoi l'ancienne version valide
//			ComputerDTO oldComputerDTO = new ComputerDTO(computer);
//			modelMap.addAttribute("computerDTO", oldComputerDTO);
//
//			List<Company> companyList = companyService.getListCompany();
//			modelMap.addAttribute("companyList", companyList);
//			//					this.getServletContext().getRequestDispatcher( "/WEB-INF/editComputer.jsp" ).forward( request, response );
//
//			return "editComputer";
//
//		}
//	}

}
