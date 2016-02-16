package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.StreamedContent;

import com.web.cementerio.bo.CotoficinaBO;
import com.web.cementerio.bo.SetpeticionclaveBO;
import com.web.cementerio.bo.SetusuarioBO;
import com.web.cementerio.global.Parametro;
import com.web.cementerio.pojo.annotations.Cotoficina;
import com.web.cementerio.pojo.annotations.Setpeticionclave;
import com.web.cementerio.pojo.annotations.Setusuario;
import com.web.util.FacesUtil;
import com.web.util.FileUtil;
import com.web.util.MailUtil;
import com.web.util.MessageUtil;
import com.web.util.Utilities;

@ManagedBean
@SessionScoped
public class UsuarioBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5577462729566726719L;
	private Setusuario setUsuario;
	private String username;
	private String password;
	private String ip;
	private String sid;
	private boolean autenticado;
	private StreamedContent streamedContent;
	private Cotoficina cotoficina;
	private String mensaje;
	private Date hoy;
	
	public UsuarioBean(){
		FacesUtil facesUtil = new FacesUtil();  
		ip = facesUtil.getClientIp();
		sid = facesUtil.getSid();
		setUsuario = new Setusuario();
		cotoficina = new Cotoficina();
		mensaje = "";
		hoy = new Date();
		
		consultarEmpresa();
	}
	
	private void consultarEmpresa() {
		try{
			cotoficina = new CotoficinaBO().getCotoficinabyId(Parametro.OFICINA_CAMPOFELIZ_LAROCA);
		}catch(Exception re){
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public void setSetUsuario(Setusuario setUsuario) {
		this.setUsuario = setUsuario;
	}

	public Setusuario getSetUsuario() {
		return setUsuario;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getIp() {
		return ip;
	}

	public String getSid() {
		return sid;
	}

	public boolean isAutenticado() {
		return autenticado;
	}

	public String login(){
		String strRedirect = null;
		
		if(validacionOk()){
			try{
				Utilities utilities = new Utilities();
				String cifrado = utilities.cifrar(password);
				setUsuario = new SetusuarioBO().getByUserPasswd(username, cifrado);
				
				if(setUsuario!=null && setUsuario.getIdusuario()>0){
					autenticado = true;
					
					FileUtil fileUtil = new FileUtil();
					strRedirect = fileUtil.getPropertyValue("home");
				}else{
					new MessageUtil().showWarnMessage("Autenticación fallida. Usuario o Contraseña no existen.","");
				}
			}catch(Exception re){
				new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
			}
		}
		
		return strRedirect;
	}
	
	private boolean validacionOk(){
		boolean ok = false;
		
		if(username != null && username.trim().length() > 0){
			if(password != null && password.trim().length() > 0){
				ok = true;
			}else{
				new MessageUtil().showWarnMessage("Ingrese la Clave", null);
			}
		}else{
			new MessageUtil().showWarnMessage("Ingrese su Usuario", null);
		}
		
		return ok;
	}
	
	public String logout(){
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			facesUtil.logout();
			facesUtil.redirectByPropertyFileKey("home");
		}catch(Exception re){
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
		
		return "";
	}
	
	public void enviarOlvidoClave(){
		if(validacionOlvidoClaveOk()){
			if(usuarioExiste()){
				try{
					//generar uid
					UUID uid = UUID.randomUUID();
					
					Setpeticionclave setpeticionclave = new Setpeticionclave();
					setpeticionclave.setUid(uid);
					setpeticionclave.setUsuario(username);
					
					SetpeticionclaveBO setpeticionclaveBO = new SetpeticionclaveBO();
					
					//insertar en tabla uid y fechaexpiracion
					boolean ok = setpeticionclaveBO.ingresar(setpeticionclave);
					
					if(ok){
						//enviar mail verificacion
						MailUtil mailUtil = new MailUtil();
						FacesUtil facesUtil = new FacesUtil();
						
						//formatear el contenido para el administrador de correo
						String formulario = facesUtil.getHostDomain() + "/pages/cambiarclave.jsf";
						String logo = facesUtil.getHostDomain() + "/resources/images/logo.jpg";
						
						String contenido = "";
						contenido += "<html>";
						contenido += "<body style='font: 12px/18px Arial, Helvetica, sans-serif;'>";
						contenido += "<table cellpadding='0' cellspacing='0' style='width: 100%'>";
						contenido += "<tr>";
						contenido += "<td style='border: 1px solid #66ad23;background-color: #66ad23;color: white;border-radius: 4px 4px 0 0;height: 30px;'>";
						contenido += "<h1 style='font-size: 1.3em;line-height: 2.1;text-align: center;'>Olvido de Clave</h1>";
						contenido += "</td>";
						contenido += "</tr>";
						contenido += "<tr>";
						contenido += "<td style='border: 1px solid #66ad23;padding: 10px 25px;'>";

						contenido += "<table cellpadding='0' cellspacing='0' style='width: 100%'>";
						contenido += "<tr>";
						contenido += "<td><center><a title='Campo Feliz' href='"+facesUtil.getHostDomain()+"' target='_blank'><img alt='Campo Feliz' src='"+logo+"'></img></a></center></td>";
						contenido += "</tr>";
						contenido += "<tr>";
						contenido += "<td style='height: 30px;'><span>Ha solicitado cambiar la clave del usuario: <strong>"+username+"</strong>.</span></td>";
						contenido += "</tr>";
						contenido += "<tr>";
						contenido += "<td style='height: 30px;'><span>Dé click en el siguiente link para acceder al formulario de cambio de clave. Este link tiene una validez de <strong>5 minutos</strong>.</span></td>";
						contenido += "</tr>";
						contenido += "<tr>";
						contenido += "<td style='height: 30px;'><a href='"+formulario+"?uid="+uid+"'>"+formulario+"?uid="+uid+"</a></td>";
						contenido += "</tr>";
						contenido += "<tr>";
						contenido += "<td><span>&nbsp;</span></td>";
						contenido += "</tr>";
						contenido += "<tr>";
						contenido += "<td><span style='display: block;'><span style='color:#808080;'>Dirección IP:</span> <strong>"+ip+"</strong></span></td>";
						contenido += "</tr>";
						contenido += "<tr>";
						contenido += "<td><span>&nbsp;</span></td>";
						contenido += "</tr>";
						contenido += "<tr>";
						contenido += "<td><span>Si ud no ha solicitado el cambio de clave ignore este correo.</span></td>";
						contenido += "</tr>";
						contenido += "<tr><td style='height: 20px;'>&nbsp;</td></tr>";
						contenido += "</table>";
						
						contenido += "</td>";
						contenido += "</tr>";
						contenido += "</table>";
						contenido += "</body>";
						contenido += "</html>";
						
						//enviar al administrador de correo
						mailUtil.enviarMail(null, "Olvido de Clave - Campo Feliz", contenido);
						//mostrar mensaje en pantalla se ha enviado mail
						new MessageUtil().showInfoMessage("Se ha enviado un link de confirmación a su cuenta de correo", "");
					}else{
						new MessageUtil().showInfoMessage("No se ha podido enviar la solicitud. Intente en unos minutos", "");
					}
				}catch(Exception e){
					e.printStackTrace();
					new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
				}
			}
		}
	}
	
	private boolean validacionOlvidoClaveOk(){
		boolean ok = false;
		
		if(username != null && username.trim().length() > 0){
			ok = true;
		}else{
			new MessageUtil().showWarnMessage("Ingrese su Usuario", "");
		}
		
		return ok;
	}
	
	private boolean usuarioExiste(){
		boolean ok = false;
		
		try{
			SetusuarioBO setusuarioBO = new SetusuarioBO();
			Setusuario setusuario = setusuarioBO.getSetusuarioByUsuario(username);
			
			if(setusuario != null && setusuario.getIdusuario() > 0){
				ok = true;
			}else{
				new MessageUtil().showWarnMessage("Usuario no existe", "");
			}
		}catch(Exception re){
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
		
		return ok;
	}
	
	public String redirect(){
		FileUtil fileUtil = new FileUtil();
		
		try{
			String strnotlogged = fileUtil.getPropertyValue("notlogged");
			new FacesUtil().redirect(strnotlogged);
		}catch(Exception re){
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
		
		return "";
	}

	public StreamedContent getStreamedContent() {
		return streamedContent;
	}

	public void setStreamedContent(StreamedContent streamedContent) {
		this.streamedContent = streamedContent;
	}

	public Cotoficina getCotoficina() {
		return cotoficina;
	}

	public void setCotoficina(Cotoficina cotoficina) {
		this.cotoficina = cotoficina;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Date getHoy() {
		return hoy;
	}

	public void setHoy(Date hoy) {
		this.hoy = hoy;
	}

}