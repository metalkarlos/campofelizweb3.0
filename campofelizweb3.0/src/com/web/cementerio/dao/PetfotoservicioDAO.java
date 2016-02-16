package com.web.cementerio.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import com.web.cementerio.pojo.annotations.Petfotoservicio;

public class PetfotoservicioDAO {

	public PetfotoservicioDAO() {
	}
	
	public int maxIdfotoservicio(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select max(idfotoservicio) as max from Petfotoservicio ").uniqueResult();
		max = (object==null?0:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	public int cantFotosPorServicio(Session session, int idservicio) throws Exception {
		int count=0;
		
		String hql = " select count(idfotoservicio) as cantidad ";
		hql += " from Petfotoservicio as n ";
		hql += " where n.petservicio.idservicio = :idservicio ";
		
		Query query = session.createQuery(hql)
				.setInteger("idservicio", idservicio);
		
		Object object = query.uniqueResult();
		count = (object==null?0:Integer.parseInt(object.toString()));
		
		return count;
	}
	
	public void savePetfotoservicio(Session session, Petfotoservicio petfotoservicio) throws Exception {
		session.save(petfotoservicio);
	}
	
	public void updatePetfotoservicio(Session session, Petfotoservicio petfotoservicio) throws Exception {
		session.update(petfotoservicio);
	}

}
