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
	
	public List<Petfotoinstalacion> lisPetfotoinstalacion(int idestado) throws Exception {
		List<Petfotoinstalacion> lisPetfotoinstalacion = null;
		Session session = null;
	
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            lisPetfotoinstalacion = petfotoinstalacionDAO.lisPetfotoinstalacion(session,idestado);
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
