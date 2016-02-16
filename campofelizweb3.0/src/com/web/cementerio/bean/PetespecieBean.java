package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.web.cementerio.bo.PetespecieBO;
import com.web.cementerio.pojo.annotations.Petespecie;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class PetespecieBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5923920239273980899L;
	List<Petespecie> listPetespecie=null;
	
	public PetespecieBean(){
		listPetespecie = new ArrayList<Petespecie>(); 
		//Cambiar por una variable global donde esta seteado el estado
		consultarListpetespecie(1);
	}
	
	public void consultarListpetespecie(int estado){
		if (estado >0){
			try {
				PetespecieBO petespecieBo =new PetespecieBO();
				listPetespecie = petespecieBo.Listpetespecie(estado);
			} catch (Exception e) {
				e.printStackTrace();
			    new MessageUtil().showErrorMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
				
			
		}
	}

	public List<Petespecie> getListPetespecie() {
		return listPetespecie;
	}

	public void setListPetespecie(List<Petespecie> listPetespecie) {
		this.listPetespecie = listPetespecie;
	}
	

}
