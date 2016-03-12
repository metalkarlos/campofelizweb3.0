package com.web.cementerio.pojo.annotations;

// Generated 20/08/2015 11:11:47 PM by Hibernate Tools 3.4.0.CR1

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
 * Cotoficina generated by hbm2java
 */
@Entity
@Table(name = "cotoficina", catalog = "campofelizweb")
public class Cotoficina implements java.io.Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3738945820672269595L;
	private int idoficina;
	private Setestado setestado;
	private Setusuario setusuario;
	private Cotempresa cotempresa;
	private String nombre;
	private String descripcion;
	private String direccion;
	private String telefono;
	private String celular;
	private String celular2;
	private String celular3;
	private String email;
	private Date fecharegistro;
	private String iplog;
	private Date fechamodificacion;
	private int tipooficina;

	public Cotoficina() {
	}

	public Cotoficina(int idoficina, Setestado setestado,
			Setusuario setusuario, Cotempresa cotempresa, String nombre,
			Date fecharegistro, String iplog, Date fechamodificacion) {
		this.idoficina = idoficina;
		this.setestado = setestado;
		this.setusuario = setusuario;
		this.cotempresa = cotempresa;
		this.nombre = nombre;
		this.fecharegistro = fecharegistro;
		this.iplog = iplog;
		this.fechamodificacion = fechamodificacion;
	}

	public Cotoficina(int idoficina, Setestado setestado,
			Setusuario setusuario, Cotempresa cotempresa, String nombre,
			String descripcion, String direccion, String telefono,
			String celular, String celular2, String celular3, String email, Date fecharegistro, String iplog,
			Date fechamodificacion,int tipooficina) {
		this.idoficina = idoficina;
		this.setestado = setestado;
		this.setusuario = setusuario;
		this.cotempresa = cotempresa;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.direccion = direccion;
		this.telefono = telefono;
		this.celular = celular;
		this.celular2 = celular2;
		this.celular3 = celular3;
		this.email = email;
		this.fecharegistro = fecharegistro;
		this.iplog = iplog;
		this.fechamodificacion = fechamodificacion;
		this.tipooficina = tipooficina;
	}

	@Id
	@Column(name = "idoficina", unique = true, nullable = false)
	public int getIdoficina() {
		return this.idoficina;
	}

	public void setIdoficina(int idoficina) {
		this.idoficina = idoficina;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idestado", nullable = false)
	public Setestado getSetestado() {
		return this.setestado;
	}

	public void setSetestado(Setestado setestado) {
		this.setestado = setestado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idusuario", nullable = false)
	public Setusuario getSetusuario() {
		return this.setusuario;
	}

	public void setSetusuario(Setusuario setusuario) {
		this.setusuario = setusuario;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idempresa", nullable = false)
	public Cotempresa getCotempresa() {
		return this.cotempresa;
	}

	public void setCotempresa(Cotempresa cotempresa) {
		this.cotempresa = cotempresa;
	}

	@Column(name = "nombre", nullable = false, length = 200)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "descripcion", length = 500)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "direccion", length = 500)
	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Column(name = "telefono", length = 40)
	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@Column(name = "celular", length = 45)
	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	@Column(name = "celular2", length = 45)
	public String getCelular2() {
		return celular2;
	}

	public void setCelular2(String celular2) {
		this.celular2 = celular2;
	}

	@Column(name = "celular3", length = 45)
	public String getCelular3() {
		return celular3;
	}

	public void setCelular3(String celular3) {
		this.celular3 = celular3;
	}

	@Column(name = "email", length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecharegistro", nullable = false, length = 19)
	public Date getFecharegistro() {
		return this.fecharegistro;
	}

	public void setFecharegistro(Date fecharegistro) {
		this.fecharegistro = fecharegistro;
	}

	@Column(name = "iplog", nullable = false, length = 20)
	public String getIplog() {
		return this.iplog;
	}

	public void setIplog(String iplog) {
		this.iplog = iplog;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechamodificacion", length = 19)
	public Date getFechamodificacion() {
		return this.fechamodificacion;
	}

	public void setFechamodificacion(Date fechamodificacion) {
		this.fechamodificacion = fechamodificacion;
	}
	
	@Column(name = "tipooficina")
	public int getTipooficina() {
		return tipooficina;
	}

	public void setTipooficina(int tipooficina) {
		this.tipooficina = tipooficina;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException{
	  Cotoficina cotoficina = (Cotoficina) super.clone();
	  
	  return cotoficina;
	}
	
	public Cotoficina clonar() throws Exception{
		return (Cotoficina)this.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((celular == null) ? 0 : celular.hashCode());
		result = prime * result
				+ ((cotempresa == null) ? 0 : cotempresa.getIdempresa());
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result
				+ ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime
				* result
				+ ((fechamodificacion == null) ? 0 : fechamodificacion
						.hashCode());
		result = prime * result
				+ ((fecharegistro == null) ? 0 : fecharegistro.hashCode());
		result = prime * result + idoficina;
		result = prime * result + ((iplog == null) ? 0 : iplog.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((setestado == null) ? 0 : setestado.getIdestado());
		result = prime * result
				+ ((setusuario == null) ? 0 : setusuario.getIdusuario());
		result = prime * result
				+ ((telefono == null) ? 0 : telefono.hashCode());
		result = prime * result	+ tipooficina;
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
		Cotoficina other = (Cotoficina) obj;
		if (celular == null) {
			if (other.celular != null)
				return false;
		} else if (!celular.equals(other.celular))
			return false;
		if (cotempresa == null) {
			if (other.cotempresa != null)
				return false;
		} else if (cotempresa.getIdempresa() != other.cotempresa.getIdempresa())
			return false;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
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
		if (idoficina != other.idoficina)
			return false;
		if (iplog == null) {
			if (other.iplog != null)
				return false;
		} else if (!iplog.equals(other.iplog))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
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
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (tipooficina != other.tipooficina)
			return false;
		return true;
	}
	
	

}
