package com.web.cementerio.pojo.annotations;

// Generated 05/03/2014 11:20:16 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Petguia generated by hbm2java
 */
@Entity
@Table(name = "petguia")
public class Petguia implements java.io.Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9018132792136466347L;
	private int idguia;
	private Setestado setestado;
	private Setusuario setusuario;
	private String titulo;
	private String descripcion;
	private String descripcioncorta;
	private String tag;
	private String rutafoto;
	private Date fecharegistro;
	private Date fechamodificacion;
	private Date fechapublicacion;
	private String iplog;
	private boolean principal;
	private int orden;
	private Set<Petfotoguia> petfotoguias = new HashSet<Petfotoguia>(0);

	public Petguia() {
	}

	public Petguia(int idguia, Date fecharegistro) {
		this.idguia = idguia;
		this.fecharegistro = fecharegistro;
	}

	public Petguia(int idguia, Setestado setestado, Setusuario setusuario,
			String titulo, String descripcion, String descripcioncorta, String tag,  String rutafoto,
			Date fecharegistro,	Date fechamodificacion,Date fechapublicacion,
			String iplog, boolean principal, Set<Petfotoguia> petfotoguias, int orden) {
		this.idguia = idguia;
		this.setestado = setestado;
		this.setusuario = setusuario;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.descripcioncorta = descripcioncorta;
		this.tag = tag;
		this.rutafoto = rutafoto;
		this.fecharegistro = fecharegistro;
		this.fechamodificacion = fechamodificacion;
		this.fechapublicacion = fechapublicacion;
		this.iplog = iplog;
		this.principal = principal;
		this.petfotoguias = petfotoguias;
		this.orden = orden;
	}

	@Id
	@Column(name = "idguia", unique = true, nullable = false)
	public int getIdguia() {
		return this.idguia;
	}

	public void setIdguia(int idguia) {
		this.idguia = idguia;
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

	@Column(name = "titulo", length = 1000)
	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Column(name = "descripcion", length = 5000)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Column(name = "descripcioncorta", length = 300)
	public String getDescripcioncorta() {
		return this.descripcioncorta;
	}

	public void setDescripcioncorta(String descripcioncorta) {
		this.descripcioncorta = descripcioncorta;
	}
	
	@Transient
	public String getDescripcionNoTags() {
		return this.descripcion != null ? this.descripcion.replaceAll("\\<.*?\\>", "") : this.descripcion;
	}

	@Column(name = "rutafoto", length = 100)
	public String getRutafoto() {
		return this.rutafoto;
	}

	public void setRutafoto(String rutafoto) {
		this.rutafoto = rutafoto;
	}
	@Column(name = "tag", length = 200)
	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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
		return this.fechamodificacion;
	}

	public void setFechamodificacion(Date fechamodificacion) {
		this.fechamodificacion = fechamodificacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechapublicacion",  length = 29)
	public Date getFechapublicacion() {
		return this.fechapublicacion;
	}

	public void setFechapublicacion(Date fechapublicacion) {
		this.fechapublicacion = fechapublicacion;
	}
	
	@Column(name = "iplog", length = 20)
	public String getIplog() {
		return this.iplog;
	}

	public void setIplog(String iplog) {
		this.iplog = iplog;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "petguia", targetEntity=Petfotoguia.class)
	public Set<Petfotoguia> getPetfotoguias() {
		return this.petfotoguias;
	}

	public void setPetfotoguias(Set<Petfotoguia> petfotoguias) {
		this.petfotoguias = petfotoguias;
	}

	@Column(name = "orden")
	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException{
	  Petguia petguia = (Petguia) super.clone();
	  
	  return petguia;
	}
	
	public Petguia clonar() throws Exception{
		return (Petguia)this.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result	+ ((descripcioncorta == null) ? 0 : descripcioncorta.hashCode());
		result = prime * result + ((fechamodificacion == null) ? 0 : fechamodificacion.hashCode());
		result = prime * result + ((fechapublicacion == null) ? 0 : fechapublicacion.hashCode());
		result = prime * result + ((fecharegistro == null) ? 0 : fecharegistro.hashCode());
		result = prime * result + idguia;
		result = prime * result + ((iplog == null) ? 0 : iplog.hashCode());
		result = prime * result + (principal ? 1231 : 1237);
		result = prime * result + ((rutafoto == null) ? 0 : rutafoto.hashCode());
		result = prime * result + ((setestado == null) ? 0 : setestado.getIdestado());
		result = prime * result + ((setusuario == null) ? 0 : setusuario.getIdusuario());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		result = prime * result + orden;
		
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
		Petguia other = (Petguia) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (descripcioncorta == null) {
			if (other.descripcioncorta != null)
				return false;
		} else if (!descripcioncorta.equals(other.descripcioncorta))
			return false;
		if (fechamodificacion == null) {
			if (other.fechamodificacion != null)
				return false;
		} else if (!fechamodificacion.equals(other.fechamodificacion))
			return false;
		if (fechapublicacion == null) {
			if (other.fechapublicacion != null)
				return false;
		} else if (!fechapublicacion.equals(other.fechapublicacion))
			return false;
		if (fecharegistro == null) {
			if (other.fecharegistro != null)
				return false;
		} else if (!fecharegistro.equals(other.fecharegistro))
			return false;
		if (idguia != other.idguia)
			return false;
		if (iplog == null) {
			if (other.iplog != null)
				return false;
		} else if (!iplog.equals(other.iplog))
			return false;
		if (principal != other.principal)
			return false;
		if (rutafoto == null) {
			if (other.rutafoto != null)
				return false;
		} else if (!rutafoto.equals(other.rutafoto))
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
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		if (orden != other.orden)
			return false;
		return true;
	}
	
}
