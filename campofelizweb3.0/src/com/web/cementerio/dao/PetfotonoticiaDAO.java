package com.web.cementerio.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import com.web.cementerio.pojo.annotations.Petfotonoticia;

public class PetfotonoticiaDAO {

	public PetfotonoticiaDAO() {
	}
	
	public int maxIdfotonoticia(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select max(idfotonoticia) as max from Petfotonoticia ").uniqueResult();
		max = (object==null?0:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	public int cantFotosPorNoticia(Session session, int idnoticia) throws Exception {
		int count=0;
		
		String hql = " select count(idfotonoticia) as cantidad ";
		hql += " from Petfotonoticia as n ";
		hql += " where n.petnoticia.idnoticia = :idnoticia ";
		
		Query query = session.createQuery(hql)
				.setInteger("idnoticia", idnoticia);
		
		Object object = query.uniqueResult();
		count = (object==null?0:Integer.parseInt(object.toString()));
		
		return count;
	}
	
	public void savePetfotonoticia(Session session, Petfotonoticia petfotonoticia) throws Exception {
		session.save(petfotonoticia);
	}
	
	public void updatePetfotonoticia(Session session, Petfotonoticia petfotonoticia) throws Exception {
		session.update(petfotonoticia);
	}

}
