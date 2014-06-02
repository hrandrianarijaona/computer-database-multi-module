package com.excilys.core.om;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name="computer")
public class Computer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -210029653257957432L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="introduced")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime introducedDate;
	
	@Column(name="discontinued")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime discontinuedDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateTime getIntroducedDate() {
		return introducedDate;
	}
	

	public void setIntroducedDate(DateTime introducedDate) {
		this.introducedDate = introducedDate;
	}
	
	public DateTime getDiscontinuedDate() {
		return discontinuedDate;
	}
	

	public void setDiscontinuedDate(DateTime discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Computer() {
		// TODO Auto-generated constructor stub
	}

	public Computer(Long id, String name, DateTime introducedDate,
			DateTime discontinuedDate, Company company) {
		super();
		this.id = id;
		this.name = name;
		this.introducedDate = introducedDate;
		this.discontinuedDate = discontinuedDate;
		this.company = company;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", introducedDate="
				+ introducedDate + ", discontinuedDate=" + discontinuedDate
				+ ", company=" + company + "]";
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime
				* result
				+ ((discontinuedDate == null) ? 0 : discontinuedDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((introducedDate == null) ? 0 : introducedDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (discontinuedDate == null) {
			if (other.discontinuedDate != null)
				return false;
		} else if (!discontinuedDate.equals(other.discontinuedDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (introducedDate == null) {
			if (other.introducedDate != null)
				return false;
		} else if (!introducedDate.equals(other.introducedDate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public Computer(ComputerBuilder cb){
		this.id = cb.id;
		this.name = cb.name;
		this.company = cb.company;
		this.discontinuedDate = cb.discontinuedDate;
		this.introducedDate = cb.introducedDate;
	}

	public static class ComputerBuilder{
		private Long id;
		private String name;
		private DateTime introducedDate; // optional
		private DateTime discontinuedDate; // optional
		private Company company;
		
		public ComputerBuilder id(Long id){
			this.id = id;
			return this;
		}
		
		public ComputerBuilder name(String name){
			this.name = name;
			return this;
		}
		
		public ComputerBuilder introducedDate(DateTime introducedDate){
			this.introducedDate = introducedDate;
			return this;
		}
		
		public ComputerBuilder discontinuedDate(DateTime discontinuedDate){
			this.discontinuedDate = discontinuedDate;
			return this;
		}
		
		public ComputerBuilder company(Company company){
			this.company = company;
			return this;
		}
		
		public Computer build(){
			return new Computer(this);
		}
	}

	public static ComputerBuilder builder(){
		return new ComputerBuilder();
	}
	
	

}
