package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.web.cementerio.bo.PetservicioBO;
import com.web.cementerio.pojo.annotations.Petservicio;
import com.web.util.FacesUtil;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class ServiciosBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3898128216790895916L;
	private LazyDataModel<Petservicio> lisPetservicio;
	private String tituloParam;
	private String descripcionParam;
	private int columnsGrid;
	private int rowsGrid;
	private int idempresa;

	public ServiciosBean() {
		tituloParam = "";
		descripcionParam = "buscar";
		
		setColumnsGrid(2);
		setRowsGrid(3);
		
		//consultarServicios();
	}
	
	@PostConstruct
	public void PostServiciosBean() {
		
		FacesUtil facesUtil = new FacesUtil();
		
		try{
			Object par = facesUtil.getParametroUrl("idempresa");
			if(par != null){
				idempresa = Integer.parseInt(par.toString());
				
				consultarServicios();
			}else{
				facesUtil.redirect("home.jsf");
			}
		} catch(NumberFormatException ne){
			try{facesUtil.redirect("home.jsf");}catch(Exception e){}
		} catch(Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	@SuppressWarnings("serial")
	public void consultarServicios(){
		try
		{
			lisPetservicio = new LazyDataModel<Petservicio>() {
				public List<Petservicio> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
					List<Petservicio> data = new ArrayList<Petservicio>();
					PetservicioBO petservicioBO = new PetservicioBO();
					int args[] = {0};
					
					String[] textoBusqueda = null;
					if(descripcionParam != null && descripcionParam.trim().length() > 0 && descripcionParam.trim().compareTo("buscar") != 0 ){
						textoBusqueda = descripcionParam.split(" ");
					}
					
					data = petservicioBO.lisPetservicioBusquedaByPage(textoBusqueda, idempresa, pageSize, first, args);
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

	public LazyDataModel<Petservicio> getLisPetservicio() {
		return lisPetservicio;
	}

	public void setLisPetservicio(LazyDataModel<Petservicio> lisPetservicio) {
		this.lisPetservicio = lisPetservicio;
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

	public int getColumnsGrid() {
		return columnsGrid;
	}

	public void setColumnsGrid(int columnsGrid) {
		this.columnsGrid = columnsGrid;
	}

	public int getRowsGrid() {
		return rowsGrid * columnsGrid;
	}

	public void setRowsGrid(int rowsGrid) {
		this.rowsGrid = rowsGrid;
	}

	public int getIdempresa() {
		return idempresa;
	}

	public void setIdempresa(int idempresa) {
		this.idempresa = idempresa;
	}
	
}
