package empresa.servidor.dao;

import empresa.modelo.Proyecto;
import empresa.servidor.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO {
    
    public boolean insertar(Proyecto proyecto) {
        String sql = "INSERT INTO Proyecto (IDProy, Nombre, Fec_Inicio, Fec_Termino, IDIng, IDDpto) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, proyecto.getIdProy());
            stmt.setString(2, proyecto.getNombre());
            stmt.setDate(3, Date.valueOf(proyecto.getFecInicio()));
            stmt.setDate(4, proyecto.getFecTermino() != null ? Date.valueOf(proyecto.getFecTermino()) : null);
            if (proyecto.getIdIng() != null) {
                stmt.setInt(5, proyecto.getIdIng());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, proyecto.getIdDpto());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar proyecto: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizar(Proyecto proyecto) {
        String sql = "UPDATE Proyecto SET Nombre = ?, Fec_Inicio = ?, Fec_Termino = ?, IDIng = ?, IDDpto = ? WHERE IDProy = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, proyecto.getNombre());
            stmt.setDate(2, Date.valueOf(proyecto.getFecInicio()));
            stmt.setDate(3, proyecto.getFecTermino() != null ? Date.valueOf(proyecto.getFecTermino()) : null);
            if (proyecto.getIdIng() != null) {
                stmt.setInt(4, proyecto.getIdIng());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, proyecto.getIdDpto());
            stmt.setInt(6, proyecto.getIdProy());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar proyecto: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(int idProy) {
        String sql = "DELETE FROM Proyecto WHERE IDProy = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProy);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar proyecto: " + e.getMessage());
            return false;
        }
    }
    
    public List<Proyecto> listarTodos() {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT p.*, i.Nombre as NombreIngeniero, d.Nombre as NombreDepartamento " +
                    "FROM Proyecto p " +
                    "LEFT JOIN Ingeniero i ON p.IDIng = i.IDIng " +
                    "LEFT JOIN Departamento d ON p.IDDpto = d.IDDpto " +
                    "ORDER BY p.IDProy";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Proyecto proy = new Proyecto();
                proy.setIdProy(rs.getInt("IDProy"));
                proy.setNombre(rs.getString("Nombre"));
                proy.setFecInicio(rs.getDate("Fec_Inicio").toLocalDate());
                Date fecTermino = rs.getDate("Fec_Termino");
                if (fecTermino != null) {
                    proy.setFecTermino(fecTermino.toLocalDate());
                }
                proy.setIdIng(rs.getObject("IDIng", Integer.class));
                proy.setIdDpto(rs.getInt("IDDpto"));
                proy.setNombreIngeniero(rs.getString("NombreIngeniero"));
                proy.setNombreDepartamento(rs.getString("NombreDepartamento"));
                proyectos.add(proy);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar proyectos: " + e.getMessage());
        }
        return proyectos;
    }
    
    public Proyecto obtenerPorId(int idProy) {
        String sql = "SELECT p.*, i.Nombre as NombreIngeniero, d.Nombre as NombreDepartamento " +
                    "FROM Proyecto p " +
                    "LEFT JOIN Ingeniero i ON p.IDIng = i.IDIng " +
                    "LEFT JOIN Departamento d ON p.IDDpto = d.IDDpto " +
                    "WHERE p.IDProy = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProy);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Proyecto proy = new Proyecto();
                proy.setIdProy(rs.getInt("IDProy"));
                proy.setNombre(rs.getString("Nombre"));
                proy.setFecInicio(rs.getDate("Fec_Inicio").toLocalDate());
                Date fecTermino = rs.getDate("Fec_Termino");
                if (fecTermino != null) {
                    proy.setFecTermino(fecTermino.toLocalDate());
                }
                proy.setIdIng(rs.getObject("IDIng", Integer.class));
                proy.setIdDpto(rs.getInt("IDDpto"));
                proy.setNombreIngeniero(rs.getString("NombreIngeniero"));
                proy.setNombreDepartamento(rs.getString("NombreDepartamento"));
                return proy;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener proyecto: " + e.getMessage());
        }
        return null;
    }
    
    // Consulta específica requerida: Proyectos de un departamento
    public List<Proyecto> obtenerPorDepartamento(int idDepartamento) {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT p.*, i.Nombre as NombreIngeniero, d.Nombre as NombreDepartamento " +
                    "FROM Proyecto p " +
                    "LEFT JOIN Ingeniero i ON p.IDIng = i.IDIng " +
                    "LEFT JOIN Departamento d ON p.IDDpto = d.IDDpto " +
                    "WHERE p.IDDpto = ? " +
                    "ORDER BY p.IDProy";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idDepartamento);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Proyecto proy = new Proyecto();
                proy.setIdProy(rs.getInt("IDProy"));
                proy.setNombre(rs.getString("Nombre"));
                proy.setFecInicio(rs.getDate("Fec_Inicio").toLocalDate());
                Date fecTermino = rs.getDate("Fec_Termino");
                if (fecTermino != null) {
                    proy.setFecTermino(fecTermino.toLocalDate());
                }
                proy.setIdIng(rs.getObject("IDIng", Integer.class));
                proy.setIdDpto(rs.getInt("IDDpto"));
                proy.setNombreIngeniero(rs.getString("NombreIngeniero"));
                proy.setNombreDepartamento(rs.getString("NombreDepartamento"));
                proyectos.add(proy);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener proyectos por departamento: " + e.getMessage());
        }
        return proyectos;
    }
}
