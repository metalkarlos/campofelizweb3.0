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

import com.web.cementerio.pojo.annotations.Petespecie;
import com.web.cementerio.pojo.annotations.Petfotomascota;
import com.web.cementerio.pojo.annotations.Petmascotahomenaje;

public class PetmascotahomenajeDAO {

	public Petmascotahomenaje getPethomenajemascotaById(Session session,int idmascota) throws Exception {

		Petmascotahomenaje petmascotahomenaje = null;
		
		Criteria criteria = session
				.createCriteria(Petmascotahomenaje.class)
				.add(Restrictions.eq("idmascota", idmascota))
				.createAlias("petespecie", "e")
				.add(Restrictions.eq("setestado.idestado", 1))
				//.createAlias("petfotomascotas", "foto", Criteria.LEFT_JOIN);
				.createAlias("petfotomascotas", "foto", JoinType.LEFT_OUTER_JOIN);

		petmascotahomenaje = (Petmascotahomenaje) criteria.uniqueResult();

		if (petmascotahomenaje != null && petmascotahomenaje.getPetfotomascotas() != null && petmascotahomenaje.getPetfotomascotas().size() > 0) {
			Set<Petfotomascota> tmp = new HashSet<Petfotomascota>();

			for (Petfotomascota petfoto : petmascotahomenaje.getPetfotomascotas()) {
				if (petfoto.getSetestado().getIdestado() == 1) {
					tmp.add(petfoto);
				}
			}
			petmascotahomenaje.setPetfotomascotas(tmp);
		}

		return petmascotahomenaje;
	}

	@SuppressWarnings("unchecked")
	public List<Petmascotahomenaje> lisPetmascotaPrincipal(Session session,
			int maxresultado) throws Exception {
		List<Petmascotahomenaje> listPetmascotahomenaje = null;

		Criteria criteria = session.createCriteria(Petmascotahomenaje.class)
				.add(Restrictions.eq("setestado.idestado", 1))
				.addOrder(Order.desc("fechapublicacion"))
				.createAlias("petespecie", "especie")
				.setMaxResults(maxresultado);
		
		listPetmascotahomenaje = (List<Petmascotahomenaje>) criteria.list();
		
		return listPetmascotahomenaje;
	}

	@SuppressWarnings("unchecked")
	public List<Petmascotahomenaje> lisPetmascotaBusquedaByPage(
			Session session, String[] texto, int pageSize, int pageNumber,
			int args[]) throws Exception {
		List<Petmascotahomenaje> listPetmascotahomenaje = null;

		Criteria criteria = session.createCriteria(Petmascotahomenaje.class)
				.add(Restrictions.eq("setestado.idestado", 1))
				.createAlias("petespecie", "e");

		if (texto != null && texto.length > 0) {
			String query = "(";
			for (int i = 0; i < texto.length; i++) {
				query += "{alias}.nombre like upper('" + texto[i] + "%') ";
				if (i < texto.length - 1) {
					query += "or ";
				}
			}
			query += ")";

			criteria.add(Restrictions.sqlRestriction(query));
		}

		criteria.addOrder(Order.desc("fechapublicacion"))
				.setMaxResults(pageSize).setFirstResult(pageNumber);

		listPetmascotahomenaje = (List<Petmascotahomenaje>) criteria.list();

		// si hay datos pero con texto de busqueda, ahi si debe ir el pagineo
		if (listPetmascotahomenaje != null && listPetmascotahomenaje.size() > 0
				&& texto != null && texto.length > 0) {
			Criteria criteriaCount = session
					.createCriteria(Petmascotahomenaje.class)
					.add(Restrictions.eq("setestado.idestado", 1))
					.setProjection(Projections.rowCount());

			if (texto != null && texto.length > 0) {
				String query = "(";
				for (int i = 0; i < texto.length; i++) {
					query += "upper({alias}.nombre) like upper('" + texto[i] + "%') ";
					if (i < texto.length - 1) {
						query += "or ";
					}
				}
				query += ")";

				criteriaCount.add(Restrictions.sqlRestriction(query));
			}

			Object object = criteriaCount.uniqueResult();
			int count = (object == null ? 0 : Integer.parseInt(object
					.toString()));
			args[0] = count;
		} else {
			// si no hay datos o si hubieron datos pero sin texto de busqueda
			args[0] = 0;
		}

		return listPetmascotahomenaje;
	}
	
