package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class Fichero {
	
	/**
	 * Método para generar una secuencia aleatoria alfanumérica
	 * @param length La longitud de la secuencia
	 * @return La secuencia generada
	 */
	public String generarAleatorio(int length){
		String cadena = "";
		Random rnd = new Random(System.currentTimeMillis());
		for (int i=0;i<length;i++){
			if (i<length/2){ //Se generar números aleatorios
				cadena += rnd.nextInt(10);
			}
			else { //Se generan caracteres aleatorios
				cadena += (char)(rnd.nextInt(123-97)+97);
			}
		}
		return cadena;
	}
	
	/**
	 * Método para almacenar un fichero en el servidor
	 * @param fichero El fichero a almacenar
	 * @return La ruta del fichero almacenado
	 */
	public String guardarFichero(String fichero){
		String datosFichero[] = fichero.split(",");
   	 	String fileName = datosFichero[0];
   	 	String dataFile = datosFichero[1];
   	 	String datosNombre[] = fileName.split("\\.");
   	 	System.out.println("El nombre del fichero es "+fileName);
   	 	System.out.println("Nombre: "+datosNombre[0]);
   	 	System.out.println("Extension: "+datosNombre[1]);
   	 	SecureRandom random = new SecureRandom();
   	 	//String secuencia = new BigInteger(130, random).toString(32);
   	 	String secuencia = this.generarAleatorio(10);
   	 	//System.out.println("Data file "+dataFile);
   	 	byte[] fileBytes = Base64.getDecoder().decode(dataFile.getBytes());
   	 	//byte[] fileBytes = DatatypeConverter.parseBase64Binary(dataFile);
        //String url = "C:\\Users\\jorge\\Desktop\\TFG\\tfg_jorgelopez\\Código fuente\\Servidor\\TFGRest\\jersey-webapp\\ficheros\\";
        //String relativeUrl = "..\\ficheros\\";
        //String filePath = url+fileName+"-"+secuencia;
        String filePath = datosNombre[0]+"-"+secuencia+"."+datosNombre[1];
        File file = new File(filePath);
        System.out.println("La ruta del fichero subido es: "+file.getAbsolutePath());
        //OutputStream os;
        try {
			//FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
        	FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(fileBytes,0,fileBytes.length);
	        fos.flush();
	        fos.close();
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        } catch (IOException e) {
       	 // TODO Auto-generated catch block
       	 e.printStackTrace();
        }
		return filePath;
	}
	
	

}
