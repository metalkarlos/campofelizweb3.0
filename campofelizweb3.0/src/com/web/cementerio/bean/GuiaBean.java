package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.web.cementerio.bo.PetguiaBO;
import com.web.cementerio.pojo.annotations.Petfotoguia;
import com.web.cementerio.pojo.annotations.Petguia;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;



@ManagedBean
@ViewScoped
public class GuiaBean implements Serializable{

	
	private static final long serialVersionUID = -1862606539425133513L;
	private int idguia;
	private Petguia petguia;
	private List<Petfotoguia> lispetfotoguia;
	
	public GuiaBean(){
	}

	@PostConstruct
	public void PostGuiaBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idguia");
			if(par != null){
				idguia = Integer.parseInt(par.toString());
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
		if(this.idguia > 0){
			try {
				PetguiaBO petguiaBO = new PetguiaBO();
				petguia= petguiaBO.getPetguiabyId(idguia, 1);
				lispetfotoguia = new ArrayList<Petfotoguia>();
				if(petguia !=null && petguia.getPetfotoguias().size()>0 && !petguia.getPetfotoguias().isEmpty()){
				  lispetfotoguia = new ArrayList<>(petguia.getPetfotoguias());
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
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

}
