package com.excilys.binding.dto;

public class CompanyDTO {

	private String name, id;
	
	public CompanyDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CompanyDTO(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}

}
