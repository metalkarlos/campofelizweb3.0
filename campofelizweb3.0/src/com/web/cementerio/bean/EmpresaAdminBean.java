package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;




import com.web.cementerio.bo.CotempresaBO;
import com.web.cementerio.bo.CotoficinaBO;
import com.web.cementerio.pojo.annotations.Cotempresa;
import com.web.cementerio.pojo.annotations.Cotoficina;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class EmpresaAdminBean implements Serializable {

	private static final long serialVersionUID = 590415272909549553L;

	private Cotoficina cotoficina;
	private Cotoficina cotoficinaclone;
	private List<Cotempresa> lisCotempresa;
	private int idoficina;
	
	public EmpresaAdminBean(){
		cotoficina = new Cotoficina(0, new Setestado(), new Setusuario(),new Cotempresa(),null,null,null,null,null,null,null,null,null,0);
		cotoficinaclone = new Cotoficina(0, new Setestado(), new Setusuario(),new Cotempresa(),null,null,null,null,null,null,null,null,null,0);
		lisCotempresa = new ArrayList<Cotempresa>();
		
		llenarListaEmpresa();
	}

	@PostConstruct
	public void PostEmpresaAdminBean() {
		
		FacesUtil facesUtil = new FacesUtil();
			
		try{
			Object par = facesUtil.getParametroUrl("idoficina");
			if(par != null){
				idoficina = Integer.parseInt(par.toString());
			
				if(idoficina > 0){
					consultar();
					cotoficinaclone = cotoficina.clonar();
				}
			}else{
				facesUtil.redirect("../pages/home.jsf");
			}
		} catch(NumberFormatException ne){
			try{facesUtil.redirect("../pages/home.jsf");}catch(Exception e){}
		} catch(Exception e) {
			e.printStackTrace();
			try{facesUtil.redirect("../pages/home.jsf");}catch(Exception e2){}
		}
	}
	
	private void llenarListaEmpresa(){
		try {
			CotempresaBO cotempresaBO = new CotempresaBO();
			lisCotempresa = cotempresaBO.lisCotempresa();
		} catch (Exception e) {
			e.printStackTrace();
		    new MessageUtil().showErrorMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	private void consultar(){
		CotoficinaBO cotoficinaBO  = new CotoficinaBO();
		try {
			cotoficina = cotoficinaBO.getCotoficinabyId(idoficina);
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public void grabar(){
		try {
			if(validarcampos()){
				CotoficinaBO cotoficinaBO = new CotoficinaBO();
				boolean ok = cotoficinaBO.modificar(cotoficina, cotoficinaclone);
				
				if(cotoficina.getIdoficina() > 0){
					if(ok){
						mostrarPaginaMensaje("Oficina modificada con exito!!");
					}else{
						new MessageUtil().showWarnMessage("No existen cambios que guardar.","");
					}
				}else{
					ok = cotoficinaBO.grabar(cotoficina);
					if(ok){
						mostrarPaginaMensaje("Oficina creada con exito!!");
					}else{
						new MessageUtil().showWarnMessage("No se ha podido ingresar la oficina. Comunicar al Webmaster.","");
					}
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
		
	}
	
	private void mostrarPaginaMensaje(String mensaje) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		usuarioBean.setMensaje(mensaje);
		
		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("../pages/mensaje.jsf");	 
	}
	
	public void eliminar(){
		try {
			CotoficinaBO cotoficinaBO = new CotoficinaBO();
			boolean ok = cotoficinaBO.eliminar(cotoficina);
			if(ok){
				mostrarPaginaMensaje("Oficina eliminada con exito!!");
			}else{
				new MessageUtil().showWarnMessage("No se ha podido eliminar la oficina. Comunicar al Webmaster.","");
			}
		}catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public boolean validarcampos(){
	   boolean ok = true;
	   String textohorario = (cotoficina.getDescripcion()!=null ? cotoficina.getDescripcion().replaceAll("\\<.*?\\>", "") : "" );

		if (cotoficina.getTipooficina() == 0) {
			ok = false;
			new MessageUtil().showWarnMessage("Es necesario seleccionar el tipo de oficina","");
		} else if (textohorario.equals("")) {
			ok = false;
			new MessageUtil().showWarnMessage("Es necesario ingresar el horario de atención","");
		} else if (cotoficina.getNombre() == null
				|| cotoficina.getNombre().length() == 0) {
			ok = false;
			new MessageUtil().showWarnMessage("Es necesario ingresar el Nombre de la oficina","");
		} else if (cotoficina.getDireccion() == null
				|| cotoficina.getDireccion().length() == 0) {
			ok = false;
			new MessageUtil().showWarnMessage("Es necesario ingresar la dirección de la oficina","");
		}
	   
	    return ok;
	 }

	public Cotoficina getCotoficina() {
		return cotoficina;
	}

	public void setCotoficina(Cotoficina cotoficina) {
		this.cotoficina = cotoficina;
	}

	public Cotoficina getCotoficinaclone() {
		return cotoficinaclone;
	}

	public void setCotoficinaclone(Cotoficina cotoficinaclone) {
		this.cotoficinaclone = cotoficinaclone;
	}

	public int getIdoficina() {
		return idoficina;
	}

	public void setIdoficina(int idoficina) {
		this.idoficina = idoficina;
	}

	public List<Cotempresa> getLisCotempresa() {
		return lisCotempresa;
	}

	public void setLisCotempresa(List<Cotempresa> lisCotempresa) {
		this.lisCotempresa = lisCotempresa;
	}
	
	
}
