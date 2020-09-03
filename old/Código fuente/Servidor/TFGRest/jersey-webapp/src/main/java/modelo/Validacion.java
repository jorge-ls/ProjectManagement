package modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacion {
	
	private String tituloExp = "^[a-zA-Zñáéíóú]+(\\s[a-zA-Zñáéíóú\\s]+)*$";
	private String expDescripcion = "^[a-zA-Zñáéíóú0-9,\\.\\/]+(\\s[a-zA-Zñáéíóú0-9,\\.\\/\\s]+)*$";
	private String nombreExp = "^[a-zA-Zñáéíóú]+\\s?[a-zA-Zñáéíóú\\s]+$";
	private String apellidosExp = "^[a-zA-Zñáéíóú]+\\s[a-zA-Zñáéíóú\\s]+$";
	private String usuarioExp = "^[a-zA-Z0-9_\\-]+$";
	private String passwordExp = "^[a-zA-Zñ0-9]+$";
	private String expUrlGit = "^https?:\\/\\/[a-zA-Z0-9_@\\.-]+\\/[a-zA-Z0-9_-]+\\/[a-zA-Z_-]+\\.git$";
	//private String expFecha = "^\\d{4}\\-\\d{2}\\-\\d{2}$";
	private String expVocales = "^[a-zA-Zñáéíóú\\n]+(\\s[a-zA-Zñáéíóú\\n\\s]+)*$";
	
	/**
	 * Método para comprobar si un valor coincide con el patrón de una expresión regular
	 * @param value El valor a comprobar
	 * @param regex La expresión regular
	 * @return true en caso de que el valor coincida con el patrón, false en caso contrario
	 */
	public boolean validarRegex(String value,String regex){
		System.out.println("El valor a testear es: "+value);
		System.out.println("El valor de la expresion es: "+regex);
		Pattern pattern = Pattern.compile(regex);
		Matcher mat = pattern.matcher(value);
		if (!mat.matches()){
			System.out.println("No se ha producido un match");
			return false;
		}
		return true;
	}
	
	/**
	 * Método para comprobar si un número es válido
	 * @param number El úmero
	 * @return true en caso de que el número sea válido, false en caso contrario
	 */
	public boolean isValidoNumber(int number){
		if (number <= 0){
			return false;
		}
		return true;
	}
	
	/**
	 * Método para comprobar si una fecha es válida
	 * @param date La fecha a comprobar
	 * @return true en caso de que la fecha sea válida, false en caso contrario
	 */
	public boolean validarFecha(Date date){
		Date fechaActual = new Date();
		if (date == null){
			System.out.println("La fecha es nula");
			return false;
		}
		System.out.println("La fecha a validar es: "+ date.toString());
		if (date.before(fechaActual)){
			System.out.println("La fecha es anterior a la fecha actual");
			return false;
		}
		return true;
	}
	
	/**
	 * Método para comprobar si los datos de un profesor son válidos
	 * @param profesor El profesor
	 * @return true en caso de que los datos sean válidos, false en caso contrario
	 */
	public boolean validarProfesor(Profesor profesor){
		boolean nombreOk = validarRegex(profesor.getNombre(),nombreExp);
		System.out.println("Nombre: "+nombreOk);
		boolean apellidosOk = validarRegex(profesor.getApellidos(),apellidosExp);
		System.out.println("Apellidos: "+apellidosOk);
		boolean usuarioOk = validarRegex(profesor.getUsuario(),usuarioExp);
		System.out.println("Usuario: "+usuarioOk);
		boolean contraseñaOk = validarRegex(profesor.getContraseña(),passwordExp);
		System.out.println("Contraseña: "+contraseñaOk);
		if (nombreOk && apellidosOk && usuarioOk && contraseñaOk){
			return true;
		}
		return false;
	}
	
	/**
	 * Método para  comprobar si los datos de una línea son válidos
	 * @param linea Los datos de la línea
	 * @return true en caso de que los datos sean válidos, false en caso contrario
	 */
	public boolean validarLinea(Linea linea){
		boolean tituloOk = validarRegex(linea.getTitulo(),tituloExp);
		System.out.println("Titulo: "+ tituloOk);
		boolean descripcionOk = validarRegex(linea.getDescripcion(),expDescripcion);
		System.out.println("Descripcion: "+ descripcionOk);
		boolean numEstudiantesOk = isValidoNumber(linea.getMaxEstudiantes());
		System.out.println("max estudiantes: "+ numEstudiantesOk);
		boolean tipoLineaOk = false;
		String tipo = linea.getTipoLinea().toString();
		if (tipo.equals("PROPUESTA_PROFESOR") || tipo.equals("PRACTICA_EMPRESA")
				|| tipo.equals("ACUERDO_ESTUDIANTE")){
			tipoLineaOk = true;
		}
		System.out.println("Tipo de linea: "+ tipoLineaOk);
		if (tituloOk && descripcionOk && numEstudiantesOk && tipoLineaOk){
			return true;
		}
		return false;
	}
	
	/**
	 * Método para comprobar si los datos de un estudiante son válidos
	 * @param estudiante Los datos del estudiante
	 * @return true en caso de que los datos sean válidos, false en caso contrario
	 */
	public boolean validarEstudiante(Estudiante estudiante){
		boolean vocalesOk = true;
		boolean nombreOk = validarRegex(estudiante.getNombre(),nombreExp);
		boolean apellidosOk = validarRegex(estudiante.getApellidos(),apellidosExp);
		boolean urlGitOk = validarRegex(estudiante.getUrlGit(),expUrlGit);
		boolean fechaOk = validarFecha(estudiante.getFechaEstimada());
		EstadoTrabajo estado = estudiante.getEstadoTrabajo();
		if (estado != null){
			if (estudiante.getEstadoTrabajo().toString().equals("ENTREGADO")){
				vocalesOk = validarRegex(estudiante.getVocales(),expVocales);
			}
		}
		if (nombreOk && apellidosOk && urlGitOk && fechaOk && vocalesOk){
			return true;
		}
		return false;
	}
	
	/**
	 * Método para comprobar si los datos de una reunión son válidos
	 * @param reunion Loa datos de la reunión
	 * @return true en caso de que los datos sean válidos, false en caso contrario
	 */
	public boolean validarReunion(Reunion reunion){
		boolean tituloOk = validarRegex(reunion.getTitulo(),tituloExp);
		boolean tipoReunionOk = false;
		String tipo = reunion.getTipoReunion().toString();
		if (tipo.equals("PRESENCIAL") || tipo.equals("CORREO") || tipo.equals("SKYPE") 
				|| tipo.equals("OTROS")){
			tipoReunionOk = true;
		}
		boolean duracionOk = isValidoNumber(reunion.getMinutos());
		boolean fechaOk = validarFecha(reunion.getFecha());
		boolean comentarioOk = false;
		if (reunion.getComentario().equals("") || validarRegex(reunion.getComentario(),expDescripcion)){
			comentarioOk = true;
		}
		if (tituloOk && tipoReunionOk && duracionOk && fechaOk && comentarioOk){
			return true;
		}
		return false;
	}

}
