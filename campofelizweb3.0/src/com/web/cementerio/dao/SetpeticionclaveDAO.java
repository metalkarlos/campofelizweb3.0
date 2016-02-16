package com.web.cementerio.dao;

import java.util.Calendar;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.web.cementerio.pojo.annotations.Setpeticionclave;

public class SetpeticionclaveDAO {

	public int maxIdpeticionclave(Session session) throws Exception {
		int max=0;
		
		Object object = session.createQuery("select max(idpeticionclave) as max from Setpeticionclave").uniqueResult();
		max = (object==null?0:Integer.parseInt(object.toString()));
		
		return max;
	}
	
	public Setpeticionclave getSetpeticionclaveByUid(Session session, UUID uid) throws Exception {
		Setpeticionclave setpeticionclave = null;
		
		Calendar ahorita = Calendar.getInstance();
		
		Criteria criteria = session.createCriteria(Setpeticionclave.class)
				.add( Restrictions.eq("uid", uid))
				.add( Restrictions.gt("fechaexpiracion", ahorita.getTime()));
		
		setpeticionclave = (Setpeticionclave) criteria.uniqueResult();
		
		return setpeticionclave;
	}
	
	public void saveSetpeticionclave(Session session, Setpeticionclave setpeticionclave) throws Exception {
		session.save(setpeticionclave);
	}
}
