package com.web.cementerio.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.web.cementerio.pojo.annotations.Cotempresa;

public class CotempresaDAO {

	public int getMaxId(Session session)throws Exception{
		int maxid = 0;
				
		Object object = session.createQuery("select max(idempresa)+1 from Cotempresa ").uniqueResult();
		maxid = (object ==null ?1: Integer.parseInt(object.toString()));
		
		return maxid;
	}
	
	@SuppressWarnings("unchecked")
	public List<Cotempresa> lisCotempresa(Session session)throws Exception{
		List<Cotempresa> lisCotempresa = null;
		
		Criteria criteria = session.createCriteria(Cotempresa.class) 
				.add(Restrictions.eq("setestado.idestado", 1))
				.addOrder(Order.asc("nombre"));
		
		lisCotempresa =(List<Cotempresa>)criteria.list();
		
		return lisCotempresa;
	}
	
	
	public void grabar (Session session, Cotempresa cotempresa)throws Exception{
		session.save(cotempresa);
		
	}
	
	public void modificar (Session session, Cotempresa cotempresa) throws Exception{
		session.update(cotempresa);
	}
}
