package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import javax.xml.bind.DatatypeConverter;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import modelo.Fichero;
import modelo.Linea;
import modelo.Reunion;

public class ReunionHibernateDAO implements ReunionDAO {
	
	/**
	 * Método para registar una reunión en base de datos
	 * @param bean La reunión a registrar
	 */
	public void addReunion(Reunion bean){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        addReunion(session,bean);        
        tx.commit();
        session.close();
        
    }
    
	/**
	 * Método para registar una reunión en base de datos
	 * @param session El objeto session
	 * @param bean La reunión a registrar
	 */
    public void addReunion(Session session, Reunion bean){
        Reunion reunion = new Reunion();
        Fichero fichero = new Fichero();
        String filePath;
        reunion.setTitulo(bean.getTitulo());
        reunion.setTipoReunion(bean.getTipoReunion());
        reunion.setFecha(bean.getFecha());
        //System.out.println("Fecha: "+bean.getFecha());
        reunion.setMinutos(bean.getMinutos());
        String memoria = bean.getMemoria();
        reunion.setMemoria(memoria);
        if (!bean.getComentario().equals("")){
        	reunion.setComentario(bean.getComentario());
        }
        reunion.setEstudiante(bean.getEstudiante());
        
        if (memoria != null) {
        	filePath = fichero.guardarFichero(memoria);
        	reunion.setUrlMemoria(filePath);
        }
       
        session.save(reunion);
    }
    
    /**
     * Método para obtener los datos de una reunión
     * @param id El identificador de la reunión
     * @return Los datos de la reunión solicitada
     */
    public Reunion getReunion (int id){
	   	 Session session = SessionUtil.getSession();
	   	 String hql = "from Reunion where id = :id";
	     Query query = session.createQuery(hql);
	     query.setInteger("id",id);
	     Reunion reunion = (Reunion) query.list().get(0);
	     session.close();
	     return reunion;
   }
    
    /**
     * Método para obtener el listado de reuniones registradas
     * @return El listado de reuniones
     */
    public List<Reunion> getReuniones(){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Reunion");
        List<Reunion> reuniones = query.list();
        session.close();
        return reuniones;
    }
    
    /**
     * Método para obtener el listado de reuniones pertenecientes a un estudiante
     * @param idEstudiante El identificador del estudiante
     * @return El listado de reuniones 
     */
    public List<Reunion> getReunionesEstudiante(int idEstudiante){
    	 Session session = SessionUtil.getSession();
    	 String hql = "from Reunion where estudiante = :estudiante";
    	 Query query = session.createQuery(hql);
    	 query.setInteger("estudiante",idEstudiante);
    	 List<Reunion> reuniones = query.list();
    	 return reuniones; 
    }
    
    /**
     * Método para eliminar una reunion de base de datos
     * @param id El identificador de la reunión
     * @return El número de filas afectadas
     */
    public int deleteReunion(int id) {
    	 Reunion reunion = this.getReunion(id);
    	 String urlMemoria = reunion.getUrlMemoria();
         if (urlMemoria != null){
         	File file = new File(urlMemoria);
         	if (file.exists()){
         		file.delete();
         	}
        }
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Reunion where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    
    /**
     * Método para actualizar una reunion de base de datos
     * @param id El identificador de la reunión
     * @return El número de filas afectadas
     */
    public int updateReunion(int id, Reunion reunion){
         if(id <=0)  
           return 0;  
         Session session = SessionUtil.getSession();
         Transaction tx = session.beginTransaction();
         String hql = "update Reunion set titulo = :titulo, tipoReunion = :tipoReunion, fecha = :fecha, "
            + "minutos = :minutos, comentario = :comentario "
            + "where id = :id";
         Query query = session.createQuery(hql);
         query.setInteger("id",id);
         query.setString("titulo", reunion.getTitulo());
         query.setString("tipoReunion",reunion.getTipoReunion().toString());
         query.setDate("fecha",reunion.getFecha());
         query.setInteger("minutos",reunion.getMinutos());
         //query.setString("urlMemoria", reunion.getUrlMemoria());
         query.setString("comentario", reunion.getComentario());
         int rowCount = query.executeUpdate();
         System.out.println("Rows affected: " + rowCount);
         tx.commit();
         session.close();
         return rowCount;
    }

}
