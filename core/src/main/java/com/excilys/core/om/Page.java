package com.excilys.core.om;

import java.util.List;

public class Page<T> {

	private int totalCount, currentPage, pageLimit, codeTri, nbPage;

	private String filter;
	
	private List<T> liste;
	
	public enum Ordre{
		ACS, DESC;
	}
	
	public Page() {
		// TODO Auto-generated constructor stub
	}
	

	public Page(int totalCount, int currentPage, int pageLimit, int codeTri,
			int nbPage, String filter, List<T> liste) {
		super();
		this.totalCount = totalCount;
		this.currentPage = currentPage;
		this.pageLimit = pageLimit;
		this.codeTri = codeTri;
		this.nbPage = nbPage;
		this.filter = filter;
		this.liste = liste;
	}


	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageLimit() {
		return pageLimit;
	}

	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}

	public List<T> getListe() {
		return liste;
	}

	public void setListe(List<T> liste) {
		this.liste = liste;
	}
	
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public int getCodeTri() {
		return codeTri;
	}

	public void setCodeTri(int codeTri) {
		this.codeTri = codeTri;
	}
	
	public int getNbPage() {
		return nbPage;
	}

	public void setNbPage(int nbPage) {
		this.nbPage = nbPage;
	}
	
	

}
