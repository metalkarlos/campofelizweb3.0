package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.web.cementerio.bo.PethomeBO;
import com.web.cementerio.pojo.annotations.Petfotoinstalacion;
import com.web.cementerio.pojo.annotations.Petguia;
import com.web.cementerio.pojo.annotations.Pethome;
import com.web.cementerio.pojo.annotations.Petinformacion;
import com.web.cementerio.pojo.annotations.Petmascotahomenaje;
import com.web.cementerio.pojo.annotations.Petnoticia;
import com.web.cementerio.pojo.annotations.Petservicio;
import com.web.cementerio.pojo.annotations.Petvenunciado;

@ManagedBean
@RequestScoped
public class InicioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -396004214619164180L;
	
	private List<Petservicio> lisPetservicio;
	private List<Petguia> lisPetguia;
	private Petinformacion petinformacion;
	private List<Petfotoinstalacion> lisPetfotoinstalacion;
	private List<Petnoticia> lisPetnoticias;
	private List<Petvenunciado> listpetvenunciado;
	private List<Petmascotahomenaje> lisPetmascotahomenaje;
	private List<Pethome> lisPethome;

	public InicioBean() {
	
	}
	
	@PostConstruct
	public void PostInicioBean() {
		Consultar();
	}
	
	private void Consultar(){
		ServiciosPrincipalesBean serviciosPrincipalesBean = new ServiciosPrincipalesBean();
		lisPetservicio = serviciosPrincipalesBean.getLisPetservicio();
		serviciosPrincipalesBean = null;
		
		PromocionBean promocionBean = new PromocionBean();
		lisPetguia = promocionBean.consultarPromocionesPrincipales();
		promocionBean = null;
		
		QuienesSomosBean quienesSomosBean = new QuienesSomosBean();
		petinformacion = quienesSomosBean.getPetinformacion();
		quienesSomosBean = null;
		
		CementerioVirtualBean cementerioVirtualBean = new CementerioVirtualBean();
		lisPetfotoinstalacion = cementerioVirtualBean.getLisPetfotoinstalacion();
		cementerioVirtualBean = null;
		
		NoticiasPrincipalesBean noticiasPrincipalesBean = new NoticiasPrincipalesBean();
		lisPetnoticias = noticiasPrincipalesBean.getLisPetnoticias();
		noticiasPrincipalesBean = null;
		
		EnunciadosBean enunciadosBean = new EnunciadosBean();
		listpetvenunciado = enunciadosBean.getListpetvenunciado();
		enunciadosBean = null;
		
		MascotasPrincipalBean mascotasPrincipalBean = new MascotasPrincipalBean();
		lisPetmascotahomenaje = mascotasPrincipalBean.getLisPetmascotahomenaje();
		mascotasPrincipalBean = null;
		
		PethomeBO pethomeBO = new PethomeBO();
		try {
			lisPethome = pethomeBO.lisPethome(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Petservicio> getLisPetservicio() {
		return lisPetservicio;
	}

	public void setLisPetservicio(List<Petservicio> lisPetservicio) {
		this.lisPetservicio = lisPetservicio;
	}

	public List<Petguia> getLisPetguia() {
		return lisPetguia;
	}

	public void setLisPetguia(List<Petguia> lisPetguia) {
		this.lisPetguia = lisPetguia;
	}

	public Petinformacion getPetinformacion() {
		return petinformacion;
	}

	public void setPetinformacion(Petinformacion petinformacion) {
		this.petinformacion = petinformacion;
	}

	public List<Petfotoinstalacion> getLisPetfotoinstalacion() {
		return lisPetfotoinstalacion;
	}

	public void setLisPetfotoinstalacion(List<Petfotoinstalacion> lisPetfotoinstalacion) {
		this.lisPetfotoinstalacion = lisPetfotoinstalacion;
	}

	public List<Petnoticia> getLisPetnoticias() {
		return lisPetnoticias;
	}

	public void setLisPetnoticias(List<Petnoticia> lisPetnoticias) {
		this.lisPetnoticias = lisPetnoticias;
	}

	public List<Petvenunciado> getListpetvenunciado() {
		return listpetvenunciado;
	}

	public void setListpetvenunciado(List<Petvenunciado> listpetvenunciado) {
		this.listpetvenunciado = listpetvenunciado;
	}

	public List<Petmascotahomenaje> getLisPetmascotahomenaje() {
		return lisPetmascotahomenaje;
	}

	public void setLisPetmascotahomenaje(List<Petmascotahomenaje> lisPetmascotahomenaje) {
		this.lisPetmascotahomenaje = lisPetmascotahomenaje;
	}

	public List<Pethome> getLisPethome() {
		return lisPethome;
	}

	public void setLisPethome(List<Pethome> lisPethome) {
		this.lisPethome = lisPethome;
	}
	
}
