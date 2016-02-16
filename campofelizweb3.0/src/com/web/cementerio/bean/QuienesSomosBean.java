package com.web.cementerio.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.web.cementerio.bo.PetinformacionBO;
import com.web.cementerio.pojo.annotations.Petfotoinformacion;
import com.web.cementerio.pojo.annotations.Petinformacion;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class QuienesSomosBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3186508458073717263L;
	private Petinformacion petinformacion;
    private List<Petfotoinformacion>listpetfotoinformacion;		
	
	public QuienesSomosBean() {
		petinformacion = new Petinformacion(0, new Setestado(), new Setusuario(), null, null, null, null, null, null,null, null,null, null, null, null);
		listpetfotoinformacion = new ArrayList<Petfotoinformacion>();
		consultarInformacion();
	}
	
	private void consultarInformacion(){
		try{
			PetinformacionBO petinformacionBO = new PetinformacionBO();
			petinformacion = petinformacionBO.getPetinformacionById(1,1);
			if(petinformacion!=null && petinformacion.getPetfotoinformaciones().size()>0 && !petinformacion.getPetfotoinformaciones().isEmpty() ){
			  listpetfotoinformacion = new ArrayList<Petfotoinformacion>(petinformacion.getPetfotoinformaciones());
			}
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showErrorMessage("Lamentamos que tenga inconvenientes","");
		}
	}
	
	public Petinformacion getPetinformacion() {
		return petinformacion;
	}

	public void setPetinformacion(Petinformacion petinformacion) {
		this.petinformacion = petinformacion;
	}

	public List<Petfotoinformacion> getListpetfotoinformacion() {
		return listpetfotoinformacion;
	}

	public void setListpetfotoinformacion(
			List<Petfotoinformacion> listpetfotoinformacion) {
		this.listpetfotoinformacion = listpetfotoinformacion;
	}
	
}
