package com.web.cementerio.pojo.annotations;

import java.util.Comparator;

// Generated 05/03/2014 11:20:16 AM by Hibernate Tools 3.4.0.CR1


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
import javax.persistence.Transient;


/**
 * Petfotomascota generated by hbm2java
 */
@Entity
@Table(name = "petfotomascota")
public class Petfotomascota implements java.io.Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2231385061245946566L;
	private int idfotomascota;
	private Setestado setestado;
	private Petmascotahomenaje petmascotahomenaje;
	private Setusuario setusuario;
	private String descripcion;
	private String ruta;
	private String nombrearchivo;
	private Date fecharegistro;
	private Date fechamodificacion;
	private String iplog;
	private byte[] imagen;

	public static Comparator<Petfotomascota> FecharegistroComparator = new Comparator<Petfotomascota>() {
		public int compare(Petfotomascota petfotomascota1, Petfotomascota petfotomascota2) {
			return petfotomascota1.getFecharegistro().compareTo(petfotomascota2.getFecharegistro());
		}
	};

	public Petfotomascota() {
	}

	public Petfotomascota(int idfotomascota,
			Petmascotahomenaje petmascotahomenaje, Date fecharegistro) {
		this.idfotomascota = idfotomascota;
		this.petmascotahomenaje = petmascotahomenaje;
		this.fecharegistro = fecharegistro;
	}

	public Petfotomascota(int idfotomascota, Setestado setestado,
			Petmascotahomenaje petmascotahomenaje, Setusuario setusuario,
			String descripcion, String ruta, String nombrearchivo,
			Integer perfil, Date fecharegistro, Date fechamodificacion, String iplog) {
		this.idfotomascota = idfotomascota;
		this.setestado = setestado;
		this.petmascotahomenaje = petmascotahomenaje;
		this.setusuario = setusuario;
		this.descripcion = descripcion;
		this.ruta = ruta;
		this.nombrearchivo = nombrearchivo;
		this.fecharegistro = fecharegistro;
		this.fechamodificacion = fechamodificacion;
		this.iplog = iplog;

	}

	@Id
	@Column(name = "idfotomascota", unique = true, nullable = false)
	public int getIdfotomascota() {
		return this.idfotomascota;
	}

	public void setIdfotomascota(int idfotomascota) {
		this.idfotomascota = idfotomascota;
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
	@JoinColumn(name = "idmascota", nullable = false)
	public Petmascotahomenaje getPetmascotahomenaje() {
		return this.petmascotahomenaje;
	}

	public void setPetmascotahomenaje(Petmascotahomenaje petmascotahomenaje) {
		this.petmascotahomenaje = petmascotahomenaje;
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
	@Column(name = "fechamodificacion",  length = 29)
	public Date getFechamodificacion() {
		return this.fechamodificacion;
	}

	public void setFechamodificacion(Date fechamodificacion) {
		this.fechamodificacion = fechamodificacion;
	}
	@Column(name = "iplog", length = 20)
	public String getIplog() {
		return this.iplog;
	}

	public void setIplog(String iplog) {
		this.iplog = iplog;
	}

	@Transient
	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}


	@Override
	protected Object clone() throws CloneNotSupportedException{
	  Petfotomascota petfotomascota = (Petfotomascota) super.clone();
	  
	  return petfotomascota;
	}
	
	public Petfotomascota clonar() throws Exception{
		return (Petfotomascota)this.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((fechamodificacion == null) ? 0 : fechamodificacion.hashCode());
		result = prime * result + ((fecharegistro == null) ? 0 : fecharegistro.hashCode());
		result = prime * result + idfotomascota;
		result = prime * result + ((iplog == null) ? 0 : iplog.hashCode());
		result = prime * result + ((nombrearchivo == null) ? 0 : nombrearchivo.hashCode());
		result = prime * result + ((petmascotahomenaje == null) ? 0 : petmascotahomenaje.getIdmascota());
		result = prime * result + ((ruta == null) ? 0 : ruta.hashCode());
		result = prime * result + ((setestado == null) ? 0 : setestado.getIdestado());
		result = prime * result + ((setusuario == null) ? 0 : setusuario.getIdusuario());
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
		Petfotomascota other = (Petfotomascota) obj;
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
		if (idfotomascota != other.idfotomascota)
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
		if (petmascotahomenaje == null) {
			if (other.petmascotahomenaje != null)
				return false;
		} else if (petmascotahomenaje.getIdmascota() != other.petmascotahomenaje.getIdmascota())
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
