package dao;


import java.io.File;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import modelo.EstadoTrabajo;
import modelo.Estudiante;
import modelo.Fichero;
import modelo.Linea;
import modelo.Profesor;


public class EstudianteHibernateDAO implements EstudianteDAO {
	
		/**
		 * Método para registrar un estudiante en base de datos
		 * @param bean El estudiante
		 */
		 public void addEstudiante(Estudiante bean){
			 Session session = SessionUtil.getSession();        
			 Transaction tx = session.beginTransaction();
			 addEstudiante(session,bean);        
			 tx.commit();
			 session.close();      
		 }
	    
		 /**
		  * Método para registrar un estudiante
		  * @param session El objeto session
		  * @param bean El estudiante a registrar
		  */
	    public void addEstudiante(Session session, Estudiante bean){
	        Estudiante estudiante = new Estudiante();
	        
	        estudiante.setNombre(bean.getNombre());
	        estudiante.setApellidos(bean.getApellidos());
	        estudiante.setFechaEstimada(bean.getFechaEstimada());
	        estudiante.setUrlGit(bean.getUrlGit());
	        estudiante.setLinea(bean.getLinea());
	        estudiante.setEstadoTrabajo(EstadoTrabajo.PENDIENTE);
	        
	        session.save(estudiante);
	    }
	    
	    /**
	     * Método para obtener los datos de un estudiante
	     * @param id El identificador del estudiante
	     * @return Los datos del estudiante
	     */
	    public Estudiante getEstudiante (int id){
	    	 Session session = SessionUtil.getSession();
	    	 String hql = "from Estudiante where id = :id";
	         Query query = session.createQuery(hql);
	         query.setInteger("id",id);
	         Estudiante estudiante = (Estudiante) query.list().get(0);
	         int minutos = this.getMinutosTotales(session,id);
	         estudiante.setMinutosTotales(minutos);
	         session.close();
	         return estudiante;
	    }
	 
	    /**
	     * Método para obtener el listado de estudiantes registrados
	     * @return El listado de estudiantes
	     */
	    public List<Estudiante> getEstudiantes(){
	        Session session = SessionUtil.getSession();    
	        Query query = session.createQuery("from Estudiante");
	        List<Estudiante> estudiantes =  query.list();
	        session.close();
	        return estudiantes;
	    }
	    
	    /**
	     * Método para obtener los minutos totales dedicados a un estudiante
	     * @param session El objeto session
	     * @param idEstudiante El identificador del estudiante
	     * @return El número de minutos totales dedicado a un estudiante
	     */
	    public int getMinutosTotales(Session session, int idEstudiante){
	    	String hql = "select minutos from Reunion where estudiante = :idEstudiante ";
	    	Query query = session.createQuery(hql);
			query.setInteger("idEstudiante",idEstudiante);
			List<Integer> tiempos = query.list();
			int tiempoTotal = 0;
			for (Integer tiempo : tiempos) {
				tiempoTotal += tiempo;
			}
			return tiempoTotal;
	    }
	    
	    /**
	     * Método para obtener el listado de estudiantes pertenecientes a una línea
	     * @param idLinea El identificador de la línea
	     * @return El listado de estudiantes
	     */
	    public List<Estudiante> getEstudiantesLinea(int idLinea){
	    	Session session = SessionUtil.getSession();
	    	String hql = "from Estudiante where linea = :linea";
	    	Query query = session.createQuery(hql);
	    	query.setInteger("linea",idLinea);
	    	List<Estudiante> estudiantes = query.list();
	    	for (Estudiante estudiante : estudiantes) {
				int tiempoTotal = this.getMinutosTotales(session,estudiante.getId());
				estudiante.setMinutosTotales(tiempoTotal);
			}
	    	return estudiantes; 
	    }
	 
