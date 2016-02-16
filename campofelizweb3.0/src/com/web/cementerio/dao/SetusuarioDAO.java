package com.web.cementerio.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.web.cementerio.pojo.annotations.Setusuario;

public class SetusuarioDAO {
	
	public Setusuario getByUserPasswd(Session session, String nombre, String clave) throws Exception {
		Setusuario setusuario = null;
		
		Criteria criteria = session.createCriteria(Setusuario.class);
		criteria.add( Restrictions.eq("nombre", nombre) );
		criteria.add( Restrictions.eq("clave", clave) );
		criteria.add( Restrictions.eq("setestado.idestado", 1) );
		
		setusuario = (Setusuario) criteria.uniqueResult();
		
		return setusuario;
	}
	
	public Setusuario getSetusuarioByUsuario(Session session, String usuario) throws Exception {
		Setusuario setusuario = null;
		
		Criteria criteria = session.createCriteria(Setusuario.class);
		criteria.add( Restrictions.eq("nombre", usuario) );
		criteria.add( Restrictions.eq("setestado.idestado", 1) );
		
		setusuario = (Setusuario) criteria.uniqueResult();
		
		return setusuario;
	}
	
	public void updateSetusuario(Session session, Setusuario setusuario) throws Exception {
		session.update(setusuario);
	}
		
	
}
