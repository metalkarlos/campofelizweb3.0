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
public class GuiaAdminBean  implements Serializable{
	
	private static final long serialVersionUID = -1314030580304673400L;
	private int idguia;
	private Petguia petguia;
	private Petguia petguiaClon;
	private List<Petfotoguia> lisPetfotoguia;
	private List<Petfotoguia> lisPetfotoguiaClon;
	private Petfotoguia petfotoguiaSeleccionada;
	private StreamedContent streamedContent;
	private UploadedFile uploadedFile;
	private String descripcionFoto;
	private boolean fotoSubida;
	private int indice;
	
	

	public GuiaAdminBean(){
		petguia = new Petguia(0, new Setestado(), new Setusuario(), null, null, null, null,null,null, new Date(), null,false, null);
		lisPetfotoguia = new ArrayList<Petfotoguia>();
		petfotoguiaSeleccionada =  new Petfotoguia(0, new Setestado(), new Setusuario(), new Petguia(), null, null, null, null, null, null, null);
		fotoSubida =false;
		descripcionFoto="";
		idguia=0;
	}
	
	 
	@PostConstruct
	public void PostGuiaAdminBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idguia");
			if(par!=null){
				idguia= (Integer.parseInt(par.toString()));
				consultaGuia();
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
	
	private void consultaGuia(){
		if(this.idguia > 0){
			try {
				petguiaClon = new Petguia(0, new Setestado(), new Setusuario(), null, null, null, null,null,null, null, null,false, null);
				lisPetfotoguiaClon = new ArrayList<Petfotoguia>();
				PetguiaBO petguiaBO = new PetguiaBO();
				petguia = petguiaBO.getPetguiabyId(idguia, 1);
				
				if(petguia != null && petguia.getIdguia() > 0){
					petguiaClon = petguia.clonar();
					
					if(petguia.getPetfotoguias() != null && !petguia.getPetfotoguias().isEmpty()){
						lisPetfotoguia = new ArrayList<Petfotoguia>(petguia.getPetfotoguias());
						
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
			if (event.getFile().getSize() <= Parametro.TAMAÑO_IMAGEN){
				uploadedFile = event.getFile();
				streamedContent = new DefaultStreamedContent(event.getFile().getInputstream(), event.getFile().getContentType());
				
				FacesUtil facesUtil = new FacesUtil();
				UsuarioBean usuarioBean = (UsuarioBean)facesUtil.getSessionBean("usuarioBean");
				usuarioBean.setStreamedContent(streamedContent);
				facesUtil.setSessionBean("usuarioBean", usuarioBean);
				fotoSubida = true;
				new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.","");
			}else{
				new MessageUtil().showErrorMessage("Tamaño de la imagen no puede ser mayor a 700KB","");
			}	
		}catch(Exception x){
			x.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public void ponerFotoPrincipal(){
		petguia.setRutafoto(petfotoguiaSeleccionada.getRuta());
		petfotoguiaSeleccionada = new Petfotoguia();
		new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.","");
	}
	
	public void quitarFotoGaleria(){
		if (petfotoguiaSeleccionada !=null){
			if (!petfotoguiaSeleccionada.getRuta().equals(petguia.getRutafoto())){
				lisPetfotoguia.remove(petfotoguiaSeleccionada);
				petfotoguiaSeleccionada = new Petfotoguia();
				new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.","");
			}
			else {
				new MessageUtil().showInfoMessage("No se puede eliminar foto que ha sido seleccionada como foto de perfil, cambie de foto de perfil y vuelva a intentarlo","");
			}
		}		
	}
	
	public void borrarFotoSubida(){
		streamedContent = null;
		uploadedFile = null;
		fotoSubida = false;
	}
	
	public void grabar(){
	 try{
		if(validarcampos()){
			PetguiaBO petguiaBO = new PetguiaBO();
			boolean ok = false;
			
			if(idguia == 0){
				ok = petguiaBO.ingresarPetguiaBO(petguia,1,  uploadedFile,descripcionFoto);
				if(ok){
					mostrarPaginaMensaje("Guía creada con éxito!!");
				}else{
					new MessageUtil().showWarnMessage("No se ha podido crear la Guía. Comunicar al Webmaster.","");
				}
			}else{
				
				ok = petguiaBO.modificar(petguia, petguiaClon, lisPetfotoguia, lisPetfotoguiaClon,2,uploadedFile,descripcionFoto);
				if(ok){
					mostrarPaginaMensaje("Guía modificada con éxito!!");
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
	
	private void mostrarPaginaMensaje(String mensaje) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		usuarioBean.setMensaje(mensaje);
		
		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("../pages/mensaje.jsf");	 
	}
	
	public boolean validarcampos(){
		boolean ok = true;
		Date fechaactual = new Date();
		String textodescripcion= (petguia.getDescripcion()!=null ? petguia.getDescripcion().replaceAll("\\<.*?\\>", "") : "" );
		if(petguia.getTitulo()==null|| petguia.getTitulo().length()==0){
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar el Título del artículo","");
		}
		else if(textodescripcion==null|| textodescripcion.length()==0){
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar el contendio del artículo","");
		}else if (petguia.getFechapublicacion().after(fechaactual)){
			ok = false;
			new MessageUtil().showInfoMessage("Fecha de publicación no pueder ser mayor a la fecha de hoy","");
		}	
		else if((streamedContent!=null || uploadedFile != null) && (descripcionFoto==null || descripcionFoto.length()==0)){
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar la descripción de la imagen a subir","");
		}
		return ok;
	}
	
	public void eliminar(){
		
		try{
			PetguiaBO petguiaBO = new PetguiaBO();
			boolean ok = petguiaBO.eliminarBO(petguia, lisPetfotoguiaClon,2);
			
			if(ok){
				mostrarPaginaMensaje("Guía eliminada con éxito!!");
			}else{
				new MessageUtil().showWarnMessage("No se ha podido eliminar la Guía. Comunicar al Webmaster.","");
			}
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
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

	public Petfotoguia getPetfotoguiaSeleccionada() {
		return petfotoguiaSeleccionada;
	}

	public void setPetfotoguiaSeleccionada(Petfotoguia petfotoguiaSeleccionada) {
		this.petfotoguiaSeleccionada = petfotoguiaSeleccionada;
	}

	public StreamedContent getStreamedContent() {
		return streamedContent;
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


	public int getIdguia() {
		return idguia;
	}


	public void setIdguia(int idguia) {
		this.idguia = idguia;
	}


	public int getIndice() {
		return indice;
	}


	public void setIndice(int indice) {
		this.indice = indice;
	}
	
	
}
