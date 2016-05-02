package com.web.cementerio.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.web.cementerio.bo.PetmascotahomenajeBO;
import com.web.cementerio.pojo.annotations.Petespecie;
import com.web.cementerio.pojo.annotations.Petmascotahomenaje;
import com.web.util.MessageUtil;

@ManagedBean
@ViewScoped
public class MascotasHomenajeBean  {
private LazyDataModel<Petmascotahomenaje>  listpetmascotahomenaje; 
/*private List<Petmascotahomenaje>  lisPetmascotahomenaje;*/
private List<Petespecie> lisPetespecie;
private int idespecie;
private String descripcionParam;


	public MascotasHomenajeBean(){
		idespecie=0;
		consultar();
		/*consultarEspecies();
		consultarMascotas(0);*/
	}
	
	/*public void consultarEspecies(){
		try
		{
			PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
			
			String[] textoBusqueda = null;
			
			if(descripcionParam != null && descripcionParam.trim().length() > 0){
				textoBusqueda = descripcionParam.split(" ");
			}
			
			lisPetespecie = petmascotahomenajeBO.lisPetespecieMascotas(textoBusqueda);
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}*/
	
	/*public void consultarMascotas(int idespecie){
		try
		{
			PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
			
			String[] textoBusqueda = null;
			
			if(descripcionParam != null && descripcionParam.trim().length() > 0){
				textoBusqueda = descripcionParam.split(" ");
			}
			
			lisPetmascotahomenaje = petmascotahomenajeBO.lisPetmascotaByNombreIdespecie(textoBusqueda, idespecie, 24);
 
		}catch(Exception e){
			e.printStackTrace();
			new MessageUtil().showFatalMessage("Ha ocurrido un error inesperado. Comunicar al Webmaster!","");
		}
	}*/
	
	/*public void buscarMascotas() {
		consultarEspecies();
		consultarMascotas(0);
	}*/
	
	@SuppressWarnings("serial")
	public void consultar(){
		try
		{
		 
			listpetmascotahomenaje = new LazyDataModel<Petmascotahomenaje>() {
				public List<Petmascotahomenaje> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
					List<Petmascotahomenaje> data = new ArrayList<Petmascotahomenaje>();
					PetmascotahomenajeBO petmascotahomenajeBO = new PetmascotahomenajeBO();
					int args[] = {0};
					
					String[] textoBusqueda = null;
					
					if(descripcionParam != null && descripcionParam.trim().length() > 0 ){
						textoBusqueda = descripcionParam.split(" ");
						//first = 0;
						//first = 1;
					}
					/*if(!texto.equals(descripcionParam)){
						first = 0;
						//this.setRowIndex(0);
						texto = descripcionParam;
						
					}*/
					data = petmascotahomenajeBO.lisPetmascotahomenajeBusquedaByPage(textoBusqueda, pageSize, first, args);
					
					if(data != null && !data.isEmpty() && args[0] == 0){
						//si hubieron datos pero sin texto de busqueda
						this.setRowCount(pageSize);
					}else{
						//args va con 0 solo cuando no hay datos
						this.setRowCount(args[0]);
					}
					
					return data;
				}
				
				
				@Override
                public void setRowIndex(int rowIndex) {
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


	public int getIdespecie() {
		return idespecie;
	}

	
	public void setIdespecie(int idespecie) {
		this.idespecie = idespecie;
	}

	public String getDescripcionParam() {
		return descripcionParam;
	}

	public void setDescripcionParam(String descripcionParam) {
		this.descripcionParam = descripcionParam;
	}

	public LazyDataModel<Petmascotahomenaje> getListpetmascotahomenaje() {
		return listpetmascotahomenaje;
	}

	public void setListpetmascotahomenaje(
			LazyDataModel<Petmascotahomenaje> listpetmascotahomenaje) {
		this.listpetmascotahomenaje = listpetmascotahomenaje;
	}

	/*public List<Petmascotahomenaje> getLisPetmascotahomenaje() {
		return lisPetmascotahomenaje;
	}

	public void setLisPetmascotahomenaje(List<Petmascotahomenaje> lisPetmascotahomenaje) {
		this.lisPetmascotahomenaje = lisPetmascotahomenaje;
	}*/

	public List<Petespecie> getLisPetespecie() {
		return lisPetespecie;
	}

	public void setLisPetespecie(List<Petespecie> lisPetespecie) {
		this.lisPetespecie = lisPetespecie;
	}

	
}
