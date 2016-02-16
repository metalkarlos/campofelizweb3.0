package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.web.cementerio.bo.PetnoticiaBO;
import com.web.cementerio.pojo.annotations.Petnoticia;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class NoticiasPrincipalesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3717062786618497479L;
	private List<Petnoticia> lisPetnoticias;

	public NoticiasPrincipalesBean() {
		lisPetnoticias = new ArrayList<Petnoticia>();
		consultarNoticiasPrincipales();
	}
	
	public void consultarNoticiasPrincipales(){
		try {
			PetnoticiaBO petnoticiaBO = new PetnoticiaBO();
			lisPetnoticias = petnoticiaBO.lisPetnoticiaPrincipales();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public List<Petnoticia> getLisPetnoticias() {
		return lisPetnoticias;
	}

	public void setLisPetnoticias(List<Petnoticia> lisPetnoticias) {
		this.lisPetnoticias = lisPetnoticias;
	}
	
	
}
