package com.excilys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.core.om.Computer;
import com.excilys.service.ComputerService;

@Controller
@RequestMapping("/deleteComputer")
public class DeleteComputer {

	@Autowired
	private ComputerService computerService;
	
	public DeleteComputer() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(method = RequestMethod.POST)
	public String deleteComputer(ModelMap modelMap, @RequestParam(value="id", required=true) String paramId){
		StringBuilder sb = new StringBuilder();

		Long id = Long.parseLong(paramId);
		if((id==null)||(id==0)){
			sb.append("Accès à la page de manière illégal...");
			// On envoie le message d'erreur
			modelMap.addAttribute("msg", sb.toString());

			// compte le nb de Computer dans la base
			int nbComputer = computerService.count();
			modelMap.addAttribute("nbComputer", nbComputer);

			// liste les Computers
			List<Computer> computerList = computerService.retrieveAll();
			modelMap.addAttribute("computerList", computerList);

			//			this.getServletContext().getRequestDispatcher( "/WEB-INF/dashboard.jsp" ).forward( request, response );

			return "dashboard";
		}
		else{
			// On supprime le Computer de la base
			computerService.delete(id);

			// compte le nb de Computer dans la base
			int nbComputer = computerService.count();
			modelMap.addAttribute("nbComputer", nbComputer);

			// liste les Computers
			List<Computer> computerList = computerService.retrieveAll();
			modelMap.addAttribute("computerList", computerList);

			//			this.getServletContext().getRequestDispatcher( "/index.jsp" ).forward( request, response );

			return "../../index";

		}
	}

}
