package com.web.cementerio.bo;
import java.util.List;

import org.hibernate.Session;

import com.web.cementerio.dao.PetespecieDAO;
import com.web.cementerio.pojo.annotations.Petespecie;
import com.web.util.HibernateUtil;

public class PetespecieBO {
	PetespecieDAO petespecieDAO;
	
	public PetespecieBO(){
		petespecieDAO = new PetespecieDAO();
	}
	
	public List<Petespecie>Listpetespecie(int estado)throws Exception{
		List<Petespecie> listpetespecie=null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			listpetespecie = (List<Petespecie>) petespecieDAO.getListpetespecie(session, estado);
			
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			 session.close();
		}
		
		return listpetespecie; 
	}
	

	public PetespecieDAO getPetespecieDAO() {
		return petespecieDAO;
	}

	public void setPetespecieDAO(PetespecieDAO petespecieDAO) {
		this.petespecieDAO = petespecieDAO;
	}

}
