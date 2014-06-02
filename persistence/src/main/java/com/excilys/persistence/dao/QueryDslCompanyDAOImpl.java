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

import com.excilys.core.om.Company;
import com.excilys.core.om.QCompany;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository
public class QueryDslCompanyDAOImpl implements CompanyDAO {

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@PersistenceContext(type = PersistenceContextType.TRANSACTION) // pour indiquer à Spring qu’il doit l’injecter et qu’en plus il doit être en mode étendu, c’est à dire que sa durée de vie est celle de l’application, et non pas crée à la demande.
	private EntityManager entityManager;
	
	public QueryDslCompanyDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Company> retrieveAll() {
		QCompany qCompany = QCompany.company;
		// where entityManager is a JPA EntityManager   
		JPQLQuery query = new JPAQuery (entityManager); 
		List<Company> tempList = query.from(qCompany).list(qCompany);
		List<Company> result = new ArrayList<>();
		for (Object o : tempList) {
			result.add((Company) o);
		}
		return result;
	}

	@Override
	public Long insert(Company cp) {
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
	public Company findById(Long paramId) {
		QCompany qCompany = QCompany.company;
		JPQLQuery query = new JPAQuery (entityManager); 
		Company company = query.from(qCompany)
		  .where(qCompany.id.eq(paramId))
		  .uniqueResult(qCompany);

        return company;
	}

}
