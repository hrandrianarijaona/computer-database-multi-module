package com.excilys.webServiceRest;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.core.om.Company;
import com.excilys.service.CompanyService;

@Path("/hello")
public class CompanyWSRestImpl {
	
	@Autowired
	CompanyService companyService;

	@GET
	@Path("/retrieveAll")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Company> retrieveAll() {
		return (ArrayList<Company>) companyService.retrieveAll();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public void insert(Company cp) {
		companyService.insert(cp);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Company findById(Long paramId) {
		return findById(paramId);
	}
	
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Jersey say : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}

}
