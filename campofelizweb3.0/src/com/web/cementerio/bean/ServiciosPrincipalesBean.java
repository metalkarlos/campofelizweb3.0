package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.web.cementerio.bo.PetservicioBO;
import com.web.cementerio.pojo.annotations.Petservicio;
import com.web.util.MessageUtil;

@ManagedBean
@RequestScoped
public class ServiciosPrincipalesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8514670824522117617L;
	private List<Petservicio> lisPetservicio;

	public ServiciosPrincipalesBean() {
		lisPetservicio = new ArrayList<Petservicio>();
		consultarServiciosPrincipales();
	}
	
	public void consultarServiciosPrincipales(){
		try {
			PetservicioBO petservicioBO = new PetservicioBO();
			lisPetservicio = petservicioBO.lisPetservicioPrincipales(3);
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public List<Petservicio> getLisPetservicio() {
		return lisPetservicio;
	}

	public void setLisPetservicio(List<Petservicio> lisPetservicio) {
		this.lisPetservicio = lisPetservicio;
	}
	
	
}
