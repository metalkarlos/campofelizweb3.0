package com.web.cementerio.bo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.PetfotomascotaDAO;
import com.web.cementerio.dao.PetmascotahomenajeDAO;
import com.web.cementerio.pojo.annotations.Petespecie;
import com.web.cementerio.pojo.annotations.Petfotomascota;
import com.web.cementerio.pojo.annotations.Petmascotahomenaje;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.HibernateUtil;

public class PetmascotahomenajeBO {
	
	PetmascotahomenajeDAO petmascotahomenajeDAO;
	
	public  PetmascotahomenajeBO(){
		petmascotahomenajeDAO = new PetmascotahomenajeDAO();
	}
	
	public Petmascotahomenaje getPetmascotahomenajebyId(int idmascota)throws Exception{
		Petmascotahomenaje petmascotahomenaje = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			petmascotahomenaje = petmascotahomenajeDAO.getPethomenajemascotaById(session,idmascota);
		} catch (Exception e) {
			throw new Exception(e.getMessage(),e.getCause()); 
		}
		finally{
			session.close();
		}
		return petmascotahomenaje;
	}
	

	
	public List<Petmascotahomenaje> lisPetmascotahomenajeBusquedaByPage(String[] texto, int pageSize, int pageNumber, int args[]) throws RuntimeException {
		List<Petmascotahomenaje> listpetmascotahomenaje = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			listpetmascotahomenaje = petmascotahomenajeDAO.lisPetmascotaBusquedaByPage(session, texto, pageSize, pageNumber, args);
			
		} catch (Exception e) {
			 throw new RuntimeException(e.getMessage(),e.getCause());
		}
		finally{
			session.close();
		}
		
		
		return listpetmascotahomenaje;
	}
	
	public List<Petmascotahomenaje> lisPetmascotaByNombreIdespecie (String[] texto, int idespecie, int pageSize) throws Exception {
		List<Petmascotahomenaje> listpetmascotahomenaje = null;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			listpetmascotahomenaje = petmascotahomenajeDAO.lisPetmascotaByNombreIdespecie(session, texto, idespecie, pageSize);
			
		} catch (Exception e) {
			 throw new Exception(e.getMessage(),e.getCause());
		}
		finally{
			session.close();
		}
		
		
		return listpetmascotahomenaje;
	}
	
	public List<Petmascotahomenaje>lisPetmascotaPrincipal(int maxresultado)throws Exception{
		List<Petmascotahomenaje> listpetmascotahomenaje = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			listpetmascotahomenaje = petmascotahomenajeDAO.lisPetmascotaPrincipal(session, maxresultado);
		} catch (Exception e) {
			throw new Exception(e.getMessage(),e.getCause());
		}finally{
			session.close();
		}
		return listpetmascotahomenaje;
	}
	
	public List<Petespecie> lisPetespecieMascotas(String[] texto)throws Exception{
		List<Petespecie> lisPetespecie = null;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			lisPetespecie = petmascotahomenajeDAO.lisPetespecieMascotas(session, texto);
		} catch (Exception e) {
			throw new Exception(e.getMessage(),e.getCause());
		}finally{
			session.close();
		}
		
		return lisPetespecie;
	}
	
	public List<Petmascotahomenaje> lisPetmascotabyParams(Petmascotahomenaje petmascotahomenajeParams)throws Exception{
		List<Petmascotahomenaje> listpetmascotahomenaje = null;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			listpetmascotahomenaje = petmascotahomenajeDAO.lisPetmascotabyParams(session, petmascotahomenajeParams);
		} catch (Exception e) {
			throw new Exception(e.getMessage(),e.getCause());
		}finally{
			session.close();
		}
		
		return listpetmascotahomenaje;
	}
	
	public boolean  ingresarPetmascotahomenajeBO(Petmascotahomenaje petmascotahomenaje,List<Petfotomascota> lisPetfotomascota) throws Exception{
		boolean ok = false;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			
			//mascota
			petmascotahomenaje.setIdmascota(petmascotahomenajeDAO.maxIdpetmascotahomenaje(session));
			petmascotahomenaje.setNombre(petmascotahomenaje.getNombre().toUpperCase());
			petmascotahomenaje.setFamilia(petmascotahomenaje.getFamilia().toUpperCase());
			Setestado setestado = new Setestado();
			setestado.setIdestado(1);
			petmascotahomenaje.setSetestado(setestado);	
			
			//auditoria
			Date fecharegistro = new Date();
			petmascotahomenaje.setFecharegistro(fecharegistro);
			petmascotahomenaje.setIplog(usuarioBean.getIp());
			petmascotahomenaje.setSetusuario(usuarioBean.getSetUsuario());
					
			//ingresar mascota
			petmascotahomenajeDAO.ingresarPetmascotahomenaje(session, petmascotahomenaje);
			
			//Si subio foto se crea en disco y en base
			for(Petfotomascota petfotomascota : lisPetfotomascota){
				creaFotoDiscoBD(petmascotahomenaje, petfotomascota, session);
			}
			if(lisPetfotomascota != null && lisPetfotomascota.size() > 0){
				//se setea la ruta de la foto tambien en petnoticia.rutafoto
				petmascotahomenaje.setRutafoto(lisPetfotomascota.get(0).getRuta());
				//update
				petmascotahomenajeDAO.modificarPetmascotahomenaje(session, petmascotahomenaje);
			}
			
			session.getTransaction().commit();
			
			ok = true;
			
		} catch (Exception e) {
			petmascotahomenaje.setIdmascota(0);
			session.getTransaction().rollback();
			throw new Exception(e.getMessage(),e.getCause());
		}finally {
			session.close();
		}

		return ok;
	}
	
	public boolean modificarPetmascotahomenajeBO(Petmascotahomenaje petmascotahomenaje,Petmascotahomenaje petmascotahomenajeclone, List<Petfotomascota> lisPetfotomascota,List<Petfotomascota> lisPetfotomascotaClon) throws Exception{
		boolean ok = false;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetfotomascotaDAO petfotomascotaDAO = new PetfotomascotaDAO();
			Date fecharegistro = new Date();
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			
			//Se evalua si han habido cambios en la lista de las fotos
			for(Petfotomascota petfotomascotaClon : lisPetfotomascotaClon){
				boolean encuentra = false;
				for(Petfotomascota petfotomascotaItem : lisPetfotomascota){
					if(petfotomascotaClon.getIdfotomascota() == petfotomascotaItem.getIdfotomascota()){
						//si encuentra
						encuentra = true;
						
						if(!petfotomascotaClon.equals(petfotomascotaItem)){
							//si han habido cambios se actualiza
							
							//auditoria
							fecharegistro = new Date();
							petfotomascotaItem.setFechamodificacion(fecharegistro);
							petfotomascotaItem.setIplog(usuarioBean.getIp());
							petfotomascotaItem.setSetusuario(usuarioBean.getSetUsuario());
							
							//actualizar
							petfotomascotaDAO.modificarFotomascota(session, petfotomascotaItem);
							ok = true;
						}
						
						break;
					}
				}
				if(!encuentra){
					//no encuentra
					//inhabilitar
					Setestado setestado = new Setestado();
					setestado.setIdestado(2);
					petfotomascotaClon.setSetestado(setestado);
					
					//auditoria
					fecharegistro = new Date();
					petfotomascotaClon.setFechamodificacion(fecharegistro);
					petfotomascotaClon.setIplog(usuarioBean.getIp());
					petfotomascotaClon.setSetusuario(usuarioBean.getSetUsuario());
					
					//actualizar
					petfotomascotaDAO.modificarFotomascota(session, petfotomascotaClon);
					
					//eliminar foto del disco
					String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
					
					String rutaArchivo = rutaImagenes + petfotomascotaClon.getRuta();
					
					fileUtil.deleteFile(rutaArchivo);
					ok = true;
				}
			}
			
			//Se evalua si han subido nuevas fotos
			for(Petfotomascota petfotomascota : lisPetfotomascota){
				boolean encuentra = false;
				for(Petfotomascota petfotomascotaClon : lisPetfotomascotaClon){
					if(petfotomascota.getIdfotomascota() == petfotomascotaClon.getIdfotomascota()){
						//si encuentra
						encuentra = true; 
						break;
					}
				}
				//no encuentra en lista clonada
				if(!encuentra){
					//es foto nueva
					creaFotoDiscoBD(petmascotahomenaje, petfotomascota, session);
					ok = true;
				}
			}
			
			if(lisPetfotomascota != null && lisPetfotomascota.size() > 0){
				//si no tiene imagen principal se setea
				if(petmascotahomenaje.getRutafoto() == null || petmascotahomenaje.getRutafoto().trim().length() == 0){
					petmascotahomenaje.setRutafoto(lisPetfotomascota.get(0).getRuta());
				}
				ok = true;
			}
			
			//Se graba el servicio si han habido cambios
			if(!petmascotahomenaje.equals(petmascotahomenajeclone)){
				//auditoria
				petmascotahomenaje.setFechamodificacion(fecharegistro);
				petmascotahomenaje.setIplog(usuarioBean.getIp());
				petmascotahomenaje.setSetusuario(usuarioBean.getSetUsuario());
		
				//actualizar
				petmascotahomenajeDAO.modificarPetmascotahomenaje(session, petmascotahomenaje);
				ok = true;
			}
			
			if(ok){
				session.getTransaction().commit();
			}
			
		} catch (Exception e) {
			ok = false;
			session.getTransaction().rollback();
			throw new Exception(e.getMessage(),e.getCause());
		}finally {
			session.close();
		}
		
		return ok;
	}
	
	private void creaFotoDiscoBD(Petmascotahomenaje petmascotahomenaje, Petfotomascota petfotomascota, Session session) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		PetfotomascotaDAO petfotomascotaDAO = new PetfotomascotaDAO();
		
		int maxIdfotomascota = petfotomascotaDAO.getMaxidpetfotomascota(session)+1;
		int cantFotosPorMascota = petfotomascotaDAO.getCantFotosPorMascota(session, petmascotahomenaje.getIdmascota())+1;
		
		//foto en disco
		FileUtil fileUtil = new FileUtil();
		FacesUtil facesUtil = new FacesUtil();
		Calendar fecha = Calendar.getInstance();
		
		String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
		String rutaMascotas =  fileUtil.getPropertyValue("repositorio-mascota") + fecha.get(Calendar.YEAR);
		String nombreArchivo = fecha.get(Calendar.YEAR) + "-" + (fecha.get(Calendar.MONTH) + 1) + "-" + fecha.get(Calendar.DAY_OF_MONTH) + "-" + petmascotahomenaje.getIdmascota() + "-" + cantFotosPorMascota + "." + fileUtil.getFileExtention(petfotomascota.getNombrearchivo()).toLowerCase();
		
		String rutaCompleta = rutaImagenes + rutaMascotas;
		
		if(fileUtil.createDir(rutaCompleta)){
			//crear foto en disco
			String rutaArchivo = rutaCompleta + "/" + nombreArchivo;
			fileUtil.createFile(rutaArchivo,petfotomascota.getImagen());
		}
		
		//foto en BD
		petfotomascota.setIdfotomascota(maxIdfotomascota);
		petfotomascota.setPetmascotahomenaje(petmascotahomenaje);
		String rutaBD = rutaMascotas + "/" + nombreArchivo;
		petfotomascota.setRuta(rutaBD);
		petfotomascota.setNombrearchivo(nombreArchivo);
		Setestado setestadoPetfotomascota = new Setestado();
		setestadoPetfotomascota.setIdestado(1);
		petfotomascota.setSetestado(setestadoPetfotomascota);
		
		//auditoria
		Date fecharegistro = new Date();
		petfotomascota.setFecharegistro(fecharegistro);
		petfotomascota.setIplog(usuarioBean.getIp());
		petfotomascota.setSetusuario(usuarioBean.getSetUsuario());
		
		//ingresar foto en BD
		petfotomascotaDAO.ingresarFotomascota(session, petfotomascota);
	}
	
	public boolean  eliminarPetmascotahomenajeBO(Petmascotahomenaje petmascotahomenaje)throws Exception{
		boolean ok = false;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetfotomascotaDAO petfotomascotaDAO = new PetfotomascotaDAO();
			
			//se eliminan fisicamente las fotos asociadas a la mascota
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
			
			for(Petfotomascota tmp : petmascotahomenaje.getPetfotomascotas()){
				//se inactivan todas las fotos asociadas a la mascota
				Setestado setestado = new Setestado();
				setestado.setIdestado(2);
				tmp.setSetestado(setestado);
				
				//auditoria
				Date fecharegistro = new Date();
				tmp.setFechamodificacion(fecharegistro);
				tmp.setIplog(usuarioBean.getIp());
				tmp.setSetusuario(usuarioBean.getSetUsuario());
				
				//actualizar
				petfotomascotaDAO.modificarFotomascota(session, tmp);
				
				//se elimina el archivo de imagen
				String rutaArchivo = rutaImagenes + tmp.getRuta();
				fileUtil.deleteFile(rutaArchivo);
			}
			
			//se inactiva el registro
			Setestado setestado = new Setestado();
			setestado.setIdestado(2);
			petmascotahomenaje.setSetestado(setestado);
			
			//auditoria
			Date fecharegistro = new Date();
			petmascotahomenaje.setFechamodificacion(fecharegistro);
			petmascotahomenaje.setIplog(usuarioBean.getIp());
			petmascotahomenaje.setSetusuario(usuarioBean.getSetUsuario());
			
			petmascotahomenajeDAO.modificarPetmascotahomenaje(session, petmascotahomenaje);
			
			session.getTransaction().commit();
			
			ok = true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e.getMessage(),e.getCause());
		}finally{
			session.close();
		}
		
		return ok;
	}
	
}
