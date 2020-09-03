package dao;

import java.util.List;

import javax.naming.AuthenticationException;

import org.hibernate.Session;

import modelo.Profesor;

public interface ProfesorDAO {
	
	 public Profesor addProfesor(Profesor bean);
	 public Profesor addProfesor(Session session, Profesor bean);
	 public boolean existeUsuario(String usuario);
	 public Profesor autenticarProfesor(String usuario,String passwd);
	 public List<Profesor> getProfesores();
	 public Profesor getProfesor(int id);
	 public int deleteProfesor(int id);
	 public int updateProfesor(int id, Profesor profesor);

}
