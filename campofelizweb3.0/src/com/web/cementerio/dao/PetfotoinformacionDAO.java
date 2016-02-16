package com.web.cementerio.dao;

import org.hibernate.Session;

import com.web.cementerio.pojo.annotations.Petfotoinformacion;

public class PetfotoinformacionDAO {

	
	public int getMaxidpetfotoinformacion(Session session)throws Exception{
		
		int maxid = 0;
				
		Object object = session.createQuery("select max(idfotoinformacion)+1 from Petfotoinformacion").uniqueResult();
		maxid = (object ==null ?1: Integer.parseInt(object.toString()));
		
		return maxid;
	}

	
	public void ingresarFotoinformacion(Session session, Petfotoinformacion petfotoinformacion)throws Exception{
		session.save(petfotoinformacion);

	}

	public void modificarFotoinformacion(Session session,  Petfotoinformacion petfotoinformacion)throws Exception{
		session.update(petfotoinformacion);
	}
	
	
}
