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

	public Computer findComputerById(Long paramId) {
		
//		entityManager = entityManagerFactory.createEntityManager();
		
//		Computer computer = (Computer) getCurrentSession().get(Computer.class, paramId);
		Computer computer = entityManager.find(Computer.class, paramId);
//		entityManager.close();
        return computer;
	}


	@SuppressWarnings("unchecked")
	public List<Computer> getListComputers() {
//		entityManager = entityManagerFactory.createEntityManager();
		
//		return getCurrentSession().createQuery("from computer").list();
		List<Computer> result = entityManager.createQuery("from Computer as computer").getResultList();
//		entityManager.close();
		return result;
	}

	public int getNbComputer() {
//		entityManager = entityManagerFactory.createEntityManager();
		
		String query = "select count(*) from Computer";
//		return getCurrentSession().createQuery(query).getFirstResult();
		int result = ((Long) entityManager.createQuery(query).getResultList().get(0)).intValue();
		
//		entityManager.close();
		return result;
	}

	public int getNbComputerFilter(String word) {
//		entityManager = entityManagerFactory.createEntityManager();
	
		String query = "select count(*) from Computer where name LIKE ?";
		String paramWord = new StringBuilder("%").append(word).append("%").toString();
		int result = ((Long) entityManager.createQuery(query).setParameter(1, paramWord).getResultList().get(0)).intValue();
		
//		entityManager.close();
		return result;
	}

	public Long insertComputer(Computer cp) {

		EntityManager em2 = entityManagerFactory.createEntityManager();
		EntityTransaction transac = em2.getTransaction();
		transac.begin();
		
		System.out.println("avant: " + cp);
		em2.persist(cp);
		em2.flush();
		
		transac.commit();
		em2.close();
		System.out.println("apres: " + cp);
		
	
		return cp.getId();
	}

	public void deleteComputer(Long id) {
		EntityManager em2 = entityManagerFactory.createEntityManager();
		EntityTransaction transac = em2.getTransaction();
		transac.begin();
		Computer computer = findComputerById(id);
		if (computer != null){
			System.out.println("Computer trouvé: " + computer);
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

	public void updateComputer(Computer computer) {
//		entityManager = entityManagerFactory.createEntityManager();
//		EntityTransaction transac = entityManager.getTransaction();
//		transac.begin();
//        getCurrentSession().update(computer);
        entityManager.merge(computer);
//        transac.commit();
//		entityManager.close();
	}

	@SuppressWarnings("unchecked")
	public List<Computer> searchComputersByFilteringAndOrdering(String word,
			int filter, boolean isAsc) {
//		entityManager = entityManagerFactory.createEntityManager();
		
		String sFilter;
		switch(filter){
		case 0: // par nom de Computer
			sFilter = "pc.name"; break;
		case 1: // par introducedDate
			sFilter = "pc.introducedDate"; break;
		case 2: // par discontinuedDate
			sFilter = "pc.discontinuedDate"; break;
		case 3: // par nom de Company
			sFilter = "co.name"; break;
		default:
			sFilter = "pc.name"; break;
		}

		String query;

		// requete de recherche du pattern
		if(isAsc)
			query = "select pc, co from Computer as pc left outer join fetch pc.company as co where pc.name LIKE ? or co.name LIKE ? ORDER BY " + sFilter;
		else
			query = "select pc, co from Computer as pc left outer join fetch pc.company as co where pc.name LIKE ? or co.name LIKE ? ORDER BY " + sFilter + " DESC";

		
//		String paramWord = new StringBuilder("%").append(word).append("%").toString();
		String paramWord = new StringBuilder("%").append(word).append("%").toString();
		
		System.out.println("La query: " + query);
		System.out.println("Le mot: " + word);
		List<Computer> tempList = entityManager.createQuery(query).setParameter(1, paramWord).setParameter(2, paramWord).getResultList();
	
		List<Computer> result = new ArrayList<>();
		for (Object o : tempList) {
			result.add((Computer) o);
		}
		
//		entityManager.close();
		return result;
		
	}


	@SuppressWarnings("unchecked")
	public List<Computer> searchComputersByFilteringAndOrderingWithRange(
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
		case 0: // par nom de Computer
			if(isAsc){
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
				break;
			}
			else{
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
				break;
			}
		case 1: // par introducedDate
			if(isAsc){
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("introducedDate")));
				break;
			}
			else{
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("introducedDate")));
				break;
			}
		case 2: // par discontinuedDate
			if(isAsc){
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("discontinuedDate")));
				break;
			}
			else{
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("discontinuedDate")));
				break;
			}
		case 3: // par nom de Company
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
        
        
		
