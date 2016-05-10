package com.web.cementerio.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.web.cementerio.bo.PetmascotahomenajeBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Petespecie;
import com.web.cementerio.pojo.annotations.Petfotomascota;
import com.web.cementerio.pojo.annotations.Petmascotahomenaje;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class MascotaHomenajeAdminBean implements Serializable {

	private static final long serialVersionUID = -6818813140393382672L;

	private Petmascotahomenaje petmascotahomenaje;
	private List<Petfotomascota> listpetfotomascota;
	private List<Petfotomascota> listpetfotomascotaclone;
	private Petmascotahomenaje petmascotahomenajeclone;
	private Petfotomascota petfotomascotaselected;
	private int idmascota;
	private String descripcionFoto;
	private boolean fotoSubida;
	private int idfotomascotaselected;
	private byte[] imagenTemporal;
	private String nombreImagen;

	public MascotaHomenajeAdminBean() {
		petmascotahomenaje = new Petmascotahomenaje(0, new Setestado(),
				new Setusuario(), new Petespecie(), null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, 0, new BigDecimal(0), null, false, false, null);
		petmascotahomenaje.setPetespecie(new Petespecie());
		petmascotahomenaje.setFechapublicacion(new Date());
		petmascotahomenaje.setIdmascotaveterinaria(null);
		petfotomascotaselected = new Petfotomascota(0, new Setestado(),
				new Petmascotahomenaje(), new Setusuario(), null, null, null,
				0, null, null, null);
		listpetfotomascota = new ArrayList<Petfotomascota>();
		listpetfotomascotaclone = new ArrayList<Petfotomascota>();
		idmascota = 0;
		fotoSubida = false;
		descripcionFoto = null;
	}

	@PostConstruct
	public void PostMascotaHomenajeAdminBean() {
		FacesUtil facesUtil = new FacesUtil();

		try {
			Object par = facesUtil.getParametroUrl("idmascota");
			if (par != null) {
				idmascota = Integer.parseInt(par.toString());
				if(idmascota > 0){
					consultarMascotas();
				}
			} else {
				facesUtil.redirect("../pages/home.jsf");
			}
		} catch (NumberFormatException ne) {
			try {
				facesUtil.redirect("../pages/home.jsf");
			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				facesUtil.redirect("../pages/home.jsf");
			} catch (Exception e2) {
			}
		}
	}

	private void consultarMascotas(){
		if(this.idmascota > 0){
			try {
				PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
				petmascotahomenaje = petmascotahomenajeBO.getPetmascotahomenajebyId(idmascota);
				
				if(petmascotahomenaje != null && petmascotahomenaje.getIdmascota() > 0){
					petmascotahomenajeclone = petmascotahomenaje.clonar();
					
					if(petmascotahomenaje.getPetfotomascotas() != null && petmascotahomenaje.getPetfotomascotas().size() > 0){
						listpetfotomascota = new ArrayList<Petfotomascota>(petmascotahomenaje.getPetfotomascotas());
						
						for(Petfotomascota petfotomascota : listpetfotomascota){
							listpetfotomascotaclone.add(petfotomascota.clonar());
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
	}

	public void grabar() {
		try {
			if (validarcampos()) {
				PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
				Petfotomascota petfotomascota = new Petfotomascota();
				boolean ok = false;
				
				if(fotoSubida && descripcionFoto != null && descripcionFoto.trim().length() > 0){
					petfotomascota.setDescripcion(descripcionFoto);
				}
				
				if (idmascota == 0) {
					ok = petmascotahomenajeBO.ingresarPetmascotahomenajeBO(petmascotahomenaje,petfotomascota,imagenTemporal,nombreImagen);
					if(ok){
						mostrarPaginaMensaje("Homenaje creado con exito!!");
					}else{
						mostrarPaginaMensaje("No existen cambios que guardar.");
					}
				} else {
					ok = petmascotahomenajeBO.modificarPetmascotahomenajeBO(
							petmascotahomenaje, petmascotahomenajeclone,
							listpetfotomascota, listpetfotomascotaclone,
							petfotomascota,imagenTemporal,nombreImagen);
					if(ok){
						mostrarPaginaMensaje("Homenaje modificado con exito!!");
					}else{
						mostrarPaginaMensaje("No existen cambios que guardar.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}

	}

	private void mostrarPaginaMensaje(String mensaje) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean) new FacesUtil().getSessionBean("usuarioBean");
		usuarioBean.setMensaje(mensaje);
		usuarioBean.setLink("/pages/mascotashomenaje");
		usuarioBean.setLinkTitulo("Ver Más Homenajes");

		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("../pages/mensaje.jsf");
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			// Tamaño imagen menor a 100KB
			if (event.getFile().getSize() <= Parametro.TAMAÑO_IMAGEN) {
				FileUtil fileUtil = new FileUtil();
				StreamedContent streamedContent = new DefaultStreamedContent(event.getFile().getInputstream(), event.getFile().getContentType());
				imagenTemporal = event.getFile().getContents();
				nombreImagen = fileUtil.getFileExtention(event.getFile().getFileName()).toLowerCase();
				
				FacesUtil facesUtil = new FacesUtil();
				UsuarioBean usuarioBean = (UsuarioBean) facesUtil.getSessionBean("usuarioBean");
				usuarioBean.setStreamedContent(streamedContent);
				facesUtil.setSessionBean("usuarioBean", usuarioBean);
				
				fotoSubida = true;
				new MessageUtil().showInfoMessage(
						"Presione Grabar para guardar los cambios.", "");
			} else {
				new MessageUtil().showErrorMessage("Tamaño de la imagen no puede ser mayor a 700KB","");
			}

		} catch (Exception x) {
			x.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public void borrarFotoSubida() {
		imagenTemporal = null;
		fotoSubida = false;
	}

	public void ponerFotoperfil() {
		if (petfotomascotaselected != null) {
			petmascotahomenaje.setRutafoto(petfotomascotaselected.getRuta());
			new MessageUtil().showInfoMessage("Presione grabar para guardar los cambios","");
			petfotomascotaselected = new Petfotomascota();
		}
	}

	public void quitarFoto() {
		if (petfotomascotaselected != null) {
			if (!petfotomascotaselected.getRuta().equals(
					petmascotahomenaje.getRutafoto())) {
				listpetfotomascota.remove(petfotomascotaselected);
				new MessageUtil().showInfoMessage("Presione grabar para guardar los cambios","");
			} else {
				new MessageUtil()
						.showInfoMessage(
								"La foto que desea eliminar es la del perfil, seleccione otra foto de perfil y vuelva a intentarlo","");
			}
			petfotomascotaselected = new Petfotomascota();
		}

	}

	public void seleccionarImagen() throws Exception{
		FacesUtil faces = new FacesUtil();
		Map<String,String> params = faces.getFacesContext().getExternalContext().getRequestParameterMap();
		String param = params.get("idfotomascota");
		
		if (param != null) {
			int idfotomascota = Integer.parseInt(param);
			for(Petfotomascota petfotomascota:listpetfotomascota){
				if(petfotomascota.getIdfotomascota()==idfotomascota){
					petfotomascotaselected = petfotomascota;
					break;
				}
			}
		}
	}
	
	public boolean validarcampos() {
		boolean ok = true;
		Date fechaactual = new Date();
		
		if (petmascotahomenaje.getNombre() == null
				|| petmascotahomenaje.getNombre().length() == 0) {
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar el Nombre de la mascota","");
		} else if (petmascotahomenaje.getPetespecie().getIdespecie() == 0) {
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario seleccionar la especie","");

		} else if (petmascotahomenaje.getFechanacimiento() == null) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Es necesario seleccionar la fecha de nacimiento de la mascota","");
		} else if (petmascotahomenaje.getFechafallecimiento() == null) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Es necesario seleccionar la fecha de fallecimiento de la mascota","");
		} else if (petmascotahomenaje.getFamilia() == null
				|| petmascotahomenaje.getFamilia().length() == 0) {
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar el dueño de la mascota","");
		} else if (petmascotahomenaje.getFechanacimiento() == null
				&& petmascotahomenaje.getFechanacimiento().after(fechaactual)) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Fecha de nacimiento debe ser menor o igual a la fecha actual","");
		} else if (petmascotahomenaje.getFechafallecimiento() == null
				&& petmascotahomenaje.getFechafallecimiento()
						.after(fechaactual)) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Fecha de fallecimiento debe ser menor o igual a la fecha actual","");
		} else if (petmascotahomenaje.getFechanacimiento().after(
				petmascotahomenaje.getFechafallecimiento())) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Fecha de nacimiento debe ser menor o igual a la fecha de fallecimiento","");
		} else if (petmascotahomenaje.getFechafallecimiento().before(
				petmascotahomenaje.getFechanacimiento())) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Fecha de fallecimiento debe ser mayor o igual a la fecha de nacimiento","");
		} else if (petmascotahomenaje.getFechapublicacion().before(
				petmascotahomenaje.getFechanacimiento())) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Fecha de publicación debe ser mayor o igual a la fecha de fallecimiento","");
		} else if (petmascotahomenaje.getFechapublicacion().before(
				petmascotahomenaje.getFechafallecimiento())) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Fecha de publicación debe ser mayor o igual a la fecha de fallecimiento","");
		} else if (petmascotahomenaje.getFechapublicacion().after(fechaactual)) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Fecha de publicación no pueder ser mayor a la fecha de hoy","");
		} else if (imagenTemporal != null && !fotoSubida) {
			ok = false;
			new MessageUtil().showInfoMessage("Para subir la foto de click en el boton de la flecha","");
		} else if (imagenTemporal != null && fotoSubida
				&& descripcionFoto.length() == 0) {
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar la descripción de la foto a subir","");
		} else if (verificaDescripcionFotoNoVacia()) {
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar la descripción en fotos de la galería","");
		} 
		
		if(existeCodigoVeterinaria()){
			ok = false;
		}
		
		return ok;
	}
	
	private boolean existeCodigoVeterinaria(){
		boolean ok = false;
		
		try {
			PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
			
			Petmascotahomenaje petmascotahomenajeParams = new Petmascotahomenaje();
			petmascotahomenajeParams.setIdmascotaveterinaria(petmascotahomenaje.getIdmascotaveterinaria());
			Setestado setestado = new Setestado();
			setestado.setIdestado(1);
			petmascotahomenajeParams.setSetestado(setestado);
			List<Petmascotahomenaje> lisPetmascotahomenaje = petmascotahomenajeBO.lisPetmascotabyParams(petmascotahomenajeParams);
			
			if(lisPetmascotahomenaje != null && lisPetmascotahomenaje.size() > 0){
				if(petmascotahomenaje.getIdmascota() == 0) {
					//si es un ingreso, el codigo veterinaria no debe existir
					ok = true;
					new MessageUtil().showInfoMessage("Código Veterinaria ya existe","");
				}else{
					//si es una modificacion, el codigo veterinaria no debe existir aparte del registro que se modifica 
					if(lisPetmascotahomenaje.get(0).getIdmascota() != petmascotahomenaje.getIdmascota()){
						ok = true;
						new MessageUtil().showInfoMessage("Código Veterinaria ya existe","");
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
		
		return ok;
	}

	public boolean verificaDescripcionFotoNoVacia() {
		boolean verifica = false;
		if (petmascotahomenaje.getIdmascota() > 0
				&& listpetfotomascota.size() > 0) {
			for (Petfotomascota temfotomascota : listpetfotomascota) {
				if (temfotomascota.getDescripcion() == null
						|| temfotomascota.getDescripcion().length() == 0) {
					verifica = true;
					break;
				}
			}
		}
		return verifica;
	}

	public void eliminar() {
		try {
			PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
			
			boolean ok = petmascotahomenajeBO.eliminarPetmascotahomenajeBO(petmascotahomenaje);
			if (ok) {
				mostrarPaginaMensaje("Homenaje eliminado con exito!!");
			} else {
				new MessageUtil()
						.showWarnMessage("No se ha podido eliminar el Homenaje. Comunicar al Webmaster.","");
			}
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public Petmascotahomenaje getPetmascotahomenaje() {
		return petmascotahomenaje;
	}

	public void setPetmascotahomenaje(Petmascotahomenaje petmascotahomenaje) {
		this.petmascotahomenaje = petmascotahomenaje;
	}

	public Petfotomascota getPetfotomascotaselected() {
		return petfotomascotaselected;
	}

	public void setPetfotomascotaselected(Petfotomascota petfotomascotaselected) {
		this.petfotomascotaselected = petfotomascotaselected;
	}

	public Petmascotahomenaje getPetmascotahomenajeclone() {
		return petmascotahomenajeclone;
	}

	public void setPetmascotahomenajeclone(
			Petmascotahomenaje petmascotahomenajeclone) {
		this.petmascotahomenajeclone = petmascotahomenajeclone;
	}

	public int getIdmascota() {
		return idmascota;
	}

	public void setIdmascota(int idmascota) {
		this.idmascota = idmascota;
	}

	public String getDescripcionFoto() {
		return descripcionFoto;
	}

	public void setDescripcionFoto(String descripcionFoto) {
		this.descripcionFoto = descripcionFoto;
	}

	public boolean isFotoSubida() {
		return fotoSubida;
	}

	public void setFotoSubida(boolean fotoSubida) {
		this.fotoSubida = fotoSubida;
	}

	public List<Petfotomascota> getListpetfotomascota() {
		return listpetfotomascota;
	}

	public void setListpetfotomascota(List<Petfotomascota> listpetfotomascota) {
		this.listpetfotomascota = listpetfotomascota;
	}

	public List<Petfotomascota> getListpetfotomascotaclone() {
		return listpetfotomascotaclone;
	}

	public void setListpetfotomascotaclone(
			List<Petfotomascota> listpetfotomascotaclone) {
		this.listpetfotomascotaclone = listpetfotomascotaclone;
	}
	
	public int getIdfotomascotaselected() {
		return idfotomascotaselected;
	}

	public void setIdfotomascotaselected(int idfotomascotaselected) {
		this.idfotomascotaselected = idfotomascotaselected;
	}

	public byte[] getImagenTemporal() {
		return imagenTemporal;
	}

	public String getNombreImagen() {
		return nombreImagen;
	}

}
