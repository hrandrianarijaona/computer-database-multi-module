package com.excilys.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.core.om.Computer;
import com.excilys.core.om.Page;
import com.excilys.service.ComputerService;
import com.excilys.binding.validator.ComputerValidator;

/**
 * Servlet implementation class RedirectIndexServlet
 */
@Controller
@RequestMapping("/RedirectIndexServlet")
public class RedirectIndexServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private ComputerService computerService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public RedirectIndexServlet() {
		// TODO Auto-generated constructor stub
		System.out.println("RedirectIndexServlet");
	}

	@RequestMapping(method = RequestMethod.GET)
	public String affichIndex(ModelMap model, @RequestParam(value="page", required=false) String sPage, @RequestParam(value="interval", required=false) String sInterval, @RequestParam(value="filter", required=false) String sFiltre, @RequestParam(value="sortCode", required=false) String sortCode) throws ServletException, IOException {
		System.out.println("RedirectIndexServlet");

		int c; // 0 => Par nom croissant, 1 => nom décroissant, 2 => introDate croissant, 3 => introDate décroissant, 4 => discDate croissant, 5 => discDate décroissant, 6 => company croissant, 7 => company décroissant
		int page = 0, interval = 20;


		// vérifie les paramètres et les initialise sinon
		if((sPage!=null)&&(ComputerValidator.isPositifNumber(sPage)))
			page = Integer.parseInt(sPage);
		if((sInterval!=null)&&(ComputerValidator.isPositifNumber(sInterval)))
			interval = Integer.parseInt(sInterval);
		if(sFiltre==null)
			sFiltre = "";


		if(sortCode!=null)
			c = Integer.parseInt(sortCode);
		else
			c = 0;

		if(computerService == null)
			System.out.println("computerService est null :(");

		List<Computer> computerList = null;
		//computerList = computerService.getListComputersWithRange(page, interval);
		switch(c){
		case 0:
			//computerList = computerService.getListComputersByFilteringAndOrdering(0, true);
			computerList = computerService.retrieve(sFiltre, page, interval, 0, true);
			break;
		case 1:
			//computerList = computerService.getListComputersByFilteringAndOrdering(0, false);
			computerList = computerService.retrieve(sFiltre, page, interval, 0, false);
			break;
		case 2:
			//computerList = computerService.getListComputersByFilteringAndOrdering(1, true);
			computerList = computerService.retrieve(sFiltre, page, interval, 1, true);
			break;
		case 3:
			//computerList = computerService.getListComputersByFilteringAndOrdering(1, false);
			computerList = computerService.retrieve(sFiltre, page, interval, 1, false);
			break;
		case 4:
			//computerList = computerService.getListComputersByFilteringAndOrdering(2, true);
			computerList = computerService.retrieve(sFiltre, page, interval, 2, true);
			break;
		case 5:
			//computerList = computerService.getListComputersByFilteringAndOrdering(2, false);
			computerList = computerService.retrieve(sFiltre, page, interval, 2, false);
			break;
		case 6:
			//computerList = computerService.getListComputersByFilteringAndOrdering(3, true);
			computerList = computerService.retrieve(sFiltre, page, interval, 3, true);
			break;
		case 7:
			//computerList = computerService.getListComputersByFilteringAndOrdering(3, false);
			computerList = computerService.retrieve(sFiltre, page, interval, 3, false);
			break;
		default:
			//computerList = computerService.getListComputersByFilteringAndOrdering(0, true);
			System.out.println("error code for sort...");
		}

		int nbComputer = computerService.countWithFilter(sFiltre);

		
		List<Computer> allComputerList = computerService.retrieveAll();

		int nbPage;
		if(sFiltre.length()>0)
			nbPage = (int) Math.ceil(computerService.retrieve(sFiltre, 0, interval, 3, false).size()/interval); // retourne le nombre de Computer correspondant au critère de recherche
		else
			nbPage = (int) Math.ceil(allComputerList.size()/interval);
		//		request.setAttribute("nbPage", nbPage);

		Page<Computer> laPage = new Page<>(nbComputer, page, interval, c, nbPage, sFiltre, computerList);

		model.addAttribute("pageComputer", laPage);

		return "dashboard";
	}


}
