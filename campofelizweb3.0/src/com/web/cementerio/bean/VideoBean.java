package com.web.cementerio.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.web.cementerio.bo.PethomeBO;
import com.web.cementerio.pojo.annotations.Pethome;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@RequestScoped
public class VideoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7602676176873193565L;
	private int idhome;
	private Pethome pethome;
	
	public VideoBean() {
		setPethome(new Pethome());
	}

	@PostConstruct
	public void PostVideoBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idhome");
			if(par != null){
				idhome = Integer.parseInt(par.toString());
				
				consultarVideo();
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
	
	private void consultarVideo(){
		if(this.idhome > 0){
			try {
				PethomeBO pethomeBO = new PethomeBO();
				pethome = pethomeBO.getPethomebyId(idhome);
			} catch(Exception e) {
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
	}

	public int getIdhome() {
		return idhome;
	}

	public void setIdhome(int idhome) {
		this.idhome = idhome;
	}

	public Pethome getPethome() {
		return pethome;
	}

	public void setPethome(Pethome pethome) {
		this.pethome = pethome;
	}

}
