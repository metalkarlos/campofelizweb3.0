package com.web.cementerio.pojo.annotations;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Pethome
 */
@Entity
@Table(name = "pethome")
public class Pethome implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3450343670540932767L;
	private int idhome; 
	private Setestado setestado;
	private Setusuario setusuario;
	private String encabezado; 
	private String urlvideo;
	private int orden; 
	private Date fecharegistro; 
	private Date fechamodificacion; 
	private String iplog; 
	
	public Pethome() {
	}
	
	public Pethome(int idhome, Date fecharegistro) {
		this.idhome = idhome;
		this.fecharegistro = fecharegistro;
	}
	
	public Pethome(int idhome, Setestado setestado, Setusuario setusuario, String encabezado, 
	String urlvideo, int orden, Date fecharegistro, Date fechamodificacion, String iplog) {
		this.idhome = idhome;
		this.setestado = setestado;
		this.setusuario = setusuario;
		this.encabezado = encabezado; 
		this.urlvideo = urlvideo;
		this.orden = orden; 
		this.fecharegistro = fecharegistro; 
		this.fechamodificacion = fechamodificacion; 
		this.iplog = iplog;
	}

	@Id
	@Column(name = "idhome", unique = true, nullable = false)
	public int getIdhome() {
		return idhome;
	}

	public void setIdhome(int idhome) {
		this.idhome = idhome;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idestado")
	public Setestado getSetestado() {
		return setestado;
	}

	public void setSetestado(Setestado setestado) {
		this.setestado = setestado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idusuario")
	public Setusuario getSetusuario() {
		return setusuario;
	}

	public void setSetusuario(Setusuario setusuario) {
		this.setusuario = setusuario;
	}

	@Column(name = "encabezado", length = 2000)
	public String getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	@Column(name = "urlvideo", length = 500)
	public String getUrlvideo() {
		return urlvideo;
	}

	public void setUrlvideo(String urlvideo) {
		this.urlvideo = urlvideo;
	}

	@Column(name = "orden")
	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecharegistro", nullable = false, length = 29)
	public Date getFecharegistro() {
		return fecharegistro;
	}

	public void setFecharegistro(Date fecharegistro) {
		this.fecharegistro = fecharegistro;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechamodificacion",  length = 29)
	public Date getFechamodificacion() {
		return fechamodificacion;
	}

	public void setFechamodificacion(Date fechamodificacion) {
		this.fechamodificacion = fechamodificacion;
	}

	@Column(name = "iplog", length = 20)
	public String getIplog() {
		return iplog;
	}

	public void setIplog(String iplog) {
		this.iplog = iplog;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Pethome pethome = (Pethome)super.clone();
		return pethome;
	}
	
	public Pethome clonar() throws Exception {
		return (Pethome)this.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((encabezado == null) ? 0 : encabezado.hashCode());
		result = prime
				* result
				+ ((fechamodificacion == null) ? 0 : fechamodificacion
						.hashCode());
		result = prime * result
				+ ((fecharegistro == null) ? 0 : fecharegistro.hashCode());
		result = prime * result + idhome;
		result = prime * result + ((iplog == null) ? 0 : iplog.hashCode());
		result = prime * result + orden;
		result = prime * result
				+ ((setestado == null) ? 0 : setestado.getIdestado());
		result = prime * result
				+ ((setusuario == null) ? 0 : setusuario.getIdusuario());
		result = prime * result
				+ ((urlvideo == null) ? 0 : urlvideo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pethome other = (Pethome) obj;
		if (encabezado == null) {
			if (other.encabezado != null)
				return false;
		} else if (!encabezado.equals(other.encabezado))
			return false;
		if (fechamodificacion == null) {
			if (other.fechamodificacion != null)
				return false;
		} else if (!fechamodificacion.equals(other.fechamodificacion))
			return false;
		if (fecharegistro == null) {
			if (other.fecharegistro != null)
				return false;
		} else if (!fecharegistro.equals(other.fecharegistro))
			return false;
		if (idhome != other.idhome)
			return false;
		if (iplog == null) {
			if (other.iplog != null)
				return false;
		} else if (!iplog.equals(other.iplog))
			return false;
		if (orden != other.orden)
			return false;
		if (setestado == null) {
			if (other.setestado != null)
				return false;
		} else if (setestado.getIdestado() != other.setestado.getIdestado())
			return false;
		if (setusuario == null) {
			if (other.setusuario != null)
				return false;
		} else if (setusuario.getIdusuario() != other.setusuario.getIdusuario())
			return false;
		if (urlvideo == null) {
			if (other.urlvideo != null)
				return false;
		} else if (!urlvideo.equals(other.urlvideo))
			return false;
		return true;
	}
	
	

}
