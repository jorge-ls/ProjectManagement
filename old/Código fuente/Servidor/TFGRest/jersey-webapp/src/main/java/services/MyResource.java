package services;
 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
 
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import dao.EstudianteDAO;
import dao.FactoriaDAO;
import dao.LineaDAO;
import dao.ProfesorDAO;
import dao.ReunionDAO;
import modelo.Credenciales;
import modelo.EstadoTrabajo;
import modelo.Estudiante;
import modelo.Fichero;
import modelo.Linea;
import modelo.Profesor;
import modelo.Reunion;
import modelo.Validacion;
import java.security.Key;
 

@Path("/profesores")
public class MyResource {
	
	private FactoriaDAO factoria = FactoriaDAO.getInstancia(FactoriaDAO.HIBERNATE);
	private Validacion validacion = new Validacion();
	public static Key clave;
 
	/**
	 * Método para realizar el inicio de sesión
	 * @param credenciales Usuario y contraseña del profesor
	 * @return Respuesta HTTP 200 OK con el token de verificación en caso de éxito, una respuesta HTTP 401
	 * 		   Unauthorized en caso contrario
	 */
	
	@POST
	@Path("/login")
	@Consumes("application/json")
	@Produces(MediaType.TEXT_PLAIN)
	public Response login(Credenciales credenciales){
		String usuario = credenciales.getUsuario();
		String contraseña = credenciales.getContraseña(); 
		ProfesorDAO profesorDAO = factoria.getProfesorDAO();
		String token = "";
		try {
			clave = MacProvider.generateKey();
			Profesor profesor = profesorDAO.autenticarProfesor(usuario,contraseña);
			token = Jwts.builder()
	        		.claim("idProfesor",profesor.getId())
	        		.signWith(SignatureAlgorithm.HS512,clave)
	        		.compact();
			
		} catch (Exception e) {
			System.out.println("El usuario no esta autorizado");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
        // Devolvemos el token en la cabecera "Authorization". 
        return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).entity(token).build();
	}
	
	/**
	 * Método para realizar el registro de un profesor en la aplicación
	 * @param profesor El profesor que se va a registrar
	 * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario una respuesta HTTP 400 Bad Request
	 */
	
	@POST
    @Consumes("application/json")
	@Produces("application/json")
    public Response addProfesor(Profesor profesor){
		if (validacion.validarProfesor(profesor)){
			ProfesorDAO profesorDAO = factoria.getProfesorDAO();
	        Profesor profesor1 = profesorDAO.addProfesor(profesor);
	        if (profesor1 != null){
	        	return Response.status(Status.OK).entity(profesor1).build();
	        }
		}
		return Response.status(Status.BAD_REQUEST).build();
    }
	
	/**
	 * Método para obtención de datos de un profesor 
	 * @param id El identidicador del profesor
	 * @return Respuesta HTTP 200 OK con los datos del profesor
	 */
	
    @GET
    @Secured
    @Path("/{idProfesor}")
    @Produces("application/json")
    public Response getProfesor(@PathParam("idProfesor") int id) {
        ProfesorDAO profesorDAO = factoria.getProfesorDAO();
        Profesor profesor = profesorDAO.getProfesor(id);
        return Response.status(Status.OK).entity(profesor).build();
    }
    
    /**
     * Método para registrar una línea en la aplicación
     * @param idProfesor El identificador del profesor que es tutor de la línea
     * @param linea La línea a registrar
     * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario una respuesta HTTP 400 Bad Request
     */
    
    @POST
    @Secured
    @Path("/{idProfesor}/lineas")
    @Consumes("application/json")
    public Response addLinea(@PathParam("idProfesor") int idProfesor,Linea linea) {
    	if (validacion.validarLinea(linea)){
    		LineaDAO lineaDAO = factoria.getLineaDAO();
        	ProfesorDAO profesorDAO = factoria.getProfesorDAO();
        	Profesor profesor = profesorDAO.getProfesor(idProfesor);
        	linea.setProfesor(profesor);
        	boolean insertado = lineaDAO.addLinea(idProfesor,linea);
        	if (insertado){
        		return Response.ok().build();
        	}
    	}
    	return Response.status(Status.BAD_REQUEST).build();	
    }
    
