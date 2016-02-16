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

//import com.web.cementerio.bo.CotempresaBO;
import com.web.cementerio.bo.CotoficinaBO;
import com.web.cementerio.bo.PetservicioBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Cotempresa;
import com.web.cementerio.pojo.annotations.Cotoficina;
import com.web.cementerio.pojo.annotations.Petfotoservicio;
import com.web.cementerio.pojo.annotations.Petservicio;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class ServicioAdminBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4748535305651371565L;
	private int idservicio;
	private int idempresa;
	private Petservicio petservicio;
	private Petservicio petservicioClon;
	private List<Petfotoservicio> lisPetfotoservicio;
	private List<Petfotoservicio> lisPetfotoservicioClon;
	private List<Cotoficina> lisCotoficina;
	//private List<Cotempresa> lisCotempresa;
	private Petfotoservicio petfotoservicioSeleccionado;
	private StreamedContent streamedContent;
	private UploadedFile uploadedFile;
	private String descripcionFoto;
	private boolean fotoSubida;
	private long maxfilesize;
	
	public ServicioAdminBean() {
		petservicio = new Petservicio(0, new Setestado(), null, new Setusuario(), null, null, null, new Cotoficina(), new Cotempresa(), null, null, null, false, new Date(), null, 0);
		petservicioClon = new Petservicio(0, new Setestado(), null, new Setusuario(), null, null, null,new Cotoficina(), new Cotempresa(), null, null, null, false, new Date(), null, 0);
		lisPetfotoservicio = new ArrayList<Petfotoservicio>();
		lisPetfotoservicioClon = new ArrayList<Petfotoservicio>();
		lisCotoficina = new ArrayList<Cotoficina>();
		//lisCotempresa = new ArrayList<Cotempresa>();
		petfotoservicioSeleccionado = new Petfotoservicio();
		descripcionFoto = "";
		fotoSubida = false;
		maxfilesize = Parametro.TAMAÑO_IMAGEN;
		
		//llenarListaEmpresa();
	}
	
	@PostConstruct
	public void PostServicioAdminBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idservicio");
			Object par2 = facesUtil.getParametroUrl("idempresa");
			if(par != null && par2 != null){
				idservicio = Integer.parseInt(par.toString());
				idempresa = Integer.parseInt(par2.toString());
				
				CotoficinaBO cotoficinaBO = new CotoficinaBO();
				lisCotoficina = cotoficinaBO.lisCotoficinaByIdempresa(idempresa);
				
				if(idservicio > 0){
					consultaServicio();
				}else{
					PetservicioBO petservicioBO = new PetservicioBO();
					int orden = petservicioBO.getMaxOrden();
					petservicio.setOrden(orden + 1);
					petservicio.setCotempresa(new Cotempresa(idempresa, null));
				}
			}else{
				facesUtil.redirect("../pages/home.jsf");
			}
		} catch(NumberFormatException ne){
			try{facesUtil.redirect("../pages/home.jsf");}catch(Exception e){}
		} catch(Exception e) {
			e.printStackTrace();
			try{facesUtil.redirect("home.jsf");}catch(Exception e2){}
		}
	}
	
	private void consultaServicio(){
		if(this.idservicio > 0){
			try {
				PetservicioBO petservicioBO = new PetservicioBO();
				petservicio = petservicioBO.getPetservicioConObjetosById(idservicio,idempresa);
				
				if(petservicio != null && petservicio.getIdservicio() > 0){
					petservicioClon = petservicio.clonar();
					
					if(petservicio.getCotempresa() != null && petservicio.getCotempresa().getIdempresa() > 0){
						CotoficinaBO cotoficinaBO = new CotoficinaBO();
						lisCotoficina = cotoficinaBO.lisCotoficinaByIdempresa(petservicio.getCotempresa().getIdempresa());
					}
					
					if(petservicio.getPetfotoservicios() != null && petservicio.getPetfotoservicios().size() > 0){
						lisPetfotoservicio = new ArrayList<Petfotoservicio>(petservicio.getPetfotoservicios());
						
						for(Petfotoservicio petfotoservicio : lisPetfotoservicio){
							lisPetfotoservicioClon.add(petfotoservicio.clonar());
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
	}
	
	/*private void llenarListaEmpresa(){
		try {
			CotempresaBO cotempresaBO = new CotempresaBO();
			lisCotempresa = cotempresaBO.lisCotempresa();
		} catch (Exception e) {
			e.printStackTrace();
		    new MessageUtil().showErrorMessage("Error", "Ha ocurrido un error inesperado. Comunicar al Webmaster!");
		}
	}*/

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
		petservicio.setRutafoto(petfotoservicioSeleccionado.getRuta());
		petfotoservicioSeleccionado = new Petfotoservicio();
		new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.","");
	}
	
	public void quitarFotoGaleria(){
		if(petfotoservicioSeleccionado.getRuta().equalsIgnoreCase(petservicio.getRutafoto())){
			new MessageUtil().showInfoMessage("La foto a eliminar es la foto principal de éste servicio. Seleccione otra foto como principal para poderla eliminar.","");
		}else{
			lisPetfotoservicio.remove(petfotoservicioSeleccionado);
			petfotoservicioSeleccionado = new Petfotoservicio();
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
			
			PetservicioBO petservicioBO = new PetservicioBO();
			Petfotoservicio petfotoservicio = new Petfotoservicio();
			
			if(fotoSubida && descripcionFoto != null && descripcionFoto.trim().length() > 0){
				petfotoservicio.setDescripcion(descripcionFoto);
			}
			
			if(idservicio == 0){
				ok = petservicioBO.ingresar(petservicio, petfotoservicio, uploadedFile);
				if(ok){
					mostrarPaginaMensaje("Servicio creado con exito!!");
				}else{
					mostrarPaginaMensaje("No existen cambios que guardar.");
				}
			}else{
				ok = petservicioBO.modificar(petservicio, petservicioClon, lisPetfotoservicio, lisPetfotoservicioClon, petfotoservicio, uploadedFile);
				if(ok){
					mostrarPaginaMensaje("Servicio modificado con exito!!");
				}else{
					mostrarPaginaMensaje("No existen cambios que guardar.");
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
			PetservicioBO petservicioBO = new PetservicioBO();
			
			boolean ok = petservicioBO.eliminar(petservicio);
			if(ok){
				mostrarPaginaMensaje("Servicio eliminado con exito!!");
			}else{
				mostrarPaginaMensaje("No se ha podido eliminar el Servicio. Comunicar al Webmaster.");
			}
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public Petservicio getPetservicio() {
		return petservicio;
	}

	public void setPetservicio(Petservicio petservicio) {
		this.petservicio = petservicio;
	}

	public int getIdservicio() {
		return idservicio;
	}

	public void setIdservicio(int idservicio) {
		this.idservicio = idservicio;
	}

	public List<Petfotoservicio> getLisPetfotoservicio() {
		return lisPetfotoservicio;
	}

	public void setLisPetfotoservicio(List<Petfotoservicio> lisPetfotoservicio) {
		this.lisPetfotoservicio = lisPetfotoservicio;
	}

	public Petfotoservicio getPetfotoservicioSeleccionado() {
		return petfotoservicioSeleccionado;
	}

	public void setPetfotoservicioSeleccionado(
			Petfotoservicio petfotoservicioSeleccionado) {
		this.petfotoservicioSeleccionado = petfotoservicioSeleccionado;
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

	public List<Cotoficina> getLisCotoficina() {
		return lisCotoficina;
	}

	public void setLisCotoficina(List<Cotoficina> lisCotoficina) {
		this.lisCotoficina = lisCotoficina;
	}

	/*public List<Cotempresa> getLisCotempresa() {
		return lisCotempresa;
	}

	public void setLisCotempresa(List<Cotempresa> lisCotempresa) {
		this.lisCotempresa = lisCotempresa;
	}*/

	public int getIdempresa() {
		return idempresa;
	}

	public void setIdempresa(int idempresa) {
		this.idempresa = idempresa;
	}
}
