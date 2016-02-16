package com.web.cementerio.bo;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.PethomeDAO;
import com.web.cementerio.pojo.annotations.Pethome;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.HibernateUtil;

public class PethomeBO {

	PethomeDAO pethomeDAO;
	
	public PethomeBO() {
		pethomeDAO = new PethomeDAO();
	}
	
	public Pethome getPethomebyId(int idhome) throws Exception{
		Pethome pethome = null;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			pethome = pethomeDAO.getPethomebyId(session, idhome);
				
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return pethome;
	}
	
	public int getMaxOrden() throws Exception{
		int orden = 0;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			orden = pethomeDAO.maxOrden(session);
				
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return orden;
	}
	
	public List<Pethome> lisPethome() throws Exception{
		List<Pethome> lisPethome = null;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			lisPethome = (List<Pethome>) pethomeDAO.lisPethome(session);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			 session.close();
		}
		
		return lisPethome; 
	}
	
	public List<Pethome> lisPethomeByPage(int pageSize, int pageNumber, int args[]) throws RuntimeException {
		List<Pethome> lisPethome = null;
		Session session = null;
		
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            lisPethome = pethomeDAO.lisPethomeByPage(session, pageSize, pageNumber, args);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
        finally{
            session.close();
        }
		
		return lisPethome;
	}
	
	public boolean grabar(Pethome pethome) throws Exception{
		Session session = null;
		boolean ok = false;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Date fecharegistro = new Date();
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			int maxidhome = pethomeDAO.maxIdhome(session)+1;
			
			pethome.setIdhome(maxidhome);
		    
			Setestado setestado = new Setestado();
			setestado.setIdestado(1);
			pethome.setSetestado(setestado);
			
			Setusuario setusuario = new Setusuario();
			setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			pethome.setSetusuario(setusuario);
			
			//Auditoria
			pethome.setFecharegistro(fecharegistro);
			pethome.setIplog(usuarioBean.getIp());
			
			pethomeDAO.savePethome(session, pethome);
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
	
	public boolean modificar(Pethome pethome, Pethome pethomeClon) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PethomeDAO pethomeDAO = new PethomeDAO();
			Date fecharegistro = new Date();
			
			//Se graba si han habido cambios
			if(!pethome.equals(pethomeClon)){
				//auditoria
				pethome.setFechamodificacion(fecharegistro);
				pethome.setIplog(usuarioBean.getIp());
				pethome.setSetusuario(usuarioBean.getSetUsuario());
		
				//actualizar
				pethomeDAO.updatePethome(session, pethome);
				ok = true;
			}
			
			if(ok){
				session.getTransaction().commit();
			}
		}catch(Exception e){
			ok = false;
			session.getTransaction().rollback();
			throw new Exception(e); 
		}finally{
			session.close();
		}
		
		return ok;
	} 
	
	public boolean eliminar(Pethome pethome) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			
			//se inactiva el registro
			Setestado setestado = new Setestado();
			setestado.setIdestado(2);
			pethome.setSetestado(setestado);
			
			//auditoria
			Date fecharegistro = new Date();
			pethome.setFechamodificacion(fecharegistro);
			pethome.setIplog(usuarioBean.getIp());
			pethome.setSetusuario(usuarioBean.getSetUsuario());
			
			pethomeDAO.updatePethome(session, pethome);
			
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
