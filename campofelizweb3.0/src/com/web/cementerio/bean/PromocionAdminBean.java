package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;

import com.web.cementerio.bo.PetguiaBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Petfotoguia;
import com.web.cementerio.pojo.annotations.Petguia;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class PromocionAdminBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1070428099645161289L;
	private int idguia;
	private Petguia petguia;
	private Petguia petguiaClon;
	private List<Petfotoguia> lisPetfotoguia;
	private List<Petfotoguia> lisPetfotoguiaClon;
	private Petfotoguia petfotoguiaSeleccionado;
	private long maxfilesize;
	
	public PromocionAdminBean() {
		petguia = new Petguia(0, new Setestado(), new Setusuario(), null, null, null, null, null, null, null, null, null, false, null, 0);
		petguiaClon = new Petguia(0, new Setestado(), new Setusuario(), null, null, null, null, null, null, null, null, null, false, null, 0);
		lisPetfotoguia = new ArrayList<Petfotoguia>();
		lisPetfotoguiaClon = new ArrayList<Petfotoguia>();
		petfotoguiaSeleccionado = new Petfotoguia();
		maxfilesize = Parametro.TAMAÑO_IMAGEN;
	}
	
	@PostConstruct
	public void PostPromocionAdminBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idguia");
			if(par != null){
				idguia = Integer.parseInt(par.toString());
				
				if(idguia > 0){
					consultaPromocion();
				}else{
					PetguiaBO petguiaBO = new PetguiaBO();
					int orden = petguiaBO.getMaxOrden();
					petguia.setOrden(orden + 1);
				}
			}else{
				facesUtil.redirect("../pages/home.jsf");
			}
		} catch(NumberFormatException ne){
			try{facesUtil.redirect("../pages/home.jsf");}catch(Exception e){}
		} catch(Exception e) {
			e.printStackTrace();
			try{facesUtil.redirect("../pages/home.jsf");}catch(Exception e2){}
		}
	}
	
	private void consultaPromocion(){
		if(this.idguia > 0){
			try {
				PetguiaBO petguiaBO = new PetguiaBO();
				petguia = petguiaBO.getPetguiaByIdConObjetos(idguia);
				
				if(petguia != null && petguia.getIdguia() > 0){
					petguiaClon = petguia.clonar();
					
					if(petguia.getPetfotoguias() != null && petguia.getPetfotoguias().size() > 0){
						//lisPetfotoguia = new ArrayList<Petfotoguia>(petguia.getPetfotoguias());
						
						//ordenar por fecharegistro
						Petfotoguia[] arr = new Petfotoguia[petguia.getPetfotoguias().size()];
						arr = petguia.getPetfotoguias().toArray(arr);
						Arrays.sort(arr, Petfotoguia.FecharegistroComparator);
						lisPetfotoguia = new ArrayList<Petfotoguia>(Arrays.asList(arr));
						
						for(Petfotoguia petfotoguia : lisPetfotoguia){
							lisPetfotoguiaClon.add(petfotoguia.clonar());
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		try{
			Petfotoguia petfotoguia = new Petfotoguia();
			petfotoguia.setImagen(event.getFile().getContents());
			petfotoguia.setNombrearchivo(event.getFile().getFileName().toLowerCase());
			lisPetfotoguia.add(petfotoguia);
			
			new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.", "");
		}catch(Exception x){
			x.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public void ponerFotoPrincipal(){
		petguia.setRutafoto(petfotoguiaSeleccionado.getRuta());
		petfotoguiaSeleccionado = new Petfotoguia();
		new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.","");
	}
	
	public void quitarFotoGaleria(){
		if(petfotoguiaSeleccionado.getRuta().equalsIgnoreCase(petguia.getRutafoto())){
			new MessageUtil().showInfoMessage("La foto a eliminar es la foto principal. Seleccione otra foto como principal para poderla eliminar.","");
		}else{
			lisPetfotoguia.remove(petfotoguiaSeleccionado);
			new MessageUtil().showInfoMessage("Presione grabar para guardar los cambios","");
		}
		petfotoguiaSeleccionado = new Petfotoguia();
	}
	
	public void grabar(){
		try{
			if (validarcampos()) {
				PetguiaBO petguiaBO = new PetguiaBO();
				boolean ok = false;
				
				if(idguia == 0){
					ok = petguiaBO.ingresar(petguia, lisPetfotoguia);
					if(ok){
						mostrarPaginaMensaje("Promoción creada con exito!!");
					}else{
						new MessageUtil().showWarnMessage("No existen cambios que guardar.","");
					}
				}else{
					ok = petguiaBO.modificar(petguia, petguiaClon, lisPetfotoguia, lisPetfotoguiaClon);
					if(ok){
						mostrarPaginaMensaje("Promoción modificada con exito!!");
					}else{
						new MessageUtil().showWarnMessage("No existen cambios que guardar.","");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	private boolean validarcampos() {
		boolean ok = true;
		
		Date fechaactual = new Date();
		String textodescripcion= (petguia.getDescripcion()!=null ? petguia.getDescripcion().replaceAll("\\<.*?\\>", "") : "" );
		
		if(textodescripcion==null|| textodescripcion.length()==0){
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar el contenido","");
		}else if (petguia.getFechapublicacion().after(fechaactual)){
			ok = false;
			new MessageUtil().showInfoMessage("Fecha de publicación no pueder ser mayor a la fecha de hoy","");
		}else if (petguia.getDescripcioncorta() == null
				|| petguia.getDescripcioncorta().length() == 0) {
			ok = false;
			new MessageUtil().showInfoMessage("Ingrese la Descripción Corta de la Promoción","");
		}else if (petguia.getOrden() <= 0) {
			ok = false;
			new MessageUtil().showInfoMessage("Ingrese el orden de la Promoción","");
		} 
		
		return ok;
	}
	
	private void mostrarPaginaMensaje(String mensaje) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		usuarioBean.setMensaje(mensaje);
		usuarioBean.setLink("/pages/promocion.jsf?idguia="+petguia.getIdguia());
		usuarioBean.setLinkTitulo("Consultar Promoción");
		
		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("../pages/mensaje.jsf");	 
	}
	
	public void eliminar(){
		try{
			PetguiaBO petguiaBO = new PetguiaBO();
			
			boolean ok = petguiaBO.eliminar(petguia);
			if(ok){
				mostrarPaginaMensaje("Promoción eliminada con exito!!");
			}else{
				mostrarPaginaMensaje("No se ha podido eliminar la Promoción. Comunicar al Webmaster.");
			}
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public int getIdguia() {
		return idguia;
	}

	public void setIdguia(int idguia) {
		this.idguia = idguia;
	}

	public Petguia getPetguia() {
		return petguia;
	}

	public void setPetguia(Petguia petguia) {
		this.petguia = petguia;
	}

	public Petguia getPetguiaClon() {
		return petguiaClon;
	}

	public void setPetguiaClon(Petguia petguiaClon) {
		this.petguiaClon = petguiaClon;
	}

	public List<Petfotoguia> getLisPetfotoguia() {
		return lisPetfotoguia;
	}

	public void setLisPetfotoguia(List<Petfotoguia> lisPetfotoguia) {
		this.lisPetfotoguia = lisPetfotoguia;
	}

	public List<Petfotoguia> getLisPetfotoguiaClon() {
		return lisPetfotoguiaClon;
	}

	public void setLisPetfotoguiaClon(List<Petfotoguia> lisPetfotoguiaClon) {
		this.lisPetfotoguiaClon = lisPetfotoguiaClon;
	}

	public Petfotoguia getPetfotoguiaSeleccionado() {
		return petfotoguiaSeleccionado;
	}

	public void setPetfotoguiaSeleccionado(Petfotoguia petfotoguiaSeleccionado) {
		this.petfotoguiaSeleccionado = petfotoguiaSeleccionado;
	}

	public long getMaxfilesize() {
		return maxfilesize;
	}

	public void setMaxfilesize(long maxfilesize) {
		this.maxfilesize = maxfilesize;
	}

}