	@SuppressWarnings("unchecked")
	public List<Petmascotahomenaje> lisPetmascotaByNombreIdespecie (
			Session session, String[] texto, int idespecie, int pageSize) throws Exception {
		List<Petmascotahomenaje> listPetmascotahomenaje = null;

		Criteria criteria = session.createCriteria(Petmascotahomenaje.class)
				.add(Restrictions.eq("setestado.idestado", 1));
		
		if(idespecie > 0){
			criteria.add(Restrictions.eq("petespecie.idespecie", idespecie));
		}

		if (texto != null && texto.length > 0) {
			String query = "(";
			for (int i = 0; i < texto.length; i++) {
				query += "upper({alias}.nombre) like upper('" + texto[i] + "%') ";
				if (i < texto.length - 1) {
					query += "or ";
				}
			}
			query += ")";

			criteria.add(Restrictions.sqlRestriction(query));
		}

		criteria.addOrder(Order.desc("fechapublicacion"))
		.setMaxResults(pageSize);

		listPetmascotahomenaje = (List<Petmascotahomenaje>) criteria.list();

		return listPetmascotahomenaje;
	}

	@SuppressWarnings("unchecked")
	public List<Petmascotahomenaje> getListpetmascotabycriteria(
			Session session, int idestado, int idespecie, String nombre) {
		List<Petmascotahomenaje> listPetmascotahomeanje = null;

		Criteria criteria = session
				.createCriteria(Petmascotahomenaje.class)
				.add(Restrictions.eq("setestado.idestado", idestado))
				.add(Restrictions.eq("petespecie.idespecie", idespecie))
				.add(Restrictions.like("nombre",
						"%" + nombre.replaceAll(" ", "%") + "%").ignoreCase());

		criteria.addOrder(Order.desc("fechapublicacion"));
		listPetmascotahomeanje = (List<Petmascotahomenaje>) criteria.list();

		return listPetmascotahomeanje;
	}
	
	@SuppressWarnings("unchecked")
	public  List<Petespecie> lisPetespecieMascotas(Session session, String[] texto) {
		List<Petespecie> lisPetespecie = null;
		
		String hql = "select distinct e.idespecie "
				+ "from Petmascotahomenaje m, Petespecie e "
				+ "where m.setestado.idestado = 1 "
				+ "and e.idespecie = m.petespecie.idespecie ";
		
		if (texto != null && texto.length > 0) {
			hql += "and (";
			for (int i = 0; i < texto.length; i++) {
				hql += "upper(m.nombre) like upper('" + texto[i] + "%') ";
				if (i < texto.length - 1) {
					hql += "or ";
				}
			}
			hql += ")";
		}
		
		Query query = session.createQuery(" from Petespecie e2 "
				+ "where e2.idespecie in ( "
				+ hql
				+ ") "
				+ "and e2.setestado.idestado = 1 "
				+ "order by e2.nombre");
		
		lisPetespecie = (List<Petespecie>) query.list();
		
		return lisPetespecie;
	}

	public int getMaxidpetmascotahomenaje(Session session) throws Exception {
		int maxid = 0;

		Object object = session.createQuery(
				"select max(idmascota)+1 from Petmascotahomenaje ")
				.uniqueResult();
		maxid = (object == null ? 1 : Integer.parseInt(object.toString()));

		return maxid;
	}

	public void ingresarPetmascotahomenaje(Session session,
			Petmascotahomenaje petmascotahomenaje) throws Exception {
		session.save(petmascotahomenaje);
	}

	public void modificarPetmascotahomenaje(Session session,
			Petmascotahomenaje petmascotahomenaje) throws Exception {
		session.update(petmascotahomenaje);
	}

}
