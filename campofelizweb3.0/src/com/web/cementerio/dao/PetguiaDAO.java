package com.web.cementerio.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.web.cementerio.pojo.annotations.Petfotoguia;
import com.web.cementerio.pojo.annotations.Petguia;


public class PetguiaDAO {
	
		public PetguiaDAO(){
			
		}
		
		public Petguia getPetguiaById(Session session, int idguia, int idestado) throws Exception {

			    Petguia petguia=null;
			    Criteria criteria = session.createCriteria(Petguia.class, "g")
						.add( Restrictions.eq("g.idguia", idguia))
						.add( Restrictions.eq("g.setestado.idestado", idestado))
						//.createAlias("g.petfotoguias", "foto", Criteria.LEFT_JOIN);
						.createAlias("g.petfotoguias", "foto", JoinType.LEFT_OUTER_JOIN);
						
			   
			    petguia= (Petguia)criteria.uniqueResult();
			    
			    if((!petguia.getPetfotoguias().isEmpty()) && petguia.getPetfotoguias().size()>0){
			    	Set<Petfotoguia> tmp = new HashSet<Petfotoguia>();
			       
			    	for(Petfotoguia petfotoguia: petguia.getPetfotoguias()){
			    		if(petfotoguia.getSetestado().getIdestado() == idestado){
			    			tmp.add(petfotoguia);
			    		}
			    	}
			    	petguia.setPetfotoguias(tmp);
			    }
			    
				return petguia;
			}
		
		@SuppressWarnings("unchecked")
		public List<Petguia> getListpetguiaByPrincipal(Session session, int maxresultado) throws Exception {
		    List<Petguia> listpetguia=null;
		
			Criteria criteria = session.createCriteria(Petguia.class)
		    		.add(Restrictions.eq("principal", true))
		    		.add(Restrictions.eq("setestado.idestado", 1))
				    .addOrder(Order.desc("fechapublicacion"))
				    .setMaxResults(maxresultado);
		    
		    listpetguia= (List<Petguia>)criteria.list();
			
			return listpetguia;
		}
	
		
		@SuppressWarnings("unchecked")
		public List<Petguia> lisPetguiaBusquedaByPage(Session session, String[] texto, int pageSize, int pageNumber, int args[], int idestado) throws Exception {
			List<Petguia> listPetguia = null;
			
			Criteria criteria = session.createCriteria(Petguia.class)
					 .add(Restrictions.eq("setestado.idestado", idestado));
			
			if(texto != null && texto.length > 0){
				String query = "(";
				
				for(int i=0;i<texto.length;i++)
				{
					query += "lower({alias}.titulo) like lower('%"+texto[i]+"%') or lower({alias}.descripcion) like lower('%"+texto[i]+"%')";
					if(i<texto.length-1){
						query += "or ";
					
					}
				}
				query += ")";
				
				
				criteria.add(Restrictions.sqlRestriction(query));
				
			}
			criteria.addOrder(Order.desc("fechapublicacion"))
			.setMaxResults(pageSize)
			.setFirstResult(pageNumber);
			
			listPetguia = (List<Petguia>)criteria.list();
			
			if(listPetguia.size() >0 && !listPetguia.isEmpty()){
				Criteria criteriaCount = session.createCriteria(Petguia.class)
						.add(Restrictions.eq("setestado.idestado", idestado))
						.setProjection( Projections.rowCount());
				
				if(texto != null && texto.length > 0){
					String query = "(";
					
					for(int i=0;i<texto.length;i++)
					{
						query += "lower({alias}.titulo) like lower('%"+texto[i]+"%') or lower({alias}.descripcion) like lower('%"+texto[i]+"%')";
					
						if(i<texto.length-1){
							query += "or ";
					
						}
					}
					query += ")";
					
					
					criteriaCount.add(Restrictions.sqlRestriction(query));
					
				}
					
					Object object = criteriaCount.uniqueResult();
					int count = (object==null?0:Integer.parseInt(object.toString()));
					args[0] = count;
		  
			}
		    else{
			   	args[0] = 0;
			}
			return listPetguia;
			
		}
		
		
		
		public int getMaxidpetguia(Session session)throws Exception{
			int maxid = 0;
					
			Object object = session.createQuery("select max(idguia)+1 from Petguia ").uniqueResult();
			maxid = (object ==null ?1: Integer.parseInt(object.toString()));
			
			return maxid;
		}
		
	    public void savePetguia(Session session, Petguia petguia) throws Exception {
	    	session.save(petguia);
	    }
	    public void updatePetguia(Session session, Petguia petguia) throws Exception {
	    	session.update(petguia);
	    }

}
