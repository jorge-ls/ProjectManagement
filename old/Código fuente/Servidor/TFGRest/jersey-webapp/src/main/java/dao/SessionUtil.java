package dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class SessionUtil {

	 	private static SessionUtil instance= new SessionUtil();
	    private SessionFactory sessionFactory;
	    
	    public static SessionUtil getInstance(){
	            return instance;
	    }
	    
		private SessionUtil(){
	        /*Configuration configuration = new Configuration();
	        configuration.configure("hibernate.cfg.xml");
	                
	        sessionFactory = configuration.buildSessionFactory();*/
			//Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
			try{
				Configuration configuration = new Configuration().configure();
				StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
				applySettings(configuration.getProperties());
				sessionFactory = configuration.buildSessionFactory(builder.build());
				//sessionFactory = configuration.buildSessionFactory();
			} catch(HibernateException exception){
			     System.out.println("Problem creating session factory");
			     exception.printStackTrace();
			}
			
	    }
	    
	    public static Session getSession(){
	        Session session =  getInstance().sessionFactory.openSession();
	        
	        return session;
	    }
}
