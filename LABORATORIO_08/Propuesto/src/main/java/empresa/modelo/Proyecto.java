package empresa.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Proyecto implements Serializable {
    private int idProy;
    private String nombre;
    private LocalDate fecInicio;
    private LocalDate fecTermino;
    private Integer idIng;
    private int idDpto;
    private String nombreIngeniero;
    private String nombreDepartamento;
    
    public Proyecto() {}
    
    public Proyecto(int idProy, String nombre, LocalDate fecInicio, LocalDate fecTermino, Integer idIng, int idDpto) {
        this.idProy = idProy;
        this.nombre = nombre;
        this.fecInicio = fecInicio;
        this.fecTermino = fecTermino;
        this.idIng = idIng;
        this.idDpto = idDpto;
    }
    
    // Getters y Setters
    public int getIdProy() { return idProy; }
    public void setIdProy(int idProy) { this.idProy = idProy; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public LocalDate getFecInicio() { return fecInicio; }
    public void setFecInicio(LocalDate fecInicio) { this.fecInicio = fecInicio; }
    
    public LocalDate getFecTermino() { return fecTermino; }
    public void setFecTermino(LocalDate fecTermino) { this.fecTermino = fecTermino; }
    
    public Integer getIdIng() { return idIng; }
    public void setIdIng(Integer idIng) { this.idIng = idIng; }
    
    public int getIdDpto() { return idDpto; }
    public void setIdDpto(int idDpto) { this.idDpto = idDpto; }
    
    public String getNombreIngeniero() { return nombreIngeniero; }
    public void setNombreIngeniero(String nombreIngeniero) { this.nombreIngeniero = nombreIngeniero; }
    
    public String getNombreDepartamento() { return nombreDepartamento; }
    public void setNombreDepartamento(String nombreDepartamento) { this.nombreDepartamento = nombreDepartamento; }
    
    @Override
    public String toString() {
        return "Proyecto{" +
                "idProy=" + idProy +
                ", nombre='" + nombre + '\'' +
                ", fecInicio=" + fecInicio +
                ", fecTermino=" + fecTermino +
                ", idIng=" + idIng +
                ", idDpto=" + idDpto +
                '}';
    }
}
