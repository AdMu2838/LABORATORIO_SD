package empresa.servidor.dao;

import empresa.modelo.Ingeniero;
import empresa.servidor.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngenieroDAO {
    
    public boolean insertar(Ingeniero ingeniero) {
        String sql = "INSERT INTO Ingeniero (IDIng, Nombre, Especialidad, Cargo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ingeniero.getIdIng());
            stmt.setString(2, ingeniero.getNombre());
            stmt.setString(3, ingeniero.getEspecialidad());
            stmt.setString(4, ingeniero.getCargo());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar ingeniero: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizar(Ingeniero ingeniero) {
        String sql = "UPDATE Ingeniero SET Nombre = ?, Especialidad = ?, Cargo = ? WHERE IDIng = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ingeniero.getNombre());
            stmt.setString(2, ingeniero.getEspecialidad());
            stmt.setString(3, ingeniero.getCargo());
            stmt.setInt(4, ingeniero.getIdIng());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar ingeniero: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(int idIng) {
        String sql = "DELETE FROM Ingeniero WHERE IDIng = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idIng);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar ingeniero: " + e.getMessage());
            return false;
        }
    }
    
    public List<Ingeniero> listarTodos() {
        List<Ingeniero> ingenieros = new ArrayList<>();
        String sql = "SELECT * FROM Ingeniero ORDER BY IDIng";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Ingeniero ing = new Ingeniero();
                ing.setIdIng(rs.getInt("IDIng"));
                ing.setNombre(rs.getString("Nombre"));
                ing.setEspecialidad(rs.getString("Especialidad"));
                ing.setCargo(rs.getString("Cargo"));
                ingenieros.add(ing);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar ingenieros: " + e.getMessage());
        }
        return ingenieros;
    }
    
    public Ingeniero obtenerPorId(int idIng) {
        String sql = "SELECT * FROM Ingeniero WHERE IDIng = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idIng);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Ingeniero ing = new Ingeniero();
                ing.setIdIng(rs.getInt("IDIng"));
                ing.setNombre(rs.getString("Nombre"));
                ing.setEspecialidad(rs.getString("Especialidad"));
                ing.setCargo(rs.getString("Cargo"));
                return ing;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ingeniero: " + e.getMessage());
        }
        return null;
    }
    
    // Consulta específica requerida: Ingenieros que participan en un proyecto
    public List<Ingeniero> obtenerPorProyecto(int idProyecto) {
        List<Ingeniero> ingenieros = new ArrayList<>();
        String sql = "SELECT DISTINCT i.* FROM Ingeniero i " +
                    "INNER JOIN Proyecto p ON i.IDIng = p.IDIng " +
                    "WHERE p.IDProy = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProyecto);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ingeniero ing = new Ingeniero();
                ing.setIdIng(rs.getInt("IDIng"));
                ing.setNombre(rs.getString("Nombre"));
                ing.setEspecialidad(rs.getString("Especialidad"));
                ing.setCargo(rs.getString("Cargo"));
                ingenieros.add(ing);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ingenieros por proyecto: " + e.getMessage());
        }
        return ingenieros;
    }
}
