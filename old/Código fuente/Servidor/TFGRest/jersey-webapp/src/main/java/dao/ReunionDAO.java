package dao;

import java.util.List;

import org.hibernate.Session;

import modelo.Reunion;

public interface ReunionDAO {
	
	public void addReunion(Reunion bean);
	 public void addReunion(Session session, Reunion bean);
	 public Reunion getReunion (int id);
	 public List<Reunion> getReuniones();
	 public int deleteReunion(int id);
	 public int updateReunion(int id, Reunion reunion);
	 public List<Reunion> getReunionesEstudiante(int idEstudiante);

}
