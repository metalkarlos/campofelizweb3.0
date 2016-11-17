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

import com.web.cementerio.bo.PetguiaBO;
import com.web.cementerio.pojo.annotations.Petguia;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class PromocionesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6807607247744580426L;
	private LazyDataModel<Petguia> lisPetguia;
	private String descripcionParam;
	private int columnsGrid;
	private int rowsGrid;
	
	public PromocionesBean() {
		setColumnsGrid(2);
		setRowsGrid(3);
	}
	
	@PostConstruct
	public void PostPromocionesBean() {
		
		try{
			consultarPromociones();
		} catch(Exception e) {
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}
	
	@SuppressWarnings("serial")
	public void consultarPromociones(){
		try
		{
			lisPetguia = new LazyDataModel<Petguia>() {
				public List<Petguia> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
					List<Petguia> data = new ArrayList<Petguia>();
					PetguiaBO petguiaBO = new PetguiaBO();
					int args[] = {0};
					
					String[] textoBusqueda = null;
					if(descripcionParam != null && descripcionParam.trim().length() > 0 && descripcionParam.trim().compareTo("buscar") != 0 ){
						textoBusqueda = descripcionParam.split(" ");
					}
					
					data = petguiaBO.lisPetguiaBusquedaByPage(textoBusqueda, pageSize, first, args);
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

	public LazyDataModel<Petguia> getLisPetguia() {
		return lisPetguia;
	}

	public void setLisPetguia(LazyDataModel<Petguia> lisPetguia) {
		this.lisPetguia = lisPetguia;
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
		return rowsGrid;
	}

	public void setRowsGrid(int rowsGrid) {
		this.rowsGrid = rowsGrid;
	}

}