	    /**
	     * Método para eliminar un estudiante de base de datos
	     * @param id El identificador del estudiante
	     * @return El número de filas afectadas
	     */
	    public int deleteEstudiante(int id) {
	    	Estudiante estudiante = this.getEstudiante(id);
	    	//Si el estado es entregado se eliminan los ficheros corespondientes
	    	if (estudiante.getEstadoTrabajo() == EstadoTrabajo.ENTREGADO){
	    		String urlInforme = estudiante.getUrlInforme();
	    		if (urlInforme != null){
	    			File informe = new File(urlInforme);
		    		if (informe.exists()){
		    			informe.delete();
		    		}
	    		}
	    		String urlMemoria = estudiante.getUrlMemoriaFinal();
	    		if (urlMemoria != null){
	    			File memFinal = new File(urlMemoria);
		    		if (memFinal.exists()){
		    			memFinal.delete();
		    		}
	    		}
	    	}
	        Session session = SessionUtil.getSession();
	        Transaction tx = session.beginTransaction();
	        String hql = "delete from Estudiante where id = :id";
	        Query query = session.createQuery(hql);
	        query.setInteger("id",id);
	        int rowCount = query.executeUpdate();
	        System.out.println("Rows affected: " + rowCount);
	        tx.commit();
	        session.close();
	        return rowCount;
	    }
	    
	    /**
	     * Método para actualizar un estudiante en base de datos
	     * @param id El identificador del estudiante
	     * @param estudiante Los nuevos datos del estudiante
	     * @return El número de filas afectadas
	     */
	    public int updateEstudiante(int id, Estudiante estudiante){
	         	if(id <=0)  
	               return 0;
	         	Session session = SessionUtil.getSession();
	            Transaction tx = session.beginTransaction();
	            Query query;
	            String hql = "update Estudiante set nombre = :nombre, apellidos= :apellidos, "
	            		+ "urlGit = :urlGit, fechaEstimada = :fechaEstimada, vocales = :vocales "
	            		+ "where id = :id";
	            query = session.createQuery(hql);
	            query.setInteger("id",id);
	            query.setString("nombre",estudiante.getNombre());
		        query.setString("apellidos",estudiante.getApellidos());
		        query.setString("urlGit", estudiante.getUrlGit());
		        query.setDate("fechaEstimada", estudiante.getFechaEstimada());
		        query.setString("vocales", estudiante.getVocales());
 	            //query.setString("urlInforme", estudiante.getUrlInforme());
 	            //query.setString("urlMemoriaFinal", estudiante.getUrlMemoriaFinal());
	            int rowCount = query.executeUpdate();
	            System.out.println("Rows affected: " + rowCount);
	            tx.commit();
	            session.close();
	            return rowCount;
	    }
	    
	    /**
	     * Método para registrar los campos de calificación de un estudiante en base de datos
	     * @param id El identificador del estudiante
	     * @param estudiante Los datos de calificación del estudiante
	     * @return El número de filas afectadas
	     */
	    public int calificarEstudiante(int id, Estudiante estudiante){
	    	if(id <=0)  
	           return 0;
	    	Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String memFinal = estudiante.getMemFinal();
            String informe = estudiante.getInforme();
            Fichero fichero = new Fichero();
            String filePath;
            if (memFinal != null){
            	filePath = fichero.guardarFichero(memFinal);
            	estudiante.setUrlMemoriaFinal(filePath);
            }
            if (informe != null){
            	filePath = fichero.guardarFichero(informe);
            	estudiante.setUrlInforme(filePath);
            }
            String hql = "update Estudiante set vocales = :vocales, urlInforme = :urlInforme, "
            		+ "urlMemoriaFinal = :urlMemoriaFinal, estadoTrabajo = :estadoTrabajo "
            		+ "where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("vocales",estudiante.getVocales());
            query.setString("urlInforme",estudiante.getUrlInforme());
            query.setString("urlMemoriaFinal", estudiante.getUrlMemoriaFinal());
            query.setString("estadoTrabajo",estudiante.getEstadoTrabajo().toString());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
	    }
	    
	    
}

