package com.web.cementerio.bo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.primefaces.model.UploadedFile;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.PetfotoinstalacionDAO;
import com.web.cementerio.pojo.annotations.Petfotoinstalacion;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.HibernateUtil;

public class PetfotoinstalacionBO {

	PetfotoinstalacionDAO petfotoinstalacionDAO;
	
	public PetfotoinstalacionBO() {
		petfotoinstalacionDAO = new PetfotoinstalacionDAO();
	}
	
	public List<Petfotoinstalacion> lisPetfotoinstalacion() throws Exception {
		List<Petfotoinstalacion> lisPetfotoinstalacion = null;
		Session session = null;
	
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            lisPetfotoinstalacion = petfotoinstalacionDAO.lisPetfotoinstalacion(session);
        }
        catch(Exception ex){
            throw new Exception(ex);
        }
        finally{
            session.close();
        }
		
		return lisPetfotoinstalacion;
	}
	
	public List<Petfotoinstalacion> lisPetfotoinstalacionBusquedaByPage(String[] texto, int pageSize, int pageNumber, int args[], int idestado) throws RuntimeException {
		List<Petfotoinstalacion> listpetfotoinstalacion = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			listpetfotoinstalacion = petfotoinstalacionDAO.lisPetfotoinstalacionBusquedaByPage(session, texto, pageSize, pageNumber, args, idestado);
			
		} catch (Exception e) {
			 throw new RuntimeException(e);
		}
		finally{
			session.close();
		}
		
		
		return listpetfotoinstalacion;
	}
	
	public Petfotoinstalacion getPetfotoinstalacionbyId(int idfotoinstalacion, int idestado)throws Exception{
		Petfotoinstalacion petfotoinstalacion = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			petfotoinstalacion = petfotoinstalacionDAO.getPetfotoinstalacionById(session, idfotoinstalacion, idestado);
		} catch (Exception e) {
			throw new Exception(e); 
		}
		finally{
			session.close();
		}
		return petfotoinstalacion;
	}
	
	
	public boolean ingresarPetfotoinstalacion (int idestado,Petfotoinstalacion petfotoinstalacion,  UploadedFile uploadedFile)throws Exception{
		Session session = null;
		boolean ok = false;
		
		try {
		
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Date fecharegistro = new Date();
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
					
			petfotoinstalacion.setIdfotoinstalacion(petfotoinstalacionDAO.maxIdfotoinstalacion(session));
				    
		    Setestado setestado = new Setestado();
			setestado.setIdestado(idestado);
			petfotoinstalacion.setSetestado(setestado);
					
			Setusuario setusuario = new Setusuario();
			setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			petfotoinstalacion.setSetusuario(setusuario);
					
			//Auditoria
			petfotoinstalacion.setFecharegistro(fecharegistro);
			petfotoinstalacion.setIplog(usuarioBean.getIp());
					
			//foto en disco
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			Calendar fecha = Calendar.getInstance();
				
					
			String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
			String rutaMascota =  fileUtil.getPropertyValue("repositorio-instalaciones") + fecha.get(Calendar.YEAR);
			String nombreArchivo = fecha.get(Calendar.YEAR) + "-" + (fecha.get(Calendar.MONTH) + 1) + "-" + fecha.get(Calendar.DAY_OF_MONTH) + "-" + petfotoinstalacion.getIdfotoinstalacion() + "." +fileUtil.getFileExtention(uploadedFile.getFileName()).toLowerCase();
					
			String rutaCompleta = rutaImagenes + rutaMascota;
			//asignar ruta y nombre de archivo en objeto
			petfotoinstalacion.setRuta(rutaMascota+"/"+nombreArchivo);
			petfotoinstalacion.setNombrearchivo(nombreArchivo);
			
			petfotoinstalacionDAO.savePetfotoinstalacion(session, petfotoinstalacion);
			
			if(fileUtil.createDir(rutaCompleta)){
				//crear foto en disco
				String rutaArchivo = rutaCompleta + "/" + nombreArchivo;
				fileUtil.createFile(rutaArchivo,uploadedFile.getContents());
			}
				
			session.getTransaction().commit();
			ok = true;
			
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e);
		}finally {
			session.close();
		}	
		
		return ok;
	}
	
	public boolean modificarPetfotoinstalacion(Petfotoinstalacion petfotoinstalacion ,Petfotoinstalacion petfotoinstalacionclone) throws Exception{
		Session session = null;
		boolean ok = false;
		
		try{
			
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			if(petfotoinstalacion!=null && petfotoinstalacionclone !=null && !petfotoinstalacion.equals(petfotoinstalacionclone)){
				//auditoria
				Date fechamodificacion= new Date();
				UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
				petfotoinstalacion.setFechamodificacion(fechamodificacion);
				petfotoinstalacion.setIplog(usuarioBean.getIp());
								
				Setusuario setusuario = new Setusuario();
			    setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			    petfotoinstalacion.setSetusuario(setusuario);
						
				petfotoinstalacionDAO.updatePetfotoinstalacion(session, petfotoinstalacion);
				
				session.getTransaction().commit();
				ok = true;
			}	
		}catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e);
		}finally {
			session.close();
		}	
		
		return ok;
	}
	
	public boolean grabarLisPetfotoinstalacion(List<Petfotoinstalacion> lisPetfotoinstalacion, List<Petfotoinstalacion> lisPetfotoinstalacionClon) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetfotoinstalacionDAO petfotoinstalacionDAO = new PetfotoinstalacionDAO();
			Date fecharegistro = new Date();
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			
			//Se evalua si han habido cambios en la lista de las fotos
			for(Petfotoinstalacion petfotoinstalacionClon : lisPetfotoinstalacionClon){
				boolean encuentra = false;
				for(Petfotoinstalacion petfotoinstalacionItem : lisPetfotoinstalacion){
					if(petfotoinstalacionClon.getIdfotoinstalacion() == petfotoinstalacionItem.getIdfotoinstalacion()){
						//si encuentra
						encuentra = true;
						
						if(!petfotoinstalacionClon.equals(petfotoinstalacionItem)){
							//si han habido cambios se actualiza
							
							//auditoria
							fecharegistro = new Date();
							petfotoinstalacionItem.setFechamodificacion(fecharegistro);
							petfotoinstalacionItem.setIplog(usuarioBean.getIp());
							petfotoinstalacionItem.setSetusuario(usuarioBean.getSetUsuario());
							
							//actualizar
							petfotoinstalacionDAO.updatePetfotoinstalacion(session, petfotoinstalacionItem);
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
					petfotoinstalacionClon.setSetestado(setestado);
					
					//auditoria
					fecharegistro = new Date();
					petfotoinstalacionClon.setFechamodificacion(fecharegistro);
					petfotoinstalacionClon.setIplog(usuarioBean.getIp());
					petfotoinstalacionClon.setSetusuario(usuarioBean.getSetUsuario());
					
					//actualizar
					petfotoinstalacionDAO.updatePetfotoinstalacion(session, petfotoinstalacionClon);
					
					//eliminar foto del disco
					String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
					
					String rutaArchivo = rutaImagenes + petfotoinstalacionClon.getRuta();
					
					fileUtil.deleteFile(rutaArchivo);
					ok = true;
				}
			}
			
			//Se evalua si han subido nuevas fotos
			for(Petfotoinstalacion petfotoinstalacion : lisPetfotoinstalacion){
				boolean encuentra = false;
				for(Petfotoinstalacion petfotoinstalacionClon : lisPetfotoinstalacionClon){
					if(petfotoinstalacion.getIdfotoinstalacion() == petfotoinstalacionClon.getIdfotoinstalacion()){
						//si encuentra
						encuentra = true; 
						break;
					}
				}
				//no encuentra en lista clonada
				if(!encuentra){
					//es foto nueva
					creaFotoDiscoBD(petfotoinstalacion, session);
					ok = true;
				}
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
	
	private void creaFotoDiscoBD(Petfotoinstalacion petfotoinstalacion, Session session) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		PetfotoinstalacionDAO petfotoinstalacionDAO = new PetfotoinstalacionDAO();
		
		int maxIdfotoinstalacion = petfotoinstalacionDAO.maxIdfotoinstalacion(session)+1;
		
		//foto en disco
		FileUtil fileUtil = new FileUtil();
		FacesUtil facesUtil = new FacesUtil();
		Calendar fecha = Calendar.getInstance();
		
		String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
		String rutaInstalaciones =  fileUtil.getPropertyValue("repositorio-instalaciones") + fecha.get(Calendar.YEAR);
		String nombreArchivo = fecha.get(Calendar.YEAR) + "-" + (fecha.get(Calendar.MONTH) + 1) + "-" + fecha.get(Calendar.DAY_OF_MONTH) + "-" + maxIdfotoinstalacion + "." +fileUtil.getFileExtention(petfotoinstalacion.getNombrearchivo()).toLowerCase();
		
		String rutaCompleta = rutaImagenes + rutaInstalaciones;
		
		if(fileUtil.createDir(rutaCompleta)){
			//crear foto en disco
			String rutaArchivo = rutaCompleta + "/" + nombreArchivo;
			fileUtil.createFile(rutaArchivo, petfotoinstalacion.getImagen());
		}
		
		//foto en BD
		petfotoinstalacion.setIdfotoinstalacion(maxIdfotoinstalacion);
		String rutaBD = rutaInstalaciones + "/" + nombreArchivo;
		petfotoinstalacion.setRuta(rutaBD);
		petfotoinstalacion.setNombrearchivo(nombreArchivo);
		Setestado setestadoPetfotoinstalacion = new Setestado();
		setestadoPetfotoinstalacion.setIdestado(1);
		petfotoinstalacion.setSetestado(setestadoPetfotoinstalacion);
		
		//campo santo grupo por defecto en caso de no venir especificado 
		if(petfotoinstalacion.getGrupo() == 0) {
			petfotoinstalacion.setGrupo(1);
		}
		
		//orden
		int orden = petfotoinstalacionDAO.maxOrden(session);
		petfotoinstalacion.setOrden(orden + 1);
		
		//auditoria
		Date fecharegistro = new Date();
		petfotoinstalacion.setFecharegistro(fecharegistro);
		petfotoinstalacion.setIplog(usuarioBean.getIp());
		petfotoinstalacion.setSetusuario(usuarioBean.getSetUsuario());
		
		//ingresar foto en BD
		petfotoinstalacionDAO.savePetfotoinstalacion(session, petfotoinstalacion);
	}
	
	public boolean eliminarPetfotoinstalacion(Petfotoinstalacion petfotoinstalacion ,Petfotoinstalacion petfotoinstalacionclone, int idestado) throws Exception{
		Session session = null;
		boolean ok = false;
		
		try{
			
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			if(petfotoinstalacion!=null && petfotoinstalacionclone !=null){
			 
				//auditoria
				Date fechamodificacion= new Date();
				UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
				petfotoinstalacion.setFechamodificacion(fechamodificacion);
				petfotoinstalacion.setIplog(usuarioBean.getIp());
								
				Setusuario setusuario = new Setusuario();
			    setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			    petfotoinstalacion.setSetusuario(setusuario);
			    
			    Setestado  setestado = new Setestado();
			    setestado.setIdestado(idestado);
			    petfotoinstalacion.setSetestado(setestado);
			    
						
				petfotoinstalacionDAO.updatePetfotoinstalacion(session, petfotoinstalacion);
				
				session.getTransaction().commit();
				
				//eliminar foto del disco
				FacesUtil facesUtil = new FacesUtil();
				FileUtil fileUtil = new FileUtil();
				String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
				String rutaArchivo = rutaImagenes + petfotoinstalacion.getRuta();
				fileUtil.deleteFile(rutaArchivo);
			
				ok = true;
			}	
		}catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e);
		}finally {
			session.close();
		}	
		
		return ok;
	}
	
}
