package com.web.cementerio.bo;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.PetenunciadoDAO;
import com.web.cementerio.dao.PetvenunciadoDAO;
import com.web.cementerio.pojo.annotations.Petenunciado;
import com.web.cementerio.pojo.annotations.Petvenunciado;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.HibernateUtil;

public class PetenunciadoBO {

	 PetenunciadoDAO petenunciadoDAO;
	 
	 public PetenunciadoBO(){
		 petenunciadoDAO = new PetenunciadoDAO();
	 }
	 
	public Petenunciado getPetenunciadobyId(int idestado)throws Exception{
		Petenunciado petenunciado = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			petenunciado = petenunciadoDAO.getPetenunciadobyId(session, idestado);
		} catch (Exception e) {
			throw new Exception(e);
		
		}finally{
			session.close();
		}
		return petenunciado;
	}
	
	public int getMaxOrden() throws Exception{
		int orden = 0;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			orden = petenunciadoDAO.maxOrden(session);
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return orden;
	}
	 
	public List<Petvenunciado> getListpetvenunaciado() throws Exception{
		List<Petvenunciado> listpetvenunciado=null;
	    PetvenunciadoDAO petvenunciadoDAO =new PetvenunciadoDAO();
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();	
			listpetvenunciado = petvenunciadoDAO.getListpetvenunciado(session);
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
		   session.close();
		}
		
		return listpetvenunciado;
	}

	public List<Petvenunciado> getListpetvenunciadoidpadre(int idenunciado) throws Exception{
		List<Petvenunciado> listpetvPetvenunciado= null;
		PetvenunciadoDAO petvenunciadoDAO = new PetvenunciadoDAO();
		Session session = null;
	   try {
		   session = HibernateUtil.getSessionFactory().openSession();
		   listpetvPetvenunciado = petvenunciadoDAO.getListpetenunciadobyId(session, idenunciado);
	   	} catch (Exception e) {
		   throw new Exception(e);
	   	}finally{
	   		session.close();
	   	}
	   return listpetvPetvenunciado;
	}
	
	public boolean grabar(List<Petenunciado> listpetenunciado, int idestado) throws Exception{
		Session session = null;
		boolean ok = false;
		int idpadre=0;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
			
			Date fecharegistro = new Date();
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");

			Setestado setestado = new Setestado();
			setestado.setIdestado(idestado);

			Setusuario setusuario = new Setusuario();
			setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			
			
			for(Petenunciado petenunciado : listpetenunciado){
			
				petenunciado.setIdenunciado(petenunciadoDAO.getMaxidenunciado(session));
				
				if(String.valueOf(petenunciado.getTipo()).equals("P")){
				  idpadre = petenunciado.getIdenunciado();
				}
				else if(String.valueOf(petenunciado.getTipo()).equals("R")){
				  petenunciado.setIdpadre(idpadre);
				  idpadre =0;
				}
				petenunciado.setSetestado(setestado);
				petenunciado.setSetusuario(setusuario);
				//Auditoria
				petenunciado.setFecharegistro(fecharegistro);
				petenunciado.setIplog(usuarioBean.getIp());
				
				petenunciadoDAO.grabar(session, petenunciado);
			}
			
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
	
	public boolean modificar(List<Petenunciado> listpetenunciado, int idestado)throws Exception{
		Session session = null;
		boolean ok = false;
		try {
			
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Date fechamodificacion = new Date();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			Setusuario setusuario = new Setusuario();
			setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			
			Setestado setestado = new Setestado();
			setestado.setIdestado(idestado);
			
			
			for(Petenunciado petenunciado : listpetenunciado){
	
				petenunciado.setFechamodificacion(fechamodificacion);
			    petenunciado.setSetusuario(setusuario);
			    petenunciado.setSetestado(setestado);
			    petenunciado.setIplog(usuarioBean.getIp());
			    
			    petenunciadoDAO.modificar(session, petenunciado);
			    ok =true;
			}
			if(ok){
				session.getTransaction().commit();
			}	
			
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e);
		}finally{
			session.close();
		}
		return ok;
	}
	
	public boolean eliminar (List<Petenunciado> listpetenunciado, int idestado)throws Exception{
		Session session = null;
		boolean ok = false;
		
		try {
			
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Date fechamodificacion = new Date();
			
			Setestado setestado = new Setestado();
			setestado.setIdestado(idestado);
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			Setusuario setusuario = new Setusuario();
			setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			
			for(Petenunciado petenunciado : listpetenunciado){
				
				petenunciado.setSetestado(setestado);
			    petenunciado.setFechamodificacion(fechamodificacion);
			    petenunciado.setSetusuario(setusuario);
			    petenunciado.setIplog(usuarioBean.getIp());
			    
			    petenunciadoDAO.modificar(session, petenunciado);
			}
			
			session.getTransaction().commit();
			
			ok = true;
			
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return ok;
	}
	
	public PetenunciadoDAO getPetenunciadoDAO() {
		return petenunciadoDAO;
	}

	public void setPetenunciadoDAO(PetenunciadoDAO petenunciadoDAO) {
		this.petenunciadoDAO = petenunciadoDAO;
	}
	 
}
