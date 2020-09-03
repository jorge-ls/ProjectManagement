package dao;

import java.util.List;

import org.hibernate.Session;

import modelo.Linea;

public interface LineaDAO {
	
	 public boolean addLinea(int id,Linea bean);
	 public void addLinea(Session session, Linea bean);
	 public List<Linea> getLineas();
	 public List<Linea> getLineasProfesor(int idProfesor);
	 public Long getNumEstudiantesLinea(Session session,int idLinea);
	 public Linea getLinea (int idLinea);
	 public int deleteLinea(int idLinea);
	 public int updateLinea(int idLinea,Linea linea);
	 public boolean existeLinea(int id,String titulo);
	 //public boolean hasAccessLinea(int idProfesor,Linea linea);
	 //public boolean hasAccessLinea(int idProfesor,int idLinea);
}
