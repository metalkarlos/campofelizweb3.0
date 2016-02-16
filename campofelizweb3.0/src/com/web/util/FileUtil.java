package com.web.util;

import com.web.cementerio.global.Parametro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class FileUtil {

	public void createFile(String path, byte[] bfile) throws Exception {
		FileOutputStream fos;
		
		fos = new FileOutputStream(new File(path));
		fos.write(bfile);
		fos.close();
	}
	
	public boolean deleteFile(String pathdirectory) throws Exception {
		boolean ok = true;
		
		File fileDir = new File(pathdirectory);
		if(fileDir.exists()){
			ok = fileDir.delete();
		}
				
		return ok;
	}
	
	public boolean createDir(String pathdirectory) throws Exception {
		boolean ok = true;
		
		File fileDir = new File(pathdirectory);
		if(!fileDir.exists()){
			ok = fileDir.mkdirs();
		}
				
		return ok;
	}
	
	public boolean existFile(String pathdirectory) throws Exception {
		boolean ok = true;
		
		File fileDir = new File(pathdirectory);
		ok = fileDir.exists();
				
		return ok;
	}
	
	public Properties getPropertiesFile(String nombre) throws Exception {
		Properties properties = new Properties();
		InputStream inputStream = null;
		
		try {
			inputStream = FileUtil.class.getClassLoader().getResourceAsStream(nombre);
			
			if(inputStream != null){
				properties.load(inputStream);
			}else{
				throw new Exception("No se ha encontrado archivo: " + nombre);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if(inputStream != null){
				try{
					inputStream.close();
				}catch(Exception e){
					
				}
			}
		}
		
		return properties;
	}
	
	public String getPropertyValue(String key) throws Exception {
		String value = null;
		
		Properties properties = getPropertiesFile(Parametro.PROPERTIES_FILE_NAME);
		value = properties.getProperty(key);
		
		return value;
	}
	
	public String getMailPropertyValue(String key) throws Exception {
		String value = null;
		
		Properties properties = getPropertiesFile(Parametro.PROPERTIES_MAIL);
		value = properties.getProperty(key);
		
		return value;
	}
	
	public String getFileExtention(String fileName){
		String fileExtention = "";
		
		//Maner 1
		//fileExtention = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		
		//Maner 2
		String[] partes = fileName.split("\\.");
		fileExtention = partes[partes.length-1];
		
		//Maner 3
		/*char[] ch = fileName.toCharArray();
		for(int i = 0; i <ch.length; i++){
			if(ch[i] == '.'){
				fileExtention = ".";
			}else{
				fileExtention = fileExtention + ch[i];
			}
		}*/
		
		return fileExtention;
	}
}
