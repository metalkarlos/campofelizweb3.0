package com.web.cementerio.global;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.web.util.FileUtil;
import com.web.util.MessageUtil;

@ManagedBean
@SessionScoped
public class Parametro {
	public static final String FILE_SEPARATOR = "/";//File.separator;
	public static final long DAY_IN_MILLISECONDS = (24*60*60*1000);
	public static final String PROPERTIES_FILE_NAME = "parametros.properties";
	public static final String PROPERTIES_MAIL = "mail.properties";
	public static final long TAMAÑO_IMAGEN = 716800;
	private String servletImagenes;
	
	/*EMPRESAS*/
	public static final int EMPRESA_CAMPOFELIZ = 1;
	public static final int EMPRESA_VETERINARIABURGOS = 2;
	
	/*OFICINAS*/
	public static final int OFICINA_CAMPOFELIZ_LAROCA = 1;
	public static final int OFICINA_CAMPOFELIZ_LAMARTHA = 2;
	public static final int OFICINA_VETERINARIABURGOS_LAMARTHA = 3;
	
	public Parametro() {
		cargarRutaImagenes();
	}
	
	public void cargarRutaImagenes(){
		try {
			servletImagenes = new FileUtil().getPropertyValue("servletImagenes");
		} catch (Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public String getServletImagenes() {
		return servletImagenes;
	}

	public void setServletImagenes(String servletImagenes) {
		this.servletImagenes = servletImagenes;
	}
}
