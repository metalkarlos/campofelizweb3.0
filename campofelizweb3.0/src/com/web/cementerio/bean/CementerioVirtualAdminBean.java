package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;

import com.web.cementerio.bo.PetfotoinstalacionBO;
import com.web.cementerio.pojo.annotations.Petfotoinstalacion;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class CementerioVirtualAdminBean implements Serializable{

	private static final long serialVersionUID = -3312876102105882061L;
	private List<Petfotoinstalacion> lisPetfotoinstalacion;
	private List<Petfotoinstalacion> lisPetfotoinstalacionClon;
	private Petfotoinstalacion petfotoinstalacionSeleccionado;
	
	public CementerioVirtualAdminBean(){
		lisPetfotoinstalacion = new ArrayList<Petfotoinstalacion>();
		lisPetfotoinstalacionClon = new ArrayList<Petfotoinstalacion>();
		petfotoinstalacionSeleccionado = new Petfotoinstalacion();
	}
	
	@PostConstruct
	public void PostCementerioVirtualAdminBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			consultar();
		}catch(Exception e) {
			e.printStackTrace();
			try{facesUtil.redirect("../pages/home.jsf");}catch(Exception e2){}
		}
	}
	
	private void consultar(){
		try {
			PetfotoinstalacionBO PetfotoinstalacionBO = new PetfotoinstalacionBO();
			lisPetfotoinstalacion = PetfotoinstalacionBO.lisPetfotoinstalacion();
				
			if(lisPetfotoinstalacion != null && lisPetfotoinstalacion.size() > 0){
				for(Petfotoinstalacion petfotoinstalacion : lisPetfotoinstalacion){
					lisPetfotoinstalacionClon.add(petfotoinstalacion.clonar());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		try{
			Petfotoinstalacion petfotoinstalacion = new Petfotoinstalacion();
			petfotoinstalacion.setImagen(event.getFile().getContents());
			petfotoinstalacion.setNombrearchivo(event.getFile().getFileName().toLowerCase());
			petfotoinstalacion.setDescripcion("Mascohijo");
			lisPetfotoinstalacion.add(petfotoinstalacion);
			
			new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.", "");
		}catch(Exception x){
			x.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public void quitarFotoGaleria(){
		lisPetfotoinstalacion.remove(petfotoinstalacionSeleccionado);
		new MessageUtil().showInfoMessage("Presione grabar para guardar los cambios","");
	}
	
	public void grabar(){
		try{
			if(lisPetfotoinstalacion != null && lisPetfotoinstalacion.size() > 0){
				PetfotoinstalacionBO petfotoinstalacionBO = new PetfotoinstalacionBO();
				boolean ok = false;
				
				ok = petfotoinstalacionBO.grabarLisPetfotoinstalacion(lisPetfotoinstalacion, lisPetfotoinstalacionClon);
				if(ok){
					mostrarPaginaMensaje("Cambios registrados con exito!!");
				}else{
					new MessageUtil().showWarnMessage("No existen cambios que guardar.","");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
		
	private void mostrarPaginaMensaje(String mensaje) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		usuarioBean.setMensaje(mensaje);
		usuarioBean.setLink("/pages/home");
		usuarioBean.setAnchor("gallery");
		usuarioBean.setLinkTitulo("Consultar Galería");
		
		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("../pages/mensaje.jsf");	 
	}

	public List<Petfotoinstalacion> getLisPetfotoinstalacion() {
		return lisPetfotoinstalacion;
	}

	public void setLisPetfotoinstalacion(List<Petfotoinstalacion> lisPetfotoinstalacion) {
		this.lisPetfotoinstalacion = lisPetfotoinstalacion;
	}

	public List<Petfotoinstalacion> getLisPetfotoinstalacionClon() {
		return lisPetfotoinstalacionClon;
	}

	public void setLisPetfotoinstalacionClon(List<Petfotoinstalacion> lisPetfotoinstalacionClon) {
		this.lisPetfotoinstalacionClon = lisPetfotoinstalacionClon;
	}

	public Petfotoinstalacion getPetfotoinstalacionSeleccionado() {
		return petfotoinstalacionSeleccionado;
	}

	public void setPetfotoinstalacionSeleccionado(Petfotoinstalacion petfotoinstalacionSeleccionado) {
		this.petfotoinstalacionSeleccionado = petfotoinstalacionSeleccionado;
	}

}
