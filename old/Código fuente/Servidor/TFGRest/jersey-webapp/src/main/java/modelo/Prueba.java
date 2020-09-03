package modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.EstudianteDAO;
import dao.EstudianteHibernateDAO;
import dao.FactoriaDAO;
import dao.LineaDAO;
import dao.LineaHibernateDAO;
import dao.ProfesorDAO;
import dao.ProfesorHibernateDAO;
import dao.ReunionDAO;
import dao.ReunionHibernateDAO;
import dao.SessionUtil;

public class Prueba {
	
	public static void main(String[] args) {
		
		/*Profesor profesor = new Profesor();
		profesor.setNombre("Jorge");
		profesor.setApellidos("Lopez Saura");
		profesor.setUsuario("jorge-ls");
		profesor.setContraseña("143434");
		
		Profesor profesor1 = new Profesor();
		profesor1.setNombre("Jose");
		profesor1.setApellidos("Lopez Hernandez");
		profesor1.setUsuario("jose-lh");
		profesor1.setContraseña("15446");
		
		ProfesorDAO profesorDAO = FactoriaDAO.getInstancia(FactoriaDAO.HIBERNATE).getProfesorDAO();
		profesorDAO.addProfesor(profesor);
		profesorDAO.addProfesor(profesor1);
		
		List<Profesor> profesores = profesorDAO.getProfesores();
		for (Profesor profesor2 : profesores) {
			System.out.println("Profesor");
			System.out.println();
			System.out.println("Id: "+profesor2.getId());
			System.out.println("Nombre: "+profesor2.getNombre());
			System.out.println("Apellidos: "+profesor2.getApellidos());
			System.out.println("Usuario: "+profesor2.getUsuario());
			System.out.println("Contraseña: "+profesor2.getContraseña());
			System.out.println();
		}
		
	    Profesor prof = profesores.get(1);
		prof.setNombre("Paco");
		prof.setApellidos("Martinez");
		prof.setUsuario("pacom");
		prof.setContraseña("656767");
		
		LineaDAO lineaDAO = FactoriaDAO.getInstancia(FactoriaDAO.HIBERNATE).getLineaDAO();
		Linea linea = new Linea();
		linea.setTitulo("Aplicacion web responsive");
		linea.setDescripcion("En este tfg se desarrollara una app responsive...");
		linea.setTipoLinea(TipoLinea.ACUERDO_ESTUDIANTE);
		linea.setProfesor(prof);
		//lineaDAO.addLinea(linea);
		List<Linea> lineas = lineaDAO.getLineas();
		
		for (Linea linea2 : lineas) {
			System.out.println("Linea: ");
			System.out.println("Titulo: "+linea2.getTitulo());
			System.out.println("Descripcion: "+linea2.getDescripcion());
			System.out.println("Tipo de la linea: "+linea2.getTipoLinea());
		}
		
		Linea linea1 = lineaDAO.getLinea(lineas.get(0).getId());
		System.out.println("Se obtiene una linea por su id....");
		System.out.println("Id: "+linea1.getId());
		System.out.println("Titulo: "+linea1.getTitulo());
		System.out.println("Descripcion: "+linea1.getDescripcion());
		System.out.println("Tipo de la linea: "+linea1.getTipoLinea());
		
		//lineaDAO.deleteLinea(2);
		//lineaDAO.deleteLinea(3);
		
		linea1.setTitulo("Aplicacion web resonsive");
		linea1.setTipoLinea(TipoLinea.PROPUESTA_PROFESOR);
		lineaDAO.updateLinea(linea1.getId(), linea1);
		
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(2020,4,6);
		Date fechaEstimada = calendar.getTime();
		System.out.println("fechaEstimada "+fechaEstimada.toString());
		Estudiante estudiante = new Estudiante();
		estudiante.setNombre("Jorge");
		estudiante.setApellidos("Lopez Saura");
		estudiante.setUrlGit("https://www.git.com/tfg.git");
		estudiante.setFechaEstimada(fechaEstimada);
		estudiante.setEstadoTrabajo(EstadoTrabajo.PENDIENTE);
		estudiante.setLinea(linea1);
		
		EstudianteDAO estudianteDAO = FactoriaDAO.getInstancia(FactoriaDAO.HIBERNATE).getEstudianteDAO();
		estudianteDAO.addEstudiante(estudiante);
		estudiante.setEstadoTrabajo(EstadoTrabajo.ENTREGADO);
		List<Estudiante> estudiantes = estudianteDAO.getEstudiantes();
		Estudiante estudiante1 = estudianteDAO.getEstudiante(estudiantes.get(0).getId());
		estudianteDAO.updateEstudiante(estudiante1.getId(),estudiante1);
		
		System.out.println("Datos del estudiante: ");
		System.out.println(estudiante1.toString());
		
		ReunionDAO reunionDAO = FactoriaDAO.getInstancia(FactoriaDAO.HIBERNATE).getReunionDAO();
		Reunion reunion = new Reunion();
		reunion.setFecha(fechaEstimada);
		reunion.setMinutos(60);
		reunion.setEstudiante(estudiante1);
		reunion.setTipoReunion(TipoReunion.PRESENCIAL);
		reunion.setComentario("Comentario de preuba");
		reunion.setUrlMemoria("www.example-tfg.git");
		//reunionDAO.addReunion(reunion);*/
		
		Profesor profesor = new Profesor();
		profesor.setNombre("Jorge");
		profesor.setApellidos("Lopez Saura");
		profesor.setUsuario("jorge-ls");
		profesor.setContraseña("1gfdg454bbgf");
		
		Linea linea = new Linea();
		linea.setTitulo("Aplicacion web responsive para gestion de eventos");
		linea.setDescripcion("En esta linea de TFG 543, se va hacer...");
		linea.setMaxEstudiantes(5);
		linea.setTipoLinea(TipoLinea.PRACTICA_EMPRESA);
		
		Estudiante estudiante = new Estudiante();
		estudiante.setNombre("Jorge");
		estudiante.setApellidos("Lopez Saura");
		estudiante.setUrlGit("https://jorge-ls94@bitbucket.org/fgs22002/tfg_jorgelopez.git");
		estudiante.setEstadoTrabajo(EstadoTrabajo.ENTREGADO);
		estudiante.setVocales("Primer vocal Segundo vocal Tercer vocal");
		String strDate = "2020-06-15";
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		Date fecha = null;
		try {
			fecha = formatoFecha.parse(strDate);
			System.out.println("Fecha: "+fecha.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		estudiante.setFechaEstimada(fecha);
		
		Reunion reunion = new Reunion();
		reunion.setTitulo("Nueva reunion");
		reunion.setTipoReunion(TipoReunion.PRESENCIAL);
		reunion.setMinutos(60);
		reunion.setComentario("En esta reunion, se han aclarado varios conceptos...");
		reunion.setFecha(fecha);
		
		Validacion validacion = new Validacion();
		boolean res = validacion.validarProfesor(profesor);
		System.out.println("¿Es valido el profesor? "+res);
		
		res = validacion.validarLinea(linea);
		System.out.println("¿Linea valida? "+res);
		
		res = validacion.validarEstudiante(estudiante);
		System.out.println("¿Es valido el estudiante? "+res);
		
		res = validacion.validarReunion(reunion);
		System.out.println("¿Es válida la reunion? "+res);
		
	}

}
