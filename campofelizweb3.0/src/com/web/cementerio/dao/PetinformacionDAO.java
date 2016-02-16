package com.web.cementerio.dao;



import java.util.HashSet;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.web.cementerio.pojo.annotations.Petfotoinformacion;
import com.web.cementerio.pojo.annotations.Petinformacion;

public class PetinformacionDAO {

	public Petinformacion getPetinformacionById(Session session, int idinformacion, int idestado) throws Exception {
		Petinformacion petinformacion = null;
		
		Criteria criteria = session.createCriteria(Petinformacion.class, "info")
				 .add(Restrictions.eq("info.idinformacion", idinformacion))
				 .add(Restrictions.eq("info.setestado.idestado", idestado))
				 //.createAlias("info.petfotoinformaciones", "foto", Criteria.LEFT_JOIN);
				 .createAlias("info.petfotoinformaciones", "foto", JoinType.LEFT_OUTER_JOIN);
				 
		
		petinformacion = (Petinformacion) criteria.uniqueResult();
		if(petinformacion!=null){
			if((!petinformacion.getPetfotoinformaciones().isEmpty()) && petinformacion.getPetfotoinformaciones().size()>0){
				Set<Petfotoinformacion> tmp = new HashSet<Petfotoinformacion>();
				for(Petfotoinformacion petfoto:petinformacion.getPetfotoinformaciones()){
				 if(petfoto.getSetestado().getIdestado() == idestado){
					tmp.add(petfoto);
			  	}
			  }
			  petinformacion.setPetfotoinformaciones(tmp);
			}
		}
		return petinformacion;
	}
	

	public void ingresarPetinformacion(Session session, Petinformacion petinformacion) throws Exception {
		session.save(petinformacion);
	}
	
	public void actualizarPetinformacion(Session session, Petinformacion petinformacion) throws Exception {
		session.update(petinformacion);
	}
}
