package com.web.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory = buildSessionFactory();

	/*private static SessionFactory buildSessionFactory() {
		try {
            // Create the SessionFactory from [hibernate].cfg.xml
            FileUtil fileUtil = new FileUtil();
            String resource = fileUtil.getPropertyValue("hibernate-config");
            //String resource = "postgresql.cfg.xml";
			return new Configuration().configure(resource).buildSessionFactory();
		} catch (Throwable ex) {
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}*/
	private static SessionFactory buildSessionFactory() {
		try {
            FileUtil fileUtil = new FileUtil();
            String resource = fileUtil.getPropertyValue("hibernate-config");
            Configuration configuration = new Configuration();
            
            configuration.configure(resource);
            ServiceRegistry ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb);
            
			return sessionFactory;
		} catch (Throwable ex) {
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}
	/*private static SessionFactory buildSessionFactory() {
		try {
            //FileUtil fileUtil = new FileUtil();
            //String resource = fileUtil.getPropertyValue("hibernate-config");
            Configuration configuration = new Configuration();
            
            //configuration.configure(resource);
            ServiceRegistry ssrb = new StandardServiceRegistryBuilder().build();
            SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb);
            
			return sessionFactory;
		} catch (Throwable ex) {
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}*/

	public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
