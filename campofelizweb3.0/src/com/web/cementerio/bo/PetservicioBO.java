package com.web.cementerio.bo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.primefaces.model.UploadedFile;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.PetfotoservicioDAO;
import com.web.cementerio.dao.PetservicioDAO;
import com.web.cementerio.pojo.annotations.Petfotoservicio;
import com.web.cementerio.pojo.annotations.Petservicio;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.HibernateUtil;

public class PetservicioBO {

	private PetservicioDAO petservicioDAO;
	
	public PetservicioBO() {
		petservicioDAO = new PetservicioDAO();
	}
	
	public int getMaxOrden() throws Exception{
		int orden = 0;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			orden = petservicioDAO.maxOrden(session);
				
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return orden;
	}
	
	public List<Petservicio> lisPetservicioPrincipales() throws Exception {
		List<Petservicio> lisPetservicio = null;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			lisPetservicio = petservicioDAO.lisPetservicioPrincipales(session);
		}catch(Exception e){
			throw new Exception(e.getMessage(),e.getCause());
		}finally{
			session.close();
		}
		
		return lisPetservicio;
	}
	
	public List<Petservicio> lisPetservicio(int idempresa, int idoficina) throws Exception {
		List<Petservicio> lisPetservicio = null;
		Session session = null;
	
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			lisPetservicio = petservicioDAO.lisPetservicio(session, idempresa, idoficina);
		}catch(Exception e){
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return lisPetservicio;
	}
	
	public List<Petservicio> lisPetservicioBusquedaByPage(String[] texto, int idempresa, int pageSize, int pageNumber, int args[]) throws RuntimeException {
		List<Petservicio> lisPetservicio = null;
		Session session = null;
		
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            lisPetservicio = petservicioDAO.lisPetservicioBusquedaByPage(session, texto, idempresa, pageSize, pageNumber, args);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
        finally{
            session.close();
        }
		
		return lisPetservicio;
	}
	
	public Petservicio getPetservicioConObjetosById(int idservicio, int idempresa) throws Exception {
		Petservicio petservicio = null;
		Session session = null;
		
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            petservicio = petservicioDAO.getPetservicioConObjetosById(session, idservicio, idempresa);
        }
        catch(Exception ex){
            throw new Exception(ex);
        }
        finally{
            session.close();
        }
		
		return petservicio;
	}
	
	public Petservicio getPetservicioById(int idservicio) throws Exception {
		Petservicio petservicio = null;
		Session session = null;
		
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            petservicio = petservicioDAO.getPetservicioById(session, idservicio);
        }
        catch(Exception ex){
            throw new Exception(ex);
        }
        finally{
            session.close();
        }
		
		return petservicio;
	}
	
	public boolean ingresar(Petservicio petservicio, Petfotoservicio petfotoservicio, UploadedFile uploadedFile) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			
			//servicio
			int maxIdservicio = petservicioDAO.maxIdservicio(session)+1;
			petservicio.setIdservicio(maxIdservicio);
			Setestado setestadoPetservicio = new Setestado();
			setestadoPetservicio.setIdestado(1);
			petservicio.setSetestado(setestadoPetservicio);

			//auditoria
			Date fecharegistro = new Date();
			petservicio.setFecharegistro(fecharegistro);
			petservicio.setIplog(usuarioBean.getIp());
			petservicio.setSetusuario(usuarioBean.getSetUsuario());
	
			//ingresar servicio
			petservicioDAO.savePetservicio(session, petservicio);
			
			//Si subio foto se crea en disco y en base
			if(uploadedFile != null){
				creaFotoDiscoBD(petservicio, petfotoservicio, uploadedFile, session);
				//se setea la ruta de la foto tambien en petnoticia.rutafoto
				petservicio.setRutafoto(petfotoservicio.getRuta());
				//update
				petservicioDAO.updatePetservicio(session, petservicio);
			}
			
			session.getTransaction().commit();
			
			ok = true;
		}catch(Exception e){
			petservicio.setIdservicio(0);
			session.getTransaction().rollback();
			throw new Exception(e); 
		}finally{
			session.close();
		}
		
		return ok;
	}
	
	public boolean eliminar(Petservicio petservicio) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetfotoservicioDAO petfotoservicioDAO = new PetfotoservicioDAO();
			
			//se eliminan fisicamente las fotos asociadas a la noticia
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
			
			for(Petfotoservicio tmp : petservicio.getPetfotoservicios()){
				//se inactivan todas las fotos asociadas al servicio
				Setestado setestado = new Setestado();
				setestado.setIdestado(2);
				tmp.setSetestado(setestado);
				
				//auditoria
				Date fecharegistro = new Date();
				tmp.setFechamodificacion(fecharegistro);
				tmp.setIplog(usuarioBean.getIp());
				tmp.setSetusuario(usuarioBean.getSetUsuario());
				
				//actualizar
				petfotoservicioDAO.updatePetfotoservicio(session, tmp);
				
				//se elimina el archivo de imagen
				String rutaArchivo = rutaImagenes + tmp.getRuta();
				fileUtil.deleteFile(rutaArchivo);
			}			 
			
			//se inactiva el registro
			Setestado setestado = new Setestado();
			setestado.setIdestado(2);
			petservicio.setSetestado(setestado);
			
			//auditoria
			Date fecharegistro = new Date();
			petservicio.setFechamodificacion(fecharegistro);
			petservicio.setIplog(usuarioBean.getIp());
			petservicio.setSetusuario(usuarioBean.getSetUsuario());
			
			petservicioDAO.updatePetservicio(session, petservicio);
			
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
	
	public boolean modificar(Petservicio petservicio, Petservicio petservicioClon, List<Petfotoservicio> lisPetfotoservicio, List<Petfotoservicio> lisPetfotoservicioClon, Petfotoservicio petfotoservicio, UploadedFile uploadedFile) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetfotoservicioDAO petfotoservicioDAO = new PetfotoservicioDAO();
			Date fecharegistro = new Date();
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			
			//Se evalua si han habido cambios en la lista de las fotos
			for(Petfotoservicio petfotoservicioClon : lisPetfotoservicioClon){
				boolean encuentra = false;
				for(Petfotoservicio petfotoservicioItem : lisPetfotoservicio){
					if(petfotoservicioClon.getIdfotoservicio() == petfotoservicioItem.getIdfotoservicio()){
						//si encuentra
						encuentra = true;
						
						if(!petfotoservicioClon.equals(petfotoservicioItem)){
							//si han habido cambios se actualiza
							
							//auditoria
							fecharegistro = new Date();
							petfotoservicioItem.setFechamodificacion(fecharegistro);
							petfotoservicioItem.setIplog(usuarioBean.getIp());
							petfotoservicioItem.setSetusuario(usuarioBean.getSetUsuario());
							
							//actualizar
							petfotoservicioDAO.updatePetfotoservicio(session, petfotoservicioItem);
							ok = true;
						}
					}
				}
				if(!encuentra){
					//no encuentra
					//inhabilitar
					Setestado setestado = new Setestado();
					setestado.setIdestado(2);
					petfotoservicioClon.setSetestado(setestado);
					
					//auditoria
					fecharegistro = new Date();
					petfotoservicioClon.setFechamodificacion(fecharegistro);
					petfotoservicioClon.setIplog(usuarioBean.getIp());
					petfotoservicioClon.setSetusuario(usuarioBean.getSetUsuario());
					
					//actualizar
					petfotoservicioDAO.updatePetfotoservicio(session, petfotoservicioClon);
					
					//eliminar foto del disco
					String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
					
					String rutaArchivo = rutaImagenes + petfotoservicioClon.getRuta();
					
					fileUtil.deleteFile(rutaArchivo);
					ok = true;
				}
			}
			
			//Si subio foto se crea en disco y en base
			if(uploadedFile != null){
				creaFotoDiscoBD(petservicio, petfotoservicio, uploadedFile, session);
				//si no tiene imagen principal se setea
				if(petservicio.getRutafoto() == null || petservicio.getRutafoto().trim().length() == 0){
					petservicio.setRutafoto(petfotoservicio.getRuta());
				}
				ok = true;
			}
			
			//Se graba el servicio si han habido cambios
			if(!petservicio.equals(petservicioClon)){
				//auditoria
				petservicio.setFechamodificacion(fecharegistro);
				petservicio.setIplog(usuarioBean.getIp());
				petservicio.setSetusuario(usuarioBean.getSetUsuario());
		
				//actualizar
				petservicioDAO.updatePetservicio(session, petservicio);
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
	
	private void creaFotoDiscoBD(Petservicio petservicio, Petfotoservicio petfotoservicio, UploadedFile uploadedFile, Session session) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		PetfotoservicioDAO petfotoservicioDAO = new PetfotoservicioDAO();
		
		int maxIdfotoservicio = petfotoservicioDAO.maxIdfotoservicio(session)+1;
		int cantFotosPorServicio = petfotoservicioDAO.cantFotosPorServicio(session, petservicio.getIdservicio())+1;
		
		//foto en disco
		FileUtil fileUtil = new FileUtil();
		FacesUtil facesUtil = new FacesUtil();
		Calendar fecha = Calendar.getInstance();
		
		String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
		String rutaServicios =  fileUtil.getPropertyValue("repositorio-servicios") + fecha.get(Calendar.YEAR);
		String nombreArchivo = fecha.get(Calendar.YEAR) + "-" + (fecha.get(Calendar.MONTH) + 1) + "-" + fecha.get(Calendar.DAY_OF_MONTH) + "-" + petservicio.getIdservicio() + "-" + cantFotosPorServicio + "." + fileUtil.getFileExtention(uploadedFile.getFileName()).toLowerCase();
		
		String rutaCompleta = rutaImagenes + rutaServicios;
		
		if(fileUtil.createDir(rutaCompleta)){
			//crear foto en disco
			String rutaArchivo = rutaCompleta + "/" + nombreArchivo;
			fileUtil.createFile(rutaArchivo,uploadedFile.getContents());
		}
		
		//foto en BD
		petfotoservicio.setIdfotoservicio(maxIdfotoservicio);
		petfotoservicio.setPetservicio(petservicio);
		String rutaBD = rutaServicios + "/" + nombreArchivo;
		petfotoservicio.setRuta(rutaBD);
		petfotoservicio.setNombrearchivo(nombreArchivo);
		petfotoservicio.setPerfil(1);//campo sin uso ya que tabla principal posee ruta de foto de perfil
		Setestado setestadoPetfotoservicio = new Setestado();
		setestadoPetfotoservicio.setIdestado(1);
		petfotoservicio.setSetestado(setestadoPetfotoservicio);
		
		//auditoria
		Date fecharegistro = new Date();
		petfotoservicio.setFecharegistro(fecharegistro);
		petfotoservicio.setIplog(usuarioBean.getIp());
		petfotoservicio.setSetusuario(usuarioBean.getSetUsuario());
		
		//ingresar foto en BD
		petfotoservicioDAO.savePetfotoservicio(session, petfotoservicio);
	}
}
