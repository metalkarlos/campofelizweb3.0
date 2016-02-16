package com.web.cementerio.bean;

import java.io.Serializable;
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

import com.web.cementerio.bo.PetnoticiaBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Petfotonoticia;
import com.web.cementerio.pojo.annotations.Petnoticia;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class NoticiaAdminBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4748535305651371565L;
	private int idnoticia;
	private Petnoticia petnoticia;
	private Petnoticia petnoticiaClon;
	private List<Petfotonoticia> lisPetfotonoticia;
	private List<Petfotonoticia> lisPetfotonoticiaClon;
	private Petfotonoticia petfotonoticiaSeleccionada;
	private StreamedContent streamedContent;
	private UploadedFile uploadedFile;
	private String descripcionFoto;
	private boolean fotoSubida;
	private long maxfilesize;
	
	public NoticiaAdminBean() {
		petnoticia = new Petnoticia(0, new Setestado(), new Setusuario(), null, null, null, new Date(), null, null, null, new Date(), new Date(), false, 0);
		petnoticiaClon = new Petnoticia(0, new Setestado(), new Setusuario(), null, null, null, new Date(), null, null, null, new Date(), new Date(), false, 0);
		lisPetfotonoticia = new ArrayList<Petfotonoticia>();
		lisPetfotonoticiaClon = new ArrayList<Petfotonoticia>();
		petfotonoticiaSeleccionada = new Petfotonoticia();
		descripcionFoto = "";
		fotoSubida = false;
		maxfilesize = Parametro.TAMAÑO_IMAGEN;
	}
	
	@PostConstruct
	public void PostNoticiaAdminBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try {
			Object par = facesUtil.getParametroUrl("idnoticia");
			if(par != null){
				idnoticia = Integer.parseInt(par.toString());
				
				if(idnoticia > 0){
					consultaNoticia();
				}else{
					PetnoticiaBO petnoticiaBO = new PetnoticiaBO();
					int orden = petnoticiaBO.getMaxOrden();
					petnoticia.setOrden(orden + 1);
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
	
	private void consultaNoticia(){
		if(this.idnoticia > 0){
			try {
				PetnoticiaBO petnoticiaBO = new PetnoticiaBO();
				petnoticia = petnoticiaBO.getPetnoticiaConObjetosById(idnoticia);
				
				if(petnoticia != null && petnoticia.getIdnoticia() > 0){
					petnoticiaClon = petnoticia.clonar();
					
					if(petnoticia.getPetfotonoticias() != null && petnoticia.getPetfotonoticias().size() > 0){
						lisPetfotonoticia = new ArrayList<Petfotonoticia>(petnoticia.getPetfotonoticias());
						
						for(Petfotonoticia petfotonoticia : lisPetfotonoticia){
							lisPetfotonoticiaClon.add(petfotonoticia.clonar());
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
			uploadedFile = event.getFile();
			streamedContent = new DefaultStreamedContent(event.getFile().getInputstream(), event.getFile().getContentType());
			
			FacesUtil facesUtil = new FacesUtil();
			UsuarioBean usuarioBean = (UsuarioBean)facesUtil.getSessionBean("usuarioBean");
			usuarioBean.setStreamedContent(streamedContent);
			facesUtil.setSessionBean("usuarioBean", usuarioBean);
			fotoSubida = true;
			
			new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.","");
		}catch(Exception x){
			x.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public void ponerFotoPrincipal(){
		petnoticia.setRutafoto(petfotonoticiaSeleccionada.getRuta());
		petfotonoticiaSeleccionada = new Petfotonoticia();
		new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.","");
	}
	
	public void quitarFotoGaleria(){
		if(petfotonoticiaSeleccionada.getRuta().equalsIgnoreCase(petnoticia.getRutafoto())){
			new MessageUtil().showInfoMessage("La foto a eliminar es la foto principal de ésta noticia. Seleccione otra foto como principal para poderla eliminar.","");
		}else{
			lisPetfotonoticia.remove(petfotonoticiaSeleccionada);
			petfotonoticiaSeleccionada = new Petfotonoticia();
		}
	}
	
	public void borrarFotoSubida(){
		streamedContent = null;
		uploadedFile = null;
		fotoSubida = false;
	}
	
	public void grabar(){
		try{
			boolean ok = false;
			
			PetnoticiaBO petnoticiaBO = new PetnoticiaBO();
			Petfotonoticia petfotonoticia = new Petfotonoticia();
			
			if(fotoSubida && descripcionFoto != null && descripcionFoto.trim().length() > 0){
				petfotonoticia.setDescripcion(descripcionFoto);
			}
			
			if(idnoticia == 0){
				ok = petnoticiaBO.ingresar(petnoticia, petfotonoticia, uploadedFile);
				if(ok){
					mostrarPaginaMensaje("Noticia creada con exito!!");
				}else{
					new MessageUtil().showWarnMessage("No se ha podido ingresar la Noticia. Comunicar al Webmaster.","");
				}
			}else{
				ok = petnoticiaBO.modificar(petnoticia, petnoticiaClon, lisPetfotonoticia, lisPetfotonoticiaClon, petfotonoticia, uploadedFile);
				if(ok){
					mostrarPaginaMensaje("Noticia modificada con exito!!");
				}else{
					new MessageUtil().showWarnMessage("No se ha podido modificar la Noticia. Comunicar al Webmaster.","");
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
		
		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("../pages/mensaje.jsf");	 
	}
	
	public void eliminar(){
		try{
			PetnoticiaBO petnoticiaBO = new PetnoticiaBO();
			boolean ok = petnoticiaBO.eliminar(petnoticia);
			if(ok){
				mostrarPaginaMensaje("Noticia eliminada con exito!!");
			}else{
				new MessageUtil().showWarnMessage("No se ha podido eliminar la Noticia. Comunicar al Webmaster.","");
			}
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public int getIdnoticia() {
		return idnoticia;
	}

	public void setIdnoticia(int idnoticia) {
		this.idnoticia = idnoticia;
	}
	
	public Petnoticia getPetnoticia() {
		return petnoticia;
	}

	public void setPetnoticia(Petnoticia petnoticia) {
		this.petnoticia = petnoticia;
	}

	public List<Petfotonoticia> getLisPetfotonoticia() {
		return lisPetfotonoticia;
	}

	public void setLisPetfotonoticia(List<Petfotonoticia> lisPetfotonoticia) {
		this.lisPetfotonoticia = lisPetfotonoticia;
	}

	public Petfotonoticia getPetfotonoticiaSeleccionada() {
		return petfotonoticiaSeleccionada;
	}

	public void setPetfotonoticiaSeleccionada(
			Petfotonoticia petfotonoticiaSeleccionada) {
		this.petfotonoticiaSeleccionada = petfotonoticiaSeleccionada;
	}

	public StreamedContent getStreamedContent() {
		return streamedContent;
	}

	public void setStreamedContent(StreamedContent streamedContent) {
		this.streamedContent = streamedContent;
	}

	public boolean isFotoSubida() {
		return fotoSubida;
	}

	public void setFotoSubida(boolean fotoSubida) {
		this.fotoSubida = fotoSubida;
	}

	public String getDescripcionFoto() {
		return descripcionFoto;
	}

	public void setDescripcionFoto(String descripcionFoto) {
		this.descripcionFoto = descripcionFoto;
	}

	public long getMaxfilesize() {
		return maxfilesize;
	}

	public void setMaxfilesize(long maxfilesize) {
		this.maxfilesize = maxfilesize;
	}
}
