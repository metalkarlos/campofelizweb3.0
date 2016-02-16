package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.web.cementerio.bo.PetfotoinstalacionBO;
import com.web.cementerio.pojo.annotations.Petfotoinstalacion;
import com.web.util.MessageUtil;


@ManagedBean
@ViewScoped
public class CementerioVirtualBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7212209718614799223L;
	
	private List<Petfotoinstalacion> lisPetfotoinstalacion;
 
    public CementerioVirtualBean() {
    	lisPetfotoinstalacion = new ArrayList<Petfotoinstalacion>();

    	consultarInstalacion();
    }

    private void consultarInstalacion(){
    	try
		{
    		PetfotoinstalacionBO petfotoinstalacionBO = new PetfotoinstalacionBO();
    		lisPetfotoinstalacion = petfotoinstalacionBO.lisPetfotoinstalacion(1);
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!", "");
		}
    }
    
	public List<Petfotoinstalacion> getLisPetfotoinstalacion() {
		return lisPetfotoinstalacion;
	}

	public void setLisPetfotoinstalacion(
			List<Petfotoinstalacion> lisPetfotoinstalacion) {
		this.lisPetfotoinstalacion = lisPetfotoinstalacion;
	}

}
