package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.web.cementerio.bo.PetguiaBO;
import com.web.cementerio.pojo.annotations.Petfotoguia;
import com.web.cementerio.pojo.annotations.Petguia;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;



@ManagedBean
@RequestScoped
public class PromocionBean implements Serializable{

	
	private static final long serialVersionUID = -1862606539425133513L;
	private int idguia;
	private Petguia petguia;
	private List<Petfotoguia> lispetfotoguia;
	private List<Petguia> lisPetguiaPrincipal;
	
	public PromocionBean(){
		petguia = new Petguia();
		lispetfotoguia = new ArrayList<Petfotoguia>();
		lisPetguiaPrincipal = new ArrayList<Petguia>();
	}

	@PostConstruct
	public void PostPromocionBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idguia");
			if(par != null){
				idguia = Integer.parseInt(par.toString());
				consultarPromociones();
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
	
	private void consultarPromociones(){
		if(this.idguia > 0){
			try {
				PetguiaBO petguiaBO = new PetguiaBO();
				petguia= petguiaBO.getPetguiaByIdConObjetos(idguia);
				
				if(petguia != null && petguia.getPetfotoguias() != null && petguia.getPetfotoguias().size() > 0){
					//lispetfotoguia = new ArrayList<>(petguia.getPetfotoguias());
				  
					//ordenar por fecharegistro
					Petfotoguia[] arr = new Petfotoguia[petguia.getPetfotoguias().size()];
					arr = petguia.getPetfotoguias().toArray(arr);
					Arrays.sort(arr, Petfotoguia.FecharegistroComparator);
					lispetfotoguia = new ArrayList<>(Arrays.asList(arr));
				}
			} catch(Exception e) {
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
	}
	
	public List<Petguia> consultarPromocionesPrincipales(){
		
		try {
			PetguiaBO petguiaBO = new PetguiaBO();
			lisPetguiaPrincipal = petguiaBO.lisPetguiaPrincipales(3);
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
		
		return lisPetguiaPrincipal;
	}

	public int getIdguia() {
		return idguia;
	}

	public void setIdguia(int idguia) {
		this.idguia = idguia;
	}

	public Petguia getPetguia() {
		return petguia;
	}

	public List<Petfotoguia> getLispetfotoguia() {
		return lispetfotoguia;
	}

	public void setLispetfotoguia(List<Petfotoguia> lispetfotoguia) {
		this.lispetfotoguia = lispetfotoguia;
	}

	public void setPetguia(Petguia petguia) {
		this.petguia = petguia;
	}

	public List<Petguia> getLisPetguiaPrincipal() {
		return lisPetguiaPrincipal;
	}

	public void setLisPetguiaPrincipal(List<Petguia> lisPetguiaPrincipal) {
		this.lisPetguiaPrincipal = lisPetguiaPrincipal;
	}

}
