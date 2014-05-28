package com.excilys.binding.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.excilys.core.om.Computer;


public class ComputerDTO {

	private String id;
	@NotEmpty
	private String name;
	@NotEmpty
	@Pattern(regexp="[0-9]{4}-[0-1][0-9]-[0-3][0-9]")
	private String introducedDate;
	@Pattern(regexp="[0-9]{4}-[0-1][0-9]-[0-3][0-9]")
	private String discontinuedDate;
	private String idCompany;
	
	public ComputerDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public ComputerDTO(Computer computer){
		this.id = computer.getId().toString();
		this.name = computer.getName();
		
		
		if(computer.getIntroducedDate() != null)
			this.introducedDate = computer.getIntroducedDate().toString("yyyy-MM-dd");
		else
			this.introducedDate = "";
		if(computer.getDiscontinuedDate() != null)
			this.discontinuedDate = computer.getDiscontinuedDate().toString("yyyy-MM-dd");
		else
			this.discontinuedDate = "";
		if(computer.getCompany() != null)
			this.idCompany = computer.getCompany().getId().toString();
		else
			this.idCompany = "";
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroducedDate() {
		return introducedDate;
	}

	public void setIntroducedDate(String introducedDate) {
		this.introducedDate = introducedDate;
	}

	public String getDiscontinuedDate() {
		return discontinuedDate;
	}

	public void setDiscontinuedDate(String discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}

	public String getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(String company) {
		this.idCompany = company;
	}

	public ComputerDTO(String id, String name, String introducedDate,
			String discontinuedDate, String idCompany) {
		super();
		this.id = id;
		this.name = name;
		this.introducedDate = introducedDate;
		this.discontinuedDate = discontinuedDate;
		this.idCompany = idCompany;
	}
	
	@Override
	public String toString() {
		return "ComputerDTO [id=" + id + ", name=" + name + ", introducedDate="
				+ introducedDate + ", discontinuedDate=" + discontinuedDate
				+ ", idCompany=" + idCompany + "]";
	}

}
