package empresa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public static void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO Productos VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.idProducto);
            ps.setInt(2, p.idCategoria);
            ps.setString(3, p.nombre);
            ps.setString(4, p.canUnidad);
            ps.setFloat(5, p.precio);
            ps.executeUpdate();
        }
    }

    public static List<Producto> listar() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Productos";
        try (Connection con = ConexionBD.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("IDProducto"),
                        rs.getInt("IDCategoria"),
                        rs.getString("Nombre"),
                        rs.getString("CanUnidad"),
                        rs.getFloat("Precio")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    public static void actualizarPrecio(int id, float nuevoPrecio) throws SQLException {
        String sql = "UPDATE Productos SET Precio = ? WHERE IDProducto = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setFloat(1, nuevoPrecio);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public static void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Productos WHERE IDProducto = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
