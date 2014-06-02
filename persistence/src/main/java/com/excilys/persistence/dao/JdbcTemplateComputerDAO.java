package com.excilys.persistence.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.excilys.core.om.Company;
import com.excilys.core.om.Computer;

@Repository
public class JdbcTemplateComputerDAO implements ComputerDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplateComputerDAO() {
		// TODO Auto-generated constructor stub
	}


	public List<Computer> retrieveAll() {
		
		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id ORDER BY pc.name;";
		List<Computer> computers = jdbcTemplate.query(query, 
				new RowMapper<Computer>(){
			public Computer mapRow(ResultSet rs, int rowNum)
					throws SQLException{
				Computer computer = Computer.builder().build();
				computer.setId(rs.getLong("pc.id"));
				computer.setName(rs.getString("pc.name"));
				computer.setIntroducedDate(new DateTime(rs.getDate("pc.introduced")));
				computer.setDiscontinuedDate(new DateTime(rs.getDate("pc.discontinued")));
				computer.setCompany(Company.builder().id(rs.getLong("comp.id")).name(rs.getString("comp.name")).build());
				return computer;
			}
		}

				);

		return computers;
	}


	public int count() {
		String query = "SELECT COUNT(*) AS nb FROM computer;";
		int nb = jdbcTemplate.queryForObject(query, Integer.class);
		System.out.println("Il y a " + nb + "computeure");
		return nb;
	}


	public int countWithFilter(String filter) {
		int size=0;
		JdbcTemplate select = this.jdbcTemplate;
		size=select.queryForObject("SELECT COUNT(*) FROM computer WHERE name LIKE ?",new Object[]{new StringBuilder("%").append(filter).append("%")},Integer.class);

		return size;
	}



	private void writeComputer(PreparedStatement ps, Computer cp) throws SQLException {
		ps.setString(1, cp.getName());
		if (cp.getIntroducedDate() != null)
			ps.setDate(2, new java.sql.Date(cp.getIntroducedDate().getMillis()));
		else
			ps.setNull(2, Types.NULL);
		if (cp.getDiscontinuedDate() != null)
			ps.setDate(3, new java.sql.Date(cp.getDiscontinuedDate().getMillis()));
		else
			ps.setNull(3, Types.NULL);
		if (cp.getCompany() != null)
			ps.setLong(4, cp.getCompany().getId());
		else
			ps.setNull(4, Types.NULL);
	}

	/*
	 * Modifiers
	 */

	public Long create(final Computer entity) {

		final String query = "insert into computer (name,introduced, discontinued, company_id) values (?,?,?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				writeComputer(ps, entity);
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}


	public void delete(Long id) {
		String query = "DELETE FROM computer WHERE id=?;";
		jdbcTemplate.update(query, id);
	}

	public List<Computer> retrieve(String word, int rang, int interval, int filter, boolean isAsc) {

		String sFilter;
		switch(filter){
		case 0: // par nom de Computer
			sFilter = "pc.name"; break;
		case 1: // par introducedDate
			sFilter = "pc.introduced"; break;
		case 2: // par discontinuedDate
			sFilter = "pc.discontinued"; break;
		case 3: // par nom de Company
			sFilter = "comp.name"; break;
		default:
			sFilter = "pc.name"; break;
		}

		if(!isAsc)
			sFilter = sFilter + " DESC";

		sFilter = sFilter + ", pc.name ASC ";
		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id WHERE pc.name LIKE ? ORDER BY " + sFilter + " LIMIT ?, ?;";

		List<Computer> computers = jdbcTemplate.query(query, 
				new RowMapper<Computer>(){
			public Computer mapRow(ResultSet rs, int rowNum)
					throws SQLException{
				Computer computer = Computer.builder().build();
				computer.setId(rs.getLong("pc.id"));
				computer.setName(rs.getString("pc.name"));
				computer.setIntroducedDate(new DateTime(rs.getDate("pc.introduced")));
				computer.setDiscontinuedDate(new DateTime(rs.getDate("pc.discontinued")));
				computer.setCompany(Company.builder().id(rs.getLong("comp.id")).name(rs.getString("comp.name")).build());
				return computer;
			}
		}, new Object[] {new StringBuilder("%").append(word).append("%").toString(), rang*interval, interval}

				);

		return computers;
	}


	public Computer findById(Long paramId) {

		String query = "SELECT pc.id, pc.name, pc.introduced, pc.discontinued, comp.id, comp.name FROM computer AS pc LEFT JOIN company AS comp ON pc.company_id=comp.id WHERE pc.id=?;";
		//		Computer computer = (Computer) jdbcTemplate.queryForObject(query, new Object[] { paramId }, new BeanPropertyRowMapper<Computer>(Computer.class));
		Computer computer = (Computer) jdbcTemplate.queryForObject(query, 
				new RowMapper<Computer>(){
			public Computer mapRow(ResultSet rs, int rowNum)
					throws SQLException{
				Computer computer = Computer.builder().build();
				computer.setId(rs.getLong("pc.id"));
				computer.setName(rs.getString("pc.name"));
				computer.setIntroducedDate(new DateTime(rs.getDate("pc.introduced")));
				computer.setDiscontinuedDate(new DateTime(rs.getDate("pc.discontinued")));
				computer.setCompany(Company.builder().id(rs.getLong("comp.id")).name(rs.getString("comp.name")).build());
				return computer;
			}
		}

		, paramId);

		return computer;
	}


	public void update(final Computer computer) {
		final String query = "update computer set name= ?, introduced = ?, discontinued = ?, company_id = ? where id = ?";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
		public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		writeComputer(ps, computer);
		ps.setLong(5, computer.getId());
		return ps;
		}
		}, keyHolder);

	}

}
