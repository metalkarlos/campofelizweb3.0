package com.web.cementerio.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.web.cementerio.pojo.annotations.Petenunciado;

public class PetenunciadoDAO {
	
	public int getMaxidenunciado(Session session)throws Exception{
		
		int maxid=0;
		
		Object object = session.createQuery("select max(idenunciado)+1 from Petenunciado ").uniqueResult();
		maxid = (object ==null ?1: Integer.parseInt(object.toString()));
		
		return maxid;
		
	}
	
	public int maxOrden(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select count(e.orden) as cant from Petenunciado as e where e.setestado.idestado = 1").uniqueResult();
		max = (object==null?0:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	public Petenunciado getPetenunciadobyId(Session session, int idestado)throws Exception{
		Petenunciado petenunciado=null;
		Criteria criteria =session.createCriteria(Petenunciado.class)
				.add(Restrictions.eq("setestado.idestado", idestado));
				
		petenunciado = (Petenunciado) criteria.uniqueResult();
		return petenunciado;
		
	}
	
	public void grabar(Session session,Petenunciado petenunciado) throws Exception{
		session.save(petenunciado);
		
	}

	public void modificar(Session session, Petenunciado petenunciado) throws Exception{
		session.update(petenunciado);
	}
}
