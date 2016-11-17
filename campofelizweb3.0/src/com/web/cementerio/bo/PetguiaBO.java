package com.web.cementerio.bo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.PetfotoguiaDAO;
import com.web.cementerio.dao.PetguiaDAO;
import com.web.cementerio.pojo.annotations.Petfotoguia;
import com.web.cementerio.pojo.annotations.Petguia;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.HibernateUtil;

public class PetguiaBO {
	
	public  PetguiaBO(){
	}
	
	public Petguia getPetguiabyId(int idguia) throws Exception{
		Petguia petguia = null;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			PetguiaDAO petguiaDAO = new PetguiaDAO();
			petguia = petguiaDAO.getPetguiaById(session, idguia);
		} catch (Exception e) {
			throw new Exception(e); 
		} finally{
			session.close();
		}
		
		return petguia;
	}
	
	public int getMaxOrden() throws Exception{
		int orden = 0;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			PetguiaDAO petguiaDAO = new PetguiaDAO();
			
			orden = petguiaDAO.maxOrden(session);
				
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return orden;
	}
	
	public Petguia getPetguiaByIdConObjetos(int idguia) throws Exception {
		Petguia petguia = null;
		Session session = null;
		
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            PetguiaDAO petguiaDAO = new PetguiaDAO();
            
            petguia = petguiaDAO.getPetguiaByIdConObjetos(session, idguia);
        }
        catch(Exception ex){
            throw new Exception(ex);
        }
        finally{
            session.close();
        }
		
		return petguia;
	}

	public List<Petguia> lisPetguiaPrincipales(int maxresultado) throws Exception {
		List<Petguia> listPetguia = null;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			PetguiaDAO petguiaDAO = new PetguiaDAO();
			listPetguia = petguiaDAO.getListpetguiaByPrincipal(session, maxresultado);
		} catch (Exception e) {
			throw new Exception(e);
		} finally{
			session.close();
		}
		
		return listPetguia;
		
	}
	
	public List<Petguia> lisPetguiaBusquedaByPage(String[] texto, int pageSize, int pageNumber, int args[]) throws RuntimeException {
		List<Petguia> listpetguia = null;
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			PetguiaDAO petguiaDAO = new PetguiaDAO();
			
			listpetguia = petguiaDAO.lisPetguiaBusquedaByPage(session, texto, pageSize, pageNumber, args);
		} catch (Exception e) {
			 throw new RuntimeException(e);
		}
		finally{
			session.close();
		}
		
		return listpetguia;
	}
	
	public boolean ingresar(Petguia petguia, List<Petfotoguia> lisPetfotoguia) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetguiaDAO petguiaDAO = new PetguiaDAO();
			
			//secuencia
			int maxIdguia = petguiaDAO.getMaxidpetguia(session) + 1;
			petguia.setIdguia(maxIdguia);
			Setestado setestado = new Setestado();
			setestado.setIdestado(1);
			petguia.setSetestado(setestado);

			//auditoria
			Date fecharegistro = new Date();
			petguia.setFecharegistro(fecharegistro);
			petguia.setIplog(usuarioBean.getIp());
			petguia.setSetusuario(usuarioBean.getSetUsuario());
	
			//ingresar 
			petguiaDAO.savePetguia(session, petguia);
			
			//Si subio foto se crea en disco y en base
			for(Petfotoguia petfotoguia : lisPetfotoguia){
				creaFotoDiscoBD(petguia, petfotoguia, session);
			}
			if(lisPetfotoguia != null && lisPetfotoguia.size() > 0){
				//se setea la ruta de la foto tambien en petnoticia.rutafoto
				petguia.setRutafoto(lisPetfotoguia.get(0).getRuta());
				//update
				petguiaDAO.updatePetguia(session, petguia);
			}
			
			session.getTransaction().commit();
			
			ok = true;
		}catch(Exception e){
			petguia.setIdguia(0);
			session.getTransaction().rollback();
			throw new Exception(e); 
		}finally{
			session.close();
		}
		
		return ok;
	}
	
	public boolean modificar(Petguia petguia, Petguia petguiaClon, List<Petfotoguia> lisPetfotoguia, List<Petfotoguia> lisPetfotoguiaClon) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetguiaDAO petguiaDAO = new PetguiaDAO();
			PetfotoguiaDAO petfotoguiaDAO = new PetfotoguiaDAO();
			Date fecharegistro = new Date();
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			
			//Se evalua si han habido cambios en la lista de las fotos
			for(Petfotoguia petfotoguiaClon : lisPetfotoguiaClon){
				boolean encuentra = false;
				for(Petfotoguia petfotoguiaItem : lisPetfotoguia){
					if(petfotoguiaClon.getIdfotoguia() == petfotoguiaItem.getIdfotoguia()){
						//si encuentra
						encuentra = true;
						
						if(!petfotoguiaClon.equals(petfotoguiaItem)){
							//si han habido cambios se actualiza
							
							//auditoria
							fecharegistro = new Date();
							petfotoguiaItem.setFechamodificacion(fecharegistro);
							petfotoguiaItem.setIplog(usuarioBean.getIp());
							petfotoguiaItem.setSetusuario(usuarioBean.getSetUsuario());
							
							//actualizar
							petfotoguiaDAO.modificarPetfotoguia(session, petfotoguiaItem);
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
					petfotoguiaClon.setSetestado(setestado);
					
					//auditoria
					fecharegistro = new Date();
					petfotoguiaClon.setFechamodificacion(fecharegistro);
					petfotoguiaClon.setIplog(usuarioBean.getIp());
					petfotoguiaClon.setSetusuario(usuarioBean.getSetUsuario());
					
					//actualizar
					petfotoguiaDAO.modificarPetfotoguia(session, petfotoguiaClon);
					
					//eliminar foto del disco
					String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
					
					String rutaArchivo = rutaImagenes + "/" + petfotoguiaClon.getRuta();
					
					fileUtil.deleteFile(rutaArchivo);
					ok = true;
				}
			}
			
			//Se evalua si han subido nuevas fotos
			for(Petfotoguia petfotoguia : lisPetfotoguia){
				boolean encuentra = false;
				for(Petfotoguia petfotoguiaClon : lisPetfotoguiaClon){
					if(petfotoguia.getIdfotoguia() == petfotoguiaClon.getIdfotoguia()){
						//si encuentra
						encuentra = true; 
						break;
					}
				}
				//no encuentra en lista clonada
				if(!encuentra){
					//es foto nueva
					creaFotoDiscoBD(petguia, petfotoguia, session);
					ok = true;
				}
			}
			
			if(lisPetfotoguia != null && lisPetfotoguia.size() > 0){
				//si no tiene imagen principal se setea
				if(petguia.getRutafoto() == null || petguia.getRutafoto().trim().length() == 0){
					petguia.setRutafoto(lisPetfotoguia.get(0).getRuta());
				}
			}
			
			//Se graba el registro si han habido cambios
			if(!petguia.equals(petguiaClon)){
				//auditoria
				petguia.setFechamodificacion(fecharegistro);
				petguia.setIplog(usuarioBean.getIp());
				petguia.setSetusuario(usuarioBean.getSetUsuario());
		
				//actualizar
				petguiaDAO.updatePetguia(session, petguia);
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
	
	private void creaFotoDiscoBD(Petguia petguia, Petfotoguia petfotoguia, Session session) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		PetfotoguiaDAO petfotoguiaDAO = new PetfotoguiaDAO();
		
		int maxIdPetfotoguia = petfotoguiaDAO.maxIdPetfotoguia(session) + 1;
		int cantFotosPorGuia = petfotoguiaDAO.cantFotosPorGuia(session, petfotoguia.getIdfotoguia()) + 1;
		
		//foto en disco
		FileUtil fileUtil = new FileUtil();
		FacesUtil facesUtil = new FacesUtil();
		Calendar fecha = Calendar.getInstance();
		
		String rutaCarpetaImagenes = facesUtil.getContextParam("imagesDirectory");
		String rutaImagenes =  fileUtil.getPropertyValue("repositorio-guia") + "/" + fecha.get(Calendar.YEAR);
		String nombreArchivo = fecha.get(Calendar.YEAR) + "-" + (fecha.get(Calendar.MONTH) + 1) + "-" + fecha.get(Calendar.DAY_OF_MONTH) + "-" + petguia.getIdguia() + "-" + cantFotosPorGuia + "." + fileUtil.getFileExtention(petfotoguia.getNombrearchivo()).toLowerCase();
		
		String rutaCompleta = rutaCarpetaImagenes + "/" + rutaImagenes;
		
		if(fileUtil.createDir(rutaCompleta)){
			//crear foto en disco
			String rutaArchivo = rutaCompleta + "/" + nombreArchivo;
			fileUtil.createFile(rutaArchivo,petfotoguia.getImagen());
		}
		
		//foto en BD
		petfotoguia.setIdfotoguia(maxIdPetfotoguia);
		petfotoguia.setPetguia(petguia);
		String rutaBD = "/" + rutaImagenes + "/" + nombreArchivo;
		petfotoguia.setRuta(rutaBD);
		petfotoguia.setNombrearchivo(nombreArchivo);
		Setestado setestado = new Setestado();
		setestado.setIdestado(1);
		petfotoguia.setSetestado(setestado);
		
		//auditoria
		Date fecharegistro = new Date();
		petfotoguia.setFecharegistro(fecharegistro);
		petfotoguia.setIplog(usuarioBean.getIp());
		petfotoguia.setSetusuario(usuarioBean.getSetUsuario());
		
		//ingresar foto en BD
		petfotoguiaDAO.savePetfotoguia(session, petfotoguia);
	}
	
	public boolean eliminar(Petguia petguia) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetguiaDAO petguiaDAO = new PetguiaDAO();
			PetfotoguiaDAO petfotoguiaDAO = new PetfotoguiaDAO();
			
			//se eliminan fisicamente las fotos asociadas a la noticia
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
			
			for(Petfotoguia petfotoguia : petguia.getPetfotoguias()){
				//se inactivan todas las fotos asociadas
				Setestado setestado = new Setestado();
				setestado.setIdestado(2);
				petfotoguia.setSetestado(setestado);
				
				//auditoria
				Date fecharegistro = new Date();
				petfotoguia.setFechamodificacion(fecharegistro);
				petfotoguia.setIplog(usuarioBean.getIp());
				petfotoguia.setSetusuario(usuarioBean.getSetUsuario());
				
				//actualizar
				petfotoguiaDAO.modificarPetfotoguia(session, petfotoguia);
				
				//se elimina el archivo de imagen
				String rutaArchivo = rutaImagenes + "/" + petfotoguia.getRuta();
				fileUtil.deleteFile(rutaArchivo);
			}			 
			
			//se inactiva el registro
			Setestado setestado = new Setestado();
			setestado.setIdestado(2);
			petguia.setSetestado(setestado);
			
			//auditoria
			Date fecharegistro = new Date();
			petguia.setFechamodificacion(fecharegistro);
			petguia.setIplog(usuarioBean.getIp());
			petguia.setSetusuario(usuarioBean.getSetUsuario());
			
			petguiaDAO.updatePetguia(session, petguia);
			
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
