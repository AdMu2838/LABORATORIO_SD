package empresa.modelo;

import java.io.Serializable;

public class Departamento implements Serializable {
    private int idDpto;
    private String nombre;
    private String telefono;
    private String fax;
    
    public Departamento() {}
    
    public Departamento(int idDpto, String nombre, String telefono, String fax) {
        this.idDpto = idDpto;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fax = fax;
    }
    
    // Getters y Setters
    public int getIdDpto() { return idDpto; }
    public void setIdDpto(int idDpto) { this.idDpto = idDpto; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getFax() { return fax; }
    public void setFax(String fax) { this.fax = fax; }
    
    @Override
    public String toString() {
        return "Departamento{" +
                "idDpto=" + idDpto +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fax='" + fax + '\'' +
                '}';
    }
}
