package com.web.cementerio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.web.cementerio.bo.PethomeBO;
import com.web.cementerio.pojo.annotations.Pethome;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class HomeBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7268920983615910156L;
	private LazyDataModel<Pethome> lisPethome;

	public HomeBean() {
		consultarVideos();
	}

	@SuppressWarnings("serial")
	public void consultarVideos(){
		try
		{
			lisPethome = new LazyDataModel<Pethome>() {
				public List<Pethome> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
					List<Pethome> data = new ArrayList<Pethome>();
					PethomeBO pethomeBO = new PethomeBO();
					int args[] = {0};
					
					data = pethomeBO.lisPethomeByPage(pageSize, first, args);
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

	public LazyDataModel<Pethome> getLisPethome() {
		return lisPethome;
	}

	public void setLisPethome(LazyDataModel<Pethome> lisPethome) {
		this.lisPethome = lisPethome;
	}
	
}
