package com.web.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

public class Utilities {
	
	private InputStream inputStream = null;
	private JasperPrint jasperPrint = null;
	private Map<String, Object> parametros;
	
	public void imprimirJasperPdf(String nombreReporte, Map<String, Object> parametros) throws Exception {
		//try{
			this.parametros = parametros;
			inputStream = new FacesUtil().getResourceAsStream("/reportes/"+nombreReporte+".jasper");

			if(inputStream != null){
		        Session session = HibernateUtil.getSessionFactory().openSession();
		        //Connection connection = session.connection();
		        
		        session.doWork(
	        	    new Work() {
	        	        public void execute(Connection connection)  
	        	        { 
							try {
								jasperPrint = JasperFillManager.fillReport(inputStream, Utilities.this.parametros, connection);
							} catch (JRException e) {
								e.printStackTrace();
							}
	        	        }
	        	    }
	        	);
		        
				//JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, connection);//new JREmptyDataSource());
				
				//crea en disco
				String rutaReporteDestino = System.getProperty("java.io.tmpdir");
				JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReporteDestino+"/"+nombreReporte+".pdf");
				
				//muestra en browser
				FacesContext.getCurrentInstance().getExternalContext().responseReset(); 
				FacesContext.getCurrentInstance().getExternalContext().setResponseContentType(FacesContext.getCurrentInstance().getExternalContext().getMimeType(nombreReporte+".pdf")); 
				//FacesContext.getCurrentInstance().getExternalContext().setResponseContentLength(contentLength); 
				FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-disposition", "inline; filename=\""+nombreReporte+".pdf\"");//inline //attachment
		        OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();
		        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
		        FacesContext.getCurrentInstance().responseComplete();
			}else{
				throw new Exception("No se ha encontrado archivo: " + nombreReporte);
			}
		/*}
		catch(Exception e){
			throw new Exception();
		} finally {
			if(inputStream != null){
				try{
					inputStream.close();
				}catch(Exception e){
					
				}
			}
		}*/
	}
	
	public String cifrar(String texto) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(texto.getBytes("UTF-8"));
       
        StringBuilder sb = new StringBuilder(2*hash.length);
        for(byte b : hash){
            sb.append(String.format("%02x", b&0xff));
        }
		
		return sb.toString();
	}
	
}
