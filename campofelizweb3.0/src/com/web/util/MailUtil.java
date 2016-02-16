package com.web.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.web.cementerio.global.Parametro;

public class MailUtil {

	private String usuario;
	private String clave;
	private String debug;
	
	public MailUtil() {
	}
	
	/***
	 * Envía correo
	 * @param destinatario Si es null se envía únicamente al administrador
	 * @param asunto
	 * @param contenido
	 * @throws Exception
	 */
	public void enviarMail(String destinatario, String asunto, String contenido) throws Exception {
		Properties properties = new FileUtil()
				.getPropertiesFile(Parametro.PROPERTIES_MAIL);
		usuario = properties.getProperty("mail.user");
		clave = properties.getProperty("mail.password");
		debug = properties.getProperty("mail.debug");

		destinatario = destinatario == null ? usuario : destinatario;
		
		InternetAddress internetAddressDestinatario = new InternetAddress(destinatario);
		internetAddressDestinatario.validate();
		
		InternetAddress internetAddressUsuario = new InternetAddress(usuario);
		internetAddressUsuario.validate();

		//Session mailSession = Session.getDefaultInstance(properties, null);
		Session mailSession = Session.getDefaultInstance(properties, 
			    new javax.mail.Authenticator(){
			        protected PasswordAuthentication getPasswordAuthentication() {
			            return new PasswordAuthentication(
			            		usuario, clave);
			        }
			});

		mailSession.setDebug(debug.equalsIgnoreCase("true"));
		
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(internetAddressUsuario);
		message.addRecipient(Message.RecipientType.TO, internetAddressDestinatario);
		message.setSubject(asunto, "utf-8");
		message.setContent(contenido, "text/html; charset=utf-8");
		message.setHeader("X-Mailer", "Javax Mail 1.5");
		message.setSentDate(new Date());
		
		Transport.send(message);
		//Transport tr = mailSession.getTransport("smtp");
		//tr.connect(host, to, clave);
		//message.saveChanges();      // don't forget this
		//tr.sendMessage(message, message.getAllRecipients());
		//tr.close();
		
		mailSession.getDebugOut();
	}
	
}
