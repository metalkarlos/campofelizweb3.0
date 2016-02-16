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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.web.cementerio.bo.PetmascotahomenajeBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Petespecie;
import com.web.cementerio.pojo.annotations.Petfotomascota;
import com.web.cementerio.pojo.annotations.Petmascotahomenaje;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
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
	private StreamedContent streamedContent;
	private UploadedFile uploadedFile;
	private int idmascota;
	private String descripcionFoto;
	private boolean fotoSubida;

	public MascotaHomenajeAdminBean() {
		inicializarobjetos();
	}

	@PostConstruct
	public void PostMascotaHomenajeAdminBean() {
		FacesUtil facesUtil = new FacesUtil();

		try {
			Object par = facesUtil.getParametroUrl("idmascota");
			if (par != null) {
				idmascota = Integer.parseInt(par.toString());
				if(idmascota > 0){
					consultar();
					clonarobjetos();
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

	public void inicializarobjetos() {
		// petmascotahomenaje = new Petmascotahomenaje(0, new Setestado(), new
		// Setusuario(), new Petespecie(), null, null, null, null, null, null,
		// null, null, null, null, null, null );
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
		streamedContent = null;
		descripcionFoto = null;
		uploadedFile = null;
	}

	public void grabar() {
		try {
			if (validarcampos()) {
				PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
				boolean ok = false;
				if (petmascotahomenaje.getIdmascota() == 0) {
					ok = petmascotahomenajeBO.ingresarPetmascotahomenajeBO(
							petmascotahomenaje, 1, uploadedFile,
							descripcionFoto);
					if (ok) {
						mostrarPaginaMensaje("Homenaje creado con exito!!");
					} else {
						new MessageUtil()
								.showWarnMessage("No se ha podido crear el Homenaje. Comunicar al Webmaster.","");
					}
				} else if (petmascotahomenaje.getIdmascota() > 0) {
					// objeto petmascotahomenaje se ha modificado
					ok = petmascotahomenajeBO.modificarPetmascotahomenajeBO(
							petmascotahomenaje, petmascotahomenajeclone,
							listpetfotomascota, listpetfotomascotaclone,
							uploadedFile, descripcionFoto);
					if (ok) {
						mostrarPaginaMensaje("Homenaje modificado con exito!!");
					} else {
						new MessageUtil()
								.showWarnMessage("No se ha podido modificar el Homenaje. Comunicar al Webmaster.","");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}

	}

	private void mostrarPaginaMensaje(String mensaje) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean) new FacesUtil()
				.getSessionBean("usuarioBean");
		usuarioBean.setMensaje(mensaje);

		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("../pages/mensaje.jsf");
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			// Tamaño imagen menor a 100KB
			if (event.getFile().getSize() <= Parametro.TAMAÑO_IMAGEN) {
				uploadedFile = event.getFile();
				streamedContent = new DefaultStreamedContent(event.getFile()
						.getInputstream(), event.getFile().getContentType());

				FacesUtil facesUtil = new FacesUtil();
				UsuarioBean usuarioBean = (UsuarioBean) facesUtil
						.getSessionBean("usuarioBean");
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
		streamedContent = null;
		uploadedFile = null;
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
								"No se puede eliminar foto que ha sido seleccionada como foto de perfil, cambie de foto de perfil y vuelva a intentarlo","");
			}
			petfotomascotaselected = new Petfotomascota();
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
		} else if (uploadedFile != null && !fotoSubida) {
			ok = false;
			new MessageUtil().showInfoMessage("Para subir la foto de click en el boton de la flecha","");
		} else if (uploadedFile != null && fotoSubida
				&& descripcionFoto.length() == 0) {
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar la descripción de la foto a subir","");
		} else if (verificaDescripcionFotoNoVacia()) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Es necesario ingresar la descripción en fotos de la galería","");
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

	private void consultar() {
		try {
			PetmascotahomenajeBO mascotaHomenajeBO = new PetmascotahomenajeBO();
			petmascotahomenaje = mascotaHomenajeBO.getPetmascotahomenajebyId(
					idmascota, 1, true);
			if ((petmascotahomenaje != null)
					&& (!petmascotahomenaje.getPetfotomascotas().isEmpty())
					& petmascotahomenaje.getPetfotomascotas().size() > 0) {
				listpetfotomascota = new ArrayList<Petfotomascota>(
						petmascotahomenaje.getPetfotomascotas());
			}

		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showErrorMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}

	}

	public void clonarobjetos() {
		try {
			petmascotahomenajeclone = petmascotahomenaje.clonar();
			if ((petmascotahomenaje != null) && (!listpetfotomascota.isEmpty())
					&& (listpetfotomascota.size() > 0)) {
				for (Petfotomascota petfotomascota : listpetfotomascota) {
					listpetfotomascotaclone.add(petfotomascota.clonar());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showErrorMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}

	}

	public void eliminar() {
		try {
			PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
			boolean ok = petmascotahomenajeBO.eliminarPetmascotahomenajeBO(
					petmascotahomenaje, listpetfotomascotaclone, 2);

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

	public StreamedContent getStreamedContent() {
		return streamedContent;
	}

	public String getDescripcionFoto() {
		return descripcionFoto;
	}

	public void setDescripcionFoto(String descripcionFoto) {
		this.descripcionFoto = descripcionFoto;
	}

	public void setStreamedContent(StreamedContent streamedContent) {
		this.streamedContent = streamedContent;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
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

}
