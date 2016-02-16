package com.web.cementerio.pojo.annotations;

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

@Entity
@Table(name = "petfotoinstalacion")
public class Petfotoinstalacion implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 7031597390438852528L;
	private int idfotoinstalacion;
	private Setestado setestado;
	private Setusuario setusuario;
	private String descripcion;
	private String ruta;
	private String nombrearchivo;
	private Date fecharegistro;
	private Date fechamodificacion;
	private int orden;
	private String iplog;

	public Petfotoinstalacion() {
	}

	public Petfotoinstalacion(int idfotoinstalacion, Setestado setestado, Setusuario setusuario, String descripcion,
			String ruta, String nombrearchivo, Date fecharegistro, Date fechamodificacion, int orden, String iplog) {
		this.idfotoinstalacion = idfotoinstalacion;
		this.setestado = setestado;
		this.setusuario = setusuario;
		this.descripcion = descripcion;
		this.ruta = ruta;
		this.nombrearchivo = nombrearchivo;
		this.fecharegistro = fecharegistro;
		this.fechamodificacion = fechamodificacion;
		this.orden = orden;
		this.iplog = iplog;
	}

	@Id
	@Column(name = "idfotoinstalacion", unique = true, nullable = false)
	public int getIdfotoinstalacion() {
		return idfotoinstalacion;
	}

	public void setIdfotoinstalacion(int idfotoinstalacion) {
		this.idfotoinstalacion = idfotoinstalacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idestado")
	public Setestado getSetestado() {
		return this.setestado;
	}

	public void setSetestado(Setestado setestado) {
		this.setestado = setestado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idusuario")
	public Setusuario getSetusuario() {
		return this.setusuario;
	}

	public void setSetusuario(Setusuario setusuario) {
		this.setusuario = setusuario;
	}

	@Column(name = "descripcion", length = 500)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "ruta", length = 100)
	public String getRuta() {
		return this.ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	@Column(name = "nombrearchivo", length = 50)
	public String getNombrearchivo() {
		return this.nombrearchivo;
	}

	public void setNombrearchivo(String nombrearchivo) {
		this.nombrearchivo = nombrearchivo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecharegistro", nullable = false, length = 29)
	public Date getFecharegistro() {
		return this.fecharegistro;
	}

	public void setFecharegistro(Date fecharegistro) {
		this.fecharegistro = fecharegistro;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechamodificacion", length = 29)
	public Date getFechamodificacion() {
		return fechamodificacion;
	}

	public void setFechamodificacion(Date fechamodificacion) {
		this.fechamodificacion = fechamodificacion;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	@Column(name = "iplog", length = 20)
	public String getIplog() {
		return this.iplog;
	}

	public void setIplog(String iplog) {
		this.iplog = iplog;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Petfotoinstalacion petfotoinstalacion = (Petfotoinstalacion)super.clone();
		return petfotoinstalacion;
	}
	
	public Petfotoinstalacion clonar() throws Exception {
		return (Petfotoinstalacion)this.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime
				* result
				+ ((fechamodificacion == null) ? 0 : fechamodificacion
						.hashCode());
		result = prime * result
				+ ((fecharegistro == null) ? 0 : fecharegistro.hashCode());
		result = prime * result + idfotoinstalacion;
		result = prime * result + ((iplog == null) ? 0 : iplog.hashCode());
		result = prime * result
				+ ((nombrearchivo == null) ? 0 : nombrearchivo.hashCode());
		result = prime * result + orden;
		result = prime * result + ((ruta == null) ? 0 : ruta.hashCode());
		result = prime * result
				+ ((setestado == null) ? 0 : setestado.getIdestado());
		result = prime * result
				+ ((setusuario == null) ? 0 : setusuario.getIdusuario());
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
		Petfotoinstalacion other = (Petfotoinstalacion) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
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
		if (idfotoinstalacion != other.idfotoinstalacion)
			return false;
		if (iplog == null) {
			if (other.iplog != null)
				return false;
		} else if (!iplog.equals(other.iplog))
			return false;
		if (nombrearchivo == null) {
			if (other.nombrearchivo != null)
				return false;
		} else if (!nombrearchivo.equals(other.nombrearchivo))
			return false;
		if (orden != other.orden)
			return false;
		if (ruta == null) {
			if (other.ruta != null)
				return false;
		} else if (!ruta.equals(other.ruta))
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
		return true;
	}

}
