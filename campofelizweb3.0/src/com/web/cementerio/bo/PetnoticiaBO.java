package com.web.cementerio.bo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.primefaces.model.UploadedFile;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.PetfotonoticiaDAO;
import com.web.cementerio.dao.PetnoticiaDAO;
import com.web.cementerio.pojo.annotations.Petfotonoticia;
import com.web.cementerio.pojo.annotations.Petnoticia;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.HibernateUtil;

public class PetnoticiaBO {

	private PetnoticiaDAO petnoticiaDAO;
	
	public PetnoticiaBO() {
		petnoticiaDAO = new PetnoticiaDAO();
	}
	
	public Petnoticia getPetnoticiaById(int idnoticia) throws Exception {
		Petnoticia petnoticia = null;
		Session session = null;
		
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            petnoticia = petnoticiaDAO.getPetnoticiaById(session, idnoticia);
        }
        catch(Exception ex){
            throw new Exception(ex);
        }
        finally{
            session.close();
        }
		
		return petnoticia;
	}
	
	public int getMaxOrden() throws Exception{
		int orden = 0;
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			orden = petnoticiaDAO.maxOrden(session);
				
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return orden;
	}
	
	public Petnoticia getPetnoticiaConObjetosById(int idnoticia) throws Exception {
		Petnoticia petnoticia = null;
		Session session = null;
		
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            petnoticia = petnoticiaDAO.getPetnoticiaConObjetosById(session, idnoticia);
        }
        catch(Exception ex){
            throw new Exception(ex);
        }
        finally{
            session.close();
        }
		
		return petnoticia;
	}
	
	public List<Petnoticia> lisPetnoticiaByPage(int pageSize, int pageNumber, int args[], String titulo, String descripcion) throws RuntimeException {
		List<Petnoticia> listPetnoticia = null;
		Session session = null;
		
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            listPetnoticia = petnoticiaDAO.lisPetnoticiaByPage(session, pageSize, pageNumber, args, titulo, descripcion);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
        finally{
            session.close();
        }
		
		return listPetnoticia;
	}
	
	public List<Petnoticia> lisPetnoticiaBusquedaByPage(String[] texto, int pageSize, int pageNumber, int args[]) throws RuntimeException {
		List<Petnoticia> listPetnoticia = null;
		Session session = null;
		
		try{
            session = HibernateUtil.getSessionFactory().openSession();
            listPetnoticia = petnoticiaDAO.lisPetnoticiaBusquedaByPage(session, texto, pageSize, pageNumber, args);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
        finally{
            session.close();
        }
		
		return listPetnoticia;
	}
	
	public List<Petnoticia> lisPetnoticiaPrincipales() throws Exception {
		List<Petnoticia> lisPetnoticia = null;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			lisPetnoticia = petnoticiaDAO.lisPetnoticiaPrincipales(session);
		}catch(Exception e){
			throw new Exception(e);
		}finally{
			session.close();
		}
		
		return lisPetnoticia;
	}
	
	public boolean ingresar(Petnoticia petnoticia, Petfotonoticia petfotonoticia, UploadedFile uploadedFile) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			
			//noticia
			int maxIdnoticia = petnoticiaDAO.maxIdnoticia(session)+1;
			petnoticia.setIdnoticia(maxIdnoticia);
			Setestado setestadoPetnoticia = new Setestado();
			setestadoPetnoticia.setIdestado(1);
			petnoticia.setSetestado(setestadoPetnoticia);

			//auditoria
			Date fecharegistro = new Date();
			petnoticia.setFecharegistro(fecharegistro);
			petnoticia.setIplog(usuarioBean.getIp());
			petnoticia.setSetusuario(usuarioBean.getSetUsuario());
	
			//ingresar noticia
			petnoticiaDAO.savePetnoticia(session, petnoticia);
			
			//Si subio foto se crea en disco y en base
			if(uploadedFile != null){
				creaFotoDiscoBD(petnoticia, petfotonoticia, uploadedFile, session);
				//se setea la ruta de la foto tambien en petnoticia.rutafoto
				petnoticia.setRutafoto(petfotonoticia.getRuta());
				//update
				petnoticiaDAO.updatePetnoticia(session, petnoticia);
			}
			
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
	
	public boolean eliminar(Petnoticia petnoticia) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetfotonoticiaDAO petfotonoticiaDAO = new PetfotonoticiaDAO();
			
			//se eliminan fisicamente las fotos asociadas a la noticia
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
			
			for(Petfotonoticia tmp : petnoticia.getPetfotonoticias()){
				//se inactivan todas las fotos asociadas a la noticia
				Setestado setestado = new Setestado();
				setestado.setIdestado(2);
				tmp.setSetestado(setestado);
				
				//auditoria
				Date fecharegistro = new Date();
				tmp.setFechamodificacion(fecharegistro);
				tmp.setIplog(usuarioBean.getIp());
				tmp.setSetusuario(usuarioBean.getSetUsuario());
				
				//actualizar
				petfotonoticiaDAO.updatePetfotonoticia(session, tmp);
				
				//se elimina el archivo de imagen
				String rutaArchivo = rutaImagenes + tmp.getRuta();
				fileUtil.deleteFile(rutaArchivo);
			}			 
			
			//se inactiva el registro
			Setestado setestado = new Setestado();
			setestado.setIdestado(2);
			petnoticia.setSetestado(setestado);
			
			//auditoria
			Date fecharegistro = new Date();
			petnoticia.setFechamodificacion(fecharegistro);
			petnoticia.setIplog(usuarioBean.getIp());
			petnoticia.setSetusuario(usuarioBean.getSetUsuario());
			
			petnoticiaDAO.updatePetnoticia(session, petnoticia);
			
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
	
	public boolean modificar(Petnoticia petnoticia, Petnoticia petnoticiaClon, List<Petfotonoticia> lisPetfotonoticia, List<Petfotonoticia> lisPetfotonoticiaClon, Petfotonoticia petfotonoticia, UploadedFile uploadedFile) throws Exception {
		boolean ok = false;
		Session session = null;
		
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			PetfotonoticiaDAO petfotonoticiaDAO = new PetfotonoticiaDAO();
			Date fecharegistro = new Date();
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			
			//Se evalua si han habido cambios en la lista de las fotos
			for(Petfotonoticia petfotonoticiaClon : lisPetfotonoticiaClon){
				boolean encuentra = false;
				for(Petfotonoticia petfotonoticiaItem : lisPetfotonoticia){
					if(petfotonoticiaClon.getIdfotonoticia() == petfotonoticiaItem.getIdfotonoticia()){
						//si encuentra
						encuentra = true;
						
						if(!petfotonoticiaClon.equals(petfotonoticiaItem)){
							//si han habido cambios se actualiza
							
							//auditoria
							fecharegistro = new Date();
							petfotonoticiaItem.setFechamodificacion(fecharegistro);
							petfotonoticiaItem.setIplog(usuarioBean.getIp());
							petfotonoticiaItem.setSetusuario(usuarioBean.getSetUsuario());
							
							//actualizar
							petfotonoticiaDAO.updatePetfotonoticia(session, petfotonoticiaItem);
							ok = true;
						}
					}
				}
				if(!encuentra){
					//no encuentra
					//inhabilitar
					Setestado setestado = new Setestado();
					setestado.setIdestado(2);
					petfotonoticiaClon.setSetestado(setestado);
					
					//auditoria
					fecharegistro = new Date();
					petfotonoticiaClon.setFechamodificacion(fecharegistro);
					petfotonoticiaClon.setIplog(usuarioBean.getIp());
					petfotonoticiaClon.setSetusuario(usuarioBean.getSetUsuario());
					
					//actualizar
					petfotonoticiaDAO.updatePetfotonoticia(session, petfotonoticiaClon);
					
					//eliminar foto del disco
					String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
					
					String rutaArchivo = rutaImagenes + petfotonoticiaClon.getRuta();
					
					fileUtil.deleteFile(rutaArchivo);
					ok = true;
				}
			}
			
			//Si subio foto se crea en disco y en base
			if(uploadedFile != null){
				creaFotoDiscoBD(petnoticia, petfotonoticia, uploadedFile, session);
				//si no tiene imagen principal se setea
				if(petnoticia.getRutafoto() == null || petnoticia.getRutafoto().trim().length() == 0){
					petnoticia.setRutafoto(petfotonoticia.getRuta());
				}
				ok = true;
			}
			
			//Se graba la noticia si han habido cambios
			if(!petnoticia.equals(petnoticiaClon)){
				//auditoria
				petnoticia.setFechamodificacion(fecharegistro);
				petnoticia.setIplog(usuarioBean.getIp());
				petnoticia.setSetusuario(usuarioBean.getSetUsuario());
		
				//actualizar
				petnoticiaDAO.updatePetnoticia(session, petnoticia);
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
	
	private void creaFotoDiscoBD(Petnoticia petnoticia, Petfotonoticia petfotonoticia, UploadedFile uploadedFile, Session session) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		PetfotonoticiaDAO petfotonoticiaDAO = new PetfotonoticiaDAO();
		
		int maxIdfotonoticia = petfotonoticiaDAO.maxIdfotonoticia(session)+1;
		int cantFotosPorNoticia = petfotonoticiaDAO.cantFotosPorNoticia(session, petnoticia.getIdnoticia())+1;
		
		//foto en disco
		FileUtil fileUtil = new FileUtil();
		FacesUtil facesUtil = new FacesUtil();
		Calendar fecha = Calendar.getInstance();
		
		String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
		String rutaNoticias =  fileUtil.getPropertyValue("repositorio-noticia") + fecha.get(Calendar.YEAR);
		String nombreArchivo = fecha.get(Calendar.YEAR) + "-" + (fecha.get(Calendar.MONTH) + 1) + "-" + fecha.get(Calendar.DAY_OF_MONTH) + "-" + petnoticia.getIdnoticia() + "-" + cantFotosPorNoticia + "." + fileUtil.getFileExtention(uploadedFile.getFileName()).toLowerCase();
		
		String rutaCompleta = rutaImagenes + rutaNoticias;
		
		if(fileUtil.createDir(rutaCompleta)){
			//crear foto en disco
			String rutaArchivo = rutaCompleta + "/" + nombreArchivo;
			fileUtil.createFile(rutaArchivo,uploadedFile.getContents());
		}
		
		//foto en BD
		petfotonoticia.setIdfotonoticia(maxIdfotonoticia);
		petfotonoticia.setPetnoticia(petnoticia);
		String rutaBD = rutaNoticias + "/" + nombreArchivo;
		petfotonoticia.setRuta(rutaBD);
		petfotonoticia.setNombrearchivo(nombreArchivo);
		petfotonoticia.setPerfil(1);//campo sin uso ya que tabla principal posee ruta de foto de perfil
		Setestado setestadoPetfotonoticia = new Setestado();
		setestadoPetfotonoticia.setIdestado(1);
		petfotonoticia.setSetestado(setestadoPetfotonoticia);
		
		//auditoria
		Date fecharegistro = new Date();
		petfotonoticia.setFecharegistro(fecharegistro);
		petfotonoticia.setIplog(usuarioBean.getIp());
		petfotonoticia.setSetusuario(usuarioBean.getSetUsuario());
		
		//ingresar foto en BD
		petfotonoticiaDAO.savePetfotonoticia(session, petfotonoticia);
	}
}
