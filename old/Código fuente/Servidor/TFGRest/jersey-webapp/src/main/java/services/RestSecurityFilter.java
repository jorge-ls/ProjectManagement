package services;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.crypto.MacProvider;


@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class RestSecurityFilter implements ContainerRequestFilter {
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		try {
			// Check if the HTTP Authorization header is present and formatted correctly 
	        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
	            throw new NotAuthorizedException("Authorization header must be provided");
	        }
	        // Extrae el token de la cabecera
            String token = authorizationHeader.substring("Bearer".length()).trim();
            // Valida el token utilizando la cadena secreta
            Jws<Claims> claims = Jwts.parser().setSigningKey(MyResource.clave).parseClaimsJws(token);
            int idProfesor = (Integer) claims.getBody().get("idProfesor");
            String urlRequest = requestContext.getUriInfo().getAbsolutePath().toString();
            //System.out.println("Url: "+urlRequest); 
            int beginIndex = urlRequest.indexOf("profesores");
            String subString = urlRequest.substring(beginIndex);
            String arrayUrl[] = subString.split("/");
            int idProfesorUrl = Integer.parseInt(arrayUrl[1]);
            //Se comprueba si el id del payload es igual al id enviado en la url de la peticion
            if (idProfesor != idProfesorUrl){
            	System.out.println("No autorizado");
            	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } catch (Exception e) {
        	System.out.println("Unauthorized exception");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

	}
}
