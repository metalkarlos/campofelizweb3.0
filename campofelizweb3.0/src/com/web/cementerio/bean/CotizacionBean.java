package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.mail.internet.AddressException;

import com.web.cementerio.bo.PetservicioBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Petservicio;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.MailUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class CotizacionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4833526764348322717L;
	private List<String> lisPetservicioCampo;
	private List<String> lisPetservicioVete;
	private String[] lisPetservicioCampoSeleccionado;
	private String[] lisPetservicioVeteSeleccionado;
	private String nombres;
	private String apellidos;
	private String correo;
	private String otrosServicios;
	
	public CotizacionBean() {
		lisPetservicioCampo = new ArrayList<String>();
		lisPetservicioVete = new ArrayList<String>();
		nombres = null;
		apellidos = null;
		correo = null;
		otrosServicios = "";
		
		consultarServiciosCampo();
		consultarServiciosVete();
	}
	
	private void consultarServiciosCampo(){
		try{
			PetservicioBO petservicioBO = new PetservicioBO(); 
			List<Petservicio> tmp = petservicioBO.lisPetservicio(Parametro.EMPRESA_CAMPOFELIZ, 0);
			for(Petservicio petservicio : tmp){
				lisPetservicioCampo.add(petservicio.getNombre());
			}
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!", null);
		}
	}
	
	private void consultarServiciosVete(){
		try{
			PetservicioBO petservicioBO = new PetservicioBO(); 
			List<Petservicio> tmp = petservicioBO.lisPetservicio(Parametro.EMPRESA_VETERINARIABURGOS, 0);
			for(Petservicio petservicio : tmp){
				lisPetservicioVete.add(petservicio.getNombre());
			}
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!", null);
		}
	}
	
	public String enviar(){
		String url = null;
		
		if(validacionOk()){
			try{
				MailUtil mailUtil = new MailUtil();
				
				//formatear el contenido para el remitente de correo
				String contenido2 = contenido("Campo Feliz Cementerio de Mascotas", "Gracias por comunicarte con nosotros! En breve te estaremos respondiendo!");
				
				//enviar respuesta al remitente
				mailUtil.enviarMail(this.correo, "Cotización - Campo Feliz", contenido2);
				
				//formatear el contenido para el administrador de correo
				String contenido = contenido("Solicitud de Cotización", "Ha recibido una solicitud de cotización del sitio web");
				
				//enviar al administrador de correo
				mailUtil.enviarMail(null, "Cotización - Campo Feliz", contenido);
				
				mostrarPaginaMensaje("Gracias por comunicarte con nosotros! En breve te estaremos respondiendo!");
			}catch(AddressException e) {
				e.printStackTrace();
				new MessageUtil().showErrorMessage("Ingrese una cuenta de correo válida e intente nuevamente.","");
			}catch(Exception e){
				e.printStackTrace();
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
		
		return url;
	}
	
	private void mostrarPaginaMensaje(String mensaje) throws Exception {
		UsuarioBean usuarioBean = (UsuarioBean)new FacesUtil().getSessionBean("usuarioBean");
		usuarioBean.setMensaje(mensaje);
		
		FacesUtil facesUtil = new FacesUtil();
		facesUtil.redirect("mensaje.jsf");	 
	}
	
	private String contenido(String titulo, String textoIntroductorio) throws Exception {
		String contenido = "";
		FacesUtil facesUtil = new FacesUtil();
		String logo = facesUtil.getHostDomain() + "/resources/images/logo.jpg";
		
		FileUtil fileUtil = new FileUtil();
		String email = fileUtil.getMailPropertyValue("mail.user");
		
		contenido += "<html>";
		contenido += "<body style='font: 12px/18px Arial, Helvetica, sans-serif;'>";
		contenido += "<table cellpadding='0' cellspacing='0' style='width: 100%'>";
		contenido += "<tr>";
		contenido += "<td style='border: 1px solid #66ad23;background-color: #66ad23;color: white;border-radius: 4px 4px 0 0;height: 30px;'>";
		contenido += "<h1 style='font-size: 1.3em;line-height: 2.1;text-align: center;'>"+titulo+"</h1>";
		contenido += "</td>";
		contenido += "</tr>";
		contenido += "<tr>";
		contenido += "<td style='border: 1px solid #66ad23;padding: 10px 25px;'>";

		contenido += "<table cellpadding='0' cellspacing='0' style='width: 100%'>";
		contenido += "<tr>";
		contenido += "<td colspan='2'><center><a title='Campo Feliz' href='"+facesUtil.getHostDomain()+"' target='_blank'><img alt='Campo Feliz' src='"+logo+"'></img></a></center></td>";
		contenido += "</tr>";
		contenido += "<tr>";
		contenido += "<td colspan='2'><span>"+textoIntroductorio+"</span></td>";
		contenido += "</tr>";
		contenido += "<tr>";
		contenido += "<td colspan='2'><span>&nbsp;</span></td>";
		contenido += "</tr>";
		contenido += "<tr>";
		contenido += "<td style='width: 10%;'><strong>Nombres: </strong></td><td style='width: 90%;'>" + nombres + "</td>";
		contenido += "</tr>";
		contenido += "<tr>";	
		contenido += "<td><strong>Apellidos: </strong></td><td>" + apellidos + "</td>";
		contenido += "</tr>";
		contenido += "<tr>";	
		contenido += "<td><strong>Email: </strong></td><td>" + correo + "</td>";
		contenido += "</tr>";
		contenido += "<tr style='height: 20px;'>";	
		contenido += "<td></td><td></td>";
		contenido += "</tr>";
		contenido += "<tr>";	
		contenido += "<td colspan='2'><strong>Servicios que solicita: </strong></td>";
		contenido += "</tr>";
		if(lisPetservicioCampoSeleccionado != null && lisPetservicioCampoSeleccionado.length > 0){
			contenido += "<tr style='height: 20px;'>";	
			contenido += "<td></td><td></td>";
			contenido += "</tr>";
			contenido += "<tr>";	
			contenido += "<td colspan='2'><strong>Servicios de Campo Feliz que solicita: </strong></td>";
			contenido += "</tr>";
		}
		for(String petservicio : lisPetservicioCampoSeleccionado){
			contenido += "<tr>";	
			contenido += "<td colspan='2'>" + petservicio + "</td>";
			contenido += "</tr>";
		}
		if(lisPetservicioVeteSeleccionado != null && lisPetservicioVeteSeleccionado.length > 0){
			contenido += "<tr style='height: 20px;'>";	
			contenido += "<td></td><td></td>";
			contenido += "</tr>";
			contenido += "<tr>";	
			contenido += "<td colspan='2'><strong>Servicios de Veterinaria Burgos que solicita: </strong></td>";
			contenido += "</tr>";
		}
		for(String petservicio : lisPetservicioVeteSeleccionado){
			contenido += "<tr>";	
			contenido += "<td colspan='2'>" + petservicio + "</td>";
			contenido += "</tr>";
		}
		if(otrosServicios != null && otrosServicios.trim().length() > 0){
			contenido += "<tr style='height: 20px;'>";	
			contenido += "<td></td><td></td>";
			contenido += "</tr>";
			contenido += "<tr>";	
			contenido += "<td colspan='2'><strong>Otros Servicios: </strong></td>";
			contenido += "</tr>";
			contenido += "<tr>";	
			contenido += "<td colspan='2'>" + otrosServicios + "</td>";
			contenido += "</tr>";
			contenido += "<tr><td style='height: 20px;'>&nbsp;</td></tr>";
		}
		contenido += "<tr>";
		contenido += "<td colspan='2'><span>&nbsp;</span></td>";
		contenido += "</tr>";
		contenido += "<tr>";
		contenido += "<td colspan='2'><a href='mailto:"+email+"'>Desuscribir</a></td>";
		contenido += "</tr>";
		contenido += "<tr>";
		contenido += "<td colspan='2' style='height: 20px;'>&nbsp;</td>";
		contenido += "</tr>";
		contenido += "</table>";
		
		contenido += "</td>";
		contenido += "</tr>";
		contenido += "</table>";
		contenido += "</body>";
		contenido += "</html>";
		
		return contenido;
	}
	
	private boolean validacionOk(){
		boolean ok = false;
		
		if(nombres != null && nombres.trim().length() > 0){
			if(apellidos != null && apellidos.trim().length() > 0){
				if(correo != null && correo.trim().length() > 0){
					if( (lisPetservicioCampoSeleccionado != null && lisPetservicioCampoSeleccionado.length > 0 ) || 
						(lisPetservicioVeteSeleccionado != null && lisPetservicioVeteSeleccionado.length > 0) ){
						ok = true;
					}else{
						new MessageUtil().showWarnMessage("*Servicios", null);
					}
				}else{
					new MessageUtil().showWarnMessage("*Email", null);
				}
			}else{
				new MessageUtil().showWarnMessage("*Apellidos", null);
			}
		}else{
			new MessageUtil().showWarnMessage("*Nombres", null);
		}
		
		return ok;
	}
	
	public String getOtrosServicios() {
		return otrosServicios;
	}

	public void setOtrosServicios(String otrosServicios) {
		this.otrosServicios = otrosServicios;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String[] getLisPetservicioCampoSeleccionado() {
		return lisPetservicioCampoSeleccionado;
	}

	public void setLisPetservicioCampoSeleccionado(
			String[] lisPetservicioCampoSeleccionado) {
		this.lisPetservicioCampoSeleccionado = lisPetservicioCampoSeleccionado;
	}

	public String[] getLisPetservicioVeteSeleccionado() {
		return lisPetservicioVeteSeleccionado;
	}

	public void setLisPetservicioVeteSeleccionado(
			String[] lisPetservicioVeteSeleccionado) {
		this.lisPetservicioVeteSeleccionado = lisPetservicioVeteSeleccionado;
	}

	public List<String> getLisPetservicioCampo() {
		return lisPetservicioCampo;
	}

	public void setLisPetservicioCampo(List<String> lisPetservicioCampo) {
		this.lisPetservicioCampo = lisPetservicioCampo;
	}

	public List<String> getLisPetservicioVete() {
		return lisPetservicioVete;
	}

	public void setLisPetservicioVete(List<String> lisPetservicioVete) {
		this.lisPetservicioVete = lisPetservicioVete;
	}
	
}
