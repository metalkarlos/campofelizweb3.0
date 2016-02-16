package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.web.cementerio.bo.PetnoticiaBO;
import com.web.cementerio.pojo.annotations.Petnoticia;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class NoticiasBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2425720865157253886L;
	private LazyDataModel<Petnoticia> lisPetnoticia;
	private String tituloParam;
	private String descripcionParam;
	
	public NoticiasBean() {
		tituloParam = "";
		descripcionParam = "buscar";
		consultarNoticias();
	}
	
	@SuppressWarnings("serial")
	public void consultarNoticias(){
		try
		{
			lisPetnoticia = new LazyDataModel<Petnoticia>() {
				public List<Petnoticia> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
					List<Petnoticia> data = new ArrayList<Petnoticia>();
					PetnoticiaBO petnoticiaBO = new PetnoticiaBO();
					int args[] = {0};
					
					String[] textoBusqueda = null;
					if(descripcionParam != null && descripcionParam.trim().length() > 0 && descripcionParam.trim().compareTo("buscar") != 0 ){
						textoBusqueda = descripcionParam.split(" ");
						first = 0;
					}
					
					data = petnoticiaBO.lisPetnoticiaBusquedaByPage(textoBusqueda, pageSize, first, args);
					this.setRowCount(args[0]);
	
			        return data;
				}
				
				@Override
                public void setRowIndex(int rowIndex) {
                    /*
                     * The following is in ancestor (LazyDataModel):
                     * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
                     */
                    if (rowIndex == -1 || getPageSize() == 0) {
                        super.setRowIndex(-1);
                    }
                    else {
                        super.setRowIndex(rowIndex % getPageSize());
                    }      
                }
			};
		}catch(Exception re){
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}

	public LazyDataModel<Petnoticia> getLisPetnoticia() {
		return lisPetnoticia;
	}

	public void setLisPetnoticia(LazyDataModel<Petnoticia> lisPetnoticia) {
		this.lisPetnoticia = lisPetnoticia;
	}

	public String getTituloParam() {
		return tituloParam;
	}

	public void setTituloParam(String tituloParam) {
		this.tituloParam = tituloParam;
	}

	public String getDescripcionParam() {
		return descripcionParam;
	}

	public void setDescripcionParam(String descripcionParam) {
		this.descripcionParam = descripcionParam;
	}


}
