package com.web.cementerio.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@RequestScoped
public class MensajeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1069184869581844243L;
	
	private String mensaje;
	private String link;
	private String linkTitulo;
	
	public MensajeBean() {
	}
	
	@PostConstruct
	public void PostMensajeBean(){
		try{
			//FacesContext context = FacesContext.getCurrentInstance();
			//mensaje = context.getExternalContext().getRequestParameterMap().get("mensaje");
			/*if(mensaje == null || mensaje.trim().length() == 0){
				FacesUtil facesUtil = new FacesUtil();
				facesUtil.redirect("home.jsf");
			}*/
			FacesUtil facesUtil = new FacesUtil();
			UsuarioBean usuarioBean = (UsuarioBean)facesUtil.getSessionBean("usuarioBean");
			
			mensaje = usuarioBean.getMensaje();
			link = usuarioBean.getLink();
			linkTitulo = usuarioBean.getLinkTitulo();
			
			usuarioBean.setMensaje("");
			usuarioBean.setLink("");
			facesUtil.setSessionBean("usuarioBean", usuarioBean);
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public String getLinkTitulo() {
		return linkTitulo;
	}

	public void setLinkTitulo(String linkTitulo) {
		this.linkTitulo = linkTitulo;
	}
	
}
