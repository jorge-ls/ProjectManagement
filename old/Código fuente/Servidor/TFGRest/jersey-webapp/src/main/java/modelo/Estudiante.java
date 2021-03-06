package modelo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Estudiante {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column (nullable = true)
	private String nombre;
	@Column (nullable = true)
	private String apellidos;
	@Column (nullable = true)
	private String urlGit;
	@Column (nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaEstimada;
	@Column (nullable = true)
	private String urlInforme;
	@Column (nullable = true)
	private String vocales;
	@Column (nullable = true)
	private String urlMemoriaFinal;
	@Transient
	private String memFinal;
	@Transient
	private String informe;
	@Transient
	private int minutosTotales;
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER ,cascade={CascadeType.REMOVE}, mappedBy="estudiante")
	private List<Reunion> reuniones;
	@JsonIgnore
	@ManyToOne
	@JoinColumn (name= "linea")
	private Linea linea;
	@Column (nullable = true)
	@Enumerated(value = EnumType.STRING)
	private EstadoTrabajo estadoTrabajo;
	
	public Estudiante(){
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getUrlGit() {
		return urlGit;
	}

	public void setUrlGit(String urlGit) {
		this.urlGit = urlGit;
	}

	public Date getFechaEstimada() {
		return fechaEstimada;
	}

	public void setFechaEstimada(Date fechaEstimada) {
		this.fechaEstimada = fechaEstimada;
	}

	public String getUrlInforme() {
		return urlInforme;
	}

	public void setUrlInforme(String urlInforme) {
		this.urlInforme = urlInforme;
	}

	public String getVocales() {
		return vocales;
	}

	public void setVocales(String vocales) {
		this.vocales = vocales;
	}

	public String getUrlMemoriaFinal() {
		return urlMemoriaFinal;
	}

	public void setUrlMemoriaFinal(String urlMemoriaFinal) {
		this.urlMemoriaFinal = urlMemoriaFinal;
	}

	public List<Reunion> getReuniones() {
		return reuniones;
	}

	public void setReuniones(List<Reunion> reuniones) {
		this.reuniones = reuniones;
	}

	public Linea getLinea() {
		return linea;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}

	public int getId() {
		return id;
	}
	
	public EstadoTrabajo getEstadoTrabajo() {
		return estadoTrabajo;
	}
	
	public void setEstadoTrabajo(EstadoTrabajo estadoTrabajo) {
		this.estadoTrabajo = estadoTrabajo;
	}
	
	public int getMinutosTotales() {
		return minutosTotales;
	}
	
	public void setMinutosTotales(int minutosTotales) {
		this.minutosTotales = minutosTotales;
	}
	
	public String getInforme() {
		return informe;
	}
	
	public void setInforme(String informe) {
		this.informe = informe;
	}
	
	public String getMemFinal() {
		return memFinal;
	}
	
	public void setMemFinal(String memFinal) {
		this.memFinal = memFinal;
	}
	
	@Override
	public String toString() {
		return "Estudiante [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", urlGit=" + urlGit
				+ ", fechaEstimada=" + fechaEstimada + ", urlInforme=" + urlInforme + ", vocales=" + vocales
				+ ", urlMemoriaFinal=" + urlMemoriaFinal +
				", estadoTrabajo=" + estadoTrabajo + "]";
	}
	
	
	
	
	
}
