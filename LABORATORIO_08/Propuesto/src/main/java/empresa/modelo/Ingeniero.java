package empresa.modelo;

import java.io.Serializable;

public class Ingeniero implements Serializable {
    private int idIng;
    private String nombre;
    private String especialidad;
    private String cargo;
    
    public Ingeniero() {}
    
    public Ingeniero(int idIng, String nombre, String especialidad, String cargo) {
        this.idIng = idIng;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.cargo = cargo;
    }
    
    // Getters y Setters
    public int getIdIng() { return idIng; }
    public void setIdIng(int idIng) { this.idIng = idIng; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    @Override
    public String toString() {
        return "Ingeniero{" +
                "idIng=" + idIng +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}