    /**
     * Método para obtener los datos de una línea
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea solicitada
     * @return Respuesta HTTP 200 OK con los datos de la línea 
     */
    
    @GET
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}")
    @Produces("application/json")
    public Response getLinea(@PathParam("idProfesor") int idProfesor,
    							@PathParam("idLinea") int idLinea){
    	LineaDAO lineaDAO = factoria.getLineaDAO();
    	Linea linea = lineaDAO.getLinea(idLinea);
    	if (!this.hasAccessLinea(idProfesor, linea)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	return Response.status(Status.OK).entity(linea).build();
    }
    
    /**
     * Método para obtener las líneas pertenecientes a un profesor
     * @param idProfesor El identificador del profesor
     * @return Una respuesta HTTP 200 OK con el listado de las líneas 
     */
    
    @GET
    @Secured
    @Path("/{idProfesor}/lineas")
    @Produces("application/json")
    public Response getLineasProfesor(@PathParam("idProfesor") int idProfesor){
    	LineaDAO lineaDAO = factoria.getLineaDAO();
    	List<Linea> lineas = lineaDAO.getLineasProfesor(idProfesor);
    	return Response.status(Status.OK).entity(lineas).build();
    }
    
    /**
     * Método para la actualización de una línea
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea a actualizar 
     * @param linea Los nuevos datos para actualizar 
     * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario respuesta HTTP 400 Bad Request
     */
    
    @PUT
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}")
    @Consumes("application/json")
    public Response updateLinea(@PathParam("idProfesor") int idProfesor,
    							@PathParam("idLinea") int idLinea,Linea linea){
    	if (validacion.validarLinea(linea)){
    		LineaDAO lineaDAO = factoria.getLineaDAO();
    		if (!this.hasAccessLinea(idProfesor,idLinea)){
        		return Response.status(Response.Status.UNAUTHORIZED).build();
        	}
            int count = lineaDAO.updateLinea(idLinea,linea);
            if(count==0){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok().build();
    	}
    	return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    /**
     * Método para eliminar una línea
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea a eliminar
     * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario respuesta HTTP 400 Bad Request
     */
    
    @DELETE
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}")
    public Response deleteLinea(@PathParam("idLinea") int idLinea,
    							@PathParam("idProfesor") int idProfesor){
        LineaDAO lineaDAO = factoria.getLineaDAO();
        if (!this.hasAccessLinea(idProfesor, idLinea)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
        EstudianteDAO estudianteDAO = factoria.getEstudianteDAO();
        ReunionDAO reunionDAO = factoria.getReunionDAO();
        List<Estudiante> estudiantes = estudianteDAO.getEstudiantesLinea(idLinea);
        for (Estudiante estudiante : estudiantes) {
        	List<Reunion> reuniones = reunionDAO.getReunionesEstudiante(estudiante.getId());
        	for (Reunion reunion : reuniones) {
				reunionDAO.deleteReunion(reunion.getId());
			}
			estudianteDAO.deleteEstudiante(estudiante.getId());
		}
        int count = lineaDAO.deleteLinea(idLinea);
        if(count==0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
    
    /**
     * Método para registrar un estudiante
     * @param idProfesor El identificador del profesor
     * @param idLinea Identificador de la línea
     * @param estudiante El estudiante a registrar
     * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario respuesta HTTP 400 Bad Request
     */
    
    @POST
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes")
    @Consumes("application/json")
    public Response addEstudiante(@PathParam("idProfesor") int idProfesor,
    							  @PathParam("idLinea") int idLinea, Estudiante estudiante){
    	if (!validacion.validarEstudiante(estudiante)){
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	}
        LineaDAO lineaDAO = factoria.getLineaDAO();
        EstudianteDAO estudianteDAO = factoria.getEstudianteDAO();
        Linea linea = lineaDAO.getLinea(idLinea);
        if (!this.hasAccessLinea(idProfesor, linea)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
        if (linea.getNumEstudiantes() < linea.getMaxEstudiantes()){
        	estudiante.setLinea(linea);
            estudianteDAO.addEstudiante(estudiante);
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    /**
     * Método para obtener el listado de estudiantes pertenecientes a una línea
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @return Respuesta HTTP 200 OK con el listado de estudiantes
     */
    
    @GET
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes")
    @Produces("application/json")
    public Response getEstudiantesLinea(@PathParam("idProfesor") int idProfesor,
    									@PathParam("idLinea") int idLinea){
    	if (!this.hasAccessLinea(idProfesor, idLinea)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	EstudianteDAO estudianteDAO = factoria.getEstudianteDAO();
    	List<Estudiante> estudiantes = estudianteDAO.getEstudiantesLinea(idLinea);
    	return Response.status(Status.OK).entity(estudiantes).build();
    }
    
    /**
     * Método para obtener los datos de un estudiante
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante
     * @return Respuesta HTTP 200 OK con los datos del estudiante solicitado.
     */
    
    @GET
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes/{idEstudiante}")
    @Produces("application/json")
    public Response getEstudiante(@PathParam("idProfesor") int idProfesor,
    							  @PathParam("idLinea") int idLinea,
    							  @PathParam("idEstudiante") int idEstudiante){
    	EstudianteDAO estudianteDAO = factoria.getEstudianteDAO();
    	Estudiante estudiante = estudianteDAO.getEstudiante(idEstudiante);
    	if (!this.hasAccessEstudiante(idProfesor, idLinea, estudiante)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	return Response.status(Status.OK).entity(estudiante).build();
    }
    
    /**
     * Método para actualizar los datos de un estudiante
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante a actualizar
     * @param estudiante Los nuevos datos del estudiante
     * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario respuesta HTTP 400 Bad Request
     */
    
    @PUT
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes/{idEstudiante}")
    @Consumes("application/json")
    public Response updateEstudiante(@PathParam("idProfesor") int idProfesor,
			  						 @PathParam("idLinea") int idLinea,
			  						 @PathParam("idEstudiante") int idEstudiante, Estudiante estudiante){
    	if (!validacion.validarEstudiante(estudiante)){
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	}
    	if (!this.hasAccessEstudiante(idProfesor,idLinea,idEstudiante)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	EstudianteDAO estudianteDAO = factoria.getEstudianteDAO();
        int count = estudianteDAO.updateEstudiante(idEstudiante,estudiante);
        if(count==0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
    
    /**
     * Método para eliminar un estudiante
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante a eliminar
     * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario respuesta HTTP 400 Bad Request
     */
    
    @DELETE
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes/{idEstudiante}")
    public Response deleteEstudiante(@PathParam("idProfesor") int idProfesor,
				 					 @PathParam("idLinea") int idLinea,
				 					 @PathParam("idEstudiante") int idEstudiante){
    	if (!this.hasAccessEstudiante(idProfesor, idLinea, idEstudiante)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	EstudianteDAO estudianteDAO = factoria.getEstudianteDAO();
    	ReunionDAO reunionDAO = factoria.getReunionDAO();
    	List<Reunion> reuniones = reunionDAO.getReunionesEstudiante(idEstudiante);
    	for (Reunion reunion : reuniones) {
			reunionDAO.deleteReunion(reunion.getId());
		}
        int count = estudianteDAO.deleteEstudiante(idEstudiante);
        if(count==0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
    
    /**
     * Método para realizar la calificación de un estudiante
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante
     * @param estudiante Los datos de calificación del estudiante
     * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario respuesta HTTP 400 Bad Request
     */
    
    @PUT
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes/{idEstudiante}/calificacion")
    @Consumes("application/json")
    public Response calificarEstudiante(@PathParam("idProfesor") int idProfesor,
    									@PathParam("idLinea") int idLinea,
    									@PathParam("idEstudiante") int idEstudiante,Estudiante estudiante){
    	if (!this.hasAccessEstudiante(idProfesor, idLinea, idEstudiante)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	EstudianteDAO estudianteDAO = factoria.getEstudianteDAO();
        estudiante.setEstadoTrabajo(EstadoTrabajo.ENTREGADO);
        int count = estudianteDAO.calificarEstudiante(idEstudiante,estudiante);
        if(count==0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
    
    /**
     * Método para realizar la descarga de un fichero
     * @param nameFile El nombre del fichero
     * @return Respuesta HTTP 200 OK con los datos del fichero en formato binario
     */ 
    
    @GET
    @Secured
    @Path("/{idProfesor}/fichero")
    @Produces (MediaType.APPLICATION_OCTET_STREAM)
    public Response getFichero(@QueryParam("fileName") String nameFile){
    	final String fileName = nameFile;
    	StreamingOutput fileStream =  new StreamingOutput() 
        {
            @Override
            public void write(java.io.OutputStream output) throws IOException, WebApplicationException 
            {
                try
                {
                    java.nio.file.Path path = Paths.get(fileName);
                    byte[] data = Files.readAllBytes(path);
                    output.write(data);
                    output.flush();
                } 
                catch (Exception e) 
                {
                    throw new WebApplicationException("File Not Found !!");
                }
            }
        };
        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition","attachment; filename="+ nameFile)
                .build();
    }
    
    /**
     * Método para registrar una reunión
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante con el que se ha realizado la reunión
     * @param reunion La reunión a registrar
     * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario respuesta HTTP 400 Bad Request
     */
    
    @POST
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes/{idEstudiante}/reuniones")
    @Consumes("application/json")
    public Response addReunion(@PathParam("idProfesor") int idProfesor,
    						   @PathParam("idLinea") int idLinea,
    						   @PathParam("idEstudiante") int idEstudiante, Reunion reunion){
    	if (!validacion.validarReunion(reunion)){
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	}
    	if (!this.hasAccessEstudiante(idProfesor, idLinea, idEstudiante)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
        ReunionDAO reunionDAO = factoria.getReunionDAO();
        EstudianteDAO estudianteDAO = factoria.getEstudianteDAO();
        Estudiante estudiante = estudianteDAO.getEstudiante(idEstudiante);
        reunion.setEstudiante(estudiante);
        reunionDAO.addReunion(reunion);
        return Response.ok().build();
    }
    
    /**
     * Método para obtener los datos de una reunión
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante
     * @param idReunion El identificador de la reunión
     * @return Respuesta HTTP 200 OK con los datos de la reunión
     */
    
    @GET
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes/{idEstudiante}/reuniones/{idReunion}")
    @Produces("application/json")
    public Response getReunion(@PathParam("idProfesor") int idProfesor,
    						   @PathParam("idLinea") int idLinea,
    						   @PathParam("idEstudiante") int idEstudiante,
    						   @PathParam("idReunion") int idReunion){
    	ReunionDAO reunionDAO = factoria.getReunionDAO();
    	Reunion reunion = reunionDAO.getReunion(idReunion);
    	if (!this.hasAccessReunion(idProfesor, idLinea, idEstudiante, reunion)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	return Response.status(Status.OK).entity(reunion).build();
    }
    
    /**
     * Método para obtener el listado de reuniones pertenecientes a un estudiante
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante
     * @return Respuesta HTTP 200 OK con el listado de reuniones
     */
    
    @GET
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes/{idEstudiante}/reuniones/")
    @Produces("application/json")
    public Response getReunionesEstudiante(@PathParam("idProfesor") int idProfesor,
    									   @PathParam("idLinea") int idLinea,
    									   @PathParam("idEstudiante") int idEstudiante){
    	if (!this.hasAccessEstudiante(idProfesor, idLinea, idEstudiante)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	ReunionDAO reunionDAO = factoria.getReunionDAO();
    	List<Reunion> reuniones = reunionDAO.getReunionesEstudiante(idEstudiante);
    	return Response.status(Status.OK).entity(reuniones).build();
    }
    
    /**
     * Método para actualizar los datos de una reunión
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante
     * @param idReunion El identificador de la reunión
     * @param reunion Los nuevos datos de la reunión
     * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario respuesta 400 Bad Request
     */
    
    @PUT
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes/{idEstudiante}/reuniones/{idReunion}")
    @Consumes("application/json")
    public Response updateReunion(@PathParam("idProfesor") int idProfesor,
			   					  @PathParam("idLinea") int idLinea,
			   					  @PathParam("idEstudiante") int idEstudiante,
			   					  @PathParam("idReunion") int idReunion,Reunion reunion){
    	if (!validacion.validarReunion(reunion)){
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	}
    	if (!this.hasAccessReunion(idProfesor, idLinea, idEstudiante, idReunion)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
        ReunionDAO reunionDAO = factoria.getReunionDAO();
        int count = reunionDAO.updateReunion(idReunion,reunion);
        if(count==0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
    
    /**
     * Método para eliminar una reunión
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante
     * @param idReunion El identificador de la reunión
     * @return Respuesta HTTP 200 OK en caso de éxito, en caso contrario respuesta 400 Bad Request
     */
    
    @DELETE
    @Secured
    @Path("/{idProfesor}/lineas/{idLinea}/estudiantes/{idEstudiante}/reuniones/{idReunion}")
    public Response deleteReunion(@PathParam("idProfesor") int idProfesor,
				  				  @PathParam("idLinea") int idLinea,
				  				  @PathParam("idEstudiante") int idEstudiante,
				  				  @PathParam("idReunion") int idReunion){
    	if (!this.hasAccessReunion(idProfesor, idLinea, idEstudiante, idReunion)){
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	ReunionDAO reunionDAO = factoria.getReunionDAO();
        int count = reunionDAO.deleteReunion(idReunion);
        if(count==0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
    
    /**
     * Se comprueba si la línea pertenece al profesor
     * @param idProfesor El identificador del profesor
     * @param linea La línea
     * @return true en caso de que la línea pertenezca al profesor, false en caso contrario
     */
    public boolean hasAccessLinea(int idProfesor,Linea linea){
    	if (linea.getProfesor().getId() == idProfesor){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Se comprueba si la línea pertenece al profesor
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @return true en caso de que la línea pertenezca al profesor, false en caso contrario
     */
    public boolean hasAccessLinea(int idProfesor,int idLinea){
    	LineaDAO lineaDAO = factoria.getLineaDAO();
    	Linea linea = lineaDAO.getLinea(idLinea);
    	if (linea.getProfesor().getId() == idProfesor){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Se comprueba si un estudiante forma parte de una línea propuesta por el profesor
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante
     * @return true en caso de que el estudiante pertenezca a la línea, false en caso contrario
     */
    public boolean hasAccessEstudiante(int idProfesor,int idLinea,int idEstudiante){
    	EstudianteDAO estudianteDAO = factoria.getEstudianteDAO();
    	Estudiante estudiante = estudianteDAO.getEstudiante(idEstudiante);
    	if (estudiante.getLinea().getId() == idLinea && this.hasAccessLinea(idProfesor, idLinea)){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Se comprueba si un estudiante forma parte de una línea propuesta por el profesor
     * @param idProfesor El identificador del profesor
     * @param idLinea El identificador de la línea
     * @param estudiante El estudiante
     * @return true en caso de que el estudiante pertenezca a la línea, false en caso contrario
     */
    public boolean hasAccessEstudiante(int idProfesor,int idLinea,Estudiante estudiante){
    	if (estudiante.getLinea().getId() == idLinea && this.hasAccessLinea(idProfesor, idLinea)){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Comprueba si una reunión pertenece a un estudiante registrado en una línea propuesta por un profesor
     * @param idProfesor El identificado del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante
     * @param idReunion El identificador de la reunión 
     * @return true en caso de que la reunion sea accesible por el profesor, false en caso contrario
     */
    public boolean hasAccessReunion(int idProfesor, int idLinea, int idEstudiante, int idReunion){
    	ReunionDAO reunionDAO = factoria.getReunionDAO();
    	Reunion reunion = reunionDAO.getReunion(idReunion);
    	if (reunion.getEstudiante().getId() == idEstudiante && 
    			this.hasAccessEstudiante(idProfesor, idLinea, idEstudiante)){
    		return true;
    	}
    	return false;
    		
    }
    
    /**
     * Comprueba si una reunión pertenece a un estudiante registrado en una línea propuesta por un profesor
     * @param idProfesor El identificado del profesor
     * @param idLinea El identificador de la línea
     * @param idEstudiante El identificador del estudiante
     * @param reunion La reunión 
     * @return true en caso de que la reunion sea accesible por el profesor, false en caso contrario
     */
    public boolean hasAccessReunion(int idProfesor, int idLinea, int idEstudiante,Reunion reunion){
    	if (reunion.getEstudiante().getId() == idEstudiante && 
    			this.hasAccessEstudiante(idProfesor, idLinea, idEstudiante)){
    		return true;
    	}
    	return false;
    		
    }
    
    
    
}