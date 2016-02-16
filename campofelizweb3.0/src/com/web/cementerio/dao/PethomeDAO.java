package com.web.cementerio.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.web.cementerio.pojo.annotations.Pethome;

public class PethomeDAO {

	public int maxIdhome(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select max(idhome) as max from Pethome").uniqueResult();
		max = (object==null?0:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	public int maxOrden(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select count(p.orden) as cant from Pethome as p where p.setestado.idestado = 1").uniqueResult();
		max = (object==null?0:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	public Pethome getPethomebyId(Session session,int idhome) throws Exception{
		Pethome pethome = null;
		
		Criteria criteria = session.createCriteria(Pethome.class) 
				.add(Restrictions.eq("idhome", idhome))
				.add(Restrictions.eq("setestado.idestado", 1));
		
		pethome =(Pethome) criteria.uniqueResult();
		
		return pethome;
	}
	
	@SuppressWarnings("unchecked")
	public List<Pethome> lisPethome(Session session) throws Exception{
		List<Pethome> lisPethome = null;
		
		Criteria criteria = session.createCriteria(Pethome.class) 
				.add(Restrictions.eq("setestado.idestado", 1))
				.addOrder(Order.desc("orden"));
		
		lisPethome =(List<Pethome>)criteria.list();
		
		return lisPethome;
	}
	
	@SuppressWarnings("unchecked")
	public List<Pethome> lisPethomeByPage(Session session, int pageSize, int pageNumber, int args[]) throws Exception {
		List<Pethome> lisPethome = null;
		
		Criteria criteria = session.createCriteria(Pethome.class)
		.add( Restrictions.eq("setestado.idestado", 1));
		
        criteria.addOrder(Order.desc("orden"))
		.setMaxResults(pageSize)
		.setFirstResult(pageNumber);
        
        lisPethome = (List<Pethome>) criteria.list();
		
		if(lisPethome != null && lisPethome.size() > 0)
		{
			Criteria criteriaCount = session.createCriteria(Pethome.class)
					.add( Restrictions.eq("setestado.idestado", 1))
                    .setProjection( Projections.rowCount());

			Object object = criteriaCount.uniqueResult();
			int count = (object==null?0:Integer.parseInt(object.toString()));
			args[0] = count;
		}
		else
		{
			args[0] = 0;
		}
		
		return lisPethome;
	}
	
	public void savePethome(Session session, Pethome pethome) throws Exception {
		session.save(pethome);
	}
	
	public void updatePethome(Session session, Pethome pethome) throws Exception {
		session.update(pethome);
	}
}
