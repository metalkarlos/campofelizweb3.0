package com.web.cementerio.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.web.cementerio.pojo.annotations.Petespecie;

public class PetespecieDAO {
	
	@SuppressWarnings("unchecked")
	public  List<Petespecie> getListpetespecie(Session session, int estado){
		List<Petespecie> listpetespecie =null;
		
		String hql = " from Petespecie ";
		       hql+= " where setestado.idestado = :idestado" ;
		       
		Query query = session.createQuery(hql);     
		      query.setInteger("idestado", estado);
		      listpetespecie = (List<Petespecie>)query.list();
		      
		return listpetespecie;
	}

}
