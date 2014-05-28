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
import com.excilys.core.om.Computer.ComputerBuilder;

@Repository
public class HibernateComputerDAOImpl implements ComputerDAO {

//	@Autowired
//	private SessionFactory sessionFactory;
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED) //pour indiquer à Spring qu’il doit l’injecter et qu’en plus il doit être en mode étendu, c’est à dire que sa durée de vie est celle de l’application, et non pas crée à la demande.
	private EntityManager entityManager;
	
//	@SuppressWarnings("unused")
//	private Session getCurrentSession() {
//        return sessionFactory.getCurrentSession();
//    }
	
	public HibernateComputerDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	public Computer findComputerById(Long paramId) {
//		Computer computer = (Computer) getCurrentSession().get(Computer.class, paramId);
		Computer computer = entityManager.find(Computer.class, paramId);
        return computer;
	}


	@SuppressWarnings("unchecked")
	public List<Computer> getListComputers() {
//		return getCurrentSession().createQuery("from computer").list();
		return entityManager.createQuery("from Computer as computer").getResultList();
	}

	public int getNbComputer() {
		String query = "select count(*) from Computer";
//		return getCurrentSession().createQuery(query).getFirstResult();
		return ((Long) entityManager.createQuery(query).getResultList().get(0)).intValue();
	}

	public int getNbComputerFilter(String word) {
		String query = "select count(*) from Computer where name LIKE ?";
		String paramWord = new StringBuilder("%").append(word).append("%").toString();
		return ((Long) entityManager.createQuery(query).setParameter(1, paramWord).getResultList().get(0)).intValue();
	}

	public Long insertComputer(Computer cp) {
//		getCurrentSession().save(cp);

		
		System.out.println("avant: " + cp);
		entityManager.persist(cp);
		System.out.println("apres: " + cp);
		
//		String query = "insert into Computer(name, introduced)"
		
		
		
//		String query = "select count(*) from Computer where name LIKE ?";
		
		
		return cp.getId();
	}

	public void deleteComputer(Long id) {
		Computer computer = findComputerById(id);
		if (computer != null)
//			getCurrentSession().delete(computer);
			entityManager.remove(computer);
	}

	public void updateComputer(Computer computer) {
//        getCurrentSession().update(computer);
        entityManager.merge(computer);
	}

	@SuppressWarnings("unchecked")
	public List<Computer> searchComputersByFilteringAndOrdering(String word,
			int filter, boolean isAsc) {
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
		return result;
		
	}


	@SuppressWarnings("unchecked")
	public List<Computer> searchComputersByFilteringAndOrderingWithRange(
			String word, int rang, int interval, int filter, boolean isAsc) {
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
		// requete de recherche du pattern
		String query = "select pc, co from Computer as pc left outer join fetch pc.company as co where pc.name LIKE ? or co.name LIKE ? ORDER BY " + sFilter;
		String paramWord = new StringBuilder("%").append(word).append("%").toString();

		List <Computer> tempList = entityManager.createQuery(query).setParameter(1, paramWord).setParameter(2, paramWord).setFirstResult(rang*interval).setMaxResults(interval).getResultList();
		
		List<Computer> result = new ArrayList<>();
		for (Object o : tempList) {
			result.add((Computer) o);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	public List<Computer> getListComputersByFilteringAndOrdering(int filter,
			boolean isAsc) {
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
		return result;
	
	}

	
	@SuppressWarnings("unchecked")
	public List<Computer> getListComputersByFilteringAndOrderingWithRange(
			int rang, int interval, int filter, boolean isAsc) {
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
		return result;
	
	}

}
