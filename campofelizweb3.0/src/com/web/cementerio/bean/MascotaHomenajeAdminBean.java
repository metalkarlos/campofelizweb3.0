package com.web.cementerio.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;

import com.web.cementerio.bo.PetespecieBO;
import com.web.cementerio.bo.PetmascotahomenajeBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Cotpersona;
import com.web.cementerio.pojo.annotations.Cottipoidentificacion;
import com.web.cementerio.pojo.annotations.Petespecie;
import com.web.cementerio.pojo.annotations.Petfotomascota;
import com.web.cementerio.pojo.annotations.Petmascotahomenaje;
import com.web.cementerio.pojo.annotations.Petraza;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class MascotaHomenajeAdminBean implements Serializable {

	private static final long serialVersionUID = -6818813140393382672L;

	private int idmascota;
	private Petmascotahomenaje petmascotahomenaje;
	private Petmascotahomenaje petmascotahomenajeclone;
	private List<Petfotomascota> lisPetfotomascota;
	private List<Petfotomascota> lisPetfotomascotaclone;
	private Petfotomascota petfotomascotaselected;
	private List<Petespecie> lisPetespecie;

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
		lisPetfotomascota = new ArrayList<Petfotomascota>();
		lisPetfotomascotaclone = new ArrayList<Petfotomascota>();
		lisPetespecie = new ArrayList<Petespecie>(); 
		idmascota = 0;
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
				consultarListpetespecie();
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
				
				if(petmascotahomenaje != null && petmascotahomenaje.getCotpersona() == null){
					petmascotahomenaje.setCotpersona(new Cotpersona());
				}
				
				if(petmascotahomenaje != null && petmascotahomenaje.getPetraza() == null){
					petmascotahomenaje.setPetraza(new Petraza());
				}
				
				if(petmascotahomenaje != null && petmascotahomenaje.getCottipoidentificacion() == null){
					petmascotahomenaje.setCottipoidentificacion(new Cottipoidentificacion());
				}
				
				if(petmascotahomenaje != null && petmascotahomenaje.getIdmascota() > 0){
					petmascotahomenajeclone = petmascotahomenaje.clonar();
					
					if(petmascotahomenaje.getPetfotomascotas() != null && petmascotahomenaje.getPetfotomascotas().size() > 0){
						lisPetfotomascota = new ArrayList<Petfotomascota>(petmascotahomenaje.getPetfotomascotas());
						
						for(Petfotomascota petfotomascota : lisPetfotomascota){
							lisPetfotomascotaclone.add(petfotomascota.clonar());
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
	}
	
	public void consultarListpetespecie(){
		try {
			PetespecieBO petespecieBo =new PetespecieBO();
			lisPetespecie = petespecieBo.Listpetespecie();
		} catch (Exception e) {
			e.printStackTrace();
		    new MessageUtil().showErrorMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public void grabar() {
		try {
			if (validarcampos()) {
				PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
				boolean ok = false;
				
				if (idmascota == 0) {
					ok = petmascotahomenajeBO.ingresarPetmascotahomenajeBO(petmascotahomenaje,lisPetfotomascota);
					if(ok){
						mostrarPaginaMensaje("Homenaje creado con exito!!");
					}else{
						new MessageUtil().showWarnMessage("No existen cambios que guardar.","");
					}
				} else {
					ok = petmascotahomenajeBO.modificarPetmascotahomenajeBO(
							petmascotahomenaje, petmascotahomenajeclone,
							lisPetfotomascota, lisPetfotomascotaclone);
					if(ok){
						mostrarPaginaMensaje("Homenaje modificado con exito!!");
					}else{
						new MessageUtil().showWarnMessage("No existen cambios que guardar.","");
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
		usuarioBean.setLink("/pages/mascotahomenaje.jsf?idmascota="+petmascotahomenaje.getIdmascota());
		usuarioBean.setLinkTitulo("Consultar Homenaje");

		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("../pages/mensaje.jsf");
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			// Tamaño imagen menor a 100KB
			if (event.getFile().getSize() <= Parametro.TAMAÑO_IMAGEN) {
				FileUtil fileUtil = new FileUtil();
				
				Petfotomascota petfotomascota = new Petfotomascota();
				petfotomascota.setImagen(event.getFile().getContents());
				petfotomascota.setNombrearchivo(fileUtil.getFileExtention(event.getFile().getFileName()).toLowerCase());
				lisPetfotomascota.add(petfotomascota);
				
				new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.", "");
			} else {
				new MessageUtil().showErrorMessage("Tamaño de la imagen no puede ser mayor a 700KB","");
			}

		} catch (Exception x) {
			x.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public void ponerFotoPrincipal() {
		petmascotahomenaje.setRutafoto(petfotomascotaselected.getRuta());
		petfotomascotaselected = new Petfotomascota();
		new MessageUtil().showInfoMessage("Presione grabar para guardar los cambios","");
	}

	public void quitarFotoGaleria() {
		if (petfotomascotaselected.getRuta().equalsIgnoreCase(petmascotahomenaje.getRutafoto())) {
			new MessageUtil().showInfoMessage("La foto que desea eliminar es la del perfil, seleccione otra foto de perfil y vuelva a intentarlo","");
		} else {
			lisPetfotomascota.remove(petfotomascotaselected);
			new MessageUtil().showInfoMessage("Presione grabar para guardar los cambios","");
		}
		petfotomascotaselected = new Petfotomascota();
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

	public List<Petfotomascota> getLisPetfotomascota() {
		return lisPetfotomascota;
	}

	public void setLisPetfotomascota(List<Petfotomascota> lisPetfotomascota) {
		this.lisPetfotomascota = lisPetfotomascota;
	}

	public List<Petfotomascota> getLisPetfotomascotaclone() {
		return lisPetfotomascotaclone;
	}

	public void setLisPetfotomascotaclone(
			List<Petfotomascota> lisPetfotomascotaclone) {
		this.lisPetfotomascotaclone = lisPetfotomascotaclone;
	}

	public List<Petespecie> getListPetespecie() {
		return lisPetespecie;
	}

	public void setListPetespecie(List<Petespecie> listPetespecie) {
		this.lisPetespecie = listPetespecie;
	}

}
