package com.web.faces.listeners;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

import com.web.cementerio.bean.UsuarioBean;
import com.web.util.FacesUtil;

public class SecurityPhaseListener implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3917092198608197271L;

	public SecurityPhaseListener() {
	}
	
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;//PhaseId.ANY_PHASE;
	}
	
	public void afterPhase(PhaseEvent phaseEvent) {
		if(phaseEvent.getPhaseId() == PhaseId.RESTORE_VIEW){
			FacesContext facesContext = phaseEvent.getFacesContext();
			UsuarioBean usuarioBean = (UsuarioBean) new FacesUtil().getSessionBean("usuarioBean");
			HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
			String vista = request.getRequestURI();
			//String param = request.getQueryString();
			
			//LIMPIA ESPACIO USADO POR FOTO VISTA PREVIA DEL UPLOAD
			/*if(usuarioBean != null && usuarioBean.getStreamedContent() != null){
				usuarioBean.setStreamedContent(null);
				facesUtil.setSessionBean("usuarioBean", usuarioBean);
			}*/
			
			boolean loginPage = vista != null && vista.contains("pages/login.jsf");
			if(loginPage && usuarioBean != null && usuarioBean.isAutenticado()){
				try{
					facesContext.getExternalContext().redirect("home.jsf");
					return;
				}catch(Exception e){}
			}

			//ARMA LA NAVEGACION DE PAGINAS VISITADAS 
			/*
			BreadCrumbBean breadCrumbBean = (BreadCrumbBean) facesUtil.getSessionBean("breadCrumbBean");
			
			if(breadCrumbBean == null){
				breadCrumbBean = new BreadCrumbBean();
				facesUtil.setSessionBean("breadCrumbBean", breadCrumbBean);
			}
			
			//HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
			//String[] urlarray = request.getRequestURI().split("/");
			String[] urlarray = vista.split("/");
			
			int x=-1;
			for(int i=0;i<urlarray.length;i++){
				if(urlarray[i].endsWith(".jsf")){
					x=i;
					break;
				}
			}
			if(x > -1){
				String pagina = urlarray[x];
				breadCrumbBean.armarBreadCrumb(pagina,param);
			}
			*/
		}
	}

	public void beforePhase(PhaseEvent phaseEvent) {
		
	}
}
