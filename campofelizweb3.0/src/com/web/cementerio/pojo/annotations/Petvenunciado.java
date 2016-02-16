package com.web.cementerio.pojo.annotations;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "petvenunciado")
public class Petvenunciado implements java.io.Serializable {
	private static final long serialVersionUID = 3054030871709700295L; 

	private Integer idenunciado;
	private String descripcion;
	private Character tipo;
	private Integer idpadre;
	private Integer orden;
	private String tag;
	private Date fecharegistro;
	
	public Petvenunciado(){
		
	}
	
    public Petvenunciado(Integer idenunciado,String descripcion,Character tipo,Integer idpadre,Integer orden, String tag, Date fecharegistro){
		this.idenunciado = idenunciado;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.idpadre = idpadre;
		this.orden = orden;
		this.tag = tag;
		this.fecharegistro = fecharegistro;
	}

    @Id
	@Column(name = "idenunciado")
	public int getIdenunciado() {
		return idenunciado;
	}

	public void setIdenunciado(Integer idenunciado) {
		this.idenunciado = idenunciado;
	}

	@Column(name = "descripcion", length = 2000)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "tipo", length = 1)
	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

	@Column(name = "idpadre",  nullable = false)
	public int getIdpadre() {
		return idpadre;
	}

	public void setIdpadre(Integer idpadre) {
		this.idpadre = idpadre;
	}
	@Column(name = "orden",  nullable = false)
	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	@Column(name = "tag", length = 200)
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecharegistro", nullable = false, length = 29)
	public Date getFecharegistro() {
		return fecharegistro;
	}

	public void setFecharegistro(Date fecharegistro) {
		this.fecharegistro = fecharegistro;
	}
	
}
