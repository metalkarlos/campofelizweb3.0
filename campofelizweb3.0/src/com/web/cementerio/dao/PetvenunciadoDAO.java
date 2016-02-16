package com.web.cementerio.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.web.cementerio.pojo.annotations.Petvenunciado;

public class PetvenunciadoDAO {
	
	@SuppressWarnings("unchecked")
	public List<Petvenunciado> getListpetvenunciado(Session session) throws Exception{
		List<Petvenunciado> listpetenunciado=null;
		
		Criteria criteria = session.createCriteria(Petvenunciado.class);
		
		listpetenunciado =(List<Petvenunciado>)criteria.list();
	    return listpetenunciado;
	}
	
	public List<Petvenunciado> getListpetenunciadobyId(Session session, int idenunciado) throws Exception{
		List<Petvenunciado> listpetenunciado=new ArrayList<Petvenunciado>();
		Petvenunciado objectpetvenunciadopreg=null;
		Petvenunciado objectpetvenunciadoresp=null;
		
		Criteria criteriaresp = session.createCriteria(Petvenunciado.class) 
			.add(Restrictions.eq("idenunciado", idenunciado));
		objectpetvenunciadoresp = (Petvenunciado) criteriaresp.uniqueResult();
		
		if(objectpetvenunciadoresp!=null){
		 if(objectpetvenunciadoresp.getIdpadre() >0){
			Criteria criteriapreg = session.createCriteria(Petvenunciado.class) 
		    		.add(Restrictions.eq("idenunciado", objectpetvenunciadoresp.getIdpadre()));
			objectpetvenunciadopreg = (Petvenunciado) criteriapreg.uniqueResult();
   		  }
		}
		if(objectpetvenunciadopreg !=null && objectpetvenunciadoresp != null){
			listpetenunciado.add(0,objectpetvenunciadopreg);
			listpetenunciado.add(1,objectpetvenunciadoresp);
		}
		return listpetenunciado;
	}

}
