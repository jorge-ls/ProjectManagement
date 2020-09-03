package modelo;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@JsonIgnoreProperties(value = "contraseña", allowSetters = true)
public class Profesor {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column (nullable = false)
	private String nombre;
	@Column (nullable = false)
	private String apellidos;
	@Column (nullable = false)
	private String usuario;
	@Column (nullable = false)
	private String contraseña;
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER ,cascade={CascadeType.REMOVE}, mappedBy="profesor")
	private List<Linea> lineas;
	
	public Profesor(){}
	
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
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Linea> getLineas() {
		return lineas;
	}
	public void setLineas(List<Linea> lineas) {
		this.lineas = lineas;
	}	

	@Override
	public String toString() {
		return "Profesor [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", usuario=" + usuario
				+ ", contraseña=" + contraseña + "]";
	}
	
	
	

}
