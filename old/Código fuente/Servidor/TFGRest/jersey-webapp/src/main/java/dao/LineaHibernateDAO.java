package dao;
 
import java.util.List;
 
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import modelo.Linea;
import modelo.Profesor;
 
public class LineaHibernateDAO implements LineaDAO {
    
	/**
	 * Registro de una linea en base de datos
	 * @param idProfesor El identificador del profesor
	 * @param bean La línea a registrar
	 * @return true en caso de que el registro se realice con éxito, false en caso contrario
	 */
    public boolean addLinea(int idProfesor,Linea bean){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        if (!existeLinea(idProfesor,bean.getTitulo())){
        	addLinea(session,bean);
        	tx.commit();
        	return true;
        }
        session.close();
        return false;
    }
    
    /**
     * Registro de una linea en base de datos
     * @param session El objeto session
	 * @param bean La línea a registrar
     */
    public void addLinea(Session session, Linea bean){
        Linea linea = new Linea();
        linea.setTitulo(bean.getTitulo());
        linea.setDescripcion(bean.getDescripcion());
        linea.setProfesor(bean.getProfesor());
        linea.setTipoLinea(bean.getTipoLinea());
        linea.setMaxEstudiantes(bean.getMaxEstudiantes());
        session.save(linea);
    }
    
    /**
     * Método para obtener todas las lineas registradas en base de datos
     * @return El listado de lineas registradas
     */
    public List<Linea> getLineas(){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Linea");
        List<Linea> lineas = query.list();
        session.close();
        return lineas;
    }
    
    /**
     * Método que obtiene de base de datos el listado de líneas pertenecientes a un profesor
     * @param El identificador del profesor
     * @return El listado de líneas pertenecientes al profesor
     */
    public List<Linea> getLineasProfesor(int idProfesor){
    	 Session session = SessionUtil.getSession();
    	 String hql = "from Linea where profesor = :profesor";
    	 Query query = session.createQuery(hql);
    	 query.setInteger("profesor",idProfesor);
    	 List<Linea> lineas = query.list();
    	 for (Linea linea : lineas) {
    		 Long numEstudiantes = this.getNumEstudiantesLinea(session,linea.getId());
    		 linea.setNumEstudiantes(numEstudiantes);
		}
    	 return lineas; 
    }
    
    /**
     * Método para obtener el número de estudiantes que pertenecen a una línea
     * @param session El objeto session
     * @param idLinea El identificador de la línea
     * @return El número de estudiantes pertenecientes a la línea
     */
    public Long getNumEstudiantesLinea(Session session,int idLinea){
    	String hql = "select count(*) from Estudiante where linea = :idLinea ";
        Query query2 = session.createQuery(hql);
    	query2.setInteger("idLinea",idLinea);
    	Long numEstudiantes = (Long)query2.uniqueResult();
    	return numEstudiantes;
    	
    }
    
    /**
     * Método para comprobar si existe una línea con un título concreto
     * @param idProfesor El identificador del profesor
     * @param titulo El título de la línea
     * @return true en caso de que la línea este registrada, false en caso contrario
     */
    public boolean existeLinea(int idProfesor,String titulo){
    	List<Linea> lineas = this.getLineasProfesor(idProfesor);
    	for (Linea linea : lineas) {
			if (linea.getTitulo().equals(titulo)){
				return true;
			}
		}
    	return false;
    }
    
    /**
     * Método para obtener los datos de una línea de base de datos
     * @param id El identificador de la línea
     * @return La línea solicitada
     */
    public Linea getLinea (int idLinea){
    	 Session session = SessionUtil.getSession();
    	 String hql = "from Linea where id = :idLinea";
         Query query = session.createQuery(hql);
         query.setInteger("idLinea",idLinea);
         Linea linea = (Linea) query.list().get(0);
         if (linea != null){
        	 Long numEstudiantes = this.getNumEstudiantesLinea(session,idLinea);
             linea.setNumEstudiantes(numEstudiantes);
         }
         session.close();
         return linea;
         
         
    }
 
    /**
     * Método para eliminar una línea de base de datos
     * @param idLinea El identificador de la línea
     * @return El número de filas modificadas
     */
    public int deleteLinea(int idLinea) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Linea where id = :idLinea";
        Query query = session.createQuery(hql);
        query.setInteger("idLinea",idLinea);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    
    /**
     * Método para actualizar una línea en base de datos
     * @param idLinea El identificador de la línea
     * @param linea La línea con los nuevos datos
     */
    public int updateLinea(int idLinea, Linea linea){
         if(idLinea <=0)  
           return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Linea set titulo = :titulo, descripcion = :descripcion, "
            		+ "maxEstudiantes = :maxEstudiantes, tipoLinea = :tipoLinea where id = :idLinea";
            Query query = session.createQuery(hql);
            query.setInteger("idLinea",idLinea);
            query.setString("titulo",linea.getTitulo());
            query.setString("descripcion",linea.getDescripcion());
            query.setInteger("maxEstudiantes", linea.getMaxEstudiantes());
            query.setString("tipoLinea", linea.getTipoLinea().toString());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
    }
    
   
}
