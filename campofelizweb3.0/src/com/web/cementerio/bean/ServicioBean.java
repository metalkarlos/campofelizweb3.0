package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.web.cementerio.bo.PetservicioBO;
import com.web.cementerio.pojo.annotations.Petfotoservicio;
import com.web.cementerio.pojo.annotations.Petservicio;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class ServicioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2778897181870142036L;
	private int idservicio;
	private int idempresa;
	private Petservicio petservicio;
	private List<Petfotoservicio> lisPetfotoservicio;
	
	public ServicioBean() {
		petservicio = new Petservicio();
		lisPetfotoservicio = new ArrayList<Petfotoservicio>();
	}

	@PostConstruct
	public void PostServicioBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idservicio");
			Object par2 = facesUtil.getParametroUrl("idempresa");
			if(par != null && par2 != null){
				idservicio = Integer.parseInt(par.toString());
				idempresa = Integer.parseInt(par2.toString());
				
				consultarServicio();
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
	
	private void consultarServicio(){
		if(this.idservicio > 0){
			try {
				PetservicioBO petservicioBO = new PetservicioBO();
				petservicio = petservicioBO.getPetservicioConObjetosById(idservicio, idempresa);
				
				if(petservicio != null && petservicio.getPetfotoservicios() != null && petservicio.getPetfotoservicios().size() > 0){
					lisPetfotoservicio = new ArrayList<Petfotoservicio>(petservicio.getPetfotoservicios());
				}
			} catch(Exception e) {
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
	}
	
	public int getIdservicio() {
		return idservicio;
	}

	public void setIdservicio(int idservicio) {
		this.idservicio = idservicio;
	}

	public Petservicio getPetservicio() {
		return petservicio;
	}

	public void setPetservicio(Petservicio petservicio) {
		this.petservicio = petservicio;
	}

	public List<Petfotoservicio> getLisPetfotoservicio() {
		return lisPetfotoservicio;
	}

	public void setLisPetfotoservicio(List<Petfotoservicio> lisPetfotoservicio) {
		this.lisPetfotoservicio = lisPetfotoservicio;
	}

	public int getIdempresa() {
		return idempresa;
	}

	public void setIdempresa(int idempresa) {
		this.idempresa = idempresa;
	}

}
