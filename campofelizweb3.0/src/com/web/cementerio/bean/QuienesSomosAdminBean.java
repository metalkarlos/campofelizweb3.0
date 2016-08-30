package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;

import com.web.cementerio.bo.PetinformacionBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Petfotoinformacion;
import com.web.cementerio.pojo.annotations.Petinformacion;
import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.MessageUtil;


@ManagedBean
@ViewScoped
public class QuienesSomosAdminBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8926932712521876552L;

	private int idinformacion;
	private Petinformacion petinformacion;
	private Petinformacion petinformacionclone;
	private Petfotoinformacion petfotoinformacionselected;
	private List<Petfotoinformacion> listpetfotoinformacion;
	private List<Petfotoinformacion> listpetfotoinformacionclone;
	
	public QuienesSomosAdminBean(){
		petinformacion= new Petinformacion(0,new Setestado(),new Setusuario(),null,null,null,null,null,null,null,null,null,null,null,null);
		petinformacionclone= new Petinformacion(0,new Setestado(),new Setusuario(),null,null,null,null,null,null,null,null,null,null,null,null);
		petfotoinformacionselected = new Petfotoinformacion();
	}
	
	@PostConstruct
	public void PostQuienesSomosAdminBean() {
		FacesUtil facesUtil = new FacesUtil();
		try{
			Object par = facesUtil.getParametroUrl("idinformacion");
			if(par != null){
				idinformacion= Integer.parseInt(par.toString());
				
				consultar();
				clonarobjetos();
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
	
	public void clonarobjetos(){
		try{
			petinformacionclone= new Petinformacion(0,new Setestado(),new Setusuario(),null,null,null,null,null,null,null,null,null,null,null,null);
			listpetfotoinformacionclone = new ArrayList<Petfotoinformacion>();
			if(petinformacion!=null){
				petinformacionclone = petinformacion.clonar();
			}
			if((listpetfotoinformacion.size()>0) && (!listpetfotoinformacion.isEmpty())){
				for(Petfotoinformacion petfotoinformacion:listpetfotoinformacion){
					listpetfotoinformacionclone.add(petfotoinformacion.clonar());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showErrorMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	private void consultar(){
		try {
			PetinformacionBO petinformacionBO= new PetinformacionBO();
			petinformacion = new Petinformacion();
			listpetfotoinformacion = new  ArrayList<Petfotoinformacion>();
			
			petinformacion = petinformacionBO.getPetinformacionById(idinformacion);
			
			if(petinformacion !=null){
			  if(petinformacion.getPetfotoinformaciones().size()>0 && !petinformacion.getPetfotoinformaciones().isEmpty()){
				listpetfotoinformacion = new ArrayList<Petfotoinformacion>(petinformacion.getPetfotoinformaciones());
			  }
			}
			
		} catch (Exception e) {
	      e.printStackTrace();
	      new MessageUtil().showErrorMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
		
	}
	
	public boolean validarcampos(){
		boolean ok = true;
		String textoquienessomos= (petinformacion.getQuienessomos()!=null ? petinformacion.getQuienessomos().replaceAll("\\<.*?\\>", "") : "" );
		String textomision= (petinformacion.getMision()!=null ? petinformacion.getMision().replaceAll("\\<.*?\\>", "") : "" );
		String textovision= (petinformacion.getVision()!=null ? petinformacion.getVision().replaceAll("\\<.*?\\>", "") : "" );
		if(textoquienessomos.equals("")){
		    ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar el contenido de Quienes Somos","");
		}else if(textomision.equals("")){
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar el contenido de Misión","");
			
		}else if(textovision.equals("")){
			ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar el contenido de Visión","");
		}else if(Integer.valueOf(textoquienessomos.length())>5000){
			ok = false;
			new MessageUtil().showInfoMessage("Contenido de Quienes Somos ha sobrepasado el límite de 5000 caracteres","");
		}
		else if(Integer.valueOf(textomision.length())>2000){
			ok = false;
			new MessageUtil().showInfoMessage("Contenido de Misión ha sobrepasado el límite de 2000 caracteres","");
		}
		else if(Integer.valueOf(textovision.length())>2000){
			ok = false;
			new MessageUtil().showInfoMessage("Contenido de Visión Somos ha sobrepasado el límite de 2000 caracteres","");
		}

		return ok;
	}	
	
	public void  grabar() {
		try {
			if(validarcampos()){
				
				PetinformacionBO petinformacionBO = new PetinformacionBO();
				
				if(petinformacion.getIdinformacion()>0){
					//objeto petmascotahomenaje se ha modificado
					boolean ok = petinformacionBO.modificarPetinformacion(petinformacion, petinformacionclone,listpetfotoinformacion, listpetfotoinformacionclone);
					if(ok){
						mostrarPaginaMensaje("Información modificada con exito!!");
					}else{
						new MessageUtil().showWarnMessage("No existen cambios que guardar.","");
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
		usuarioBean.setLink("/pages/home");
		usuarioBean.setAnchor("about");
		usuarioBean.setLinkTitulo("Consultar Nosotros");
		
		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("../pages/mensaje.jsf");	 
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		try{
			
			if (event.getFile().getSize() < Parametro.TAMAÑO_IMAGEN){
				FileUtil fileUtil = new FileUtil();

				Petfotoinformacion petfotoinformacion = new Petfotoinformacion();
				petfotoinformacion.setImagen(event.getFile().getContents());
				petfotoinformacion.setNombrearchivo(fileUtil.getFileExtention(event.getFile().getFileName()).toLowerCase());
				listpetfotoinformacion.add(petfotoinformacion);
				
				new MessageUtil().showInfoMessage("Presione Grabar para guardar los cambios.", "");
			}else{
				new MessageUtil().showErrorMessage("Tamaño de la imagen no puede ser mayor a 700KB","");
			}
			
			
		}catch(Exception x){
			x.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	public void quitarFoto(){
		listpetfotoinformacion.remove(petfotoinformacionselected);
		petfotoinformacionselected = new Petfotoinformacion();
		
		new MessageUtil().showInfoMessage("Presione grabar para registar los cambios","");	
	}
	
	public Petinformacion getPetinformacion() {
		return petinformacion;
	}

	public void setPetinformacion(Petinformacion petinformacion) {
		this.petinformacion = petinformacion;
	}

	public Petinformacion getPetinformacionclone() {
		return petinformacionclone;
	}

	public void setPetinformacionclone(Petinformacion petinformacionclone) {
		this.petinformacionclone = petinformacionclone;
	}

	public int getIdinformacion() {
		return idinformacion;
	}

	public void setIdinformacion(int idinformacion) {
		this.idinformacion = idinformacion;
	}

	public List<Petfotoinformacion> getlistpetfotoinformacionclone() {
		return listpetfotoinformacionclone;
	}

	public void setlistpetfotoinformacionclone(
			List<Petfotoinformacion> listpetfotoinformacionclone) {
		this.listpetfotoinformacionclone = listpetfotoinformacionclone;
	}

	public Petfotoinformacion getPetfotoinformacionselected() {
		return petfotoinformacionselected;
	}

	public void setPetfotoinformacionselected(
			Petfotoinformacion petfotoinformacionselected) {
		this.petfotoinformacionselected = petfotoinformacionselected;
	}

	public List<Petfotoinformacion> getListpetfotoinformacion() {
		return listpetfotoinformacion;
	}

	public void setListpetfotoinformacion(
			List<Petfotoinformacion> listpetfotoinformacion) {
		this.listpetfotoinformacion = listpetfotoinformacion;
	}

	public List<Petfotoinformacion> getListpetfotoinformacionclone() {
		return listpetfotoinformacionclone;
	}

	public void setListpetfotoinformacionclone(
			List<Petfotoinformacion> listpetfotoinformacionclone) {
		this.listpetfotoinformacionclone = listpetfotoinformacionclone;
	}

	
}
