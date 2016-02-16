package com.web.cementerio.bean;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

//import org.primefaces.component.menuitem.MenuItem;
//import org.primefaces.model.DefaultMenuModel;
//import org.primefaces.model.MenuModel;

@ManagedBean
@SessionScoped
public class BreadCrumbBean {

	private MenuModel breadCrumb;
	private List<String> opciones = Arrays.asList("quienessomos.jsf",
			"servicios.jsf",
			"servicio.jsf",
			"encuesta.jsf",
			"cotizacion.jsf",
			"cementeriovirtual.jsf",
			"mascotashomenaje.jsf",
			"mascotahomenaje.jsf",
			"noticias.jsf",
			"noticia.jsf",
			"guiageneral.jsf",
			"guia.jsf",
			"preguntas.jsf",
			"contactenos.jsf");
	
	private String[][] rutas = {{"Home;Quienes Somos","home.jsf;quienessomos.jsf",";"},
			{"Home;Servicios","home.jsf;servicios.jsf",";idempresa"},
			{"Home;Servicios;Servicio","home.jsf;servicios.jsf;servicio.jsf",";idempresa;idservicio,idempresa"},
			{"Home;Encuesta","home.jsf;encuesta.jsf",";"},
			{"Home;Cotización","home.jsf;cotizacion.jsf",";"},
			{"Home;Instalaciones","home.jsf;cementeriovirtual.jsf",";"},
			{"Home;Homenaje Póstumo","home.jsf;mascotashomenaje.jsf",";"},
			{"Home;Homenaje Póstumo;Mascota","home.jsf;mascotashomenaje.jsf;mascotahomenaje.jsf",";;idmascota"},
			{"Home;Noticias","home.jsf;noticias.jsf",";"},
			{"Home;Noticias;Noticia","home.jsf;noticias.jsf;noticia.jsf",";;idnoticia"},
			{"Home;Guía General","home.jsf;guiageneral.jsf",";"},
			{"Home;Guía General;Cómo Decir Adiós","home.jsf;guiageneral.jsf;guia.jsf",";;idguia"},
			{"Home;Preguntas Frecuentes","home.jsf;preguntas.jsf",";"},
			{"Home;Contáctenos","home.jsf;contactenos.jsf",";"}};
	
	public BreadCrumbBean() {
		breadCrumb = new DefaultMenuModel();
	}
	
	public void armarBreadCrumb(String opcion,String param){
		int index = opciones.indexOf(opcion);
		breadCrumb = new DefaultMenuModel();
		
		if(index >= 0){
			String rutaTitulos = rutas[index][0];
			String[] arrTitulos = rutaTitulos.split(";");
			
			String rutaOpciones = rutas[index][1];
			String[] arrOpciones = rutaOpciones.split(";");
			String url = null;
			
			String rutaParametros = rutas[index][2];//";idempresa;idservicio,idempresa"
			String[] arrParametros = rutaParametros.split(";");//idservicio,idempresa

			String[] arrParametrosUrl = null;
			if(param != null && param.trim().length() > 0){
				arrParametrosUrl = param.split("&");
			}
			
			for(int i = 0 ; i<arrOpciones.length ; i++){
				DefaultMenuItem item = new DefaultMenuItem();
				item.setValue(arrTitulos[i]);

				String par = "";
						
				if(arrParametros != null && arrParametros.length > 0 && arrParametros[i] != null && arrParametros[i].trim().length() > 0 && 
				   arrParametrosUrl != null){
					String tmp = arrParametros[i].toString();
					String[] arr = tmp.split(",");//idservicio,idempresa
					
					for(String strparam : arr){//idservicio <= idservicio,idempresa
						for(String strurl : arrParametrosUrl){//idservicio=6 <= idservicio=6&idempresa=2
							int idx = strurl.indexOf(strparam);
							if(idx > -1){
								if(par != null && par.trim().length() > 0){
									par += "&";
								}
								par += strurl;
							}
						}
					}
					
				}
				
				if(par != null && par.trim().length() > 0){
					url = arrOpciones[i]+"?"+par;
				}else{
					url = arrOpciones[i];
				}
				
				item.setUrl(url);
				breadCrumb.addElement(item);
			}
		}
	}
	
	public MenuModel getBreadCrumb() { return breadCrumb; }
}
