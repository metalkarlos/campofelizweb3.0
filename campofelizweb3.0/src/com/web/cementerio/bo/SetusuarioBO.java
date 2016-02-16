package com.web.cementerio.bo;

import java.util.Date;

import org.hibernate.Session;

import com.web.cementerio.dao.SetusuarioDAO;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.HibernateUtil;

public class SetusuarioBO {
	
	private SetusuarioDAO setusuarioDAO;
	
	public SetusuarioBO() throws RuntimeException {
		setusuarioDAO = new SetusuarioDAO();
	}

	public Setusuario getByUserPasswd(String nombre, String clave ) throws Exception {
		Setusuario setusuario = null;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			setusuario = setusuarioDAO.getByUserPasswd(session, nombre, clave);
		}catch(Exception he){
			throw new Exception(he);
		}finally{
			session.close();
		}
		
		return setusuario;
	}
	
	public Setusuario getSetusuarioByUsuario(String usuario) throws Exception {
		Setusuario setusuario = null;
		Session session = null;
	
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			setusuario = setusuarioDAO.getSetusuarioByUsuario(session, usuario);
		}catch(Exception he){
			throw new Exception(he);
		}finally{
			session.close();
		}
		
		return setusuario;
	}
	
	public boolean modificar(Setusuario setusuario) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			setusuarioDAO.updateSetusuario(session, setusuario);
			
			session.getTransaction().commit();
			
			ok = true;
		}catch(Exception e){
			session.getTransaction().rollback();
			throw new Exception(e); 
		}finally{
			session.close();
		}
		
		return ok;
	}
	
	public boolean cambiarClave(String usuario, String claveNueva) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Setusuario setusuario = setusuarioDAO.getSetusuarioByUsuario(session, usuario);
			setusuario.setClave(claveNueva);
			
			//auditoria
			Date fechamodificacion = new Date();
			setusuario.setFechamodificacion(fechamodificacion);
			String iplog = new FacesUtil().getIp();
			setusuario.setIplog(iplog);

			setusuarioDAO.updateSetusuario(session, setusuario);
			
			session.getTransaction().commit();
			
			ok = true;
		}catch(Exception e){
			session.getTransaction().rollback();
			throw new Exception(e); 
		}finally{
			session.close();
		}
		
		return ok;
	}
}
