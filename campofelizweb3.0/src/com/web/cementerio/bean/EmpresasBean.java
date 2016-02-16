package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import com.web.cementerio.bo.CotoficinaBO;
import com.web.cementerio.pojo.annotations.Cotempresa;
import com.web.cementerio.pojo.annotations.Cotoficina;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class EmpresasBean implements Serializable{

	private static final long serialVersionUID = 5406982578731779952L;
    private List<Cotoficina> lisCotoficina;
    private Cotoficina cotoficinaseleccionada;
	
	
	public EmpresasBean(){
		cotoficinaseleccionada = new Cotoficina(0, new Setestado(), new Setusuario(),new Cotempresa(),null,null,null,null,null,null,null,null,null,0);
		consultar();
	}
	
	public void consultar(){
		CotoficinaBO cotoficinaBO  = new CotoficinaBO();
		try {
			lisCotoficina = new ArrayList<Cotoficina>();
			lisCotoficina = cotoficinaBO.lisCotoficinaByIdempresa(0);
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public void onUserRowSelect(SelectEvent event) throws Exception{
		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("../admin/empresa-admin.jsf?faces-redirect=true&idoficina="+cotoficinaseleccionada.getIdoficina());
	}

	public List<Cotoficina> getLisCotoficina() {
		return lisCotoficina;
	}

	public void setLisCotoficina(List<Cotoficina> lisCotoficina) {
		this.lisCotoficina = lisCotoficina;
	}

	public Cotoficina getCotoficinaseleccionada() {
		return cotoficinaseleccionada;
	}

	public void setCotoficinaseleccionada(Cotoficina cotoficinaseleccionada) {
		this.cotoficinaseleccionada = cotoficinaseleccionada;
	}
	

}
