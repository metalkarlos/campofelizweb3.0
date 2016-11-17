package com.web.cementerio.bo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.PetfotoinformacionDAO;
import com.web.cementerio.dao.PetinformacionDAO;
import com.web.cementerio.pojo.annotations.Petfotoinformacion;
import com.web.cementerio.pojo.annotations.Petinformacion;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.HibernateUtil;

public class PetinformacionBO {

	public PetinformacionBO() {
	}
	
	public Petinformacion getPetinformacionById(int idinformacion) throws Exception {
		Petinformacion petinformacion = null;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			PetinformacionDAO petinformacionDAO = new PetinformacionDAO();
			petinformacion = petinformacionDAO.getPetinformacionById(session, idinformacion);
		}catch(Exception e){
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return petinformacion;
	}

	public  boolean modificarPetinformacion(Petinformacion petinformacion,Petinformacion petinformacionclone,List<Petfotoinformacion> listPetfotoinformacion,List<Petfotoinformacion> listPetfotoinformacionclone) throws Exception{
		Session session = null;
		boolean ok = false;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetinformacionDAO petinformacionDAO = new PetinformacionDAO();
			PetfotoinformacionDAO petfotoinformacionDAO = new PetfotoinformacionDAO();
			Date fecharegistro = new Date();
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			
			//Se evalua si han habido cambios en la lista de las fotos
			for(Petfotoinformacion petfotoinformacionClon : listPetfotoinformacionclone){
				boolean encuentra = false;
				for(Petfotoinformacion petfotoinformacionItem : listPetfotoinformacion){
					if(petfotoinformacionClon.getIdfotoinformacion() == petfotoinformacionItem.getIdfotoinformacion()){
						//si encuentra
						encuentra = true;
						
						if(!petfotoinformacionClon.equals(petfotoinformacionItem)){
							//si han habido cambios se actualiza
							
							//auditoria
							fecharegistro = new Date();
							petfotoinformacionItem.setFechamodificacion(fecharegistro);
							petfotoinformacionItem.setIplog(usuarioBean.getIp());
							petfotoinformacionItem.setSetusuario(usuarioBean.getSetUsuario());
							
							//actualizar
							petfotoinformacionDAO.modificarFotoinformacion(session, petfotoinformacionItem);
							ok = true;
						}
						
						break;
					}
				}
				//si no encuentra lo han borrado
				if(!encuentra){
					//inhabilitar
					Setestado setestado = new Setestado();
					setestado.setIdestado(2);
					petfotoinformacionClon.setSetestado(setestado);
					
					//auditoria
					fecharegistro = new Date();
					petfotoinformacionClon.setFechamodificacion(fecharegistro);
					petfotoinformacionClon.setIplog(usuarioBean.getIp());
					petfotoinformacionClon.setSetusuario(usuarioBean.getSetUsuario());
					
					//actualizar
					petfotoinformacionDAO.modificarFotoinformacion(session, petfotoinformacionClon);
					
					//eliminar foto del disco
					String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
					
					String rutaArchivo = rutaImagenes + "/" + petfotoinformacionClon.getRuta();
					
					fileUtil.deleteFile(rutaArchivo);
					ok = true;
				}
			}
			
			//Se evalua si han subido nuevas fotos
			for(Petfotoinformacion petfotoinformacion : listPetfotoinformacion){
				boolean encuentra = false;
				for(Petfotoinformacion petfotoinformacionClon : listPetfotoinformacionclone){
					if(petfotoinformacion.getIdfotoinformacion() == petfotoinformacionClon.getIdfotoinformacion()){
						//si encuentra
						encuentra = true; 
						break;
					}
				}
				//no encuentra en lista clonada
				if(!encuentra){
					//es foto nueva
					creaFotoDiscoBD(petinformacion, petfotoinformacion, session);
					ok = true;
				}
			}
			
			//Se graba si han habido cambios
			if(!petinformacion.equals(petinformacionclone)){
				//auditoria
				petinformacion.setFechamodificacion(fecharegistro);
				petinformacion.setIplog(usuarioBean.getIp());
				petinformacion.setSetusuario(usuarioBean.getSetUsuario());
		
				//actualizar
				petinformacionDAO.actualizarPetinformacion(session, petinformacion);
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
	
	private void creaFotoDiscoBD(Petinformacion petinformacion, Petfotoinformacion petfotoinformacion, Session session) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		PetfotoinformacionDAO petfotoinformacionDAO = new PetfotoinformacionDAO();
		
		int maxIdfotoinformacion = petfotoinformacionDAO.getMaxidpetfotoinformacion(session)+1;
		int cantFotosPorInformacion = petfotoinformacionDAO.cantFotosPorInformacion(session, petinformacion.getIdinformacion())+1;
		
		//foto en disco
		FileUtil fileUtil = new FileUtil();
		FacesUtil facesUtil = new FacesUtil();
		Calendar fecha = Calendar.getInstance();
		
		String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
		String rutaQuienesSomos =  fileUtil.getPropertyValue("repositorio-quienessomos") +"/" + fecha.get(Calendar.YEAR);
		String nombreArchivo = fecha.get(Calendar.YEAR) + "-" + (fecha.get(Calendar.MONTH) + 1) + "-" + fecha.get(Calendar.DAY_OF_MONTH) + "-" + petinformacion.getIdinformacion() + "-" + cantFotosPorInformacion + "." + fileUtil.getFileExtention(petfotoinformacion.getNombrearchivo()).toLowerCase();
		
		String rutaCompleta = rutaImagenes + "/" + rutaQuienesSomos;
		
		if(fileUtil.createDir(rutaCompleta)){
			//crear foto en disco
			String rutaArchivo = rutaCompleta + "/" + nombreArchivo;
			fileUtil.createFile(rutaArchivo,petfotoinformacion.getImagen());
		}
		
		//foto en BD
		petfotoinformacion.setIdfotoinformacion(maxIdfotoinformacion);
		petfotoinformacion.setPetinformacion(petinformacion);
		String rutaBD = "/" + rutaQuienesSomos + "/" + nombreArchivo;
		petfotoinformacion.setRuta(rutaBD);
		petfotoinformacion.setNombrearchivo(nombreArchivo);
		Setestado setestadoPetfotoinformacion = new Setestado();
		setestadoPetfotoinformacion.setIdestado(1);
		petfotoinformacion.setSetestado(setestadoPetfotoinformacion);
		
		//auditoria
		Date fecharegistro = new Date();
		petfotoinformacion.setFecharegistro(fecharegistro);
		petfotoinformacion.setIplog(usuarioBean.getIp());
		petfotoinformacion.setSetusuario(usuarioBean.getSetUsuario());
		
		//ingresar foto en BD
		petfotoinformacionDAO.ingresarFotoinformacion(session, petfotoinformacion);
	}
	
	public void eliminarpetfotoinformacion(Session session,Petfotoinformacion petfotoinformacion)throws Exception{
		
		Date fechamodificacion= new Date();
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		PetfotoinformacionDAO petfotoinformacionDAO = new PetfotoinformacionDAO();	
		FacesUtil facesUtil = new FacesUtil();
		FileUtil fileUtil = new FileUtil();
		
		Setestado setestado = new Setestado();
		setestado.setIdestado(2);
		petfotoinformacion.setSetestado(setestado);
			
		Setusuario setusuario = new Setusuario();
		setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
		petfotoinformacion.setSetusuario(setusuario);
			
		//Auditoria
		petfotoinformacion.setFechamodificacion(fechamodificacion);
		petfotoinformacion.setIplog(usuarioBean.getIp());
		petfotoinformacionDAO.modificarFotoinformacion(session, petfotoinformacion);
			
		//eliminar foto del disco
		String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
		
		String rutaArchivo = rutaImagenes + "/" + petfotoinformacion.getRuta();
		
		fileUtil.deleteFile(rutaArchivo);
	}
}
