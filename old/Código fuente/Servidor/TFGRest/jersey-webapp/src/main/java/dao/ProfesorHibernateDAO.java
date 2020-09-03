package dao;
 
import java.util.List;

import javax.naming.AuthenticationException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import modelo.Linea;
import modelo.Profesor;
 
public class ProfesorHibernateDAO implements ProfesorDAO {
    
	/**
	 * Método para registrar un profesor en base de datos
	 * @param bean El profesor
	 * @return El profesor en caso de que el registro se realice con éxito, null en caso contrario
	 */
    public Profesor addProfesor(Profesor bean){
    	Profesor profesor = null;
        if (!existeUsuario(bean.getUsuario())){
        	Session session = SessionUtil.getSession();        
            Transaction tx = session.beginTransaction();
        	profesor = addProfesor(session,bean);        
            tx.commit();
            session.close();
        }
        return profesor;
        
    }
    
    /**
     * Método para registrar un profesor en base de datos
     * @param session El objeto session
     * @param bean El profesor
     * @return Los datos del profesor registrado
     */
    public Profesor addProfesor(Session session, Profesor bean){
        Profesor profesor = new Profesor();
        
        profesor.setNombre(bean.getNombre());
        profesor.setApellidos(bean.getApellidos());
        profesor.setUsuario(bean.getUsuario());
        profesor.setContraseña(bean.getContraseña());
        
        session.save(profesor);
        return profesor;
    }
    
    /**
     * Método para comprobar si existe un profesor registrado con un nombre de usuario concreto
     * @param usuario El nombre de usuario
     * @return true en caso de que exista un profesor con ese nombre de usuario, false en caso contrario 
     */
    public boolean existeUsuario(String usuario){
    	List<Profesor> profesores = this.getProfesores();
    	for (Profesor profesor : profesores) {
			if (profesor.getUsuario().equals(usuario)){
				return true;
			}
		}
    	return false;
    }
    
    /**
     * Método que comprueba si un profesor esta registrado
     * @param usuario El nombre de usuario
     * @param passwd La contraseña del profesor
     * @return Los datos del profesor en caso de estar ya registrado, null en caso contrario
     */
    public Profesor autenticarProfesor(String usuario,String passwd) {
    	List<Profesor> profesores = this.getProfesores();
    	for (Profesor profesor : profesores) {
			if (profesor.getUsuario().equals(usuario) && profesor.getContraseña().equals(passwd)){
				return profesor;
			}
		}
    	return null;
    }
    
    /**
     * Método para obtener un listado de los profesores registrados
     * @return El listado de profesores
     */
    public List<Profesor> getProfesores(){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Profesor");
        List<Profesor> profesores = query.list();
        session.close();
        return profesores;
    }
    
    /**
     * Método para obtener los datos de un profesor
     * @param id El identificador del profesor
     * @return Los datos del profesor
     */
    public Profesor getProfesor(int id){
   	 	Session session = SessionUtil.getSession();
   	 	String hql = "from Profesor where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        Profesor profesor = (Profesor) query.list().get(0);
        //Profesor profesor = (Profesor) session.get(Profesor.class, id);
        session.close();
        return profesor;
    }
    
    /**
     * Método para eliminar un profesor de base de datos
     * @param id El identificador del profesor
     * @return El número de filas afectadas
     */
    public int deleteProfesor(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Profesor where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    
    /**
     * Método para actualizar un profesor en base de datos
     * @param id El identificador del profesor
     * @param Los nuevos datos del profesor
     * @return El número de filas afectadas
     */
    public int updateProfesor(int id, Profesor profesor){
         if(id <=0)  
           return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Profesor set nombre = :nombre, apellidos = :apellidos, "
            		+ "usuario = :usuario, contraseña = :contraseña "
            		+ "where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("nombre",profesor.getNombre());
            query.setString("apellidos",profesor.getApellidos());
            query.setString("usuario", profesor.getUsuario());
            query.setString("contraseña", profesor.getContraseña());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
    }
}
