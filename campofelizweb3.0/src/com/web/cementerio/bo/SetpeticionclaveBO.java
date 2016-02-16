package com.web.cementerio.bo;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.hibernate.Session;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.SetpeticionclaveDAO;
import com.web.cementerio.pojo.annotations.Setpeticionclave;
import com.web.util.FacesUtil;
import com.web.util.HibernateUtil;

public class SetpeticionclaveBO {

	private SetpeticionclaveDAO setpeticionclaveDAO;
	
	public SetpeticionclaveBO() {
		setpeticionclaveDAO = new SetpeticionclaveDAO();
	}
	
	public Setpeticionclave getSetpeticionclaveByUid(UUID uid) throws Exception {
		Setpeticionclave setpeticionclave = null;
		Session session = null;

		try{
			session = HibernateUtil.getSessionFactory().openSession();
			setpeticionclave = setpeticionclaveDAO.getSetpeticionclaveByUid(session, uid);
		}
        catch(Exception ex){
            throw new Exception(ex);
        }
        finally{
            session.close();
        }
		
		return setpeticionclave;
	}
	
	public boolean ingresar(Setpeticionclave setpeticionclave) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			
			int maxIdpeticionclave = setpeticionclaveDAO.maxIdpeticionclave(session)+1;
			setpeticionclave.setIdpeticionclave(maxIdpeticionclave);
			
			//auditoria
			Date fecharegistro = new Date();
			setpeticionclave.setFecharegistro(fecharegistro);
			setpeticionclave.setIplog(usuarioBean.getIp());
			
			Calendar fechaexpiracion = Calendar.getInstance();
			fechaexpiracion.setTime(fecharegistro);
			fechaexpiracion.set(Calendar.MINUTE, fechaexpiracion.get(Calendar.MINUTE)+5);
			setpeticionclave.setFechaexpiracion(fechaexpiracion.getTime());
			
			//grabar
			setpeticionclaveDAO.saveSetpeticionclave(session, setpeticionclave);
			
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
