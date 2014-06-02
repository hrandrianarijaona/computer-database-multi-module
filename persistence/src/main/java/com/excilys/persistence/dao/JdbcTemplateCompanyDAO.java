package com.excilys.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.excilys.core.om.Company;
import com.excilys.core.om.Computer;
import com.excilys.persistence.dao.*;

@Repository
public class JdbcTemplateCompanyDAO implements CompanyDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplateCompanyDAO() {
	}

	public List<Company> retrieveAll() {
		String query = "SELECT * FROM company;";
		List<Company> companies = jdbcTemplate.query(query, 
				new RowMapper<Company>(){
					public Company mapRow(ResultSet rs, int rowNum)
							throws SQLException{
						Company company = Company.builder().build();
						company.setId(rs.getLong("id"));
						company.setName(rs.getString("name"));
						return company;
					}
				}	

		);

		return companies;
	}


	public Long insert(Company cp) {
		String query = "INSERT INTO company(name) VALUES(?);";
		jdbcTemplate.update(query, new Object[] { cp.getName() });
		return 0l;
	}



	public Company findById(Long paramId) {
		String query = "SELECT * FROM company WHERE id=?;";
		Company company ;
		
		System.out.println("paramId = " + paramId);
		
		company = jdbcTemplate.queryForObject(query,
				new RowMapper<Company>(){
			public Company mapRow(ResultSet rs, int rowNum)
					throws SQLException{
				Company company = Company.builder().build();
				company.setId(rs.getLong("id"));
				company.setName(rs.getString("name"));
				return company;
			}
		}	
				, paramId);

		return company;
	}

}
