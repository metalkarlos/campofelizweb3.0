package com.web.cementerio.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.web.cementerio.pojo.annotations.Cotoficina;

public class CotoficinaDAO {
	
	public int getMaxidoficina(Session session)throws Exception{
		int maxid = 0;
				
		Object object = session.createQuery("select max(idoficina)+1 from Cotoficina").uniqueResult();
		maxid = (object ==null ?1: Integer.parseInt(object.toString()));
		
		return maxid;
	}
	
	public Cotoficina getCotoficinabyId(Session session,int idoficina)throws Exception{
		Cotoficina cotoficina = null;
		
		Criteria criteria = session.createCriteria(Cotoficina.class) 
				.createAlias("cotempresa", "emp")
				.add(Restrictions.eq("idoficina", idoficina))
				.add(Restrictions.eq("setestado.idestado", 1));
		
		cotoficina =(Cotoficina) criteria.uniqueResult();
		
		return cotoficina;
	}
	
	@SuppressWarnings("unchecked")
	public List<Cotoficina> lisCotoficinaByIdempresa(Session session, int idempresa) throws Exception {
		List<Cotoficina> lisCotoficina = null;
		
		String hql = " from Cotoficina ";
		hql += " where setestado.idestado = " + 1;
		
		if(idempresa > 0){
			hql += " and cotempresa.idempresa = " + idempresa;
		}
		
		hql += " order by nombre asc ";
		
		Query query = session.createQuery(hql);
		
		lisCotoficina = (List<Cotoficina>) query.list();
		
		return lisCotoficina;
	}
	
	@SuppressWarnings("unchecked")
	public List<Cotoficina> lisCotoficinaByIdoficina(Session session, int idoficina) throws Exception {
		List<Cotoficina> lisCotoficina = null;
		
		String hql = " from Cotoficina ";
		hql += " where setestado.idestado = " + 1;
		hql += " and cotempresa.idempresa = (select ofi2.cotempresa.idempresa from Cotoficina ofi2 where ofi2.idoficina = "+idoficina+")";
		
		hql += " order by nombre asc ";
		
		Query query = session.createQuery(hql);
		
		lisCotoficina = (List<Cotoficina>) query.list();
		
		return lisCotoficina;
	}
	
	public void grabar (Session session, Cotoficina cotoficina)throws Exception{
		session.save(cotoficina);
		
	}
	
	public void modificar (Session session, Cotoficina cotoficina) throws Exception{
		session.update(cotoficina);
	}

}
