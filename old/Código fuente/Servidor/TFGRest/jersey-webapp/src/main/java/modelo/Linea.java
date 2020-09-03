package modelo;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Linea {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column (nullable = false)
	private String titulo;
	@Column (nullable = false)
	private String descripcion;
	@Column (nullable = false)
	@Enumerated(value = EnumType.STRING)
	private TipoLinea tipoLinea;
	@Column (nullable = false)
	private int maxEstudiantes;
	@Transient
	private Long numEstudiantes;
	@JsonIgnore
	@ManyToOne
	@JoinColumn (name= "profesor")
	private Profesor profesor;
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER ,cascade={CascadeType.REMOVE}, mappedBy="linea") 
	private List<Estudiante> estudiantes;
	
	public Linea(){
		
	}
	
	public int getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public TipoLinea getTipoLinea() {
		return tipoLinea;
	}
	public void setTipoLinea(TipoLinea tipoLinea) {
		this.tipoLinea = tipoLinea;
	}
	public Profesor getProfesor() {
		return profesor;
	}
	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}
	public List<Estudiante> getEstudiantes() {
		return estudiantes;
	}
	public void setEstudiantes(List<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}
	
	public int getMaxEstudiantes() {
		return maxEstudiantes;
	}
	
	public void setMaxEstudiantes(int maxEstudiantes) {
		this.maxEstudiantes = maxEstudiantes;
	}
	
	public Long getNumEstudiantes() {
		return numEstudiantes;
	}
	
	public void setNumEstudiantes(Long numEstudiantes) {
		this.numEstudiantes = numEstudiantes;
	}

	@Override
	public String toString() {
		return "Linea [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", tipoLinea=" + tipoLinea
				+ "]";
	}
	
	
	
	

}
