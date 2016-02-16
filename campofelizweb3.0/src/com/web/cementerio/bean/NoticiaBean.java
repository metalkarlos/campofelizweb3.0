package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.web.cementerio.bo.PetnoticiaBO;
import com.web.cementerio.pojo.annotations.Petfotonoticia;
import com.web.cementerio.pojo.annotations.Petnoticia;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class NoticiaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4154729518983967192L;
	private int idnoticia;
	private Petnoticia petnoticia;
	private List<Petfotonoticia> lisPetfotonoticia;
	
	public NoticiaBean() {
		petnoticia = new Petnoticia();
		lisPetfotonoticia = new ArrayList<Petfotonoticia>();
	}

	@PostConstruct
	public void PostNoticiaBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idnoticia");
			if(par != null){
				idnoticia = Integer.parseInt(par.toString());
				consultarNoticias();
			}else{
				facesUtil.redirect("home.jsf");
			}
		} catch(NumberFormatException ne){
			try{facesUtil.redirect("home.jsf");}catch(Exception e){}
		} catch(Exception e) {
			e.printStackTrace();
			try{facesUtil.redirect("home.jsf");}catch(Exception e2){}
		}
	}
	
	private void consultarNoticias(){
		if(this.idnoticia > 0){
			try {
				PetnoticiaBO petnoticiaBO = new PetnoticiaBO();
				petnoticia = petnoticiaBO.getPetnoticiaConObjetosById(idnoticia);
				
				if(petnoticia != null && petnoticia.getPetfotonoticias() != null && petnoticia.getPetfotonoticias().size() > 0){
					lisPetfotonoticia = new ArrayList<Petfotonoticia>(petnoticia.getPetfotonoticias());
				}
			} catch(Exception e) {
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
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
}
