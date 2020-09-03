package modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Reunion {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column (nullable = false)
	private String titulo;
	@Column (nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	@Column (nullable = false)
	private int minutos;
	@Column (nullable = true)
	private String urlMemoria;
	@Column (nullable = true)
	private String comentario;
	@Column (nullable = false)
	@Enumerated(value = EnumType.STRING)
	private TipoReunion tipoReunion;
	@JsonIgnore
	@ManyToOne
	@JoinColumn (name="estudiante")
	private Estudiante estudiante;
	@Transient
	private String memoria;
	
	
	public int getId() {
		return id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getMinutos() {
		return minutos;
	}
	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}
	public String getUrlMemoria() {
		return urlMemoria;
	}
	public void setUrlMemoria(String urlMemoria) {
		this.urlMemoria = urlMemoria;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Estudiante getEstudiante() {
		return estudiante;
	}
	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}
	public TipoReunion getTipoReunion() {
		return tipoReunion;
	}
	public void setTipoReunion(TipoReunion tipoReunion) {
		this.tipoReunion = tipoReunion;
	}
	
	public String getMemoria() {
		return memoria;
	}
	
	public void setMemoria(String memoria) {
		this.memoria = memoria;
	}

	@Override
	public String toString() {
		return "Reunion [id=" + id + ", fecha=" + fecha + ", minutos=" + minutos + ", urlMemoria=" + urlMemoria
				+ ", comentario=" + comentario + ", tipoReunion=" + tipoReunion + "]";
	}
	
	
	
	
}
