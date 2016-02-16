package com.web.cementerio.bo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.primefaces.model.UploadedFile;

import com.web.cementerio.bean.UsuarioBean;
import com.web.cementerio.dao.PetfotomascotaDAO;
import com.web.cementerio.dao.PetmascotahomenajeDAO;
import com.web.cementerio.pojo.annotations.Petfotomascota;
import com.web.cementerio.pojo.annotations.Petmascotahomenaje;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.HibernateUtil;

public class PetmascotahomenajeBO {
	
	PetmascotahomenajeDAO petmascotahomenajeDAO;
	
	public  PetmascotahomenajeBO(){
		petmascotahomenajeDAO = new PetmascotahomenajeDAO();
	}
	
	public Petmascotahomenaje getPetmascotahomenajebyId(int idmascota, int idestado,boolean especie)throws Exception{
		Petmascotahomenaje petmascotahomenaje = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			petmascotahomenaje = petmascotahomenajeDAO.getPethomenajemascotaById(session,idmascota, idestado,especie);
		} catch (Exception e) {
			throw new Exception(e); 
		}
		finally{
			session.close();
		}
		return petmascotahomenaje;
	}
	

	
	public List<Petmascotahomenaje> lisPetmascotahomenajeBusquedaByPage(String[] texto, int pageSize, int pageNumber, int args[], int idestado) throws RuntimeException {
		List<Petmascotahomenaje> listpetmascotahomenaje = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			listpetmascotahomenaje = petmascotahomenajeDAO.lisPetmascotaBusquedaByPage(session, texto, pageSize, pageNumber, args, idestado);
			
		} catch (Exception e) {
			 throw new RuntimeException(e);
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
			throw new Exception(e);
		}finally{
			session.close();
		}
		return listpetmascotahomenaje;
	}
	
	
	public boolean  ingresarPetmascotahomenajeBO(Petmascotahomenaje petmascotahomenaje,int idestado, UploadedFile uploadedFile, String descripcionFoto) throws Exception{
		Session session = null;
		boolean ok = false;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Date fecharegistro = new Date();
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			
			petmascotahomenaje.setIdmascota(petmascotahomenajeDAO.getMaxidpetmascotahomenaje(session));
			petmascotahomenaje.setNombre(petmascotahomenaje.getNombre().toUpperCase());
			petmascotahomenaje.setFamilia(petmascotahomenaje.getFamilia().toUpperCase());
			
			Setestado setestado = new Setestado();
			setestado.setIdestado(idestado);
			petmascotahomenaje.setSetestado(setestado);	
			
			Setusuario setusuario = new Setusuario();
			setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			petmascotahomenaje.setSetusuario(setusuario);
	
			
			//Auditoria
			petmascotahomenaje.setFecharegistro(fecharegistro);
			petmascotahomenaje.setIplog(usuarioBean.getIp());
						
			petmascotahomenajeDAO.ingresarPetmascotahomenaje(session, petmascotahomenaje);
			
			if(uploadedFile !=null){
			  if(ingresarPetfotomascota(session, idestado, petmascotahomenaje,uploadedFile,descripcionFoto)){
			     petmascotahomenajeDAO.modificarPetmascotahomenaje(session, petmascotahomenaje);
			   }
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
	
	public boolean modificarPetmascotahomenajeBO(Petmascotahomenaje petmascotahomenaje,Petmascotahomenaje petmascotahomenajeclone, List<Petfotomascota>listpetfotomascota,List<Petfotomascota>listpetfotomascotaclone,UploadedFile uploadedFile,String descripcionFoto) throws Exception{
		Session session = null;
		boolean ok = false;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			if(!petmascotahomenaje.equals(petmascotahomenajeclone)){
			
				//auditoria
				Date fechamodificacion= new Date();
				UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
				petmascotahomenaje.setFechamodificacion(fechamodificacion);
				petmascotahomenaje.setIplog(usuarioBean.getIp());
				
				petmascotahomenaje.setNombre(petmascotahomenaje.getNombre().toUpperCase());
				petmascotahomenaje.setFamilia(petmascotahomenaje.getFamilia().toUpperCase());
				
				Setusuario setusuario = new Setusuario();
				setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
				petmascotahomenaje.setSetusuario(setusuario);
		
				petmascotahomenajeDAO.modificarPetmascotahomenaje(session, petmascotahomenaje);
				ok = true;
			}
			if(uploadedFile !=null){
			   if(ingresarPetfotomascota(session, 1,petmascotahomenaje,uploadedFile,descripcionFoto)){
				   petmascotahomenajeDAO.modificarPetmascotahomenaje(session, petmascotahomenaje);
			    }
			   ok = true;
			}
			if(!(listpetfotomascota.isEmpty() && listpetfotomascotaclone.isEmpty())){
			  //Cuando se ha eliminado una foto 
			  if (listpetfotomascota.size() != listpetfotomascotaclone.size()){
			    modificarPetfotomascota(session,2, listpetfotomascota, listpetfotomascotaclone,petmascotahomenaje) ;
			    ok = true;
			  }
			  //Cuando se ha modificado la descripcion de una foto 
			  if (modificarDescripcionPetfotomascota(session,1, listpetfotomascota, listpetfotomascotaclone,petmascotahomenaje)){
			  ok = true;
			  }
			  
			}
			
			if(ok){
				session.getTransaction().commit();
			}
			
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e);
		}finally {
			session.close();
		}
		
		return ok;
	}
	
	

	public boolean ingresarPetfotomascota(Session session, int idestado,Petmascotahomenaje petmascotahomenaje,  UploadedFile uploadedFile, String descripcionFoto)throws Exception{
		PetfotomascotaDAO petfotomascotaDAO = new PetfotomascotaDAO();
		Petfotomascota petfotomascota = new Petfotomascota();
		int secuencia = 0;
		boolean rutamodificada=false;
		
		try {
			petfotomascota.setPetmascotahomenaje(petmascotahomenaje);
			Date fecharegistro = new Date();
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
					
		    petfotomascota.setIdfotomascota(petfotomascotaDAO.getMaxidpetfotomascota(session));
				    
		    //Auditoria
			petfotomascota.setFecharegistro(fecharegistro);
			petfotomascota.setIplog(usuarioBean.getIp());
			
		    Setestado setestado = new Setestado();
			setestado.setIdestado(idestado);
			petfotomascota.setSetestado(setestado);
					
			Setusuario setusuario = new Setusuario();
			setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			petfotomascota.setSetusuario(setusuario);
					
			if(descripcionFoto != null && descripcionFoto.length()>0){
				petfotomascota.setDescripcion(descripcionFoto);	
			}
					
			//foto en disco
			FileUtil fileUtil = new FileUtil();
			FacesUtil facesUtil = new FacesUtil();
			Calendar fecha = Calendar.getInstance();
				
			secuencia = petfotomascotaDAO.getCantFotosPorMascota(session, petmascotahomenaje.getIdmascota());
					
			String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
			String rutaMascota =  fileUtil.getPropertyValue("repositorio-mascota") + fecha.get(Calendar.YEAR);
			String nombreArchivo = fecha.get(Calendar.YEAR) + "-" + (fecha.get(Calendar.MONTH) + 1) + "-" + fecha.get(Calendar.DAY_OF_MONTH) + "-" + petmascotahomenaje.getPetespecie().getIdespecie() + "-" + petmascotahomenaje.getIdmascota() + "-" + secuencia + "." +fileUtil.getFileExtention(uploadedFile.getFileName()).toLowerCase();
					
			String rutaCompleta = rutaImagenes + rutaMascota;
			//asignar ruta y nombre de archivo en objeto
			petfotomascota.setRuta(rutaMascota+"/"+nombreArchivo);
			if(petmascotahomenaje.getRutafoto()==null || petmascotahomenaje.getRutafoto().equals("") ){
			  petmascotahomenaje.setRutafoto(petfotomascota.getRuta());
			  rutamodificada = true;
			}
			petfotomascota.setNombrearchivo(nombreArchivo);
			
			petfotomascotaDAO.ingresarFotomascota(session, petfotomascota);
			
			if(fileUtil.createDir(rutaCompleta)){
				//crear foto en disco
				String rutaArchivo = rutaCompleta + "/" + nombreArchivo;
				fileUtil.createFile(rutaArchivo,uploadedFile.getContents());
			}
					
			
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e);
		}
	  return rutamodificada;
	}
	
	
	public void modificarPetfotomascota(Session session,int idestado,List<Petfotomascota>  lisPetfotomascota, List<Petfotomascota> lisPetfotomascotaclone,  Petmascotahomenaje petmascotahomenaje)throws Exception{
		try {
			for (Petfotomascota petfotomascota : lisPetfotomascotaclone){
				
				if(!lisPetfotomascota.contains(petfotomascota)){
					
					petfotomascota.setPetmascotahomenaje(petmascotahomenaje);
					eliminarPetfotomascota(session,petfotomascota);
				}					
			}
			
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception(e);
		}
	}
	
	public boolean modificarDescripcionPetfotomascota(Session session,int idestado,List<Petfotomascota>  lisPetfotomascota, List<Petfotomascota> lisPetfotomascotaclone,  Petmascotahomenaje petmascotahomenaje)throws Exception{
		PetfotomascotaDAO petfotomascotaDAO = new PetfotomascotaDAO();
		boolean modificar=false;
		for (Petfotomascota petfotomascotaclone : lisPetfotomascotaclone){
		 for (Petfotomascota petfotomascota : lisPetfotomascota){
			if(petfotomascotaclone.getIdfotomascota() == petfotomascota.getIdfotomascota()){
		    	if((!petfotomascota.getDescripcion().equals(petfotomascotaclone.getDescripcion()) && petfotomascota.getDescripcion()!=null && petfotomascotaclone.getDescripcion()!=null &&
					petfotomascota.getDescripcion().length()>0	&& (petfotomascotaclone.getDescripcion().length()>0))){
		    		Date fechamodificacion = new Date();
					UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
					 
					petfotomascota.setPetmascotahomenaje(petmascotahomenaje);
					petfotomascota.setFechamodificacion(fechamodificacion);
					petfotomascota.setIplog(usuarioBean.getIp());
					
					Setusuario setusuario = new Setusuario();
					setusuario = new Setusuario();
					setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
					petfotomascota.setSetusuario(setusuario);
					
					Setestado setestado = new Setestado();
					setestado.setIdestado(idestado);
					petfotomascota.setSetestado(setestado);
					
					petfotomascotaDAO.modificarFotomascota(session, petfotomascota);
					modificar=true;
				}
			 }
		   }
	    }
		return modificar;
	}
	
	
	
	public boolean  eliminarPetmascotahomenajeBO(Petmascotahomenaje petmascotahomenaje,List<Petfotomascota> listpetfotomascotaclone, int idestado)throws Exception{
		Session session = null;
		boolean ok = false;
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Date fechamodificacion = new Date();
			UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
			
			Setestado setestado = new Setestado();
			setestado.setIdestado(idestado);
			petmascotahomenaje.setSetestado(setestado);	
			
			Setusuario setusuario = new Setusuario();
			setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
			petmascotahomenaje.setSetusuario(setusuario);
			
			petmascotahomenaje.setFechamodificacion(fechamodificacion);
			petmascotahomenaje.setIplog(usuarioBean.getIp());
			
			petmascotahomenajeDAO.modificarPetmascotahomenaje(session, petmascotahomenaje);
			
			//inactivar registros asociados en petfotomascotahomenaje
			if(!listpetfotomascotaclone.isEmpty()){
			   	
				for(Petfotomascota petfotomascota: listpetfotomascotaclone){
					eliminarPetfotomascota(session,petfotomascota);
					
				}
			}
			session.getTransaction().commit();
			
			ok = true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new Exception();
		}finally{
			session.close();
		}
		
		return ok;
	}
	
	/***
	 * Metodo que es llamado al momento de modificar un inactivar registros en petfotomascota
	 * @param petfotomascota
	 */
    public void eliminarPetfotomascota(Session session,Petfotomascota petfotomascota)throws Exception{
    	
    	Date fechamodificacion= new Date();
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		PetfotomascotaDAO petfotomascotaDAO = new PetfotomascotaDAO();
		FacesUtil facesUtil = new FacesUtil();
		FileUtil fileUtil = new FileUtil();
		
		Setestado setestado = new Setestado();
		setestado.setIdestado(2);
		petfotomascota.setSetestado(setestado);
			
		Setusuario setusuario = new Setusuario();
		setusuario.setIdusuario(usuarioBean.getSetUsuario().getIdusuario());
		petfotomascota.setSetusuario(setusuario);
			
		//Auditoria
		petfotomascota.setFechamodificacion(fechamodificacion);
		petfotomascota.setIplog(usuarioBean.getIp());
		petfotomascotaDAO.modificarFotomascota(session, petfotomascota);
			
		//eliminar foto del disco
		String rutaImagenes = facesUtil.getContextParam("imagesDirectory");
		
		String rutaArchivo = rutaImagenes + petfotomascota.getRuta();
		
		fileUtil.deleteFile(rutaArchivo);
    	
    }
	
}
