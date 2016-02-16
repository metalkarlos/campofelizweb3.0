package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;



import com.web.cementerio.bo.PetenunciadoBO;

import com.web.cementerio.pojo.annotations.Petenunciado;
import com.web.cementerio.pojo.annotations.Petvenunciado;

import com.web.cementerio.pojo.annotations.Setestado;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class EnunciadoAdminBean implements Serializable {

	private static final long serialVersionUID = -6325180654691511826L;
	private int idenunciado;
	private Petenunciado petenunciadopregunta;
	private Petenunciado petenunciadorespuesta;
	private List<Petvenunciado> listpetvenunciado;
	private List<Petenunciado> listpetenunciadoclone;
	private String paginaRetorno;
	

	public EnunciadoAdminBean(){
		idenunciado =0;
		petenunciadopregunta = new Petenunciado(0, new Setestado(),new Setusuario(), 'P', null, 0,0, null, null,null, null);
		petenunciadorespuesta = new Petenunciado(0, new Setestado(),new Setusuario(), 'R', null, 0,0, null, null,null, null);
	}

	@PostConstruct
	public void PostEnunciadoBean() {
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idenunciado");
			
			if(par != null){
				idenunciado = Integer.parseInt(par.toString());
				if(idenunciado > 0){
					consultar();
					clonar();
				}else{
					PetenunciadoBO petenunciadoBO = new PetenunciadoBO();
					int orden = petenunciadoBO.getMaxOrden();
					if(orden > 0){
						orden = orden/2;
					}
					petenunciadopregunta.setOrden(orden + 1);
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

	private void consultar(){
		listpetvenunciado = new ArrayList<Petvenunciado>();
		PetenunciadoBO petenunciadoBO = new PetenunciadoBO();
		try {
			listpetvenunciado = petenunciadoBO.getListpetvenunciadoidpadre(idenunciado);
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
		
	}
	
	public void  grabar(){
		PetenunciadoBO petenunciadoBO = new PetenunciadoBO();
		List<Petenunciado> listpetenunciado = new ArrayList<Petenunciado>();
		
		try {
			if(validarcampos()){
				boolean ok = false;
				
				if(idenunciado ==0){
				   listpetenunciado.add(0,petenunciadopregunta);
				   petenunciadorespuesta.setOrden(petenunciadopregunta.getOrden());
				   listpetenunciado.add(1,petenunciadorespuesta);
				   ok = petenunciadoBO.grabar(listpetenunciado, 1);	
				   if(ok){
						mostrarPaginaMensaje("Pregunta ingresada con exito!!");
					}else{
						new MessageUtil().showWarnMessage("No se ha podido ingresar la Pregunta. Comunicar al Webmaster.","");
					}
				}else if(idenunciado >0){
				  for(Petenunciado petenunciadoclone: listpetenunciadoclone){
					  int indice = 0;
					  if(petenunciadoclone.getIdenunciado()==petenunciadopregunta.getIdenunciado()){
						 if(!petenunciadoclone.equals(petenunciadopregunta)){
							if(petenunciadoclone.getOrden()!=petenunciadopregunta.getOrden()){
							  petenunciadorespuesta.setOrden(petenunciadopregunta.getOrden());	
							}
					       listpetenunciado.add(indice,petenunciadopregunta);
						}
	  				  }else if(petenunciadoclone.getIdenunciado()==petenunciadorespuesta.getIdenunciado()){
						if(!petenunciadoclone.equals(petenunciadorespuesta)){
						
						  listpetenunciado.add(indice,petenunciadorespuesta);
						}
					  }
					  indice++;
					}
				   if((listpetenunciado.size()>0) && (!listpetenunciado.isEmpty())){	
					   ok = petenunciadoBO.modificar(listpetenunciado, 1);
				   }
				   
				   if(ok){
					   mostrarPaginaMensaje("Pregunta modificada con exito!!");
				   }else{
					   new MessageUtil().showWarnMessage("No se ha podido modificar la Pregunta. Comunicar al Webmaster.","");
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
		PetenunciadoBO petenunciadoBO = new PetenunciadoBO();
		List<Petenunciado> listpetenunciado = new ArrayList<Petenunciado>();
		try {
			listpetenunciado.add(0,petenunciadopregunta);
			listpetenunciado.add(1,petenunciadorespuesta);
			boolean ok = petenunciadoBO.eliminar(listpetenunciado, 2);
			if(ok){
			   mostrarPaginaMensaje("Pregunta eliminada con exito!!");
		   }else{
			   new MessageUtil().showWarnMessage("No se ha podido eliminar la Pregunta. Comunicar al Webmaster.","");
		   }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean validarcampos(){
		boolean ok = true;
		String textorespuesta= (petenunciadorespuesta.getDescripcion()!=null ? petenunciadorespuesta.getDescripcion().replaceAll("\\<.*?\\>", "") : "" );
	    if(petenunciadopregunta.getDescripcion()== null || petenunciadopregunta.getDescripcion().length() ==0){
	    	ok = false;
	    	new MessageUtil().showInfoMessage("Es necesario ingresar el contenido de la pregunta","");
	    }
	    else if(textorespuesta.equals("")){
		    ok = false;
			new MessageUtil().showInfoMessage("Es necesario ingresar el contenido de la respuesta","");
		}
	    else if(petenunciadopregunta.getOrden()<=0){
	    	ok = false;
	    	new MessageUtil().showInfoMessage("Es necesario ingresar el orden de presentación de la pregunta","");
	    }
		return ok;
		
	}
	
	public void clonar(){
		
		int indice =0;
		Petenunciado petenunciado = new Petenunciado(0, null, null, null, null, 0,0, null, null, null, null);
		listpetenunciadoclone = new ArrayList<Petenunciado>();
		try{
			if(!listpetvenunciado.isEmpty()){
			for(Petvenunciado petvenunciado : listpetvenunciado){
			    petenunciado.setIdenunciado(petvenunciado.getIdenunciado());
			    petenunciado.setDescripcion(petvenunciado.getDescripcion());
			    petenunciado.setTipo(petvenunciado.getTipo());
			    petenunciado.setTag(petvenunciado.getTag());
			    petenunciado.setOrden(petvenunciado.getOrden());
			    petenunciado.setFecharegistro(petvenunciado.getFecharegistro());
			    if(petvenunciado.getIdpadre()>0){
			       petenunciado.setIdpadre(petvenunciado.getIdpadre());
			    }
			    
			    if(String.valueOf(petvenunciado.getTipo()).equals("P")){
				   petenunciadopregunta = petenunciado;
				}else if(String.valueOf(petvenunciado.getTipo()).equals("R")){
					petenunciadorespuesta= petenunciado;
				 }
			    listpetenunciadoclone.add(indice,petenunciado.clonar());
			    petenunciado = new Petenunciado(0, null, null, null, null, 0,0, null, null, null, null);
			    indice ++;
			 }
			 
		   }
		}
		catch (Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	public int getIdenunciado() {
		return idenunciado;
	}

	public void setIdenunciado(int idenunciado) {
		this.idenunciado = idenunciado;
	}

	public Petenunciado getPetenunciadopregunta() {
		return petenunciadopregunta;
	}

	public void setPetenunciadopregunta(Petenunciado petenunciadopregunta) {
		this.petenunciadopregunta = petenunciadopregunta;
	}

	public Petenunciado getPetenunciadorespuesta() {
		return petenunciadorespuesta;
	}

	public void setPetenunciadorespuesta(Petenunciado petenunciadorespuesta) {
		this.petenunciadorespuesta = petenunciadorespuesta;
	}

	public String getPaginaRetorno() {
		return paginaRetorno;
	}

	public void setPaginaRetorno(String paginaRetorno) {
		this.paginaRetorno = paginaRetorno;
	}

	public List<Petvenunciado> getListpetvenunciado() {
		return listpetvenunciado;
	}

	public void setListpetvenunciado(List<Petvenunciado> listpetvenunciado) {
		this.listpetvenunciado = listpetvenunciado;
	}

	public List<Petenunciado> getListpetenunciadoclone() {
		return listpetenunciadoclone;
	}

	public void setListpetenunciadoclone(List<Petenunciado> listpetenunciadoclone) {
		this.listpetenunciadoclone = listpetenunciadoclone;
	}

	
}
