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

import com.web.cementerio.pojo.annotations.Petfotoservicio;
import com.web.cementerio.pojo.annotations.Petservicio;

public class PetservicioDAO {

	public int maxIdservicio(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select max(idservicio) as max from Petservicio ").uniqueResult();
		max = (object==null?0:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	public int maxOrden(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select count(p.orden) as cant from Petservicio as p where p.setestado.idestado = 1").uniqueResult();
		max = (object==null?0:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	@SuppressWarnings("unchecked")
	public List<Petservicio> lisPetservicioPrincipales(Session session) throws Exception {
		List<Petservicio> lisPetservicio = null;
		
		String hql = " from Petservicio ser inner join fetch ser.cotempresa emp ";
		hql += " where ser.principal = :principal ";
		hql += " and ser.setestado.idestado = :idestado ";
		hql += " order by ser.orden desc ";
		
		Query query = session.createQuery(hql)
				.setInteger("idestado", 1)
				.setBoolean("principal", true);
		
		lisPetservicio = (List<Petservicio>) query.list();
		
		return lisPetservicio;
	}
	
	@SuppressWarnings("unchecked")
	public List<Petservicio> lisPetservicio(Session session, int idempresa, int idoficina) throws Exception {
		List<Petservicio> lisPetservicio = null;
		
		String hql = " from Petservicio ";
		hql += " where setestado.idestado = :idestado ";
		
		if(idempresa > 0){
			hql += " and cotempresa.idempresa = " + idempresa;
		}
		
		if(idoficina > 0){
			hql += " and cotoficina.idoficina = " + idoficina;
		}
		
		hql += " order by nombre ";
		
		Query query = session.createQuery(hql)
				.setInteger("idestado", 1);
		
		lisPetservicio = (List<Petservicio>) query.list();
		
		return lisPetservicio;
	}
	
	@SuppressWarnings("unchecked")
	public List<Petservicio> lisPetservicioBusquedaByPage(Session session, String[] texto, int idempresa, int pageSize, int pageNumber, int args[]) throws Exception {
		List<Petservicio> lisPetservicio = null;
		
		Criteria criteria = session.createCriteria(Petservicio.class)
				.createAlias("cotoficina", "ofi")
				.add( Restrictions.eq("ofi.cotempresa.idempresa", idempresa))
				.add( Restrictions.eq("setestado.idestado", 1));
		
		if(texto != null && texto.length > 0){
			String query = "(";
			for(int i=0;i<texto.length;i++)
			{
				query += "lower({alias}.nombre) like lower('%"+texto[i]+"%') ";
				query += "or lower({alias}.descripcion) like lower('%"+texto[i]+"%') ";
				if(i<texto.length-1){
					query += "or ";
				}
			}
			query += ")";
			
			criteria.add(Restrictions.sqlRestriction(query));
		}
		
        criteria.addOrder(Order.asc("orden"))
		.setMaxResults(pageSize)
		.setFirstResult(pageNumber);
        
		lisPetservicio = (List<Petservicio>) criteria.list();
		
		if(lisPetservicio != null && lisPetservicio.size() > 0)
		{
			Criteria criteriaCount = session.createCriteria(Petservicio.class)
					.createAlias("cotoficina", "ofi")
					.add( Restrictions.eq("ofi.cotempresa.idempresa", idempresa))
					.add( Restrictions.eq("setestado.idestado", 1))
                    .setProjection( Projections.rowCount());

			if(texto != null && texto.length > 0){
				String query = "(";
				for(int i=0;i<texto.length;i++)
				{
					query += "lower({alias}.nombre) like lower('%"+texto[i]+"%') ";
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
		
		return lisPetservicio;
	}
	
	public Petservicio getPetservicioConObjetosById(Session session, int idservicio, int idempresa) throws Exception {
		Petservicio petservicio = null;
		
		Criteria criteria = session.createCriteria(Petservicio.class, "serv")
				.add( Restrictions.eq("serv.idservicio", idservicio))
				.add( Restrictions.eq("serv.setestado.idestado", 1))
				.createAlias("cotempresa", "emp")
				.add(Restrictions.eq("emp.idempresa", idempresa))
				.createAlias("cotoficina", "ofi")
				//.createAlias("serv.petfotoservicios", "foto", Criteria.LEFT_JOIN);
				.createAlias("serv.petfotoservicios", "foto", JoinType.LEFT_OUTER_JOIN);
		
		petservicio = (Petservicio) criteria.uniqueResult();
		
		if(petservicio != null && petservicio.getPetfotoservicios() != null && petservicio.getPetfotoservicios().size() > 0){
			
			Set<Petfotoservicio> tmp = new HashSet<Petfotoservicio>();
			for(Petfotoservicio foto : petservicio.getPetfotoservicios()){
				if(foto.getSetestado().getIdestado() == 1){
					tmp.add(foto);
				}
			}
			petservicio.setPetfotoservicios(tmp);
			
		}
		
		return petservicio;
	}
	
	public Petservicio getPetservicioById(Session session, int idservicio) throws Exception {
		Petservicio petservicio = null;
		
		Criteria criteria = session.createCriteria(Petservicio.class)
				.add( Restrictions.eq("idservicio", idservicio));
		
		petservicio = (Petservicio) criteria.uniqueResult();
		
		return petservicio;
	}
	
	public void savePetservicio(Session session, Petservicio petservicio) throws Exception {
		session.save(petservicio);
	}
	
	public void updatePetservicio(Session session, Petservicio petservicio) throws Exception {
		session.update(petservicio);
	}
	
}
