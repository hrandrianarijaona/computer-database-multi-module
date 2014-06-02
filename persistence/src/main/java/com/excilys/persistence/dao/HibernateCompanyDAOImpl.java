package com.excilys.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.core.om.Company;
import com.excilys.core.om.Computer;

@Repository
public class HibernateCompanyDAOImpl implements CompanyDAO {

//	@Autowired
//	private SessionFactory sessionFactory;
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED) //pour indiquer à Spring qu’il doit l’injecter et qu’en plus il doit être en mode étendu, c’est à dire que sa durée de vie est celle de l’application, et non pas crée à la demande.
	private EntityManager entityManager;
	
	public HibernateCompanyDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<Company> retrieveAll() {
		List<Company> tempList = entityManager.createQuery("from Company").getResultList();
		List<Company> result = new ArrayList<>();
		for (Object o : tempList) {
			result.add((Company) o);
		}
		return result;
	}

	public Long insert(Company cp) {
		//getCurrentSession().save(cp);
		entityManager.merge(cp);
		return 0l;
	}

	public Company findById(Long paramId) {
		Company company = entityManager.find(Company.class, paramId);
        return company;
	}

}
