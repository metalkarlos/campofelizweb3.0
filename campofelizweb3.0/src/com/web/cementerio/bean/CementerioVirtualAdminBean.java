package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.web.cementerio.bo.PetfotoinstalacionBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Petfotoinstalacion;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class CementerioVirtualAdminBean implements Serializable{

	private static final long serialVersionUID = -3312876102105882061L;
	private boolean ingreso;
	private boolean modificacion;
	private boolean fotosubida;
	private UploadedFile uploadedFile;
	private StreamedContent streamedContent;
	private Petfotoinstalacion petfotoinstalacion;
	private Petfotoinstalacion petfotoinstalacionclone;
	private LazyDataModel<Petfotoinstalacion> listpetfotoinstalacion;
	private int idfoto;
	private String descripcionParam;
	private String texto;
	
	
	public CementerioVirtualAdminBean(){
		petfotoinstalacion = new Petfotoinstalacion();
		petfotoinstalacionclone = new Petfotoinstalacion();
		idfoto =0;
		fotosubida = false;
		descripcionParam = "buscar";
		texto="buscar";
	}
	
	
	@SuppressWarnings("serial")
	private void consultar(){
	   try {
		   
		   listpetfotoinstalacion = new LazyDataModel<Petfotoinstalacion>() {
		   public List<Petfotoinstalacion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
			   petfotoinstalacion = new Petfotoinstalacion();
			   List<Petfotoinstalacion> data = new ArrayList<Petfotoinstalacion>();
			   PetfotoinstalacionBO petfotoinstalacionBO = new PetfotoinstalacionBO();
			   int args[] = {0};
					
			   String[] textoBusqueda = null;
			   if(descripcionParam != null && descripcionParam.trim().length() > 0 && descripcionParam.trim().compareTo("buscar") != 0 ){
				  textoBusqueda = descripcionParam.split(" ");
				  first = 0;
				}
					
			   data = petfotoinstalacionBO.lisPetfotoinstalacionBusquedaByPage(textoBusqueda, pageSize, first, args,1);
			   this.setRowCount(args[0]);
   	           return data;
			   }
				
				@Override
               public void setRowIndex(int rowIndex) {
                   /*
                    * The following is in ancestor (LazyDataModel):
                    * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
                    */
                   if (rowIndex == -1 || getPageSize() == 0) {
                       super.setRowIndex(-1);
                   }
                   else {
                       super.setRowIndex(rowIndex % getPageSize());
                   }      
               }
			};
		   
		} catch(Exception e) {
		  e.printStackTrace();
		  new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		 }
	  
	}


	
	@PostConstruct
	public void PostCementerioVirtualAdminBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idfoto");
			
			if(par != null){
				idfoto = Integer.parseInt(par.toString());
				
				if(idfoto > 0){
					ingreso = true;
					modificacion=false;
					consultar();
				}else{
					ingreso = false;
					modificacion = true;
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
	
	
	
	public void handleFileUpload(FileUploadEvent event) {
		try{
			if (event.getFile().getSize() <= Parametro.TAMAÑO_IMAGEN){
				uploadedFile = event.getFile();
				streamedContent = new DefaultStreamedContent(event.getFile().getInputstream(), event.getFile().getContentType());
				fotosubida = true;
				FacesUtil facesUtil = new FacesUtil();
				UsuarioBean usuarioBean = (UsuarioBean)facesUtil.getSessionBean("usuarioBean");
				usuarioBean.setStreamedContent(streamedContent);
				facesUtil.setSessionBean("usuarioBean", usuarioBean);
				new MessageUtil().showInfoMessage("Ingrese la descripción y el orden de presentación de la foto a subir.","");
			}else{
				new MessageUtil().showErrorMessage("Tamaño de la imagen no puede ser mayor a 700KB","");
			}	
		}catch(Exception x){
			x.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	
	public void borrarFotoSubida(){
		streamedContent = null;
		uploadedFile = null;
		ingreso = false;
		modificacion = true;
		fotosubida = false;
	}
	
	public void clonar() throws Exception{
		try{
		  if(petfotoinstalacion != null ){
			  petfotoinstalacionclone = petfotoinstalacion.clonar();
		  }
		}catch(Exception x){
			x.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public void grabar(){
		try{
			if(validarcampos()){
				PetfotoinstalacionBO petfotoinstalacionBO = new PetfotoinstalacionBO();
				boolean ok = false;
				
				//ingreso
				if(petfotoinstalacion.getIdfotoinstalacion()==0){
					ok = petfotoinstalacionBO.ingresarPetfotoinstalacion(1, petfotoinstalacion, uploadedFile);
					if(ok){
						mostrarPaginaMensaje("Instalación ingresada con exito!!");
					}else{
						new MessageUtil().showWarnMessage("No se ha podido ingresar la instalación. Comunicar al Webmaster.","");
					}
				}else if(petfotoinstalacion.getIdfotoinstalacion()>0){
					ok = petfotoinstalacionBO.modificarPetfotoinstalacion(petfotoinstalacion, petfotoinstalacionclone);
					if(ok){
						mostrarPaginaMensaje("Instalación modificada con exito!!");
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
	
	public void eliminar(){
		try{
			PetfotoinstalacionBO petfotoinstalacionBO = new PetfotoinstalacionBO();
			boolean ok = false;
				
			ok = petfotoinstalacionBO.eliminarPetfotoinstalacion(petfotoinstalacion, petfotoinstalacionclone, 2);
			if(ok){
				mostrarPaginaMensaje("Instalación eliminada con exito!!");
			}else{
				new MessageUtil().showWarnMessage("No se ha podido eliminar la instalación. Comunicar al Webmaster.","");
			}
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
		public boolean validarcampos(){
			boolean ok = true;
			if(idfoto ==0 && uploadedFile ==null ){
			  ok = false;
			  new MessageUtil().showInfoMessage("Es necesario seleccionar la foto a subir","");
			}
			else if((idfoto ==0 && (petfotoinstalacion.getDescripcion()==null|| petfotoinstalacion.getDescripcion().length()==0))){
				ok = false;
				new MessageUtil().showInfoMessage("Es necesario ingresar la descripción de la foto a subir","");
			}
			else if(idfoto ==0 &&  petfotoinstalacion.getOrden()<=0 ){
				ok = false;
				new MessageUtil().showInfoMessage("Es necesario ingresar el orden de presentación de la foto a subir","");
			}	
			else if(idfoto >0 && listpetfotoinstalacion==null ){
				ok = false;
				new MessageUtil().showInfoMessage("Es necesario consultar la foto a modificar/eliminar","");
			}
			else if(idfoto >0 && petfotoinstalacion.getRuta()==null ){
				ok = false;
				new MessageUtil().showInfoMessage("Es necesario seleccionar la foto a modificar/eliminar","");
			}
			else if((idfoto >0) && (petfotoinstalacion.getDescripcion()==null|| petfotoinstalacion.getDescripcion().length()==0)) {
				ok = false;
				new MessageUtil().showInfoMessage("Es necesario ingresar la descripción de la foto a modificar/eliminar","");
			}
			else if(idfoto >0 && petfotoinstalacion.getOrden()<=0 ){
				ok = false;
				new MessageUtil().showInfoMessage("Es necesario ingresar el orden de presentación de la foto a modificar/eliminar","");
			}
			return ok;
		}

	public boolean isIngreso() {
		return ingreso;
	}


	public void setIngreso(boolean ingreso) {
		this.ingreso = ingreso;
	}


	public boolean isModificacion() {
		return modificacion;
	}


	public boolean isFotosubida() {
		return fotosubida;
	}


	public void setFotosubida(boolean fotosubida) {
		this.fotosubida = fotosubida;
	}


	public void setModificacion(boolean modificacion) {
		this.modificacion = modificacion;
	}


	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}


	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}


	public StreamedContent getStreamedContent() {
		return streamedContent;
	}


	public void setStreamedContent(StreamedContent streamedContent) {
		this.streamedContent = streamedContent;
	}


	public Petfotoinstalacion getPetfotoinstalacion() {
		return petfotoinstalacion;
	}


	public void setPetfotoinstalacion(Petfotoinstalacion petfotoinstalacion) {
		this.petfotoinstalacion = petfotoinstalacion;
	}


	public int getIdfoto() {
		return idfoto;
	}


	public void setIdfoto(int idfoto) {
		this.idfoto = idfoto;
	}

	

	public LazyDataModel<Petfotoinstalacion> getListpetfotoinstalacion() {
		return listpetfotoinstalacion;
	}


	public void setListpetfotoinstalacion(
			LazyDataModel<Petfotoinstalacion> listpetfotoinstalacion) {
		this.listpetfotoinstalacion = listpetfotoinstalacion;
	}


	public String getDescripcionParam() {
		return descripcionParam;
	}


	public void setDescripcionParam(String descripcionParam) {
		this.descripcionParam = descripcionParam;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}


	public Petfotoinstalacion getPetfotoinstalacionclone() {
		return petfotoinstalacionclone;
	}


	public void setPetfotoinstalacionclone(
			Petfotoinstalacion petfotoinstalacionclone) {
		this.petfotoinstalacionclone = petfotoinstalacionclone;
	}


	


}
