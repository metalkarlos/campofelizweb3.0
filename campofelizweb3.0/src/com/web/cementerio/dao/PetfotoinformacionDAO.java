package com.web.cementerio.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import com.web.cementerio.pojo.annotations.Petfotoinformacion;

public class PetfotoinformacionDAO {

	
	public int getMaxidpetfotoinformacion(Session session)throws Exception{
		
		int maxid = 0;
				
		Object object = session.createQuery("select max(idfotoinformacion) from Petfotoinformacion").uniqueResult();
		maxid = (object ==null ?1: Integer.parseInt(object.toString()));
		
		return maxid;
	}

	public int cantFotosPorInformacion(Session session, int idinformacion) throws Exception {
		int count=0;
		
		String hql = " select count(idfotoinformacion) as cantidad ";
		hql += " from Petfotoinformacion as n ";
		hql += " where n.petinformacion.idinformacion = :idinformacion ";
		
		Query query = session.createQuery(hql)
				.setInteger("idinformacion", idinformacion);
		
		Object object = query.uniqueResult();
		count = (object==null?0:Integer.parseInt(object.toString()));
		
		return count;
	}
	
	public void ingresarFotoinformacion(Session session, Petfotoinformacion petfotoinformacion)throws Exception{
		session.save(petfotoinformacion);

	}

	public void modificarFotoinformacion(Session session,  Petfotoinformacion petfotoinformacion)throws Exception{
		session.update(petfotoinformacion);
	}
	
	
}
