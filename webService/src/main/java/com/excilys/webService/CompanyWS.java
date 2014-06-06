package com.excilys.webService;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.excilys.core.om.Company;

@WebService(name="Company", targetNamespace="http://localhost:8080/webapp/")
@SOAPBinding(style=Style.RPC, use=Use.LITERAL)
public interface CompanyWS {
	
	@WebMethod(operationName="retrieveAll")
	@WebResult(name="restrieveAllResult")
	public ArrayList<Company> retrieveAll();
	
	@WebMethod(operationName="insert")
	public void insert(Company cp);
	
	@WebMethod(operationName="findById")
	@WebResult(name="findByIdResult")
	public Company findById(Long paramId);
	
	@WebMethod(operationName="hello")
	public void hello();
	
}
