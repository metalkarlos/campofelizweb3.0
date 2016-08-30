package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;

import com.web.cementerio.bo.PetnoticiaBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Petfotonoticia;
import com.web.cementerio.pojo.annotations.Petnoticia;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
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
	private long maxfilesize;
	
	public NoticiaAdminBean() {
		petnoticia = new Petnoticia(0, new Setestado(), new Setusuario(), null, null, null, new Date(), null, null, null, new Date(), new Date(), false, 0);
		petnoticiaClon = new Petnoticia(0, new Setestado(), new Setusuario(), null, null, null, new Date(), null, null, null, new Date(), new Date(), false, 0);
		lisPetfotonoticia = new ArrayList<Petfotonoticia>();
		lisPetfotonoticiaClon = new ArrayList<Petfotonoticia>();
		petfotonoticiaSeleccionada = new Petfotonoticia();
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
			FileUtil fileUtil = new FileUtil();
			
			Petfotonoticia petfotonoticia = new Petfotonoticia();
			petfotonoticia.setImagen(event.getFile().getContents());
			petfotonoticia.setNombrearchivo(fileUtil.getFileExtention(event.getFile().getFileName()).toLowerCase());
			lisPetfotonoticia.add(petfotonoticia);
			
			new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.", "");
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
			new MessageUtil().showInfoMessage("La foto que desea eliminar es la principal. Seleccione otra foto como principal y vuelva a intentarlo","");
		} else {
			lisPetfotonoticia.remove(petfotonoticiaSeleccionada);
			new MessageUtil().showInfoMessage("Presione grabar para guardar los cambios","");
		}
		petfotonoticiaSeleccionada = new Petfotonoticia();
	}
	
	public void grabar(){
		try{
			if (validarcampos()) {
				PetnoticiaBO petnoticiaBO = new PetnoticiaBO();
				boolean ok = false;
				
				if(idnoticia == 0){
					ok = petnoticiaBO.ingresar(petnoticia, lisPetfotonoticia);
					if(ok){
						mostrarPaginaMensaje("Noticia creada con exito!!");
					}else{
						new MessageUtil().showWarnMessage("No existen cambios que guardar.","");
					}
				}else{
					ok = petnoticiaBO.modificar(petnoticia, petnoticiaClon, lisPetfotonoticia, lisPetfotonoticiaClon);
					if(ok){
						mostrarPaginaMensaje("Noticia modificada con exito!!");
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
	
	public boolean validarcampos() {
		boolean ok = true;
		
		if (petnoticia.getTitulo() == null
				|| petnoticia.getTitulo().length() == 0) {
			ok = false;
			new MessageUtil().showInfoMessage("Ingrese el Título de la Noticia","");
		} else if (petnoticia.getDescripcion() == null
				|| petnoticia.getDescripcion().length() == 0) {
			ok = false;
			new MessageUtil().showInfoMessage("Ingrese el Contenido de la Noticia","");
		} else if (petnoticia.getOrden() <= 0) {
			ok = false;
			new MessageUtil()
					.showInfoMessage("Ingrese el orden de la Noticia","");
		} 
		
		return ok;
	}
	
	private void mostrarPaginaMensaje(String mensaje) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean) new FacesUtil().getSessionBean("usuarioBean");
		usuarioBean.setMensaje(mensaje);
		usuarioBean.setLink("/pages/noticia?idnoticia="+idnoticia);
		usuarioBean.setLinkTitulo("Consultar Noticia");

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
				mostrarPaginaMensaje("No se ha podido eliminar la Noticia. Comunicar al Webmaster.");
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

	public long getMaxfilesize() {
		return maxfilesize;
	}

	public void setMaxfilesize(long maxfilesize) {
		this.maxfilesize = maxfilesize;
	}

}
