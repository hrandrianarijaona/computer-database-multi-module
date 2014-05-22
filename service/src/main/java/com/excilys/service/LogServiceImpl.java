package com.excilys.service;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.persistence.dao.LogDAOImpl;

@Service
@Transactional
public class LogServiceImpl implements LogService {
	
	private Logger log = null;
	@Autowired
	private LogDAOImpl logDAO;
	
	@Autowired
	public LogServiceImpl(LogDAOImpl logDAO) {
		// TODO Auto-generated constructor stub
		log = LoggerFactory.getLogger(this.getClass());
		this.logDAO = logDAO;
	}

	/**
	 * Ajoute le message de log dans la base de donnée
	 * @param msg le message
	 * @param type le type de log
	 */
	@Transactional(readOnly=false)
	public void addLog(String msg, TypeLog type, Connection connection) {
		
		String sType = null;
		switch(type){
		case INFOS:
			sType = "Infos"; break;
		case ERROR:
			sType = "Erreur"; break;
		case DEBUG:
			sType = "Debug"; break;
		case TRACE:
			sType = "Trace"; break;
		case WARN:
			sType = "Warn"; break;
		default:
			sType = "Default";
		}
		log.info("addLog... " + connection);
		logDAO.addLog(msg, sType, connection);
	}

}
