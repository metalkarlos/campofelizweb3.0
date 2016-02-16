package com.web.cementerio.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import com.web.cementerio.pojo.annotations.Petfotoguia;


public class PetfotoguiaDAO {

	public PetfotoguiaDAO(){
		
	}
	
	public int maxIdPetfotoguia(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select max(idfotoguia)+1 as max from Petfotoguia ").uniqueResult();
		max = (object==null?1:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	public int cantFotosPorGuia(Session session, int idguia) throws Exception {
		int count=0;
		
		String hql = " select count(idfotoguia)+1 as cantidad ";
		hql += " from Petfotoguia as n ";
		hql += " where n.petguia.idguia = :idguia ";
		
		Query query = session.createQuery(hql)
				.setInteger("idguia", idguia);
		
		Object object = query.uniqueResult();
		count = (object==null?0:Integer.parseInt(object.toString()));
		
		return count;
	}
	
	public void savePetfotoguia(Session session, Petfotoguia petfotoguia )throws Exception {
		session.save(petfotoguia);
	}
	
	public void modificarPetfotoguia(Session session,  Petfotoguia petfotoguia ) throws Exception {
		session.update(petfotoguia);
	}
}
