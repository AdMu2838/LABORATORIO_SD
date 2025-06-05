package empresa;

public class Producto {
    public int idProducto;
    public int idCategoria;
    public String nombre;
    public String canUnidad;
    public float precio;

    public Producto(int idProducto, int idCategoria, String nombre, String canUnidad, float precio) {
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.canUnidad = canUnidad;
        this.precio = precio;
    }
}
