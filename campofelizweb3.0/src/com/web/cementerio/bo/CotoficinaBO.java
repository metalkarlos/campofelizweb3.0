package com.web.cementerio.bo;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.CotoficinaDAO;
import com.web.cementerio.pojo.annotations.Cotoficina;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.HibernateUtil;

public class CotoficinaBO {
	
	public Cotoficina getCotoficinabyId(int idoficina) throws Exception{
		Cotoficina cotoficina = null;
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			CotoficinaDAO cotoficinaDAO = new CotoficinaDAO();
			cotoficina = cotoficinaDAO.getCotoficinabyId(session, idoficina);
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			session.close();
		}
		return cotoficina;
	}
	
	public List<Cotoficina> lisCotoficinaByIdempresa(int idempresa) throws Exception{
		List<Cotoficina> lisCotoficina = null;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			CotoficinaDAO cotoficinaDAO = new CotoficinaDAO();
			lisCotoficina = (List<Cotoficina>) cotoficinaDAO.lisCotoficinaByIdempresa(session, idempresa);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			 session.close();
		}
		
		return lisCotoficina; 
	}
	
	public List<Cotoficina> lisCotoficinaByIdoficina(int idoficina) throws Exception{
		List<Cotoficina> lisCotoficina = null;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			CotoficinaDAO cotoficinaDAO = new CotoficinaDAO();
			lisCotoficina = (List<Cotoficina>) cotoficinaDAO.lisCotoficinaByIdoficina(session, idoficina);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			 session.close();
		}
		
		return lisCotoficina; 
	}
	
	public boolean grabar(Cotoficina cotoficina)throws Exception{
		Session session = null;
		boolean ok = false;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Date fecharegistro = new Date();
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			CotoficinaDAO cotoficinaDAO = new CotoficinaDAO();
			
			int maxidoficina = cotoficinaDAO.getMaxidoficina(session);
			
			cotoficina.setIdoficina(maxidoficina);
		    
			Setestado setestado = new Setestado();
			setestado.setIdestado(1);
			cotoficina.setSetestado(setestado);
			
			Setusuario setusuario = new Setusuario();
			setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			cotoficina.setSetusuario(setusuario);
			
			//Auditoria
			cotoficina.setFecharegistro(fecharegistro);
			cotoficina.setIplog(usuarioBean.getIp());
			
			cotoficinaDAO.grabar(session, cotoficina);
			session.beginTransaction().commit();
			
			ok = true;
		} catch (Exception e) {
			cotoficina.setIdoficina(0);
			session.getTransaction().rollback();
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return ok;
	}
	
	public boolean eliminar(Cotoficina cotoficina)throws Exception{
		Session session = null;
		boolean ok = false;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Date fechamodificacion = new Date();
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			CotoficinaDAO cotoficinaDAO = new CotoficinaDAO();
			
			Setestado setestado = new Setestado();
			setestado.setIdestado(2);
			cotoficina.setSetestado(setestado);
			
			Setusuario setusuario = new Setusuario();
			setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			cotoficina.setSetusuario(setusuario);
			
			//Auditoria
			cotoficina.setFechamodificacion(fechamodificacion);
			cotoficina.setIplog(usuarioBean.getIp());
			
			cotoficinaDAO.modificar(session, cotoficina);
			session.beginTransaction().commit();
			
			ok = true;
			
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return ok;
	}
	
	public boolean modificar(Cotoficina cotoficina,Cotoficina cotoficinaclone)throws Exception{
		Session session = null;
		boolean ok = false;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			if(!cotoficina.equals(cotoficinaclone)){
				Date fechamodificacion = new Date();
				UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
				CotoficinaDAO cotoficinaDAO = new CotoficinaDAO();
				
				Setusuario setusuario = new Setusuario();
				setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
				cotoficina.setSetusuario(setusuario);
				
				//Auditoria
				cotoficina.setFechamodificacion(fechamodificacion);
				cotoficina.setIplog(usuarioBean.getIp());
				
				cotoficinaDAO.modificar(session, cotoficina);
				session.beginTransaction().commit();
				ok= true;
			}	
			return ok;
			
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e);
		}finally{
			session.close();
		}
	}

}
