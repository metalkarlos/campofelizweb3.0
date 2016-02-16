package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import com.web.cementerio.bo.PetenunciadoBO;
import com.web.cementerio.pojo.annotations.Petvenunciado;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped

public class EnunciadosBean  implements Serializable{
	
	private static final long serialVersionUID = -6325180654691511826L;
	
	private Petvenunciado petvenunciadoseleccionado;
	private List<Petvenunciado> listpetvenunciado;
	
	public EnunciadosBean(){
		petvenunciadoseleccionado = new Petvenunciado(0, null, null, 0, 0,null,null);
		consultar();
	}
	
	public void consultar(){
		try {
			listpetvenunciado = new ArrayList<Petvenunciado>();
			PetenunciadoBO petenunciadoBO = new PetenunciadoBO();
			listpetvenunciado = petenunciadoBO.getListpetvenunaciado();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
		
	}

	public void onRowSelect(SelectEvent event) throws Exception{
		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("enunciado.jsf?faces-redirect=true&idenuciado="+petvenunciadoseleccionado.getIdenunciado());
	}
	
	
	public Petvenunciado getPetvenunciadoseleccionado() {
		return petvenunciadoseleccionado;
	}

	public void setPetvenunciadoseleccionado(Petvenunciado petvenunciadoseleccionado) {
		this.petvenunciadoseleccionado = petvenunciadoseleccionado;
	}

	public List<Petvenunciado> getListpetvenunciado() {
		return listpetvenunciado;
	}

	public void setListpetvenunciado(List<Petvenunciado> listpetvenunciado) {
		this.listpetvenunciado = listpetvenunciado;
	}
	
}
