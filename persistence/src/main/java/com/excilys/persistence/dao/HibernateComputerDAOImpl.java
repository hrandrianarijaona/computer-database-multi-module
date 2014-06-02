package com.excilys.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.core.om.Company;
import com.excilys.core.om.Computer;
import com.excilys.core.om.Computer.ComputerBuilder;

@Repository
public class HibernateComputerDAOImpl implements ComputerDAO {

//	@Autowired
//	private SessionFactory sessionFactory;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@PersistenceContext(unitName = "entityManager", type = PersistenceContextType.EXTENDED) //pour indiquer à Spring qu’il doit l’injecter et qu’en plus il doit être en mode étendu, c’est à dire que sa durée de vie est celle de l’application, et non pas crée à la demande.
	private EntityManager entityManager;
	
//	@SuppressWarnings("unused")
//	private Session getCurrentSession() {
//        return sessionFactory.getCurrentSession();
//    }
	
	public HibernateComputerDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	public Computer findById(Long paramId) {
		
//		entityManager = entityManagerFactory.createEntityManager();
		
//		Computer computer = (Computer) getCurrentSession().get(Computer.class, paramId);
		Computer computer = entityManager.find(Computer.class, paramId);
//		entityManager.close();
        return computer;
	}


	@SuppressWarnings("unchecked")
	public List<Computer> retrieveAll() {
//		entityManager = entityManagerFactory.createEntityManager();
		
//		return getCurrentSession().createQuery("from computer").list();
		List<Computer> result = entityManager.createQuery("from Computer as computer").getResultList();
//		entityManager.close();
		return result;
	}

	public int count() {
//		entityManager = entityManagerFactory.createEntityManager();
		
		String query = "select count(*) from Computer";
//		return getCurrentSession().createQuery(query).getFirstResult();
		int result = ((Long) entityManager.createQuery(query).getResultList().get(0)).intValue();
		
//		entityManager.close();
		return result;
	}

	public int countWithFilter(String word) {
//		entityManager = entityManagerFactory.createEntityManager();
	
		String query = "select count(*) from Computer where name LIKE ?";
		String paramWord = new StringBuilder("%").append(word).append("%").toString();
		int result = ((Long) entityManager.createQuery(query).setParameter(1, paramWord).getResultList().get(0)).intValue();
		
//		entityManager.close();
		return result;
	}

	public Long create(Computer cp) {

		EntityManager em2 = entityManagerFactory.createEntityManager();
		EntityTransaction transac = em2.getTransaction();
		transac.begin();
		
		em2.persist(cp);
		em2.flush();
		
		transac.commit();
		em2.close();
		
	
		return cp.getId();
	}

	public void delete(Long id) {
		EntityManager em2 = entityManagerFactory.createEntityManager();
		EntityTransaction transac = em2.getTransaction();
		transac.begin();
		Computer computer = findById(id);
		if (computer != null){
//			getCurrentSession().delete(computer);
			if(!em2.isOpen()){
				em2 = entityManagerFactory.createEntityManager();
			}

//			em2.remove(computer);
			// need to check if the entity is managed by EntityManager#contains() and if not, then make it managed it EntityManager#merge().
			em2.remove(em2.contains(computer) ? computer : em2.merge(computer));
			transac.commit();
		}
		em2.close();
	}

	public void update(Computer computer) {
//		entityManager = entityManagerFactory.createEntityManager();
//		EntityTransaction transac = entityManager.getTransaction();
//		transac.begin();
//        getCurrentSession().update(computer);
        entityManager.merge(computer);
//        transac.commit();
//		entityManager.close();
	}



	@SuppressWarnings("unchecked")
	public List<Computer> retrieve(
			String word, int rang, int interval, int filter, boolean isAsc) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Computer> criteriaQuery = criteriaBuilder.createQuery(Computer.class);
		Root<Computer> root = criteriaQuery.from(Computer.class);
		root.join("company", JoinType.LEFT);
				
		
		//Search restriction
        if(word != null && !word.isEmpty()) {
            Predicate clause = criteriaBuilder.or(criteriaBuilder.like(root.<String>get("name"), word),criteriaBuilder.like(root.<String>get("company.name"), word));
            criteriaQuery.where(clause);
        }
		
        
        switch(filter){
		case 0: // by Computer's name
			if(isAsc){
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
				break;
			}
			else{
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
				break;
			}
		case 1: // by introducedDate
			if(isAsc){
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("introducedDate")));
				break;
			}
			else{
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("introducedDate")));
				break;
			}
		case 2: // by discontinuedDate
			if(isAsc){
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("discontinuedDate")));
				break;
			}
			else{
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("discontinuedDate")));
				break;
			}
		case 3: // by Company's name
			if(isAsc){
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("company.name")));
				break;
			}
			else{
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("company.name")));
				break;
			}
		default:
			if(isAsc){
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
				break;
			}
			else{
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
				break;
			}
		}
        
//		Here entity manager will create actual SQL query out of criteria query
        final TypedQuery query = entityManager.createQuery(criteriaQuery);
	
        List<Computer> tempList = query.setFirstResult(rang*interval).setMaxResults(interval).getResultList();
        List<Computer> result = new ArrayList<>();
		for (Object o : tempList) {
			result.add((Computer) o);
		}
		
		return result;

	}

}
