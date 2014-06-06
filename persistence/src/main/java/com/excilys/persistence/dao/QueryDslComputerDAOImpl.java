package com.excilys.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.core.om.Computer;
import com.excilys.core.om.QCompany;
import com.excilys.core.om.QComputer;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

@Repository
public class QueryDslComputerDAOImpl implements ComputerDAO {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION) // pour indiquer à Spring qu’il doit l’injecter et qu’en plus il doit être en mode étendu, c’est à dire que sa durée de vie est celle de l’application, et non pas crée à la demande.
	private EntityManager entityManager;

	public QueryDslComputerDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Computer findById(Long paramId) {
		QComputer qComputer = QComputer.computer;
		JPQLQuery query = new JPAQuery (entityManager); 
		Computer computer = query.from(qComputer)
				.where(qComputer.id.eq(paramId))
				.uniqueResult(qComputer);
		return computer;
	}

	@Override
	public List<Computer> retrieveAll() {
		QComputer qComputer = QComputer.computer;
		// where entityManager is a JPA EntityManager   
		JPQLQuery query = new JPAQuery (entityManager); 
		List<Computer> tempList = query.from(qComputer).list(qComputer);
		List<Computer> result = new ArrayList<>();
		for (Object o : tempList) {
			result.add((Computer) o);
		}
		return result;
	}

	@Override
	public int count() {
		QComputer qComputer = QComputer.computer;
		JPQLQuery query = new JPAQuery (entityManager); 
		return (int)query.from(qComputer).count();
	}

	@Override
	public int countWithFilter(String filter) {
		QComputer qComputer = QComputer.computer;
		JPQLQuery query = new JPAQuery (entityManager); 
		return (int)query.from(qComputer).where(qComputer.name.like(new StringBuilder("%").append(filter).append("%").toString())).count();
	}

	@Override
	public Long create(Computer cp) {
		//Querydsl supports only the DML clauses JPQL supports: UPDATE and DELETE
		//There is no JPQL equivalent to define insert

		EntityManager em2 = entityManagerFactory.createEntityManager();
		EntityTransaction transac = em2.getTransaction();
		transac.begin();

		em2.persist(cp);
		em2.flush();

		transac.commit();
		em2.close();

		return cp.getId();
	}

	@Override
	public void delete(Long id) {
//		EntityManager em2 = entityManagerFactory.createEntityManager();
//		EntityTransaction transac = em2.getTransaction();
//		transac.begin();
//		QComputer qComputer = QComputer.computer;
//		new JPADeleteClause(em2, qComputer).where(qComputer.id.eq(id)).execute();
//		transac.commit();
//		em2.close();
		
		QComputer qComputer = QComputer.computer;
		new JPADeleteClause(entityManager, qComputer).where(qComputer.id.eq(id)).execute();
		
		
	}

	@Override
	public void update(Computer computer) {
		EntityManager em2 = entityManagerFactory.createEntityManager();
		EntityTransaction transac = em2.getTransaction();
		transac.begin();
		System.out.println(computer);
		em2.merge(computer);
		
		transac.commit();
		em2.close();
	}

	@Override
	public List<Computer> retrieve(String word, int rang, int interval,
			int filter, boolean isAsc) {
		QComputer qComputer = QComputer.computer;
		QCompany company = QCompany.company;
		JPQLQuery query = new JPAQuery (entityManager);
		List <Computer> tempList = null;
		String paramWord = new StringBuilder("%").append(word).append("%").toString();
		switch(filter){
		case 0: // by Computer's name
			if(isAsc){
				tempList = query.from(qComputer).leftJoin(qComputer.company, company).where( qComputer.name.like(paramWord).or(company.name.like(paramWord)) ).orderBy(qComputer.name.asc()).offset(rang*interval).limit(interval).list(qComputer);
				break;
			}
			else{
				tempList = query.from(qComputer).leftJoin(qComputer.company, company).where( qComputer.name.like(paramWord).or(company.name.like(paramWord)) ).orderBy(qComputer.name.desc()).offset(rang*interval).limit(interval).list(qComputer);
				break;
			}
		case 1: // by introducedDate
			if(isAsc){
				tempList = query.from(qComputer).leftJoin(qComputer.company, company).where( qComputer.name.like(paramWord).or(company.name.like(paramWord)) ).orderBy(qComputer.introducedDate.asc()).offset(rang*interval).limit(interval).list(qComputer);
				break;
			}
			else{
				tempList = query.from(qComputer).leftJoin(qComputer.company, company).where( qComputer.name.like(paramWord).or(company.name.like(paramWord)) ).orderBy(qComputer.introducedDate.desc()).offset(rang*interval).limit(interval).list(qComputer);
				break;
			}
		case 2: // by discontinuedDate
			if(isAsc){
				tempList = query.from(qComputer).leftJoin(qComputer.company, company).where( qComputer.name.like(paramWord).or(company.name.like(paramWord)) ).orderBy(qComputer.discontinuedDate.asc()).offset(rang*interval).limit(interval).list(qComputer);
				break;
			}
			else{
				tempList = query.from(qComputer).leftJoin(qComputer.company, company).where( qComputer.name.like(paramWord).or(company.name.like(paramWord)) ).orderBy(qComputer.discontinuedDate.desc()).offset(rang*interval).limit(interval).list(qComputer);
				break;
			}
		case 3: //by Company's name
			if(isAsc){
				tempList = query.from(qComputer).leftJoin(qComputer.company, company).where( qComputer.name.like(paramWord).or(company.name.like(paramWord)) ).orderBy(company.name.asc()).offset(rang*interval).limit(interval).list(qComputer);
				break;
			}
			else{
				tempList = query.from(qComputer).leftJoin(qComputer.company, company).where( qComputer.name.like(paramWord).or(company.name.like(paramWord)) ).orderBy(company.name.desc()).offset(rang*interval).limit(interval).list(qComputer);
				break;
			}
		default:
			if(isAsc){
				tempList = query.from(qComputer).leftJoin(qComputer.company, company).where( qComputer.name.like(paramWord).or(company.name.like(paramWord)) ).orderBy(qComputer.name.asc()).offset(rang*interval).limit(interval).list(qComputer);
				break;
			}
			else{
				tempList = query.from(qComputer).leftJoin(qComputer.company, company).where( qComputer.name.like(paramWord).or(company.name.like(paramWord)) ).orderBy(qComputer.name.desc()).offset(rang*interval).limit(interval).list(qComputer);
				break;
			}
		}

        List<Computer> result = new ArrayList<>();
		for (Object o : tempList) {
			result.add((Computer) o);
		}
		
		return result;
	}

}
