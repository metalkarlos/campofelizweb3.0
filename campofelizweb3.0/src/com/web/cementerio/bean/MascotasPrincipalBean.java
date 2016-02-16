package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.web.cementerio.bo.PetmascotahomenajeBO;
import com.web.cementerio.pojo.annotations.Petmascotahomenaje;
import com.web.util.MessageUtil;


@ManagedBean
@ViewScoped
public class MascotasPrincipalBean implements Serializable {

	
	private static final long serialVersionUID = -3691703732690624440L;
	
	List<Petmascotahomenaje> lisPetmascotahomenaje;

	public MascotasPrincipalBean() {
		lisPetmascotahomenaje = new ArrayList<Petmascotahomenaje>();
		consultarMascotasPrincipal();
	}

	public void consultarMascotasPrincipal(){
		try {
			PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
			lisPetmascotahomenaje = petmascotahomenajeBO.lisPetmascotaPrincipal(4);
			
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	
	public List<Petmascotahomenaje> getLisPetmascotahomenaje() {
		return lisPetmascotahomenaje;
	}

	public void setLisPetmascotahomenaje(List<Petmascotahomenaje> lisPetmascotahomenaje) {
		this.lisPetmascotahomenaje = lisPetmascotahomenaje;
	}

}
