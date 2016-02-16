package com.web.cementerio.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import java.util.ArrayList;
import java.util.List;
import com.web.cementerio.bo.PetmascotahomenajeBO;
import com.web.cementerio.pojo.annotations.Petespecie;
import com.web.cementerio.pojo.annotations.Petfotomascota;
import com.web.cementerio.pojo.annotations.Petmascotahomenaje;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class MascotaHomenajeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5791044831809774834L;
	private Petmascotahomenaje petmascotahomenaje;
	private List<Petfotomascota>listpetfotomascota;
	private int idmascota;
	
	public MascotaHomenajeBean() {
		inicializarobjetos();
	}
	
	@PostConstruct
	public void initMascotaHomenajeBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idmascota");
			if(par != null){
				idmascota = Integer.parseInt(par.toString());
				consultarMascotaHomenaje();
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
	
	private void consultarMascotaHomenaje(){
		try {
			PetmascotahomenajeBO mascotaHomenajeBO= new PetmascotahomenajeBO();
			petmascotahomenaje = mascotaHomenajeBO.getPetmascotahomenajebyId(idmascota, 1,false);
			if((petmascotahomenaje !=null)&&(!petmascotahomenaje.getPetfotomascotas().isEmpty()) && petmascotahomenaje.getPetfotomascotas().size()>0 ){
				listpetfotomascota = new ArrayList<Petfotomascota>(petmascotahomenaje.getPetfotomascotas());

			}
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public void inicializarobjetos(){
		petmascotahomenaje = new Petmascotahomenaje(0,new Setestado(),new Setusuario(),new Petespecie(),null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0,new BigDecimal(0),null,false,false,null);
		Petespecie petespecie = new Petespecie();
		petmascotahomenaje.setPetespecie(petespecie);
		idmascota =0;
	}
	
	public Petmascotahomenaje getPetmascotahomenaje() {
		return petmascotahomenaje;
	}

	public void setPetmascotahomenaje(Petmascotahomenaje petmascotahomenaje) {
		this.petmascotahomenaje = petmascotahomenaje;
	}


	public int getIdmascota() {
		return idmascota;
	}


	public void setIdmascota(int idmascota) {
		this.idmascota = idmascota;
		
	}

	public List<Petfotomascota> getListpetfotomascota() {
		return listpetfotomascota;
	}

	public void setListpetfotomascota(List<Petfotomascota> listpetfotomascota) {
		this.listpetfotomascota = listpetfotomascota;
	}
	

	
}