//		criteriaQuery.select(root);
		
//		-----------------------------------------------------------------------------------------------
		
//		String sFilter;
//		switch(filter){
//		case 0: // par nom de Computer
//			sFilter = "pc.name"; break;
//		case 1: // par introducedDate
//			sFilter = "pc.introducedDate"; break;
//		case 2: // par discontinuedDate
//			sFilter = "pc.discontinuedDate"; break;
//		case 3: // par nom de Company
//			sFilter = "co.name"; break;
//		default:
//			sFilter = "pc.name"; break;
//		}
//
//		if(!isAsc)
//			sFilter = sFilter + " DESC";
//
//		sFilter = sFilter + ", pc.name ASC ";
//		// requete de recherche du pattern
//		String query = "select pc, co from Computer as pc left outer join fetch pc.company as co where pc.name LIKE ? or co.name LIKE ? ORDER BY " + sFilter;
//		String paramWord = new StringBuilder("%").append(word).append("%").toString();
//
//		List <Computer> tempList = entityManager.createQuery(query).setParameter(1, paramWord).setParameter(2, paramWord).setFirstResult(rang*interval).setMaxResults(interval).getResultList();
//		
		
        List<Computer> tempList = query.setFirstResult(rang*interval).setMaxResults(interval).getResultList();
        List<Computer> result = new ArrayList<>();
		for (Object o : tempList) {
			result.add((Computer) o);
		}
		
		return result;

	}

	@SuppressWarnings("unchecked")
	public List<Computer> getListComputersByFilteringAndOrdering(int filter,
			boolean isAsc) {
//		entityManager = entityManagerFactory.createEntityManager();
		
		String sFilter;
		switch(filter){
		case 0: // par nom de Computer
			sFilter = "pc.name"; break;
		case 1: // par introducedDate
			sFilter = "pc.introducedDate"; break;
		case 2: // par discontinuedDate
			sFilter = "pc.discontinuedDate"; break;
		case 3: // par nom de Company
			sFilter = "co.name"; break;
		default:
			sFilter = "pc.name"; break;
		}

		if(!isAsc)
			sFilter = sFilter + " DESC";
		
		sFilter = sFilter + ", pc.name ASC";

		// ajoutez ici le code de r�cup�ration des produits
		String query = "select pc, co from Computer as pc left outer join pc.company as co ORDER BY " + sFilter;
		List<Computer> tempList = entityManager.createQuery(query).getResultList();
	
		List<Computer> result = new ArrayList<>();
		for (Object o : tempList) {
			result.add((Computer) o);
		}
		
//		entityManager.close();
		return result;
	
	}

	
	@SuppressWarnings("unchecked")
	public List<Computer> getListComputersByFilteringAndOrderingWithRange(
			int rang, int interval, int filter, boolean isAsc) {
//		entityManager = entityManagerFactory.createEntityManager();
		
		String sFilter;
		switch(filter){
		case 0: // par nom de Computer
			sFilter = "pc.name"; break;
		case 1: // par introducedDate
			sFilter = "pc.introducedDate"; break;
		case 2: // par discontinuedDate
			sFilter = "pc.discontinuedDate"; break;
		case 3: // par nom de Company
			sFilter = "co.name"; break;
		default:
			sFilter = "pc.name"; break;
		}

		if(!isAsc)
			sFilter = sFilter + " DESC";
		
		sFilter = sFilter + ", pc.name ASC ";
		
		// ajoutez ici le code de r�cup�ration des produits
		String query = "select pc, co from Computer as pc left outer join pc.company as co ORDER BY " + sFilter;
		List<Computer> tempList = entityManager.createQuery(query).setFirstResult(rang*interval).setMaxResults(interval).getResultList();
	
		List<Computer> result = new ArrayList<>();
		for (Object o : tempList) {
			result.add((Computer) o);
		}
		
//		entityManager.close();
		return result;
	
	}

}
