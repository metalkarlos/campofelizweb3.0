package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.web.cementerio.bo.SetpeticionclaveBO;
import com.web.cementerio.bo.SetusuarioBO;
import com.web.cementerio.pojo.annotations.Setpeticionclave;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;
import com.web.util.Utilities;

@ManagedBean
@ViewScoped
public class CambiarClaveBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5798872749559968846L;
	private String claveNueva;
	private String claveNuevaRep;
	private UUID uid;
	private Setpeticionclave setpeticionclave;
	
	public CambiarClaveBean() {
		claveNueva = "";
		claveNuevaRep = "";
	}
	
	@PostConstruct
	public void PostCambiarClaveBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("uid");
			if(par != null){
				uid = UUID.fromString(par.toString());
				
				SetpeticionclaveBO setpeticionclaveBO = new SetpeticionclaveBO();
				setpeticionclave = setpeticionclaveBO.getSetpeticionclaveByUid(uid);
				
				if(setpeticionclave == null || setpeticionclave.getIdpeticionclave() == 0){
					redireccionar("home");
				}
			}else{
				redireccionar("home");
			}
		}catch(Exception e){
			e.printStackTrace();
			redireccionar("home");
		}
	}
	
	private void redireccionar(String pagina){
		try{
			FacesUtil facesUtil = new FacesUtil();
			facesUtil.redirectByPropertyFileKey(pagina);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void cambiarClave(){
		if(validacionOk()){
			try{
				boolean ok = false;
				
				Utilities utilities = new Utilities();
				String cifrado = utilities.cifrar(claveNueva);
				
				SetusuarioBO setusuarioBO = new SetusuarioBO();
				ok = setusuarioBO.cambiarClave(setpeticionclave.getUsuario(), cifrado);
				
				if(ok){
					redireccionar("login");
				}else{
					new MessageUtil().showInfoMessage("No se ha podido cambiar la clave", "");
				}
			}catch(Exception e){
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
	}
	
	private boolean validacionOk(){
		boolean ok = false;
		
		if(claveNueva.compareTo(claveNuevaRep) == 0){
			ok = true;
		}else{
			new MessageUtil().showWarnMessage("Claves no son iguales. Corrija e intente nuevamente", "");
		}
		
		return ok;
	}
	
	public String getClaveNueva() {
		return claveNueva;
	}

	public void setClaveNueva(String claveNueva) {
		this.claveNueva = claveNueva;
	}

	public String getClaveNuevaRep() {
		return claveNuevaRep;
	}

	public void setClaveNuevaRep(String claveNuevaRep) {
		this.claveNuevaRep = claveNuevaRep;
	}

}
