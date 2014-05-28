package com.excilys.persistence.dao;

import java.sql.Connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateLogDAOImpl implements LogDAO {

//	@Autowired
//	private SessionFactory sessionFactory;
//	
//	@SuppressWarnings("unused")
//	private Session getCurrentSession() {
//        return sessionFactory.getCurrentSession();
//    }
	
	public HibernateLogDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addLog(String msg, String type, Connection c) {
		// TODO Auto-generated method stub

	}

}
