package com.web.cementerio.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import com.web.cementerio.pojo.annotations.Petfotomascota;


public class PetfotomascotaDAO {
	
	
	public int getMaxidpetfotomascota(Session session)throws Exception{
		
		int maxid = 0;
				
		Object object = session.createQuery("select max(idfotomascota)+1 from Petfotomascota ").uniqueResult();
		maxid = (object ==null ?1: Integer.parseInt(object.toString()));
		return maxid;
	}

	
	public int getCantFotosPorMascota(Session session, int idmascota) throws Exception{
		int count = 0;
		
		String hql = " select count(idfotomascota)+1 as cantidad ";
		hql += " from Petfotomascota  fm ";
		hql += " where fm.petmascotahomenaje.idmascota = :idmascota ";
		
		Query query = session.createQuery(hql)
				.setInteger("idmascota", idmascota);
		
		Object object = query.uniqueResult();                 
		count = (object==null ?1: Integer.parseInt(object.toString()));
		return count;
	}
	
	public void ingresarFotomascota(Session session, Petfotomascota petfotomascota)throws Exception{
		session.save(petfotomascota);

	}

	public void modificarFotomascota(Session session, Petfotomascota petfotomascota)throws Exception{
		session.update(petfotomascota);
	}
	
	
}
