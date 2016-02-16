package com.web.cementerio.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.web.cementerio.pojo.annotations.Petfotonoticia;
import com.web.cementerio.pojo.annotations.Petnoticia;

public class PetnoticiaDAO {

	public int maxIdnoticia(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select max(idnoticia) as max from Petnoticia ").uniqueResult();
		max = (object==null?0:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	public int maxOrden(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select count(p.orden) as cant from Petnoticia as p where p.setestado.idestado = 1").uniqueResult();
		max = (object==null?0:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	public Petnoticia getPetnoticiaById(Session session, int idnoticia) throws Exception {
		Petnoticia petnoticia = null;
		
		String hql = " from Petnoticia ";
		hql += " where idnoticia = :idnoticia ";

		Query query = session.createQuery(hql)
				.setInteger("idnoticia", idnoticia);
		
		petnoticia = (Petnoticia) query.uniqueResult();
		
		return petnoticia;
	}
	
	public Petnoticia getPetnoticiaConObjetosById(Session session, int idnoticia) throws Exception {
		Petnoticia petnoticia = null;
		
		Criteria criteria = session.createCriteria(Petnoticia.class, "noti")
				.add( Restrictions.eq("noti.idnoticia", idnoticia))
				.add( Restrictions.eq("noti.setestado.idestado", 1))
				//.createAlias("noti.petfotonoticias", "foto", Criteria.LEFT_JOIN);
				.createAlias("noti.petfotonoticias", "foto", JoinType.LEFT_OUTER_JOIN);
		
		petnoticia = (Petnoticia) criteria.uniqueResult();
		
		if(petnoticia.getPetfotonoticias() != null && petnoticia.getPetfotonoticias().size() > 0){
			
				Set<Petfotonoticia> tmp = new HashSet<Petfotonoticia>();
				for(Petfotonoticia foto : petnoticia.getPetfotonoticias()){
					if(foto.getSetestado().getIdestado() == 1){
						tmp.add(foto);
					}
				}
				petnoticia.setPetfotonoticias(tmp);
				
		}
		
		return petnoticia;
	}
	
	@SuppressWarnings("unchecked")
	public List<Petnoticia> lisPetnoticiaByPage(Session session, int pageSize, int pageNumber, int args[], String titulo, String descripcion) throws Exception {
		List<Petnoticia> listPetnoticia = null;
		
		Criteria criteria = session.createCriteria(Petnoticia.class)
		.add( Restrictions.eq("setestado.idestado", 1));
		
		if(titulo != null && titulo.trim().length() > 0){
            criteria.add( Restrictions.like("titulo", "%"+titulo.replaceAll(" ", "%")+"%").ignoreCase());
        }
        
        if(descripcion != null && descripcion.trim().length() > 0){
            criteria.add( Restrictions.like("descripcion", "%"+descripcion.replaceAll(" ", "%")+"%").ignoreCase());
        }
		
        criteria.addOrder(Order.desc("fecharegistro"))
		.setMaxResults(pageSize)
		.setFirstResult(pageNumber);
        
		listPetnoticia = (List<Petnoticia>) criteria.list();
		
		if(listPetnoticia != null && listPetnoticia.size() > 0)
		{
			Criteria criteriaCount = session.createCriteria(Petnoticia.class)
					.add( Restrictions.eq("setestado.idestado", 1))
                    .setProjection( Projections.rowCount());

			if(titulo != null && titulo.trim().length() > 0){
				criteriaCount.add( Restrictions.like("titulo", "%"+titulo.replaceAll(" ", "%")+"%").ignoreCase());
	        }
	        
	        if(descripcion != null && descripcion.trim().length() > 0){
	        	criteriaCount.add( Restrictions.like("descripcion", "%"+descripcion.replaceAll(" ", "%")+"%").ignoreCase());
	        }
			
			Object object = criteriaCount.uniqueResult();
			int count = (object==null?0:Integer.parseInt(object.toString()));
			args[0] = count;
		}
		else
		{
			args[0] = 0;
		}
		
		return listPetnoticia;
	}
	
	@SuppressWarnings("unchecked")
	public List<Petnoticia> lisPetnoticiaBusquedaByPage(Session session, String[] texto, int pageSize, int pageNumber, int args[]) throws Exception {
		List<Petnoticia> listPetnoticia = null;
		
		Criteria criteria = session.createCriteria(Petnoticia.class)
		.add( Restrictions.eq("setestado.idestado", 1));
		
		if(texto != null && texto.length > 0){
			String query = "(";
			for(int i=0;i<texto.length;i++)
			{
				query += "lower({alias}.titulo) like lower('%"+texto[i]+"%') ";
				query += "or lower({alias}.descripcion) like lower('%"+texto[i]+"%') ";
				if(i<texto.length-1){
					query += "or ";
				}
			}
			query += ")";
			
			criteria.add(Restrictions.sqlRestriction(query));
		}
		
        criteria.addOrder(Order.desc("orden"))
		.setMaxResults(pageSize)
		.setFirstResult(pageNumber);
        
		listPetnoticia = (List<Petnoticia>) criteria.list();
		
		if(listPetnoticia != null && listPetnoticia.size() > 0)
		{
			Criteria criteriaCount = session.createCriteria(Petnoticia.class)
					.add( Restrictions.eq("setestado.idestado", 1))
                    .setProjection( Projections.rowCount());

			if(texto != null && texto.length > 0){
				String query = "(";
				for(int i=0;i<texto.length;i++)
				{
					query += "lower({alias}.titulo) like lower('%"+texto[i]+"%') ";
					query += "or lower({alias}.descripcion) like lower('%"+texto[i]+"%') ";
					if(i<texto.length-1){
						query += "or ";
					}
				}
				query += ")";
				
				criteriaCount.add(Restrictions.sqlRestriction(query));
			}
			
			Object object = criteriaCount.uniqueResult();
			int count = (object==null?0:Integer.parseInt(object.toString()));
			args[0] = count;
		}
		else
		{
			args[0] = 0;
		}
		
		return listPetnoticia;
	} 
	
	@SuppressWarnings("unchecked")
	public List<Petnoticia> lisPetnoticiaPrincipales(Session session) throws Exception {
		List<Petnoticia> lisPetnoticia = null;
		
		String hql = " from Petnoticia ";
		hql += " where principal = :principal ";
		hql += " and setestado.idestado = :idestado ";
		hql += " order by orden desc ";
		
		Query query = session.createQuery(hql)
				.setInteger("idestado", 1)
				.setBoolean("principal", true);
		
		lisPetnoticia = (List<Petnoticia>) query.list();
		
		return lisPetnoticia;
	}
	
	public void savePetnoticia(Session session, Petnoticia petnoticia) throws Exception {
		session.save(petnoticia);
	}
	
	public void updatePetnoticia(Session session, Petnoticia petnoticia) throws Exception {
		session.update(petnoticia);
	}
	
}
