package com.web.cementerio.bo;

import java.util.List;

import org.hibernate.Session;

import com.web.cementerio.dao.CotempresaDAO;
import com.web.cementerio.pojo.annotations.Cotempresa;
import com.web.util.HibernateUtil;

public class CotempresaBO {

	public List<Cotempresa> lisCotempresa() throws Exception{
		List<Cotempresa> lisCotempresa = null;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			CotempresaDAO cotempresaDAO = new CotempresaDAO();
			lisCotempresa = (List<Cotempresa>) cotempresaDAO.lisCotempresa(session);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			 session.close();
		}
		
		return lisCotempresa; 
	}
	
}
