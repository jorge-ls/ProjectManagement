package dao;

import java.util.List;

import org.hibernate.Session;

import modelo.Estudiante;

public interface EstudianteDAO {
	
	 public void addEstudiante(Estudiante bean);
	 public void addEstudiante(Session session, Estudiante bean);
	 public Estudiante getEstudiante (int id);
	 public List<Estudiante> getEstudiantes();
	 public List<Estudiante> getEstudiantesLinea(int idLinea);
	 public int deleteEstudiante(int id);
	 public int updateEstudiante(int id, Estudiante estudiante);
	 public int calificarEstudiante(int id, Estudiante estudiante);

}
