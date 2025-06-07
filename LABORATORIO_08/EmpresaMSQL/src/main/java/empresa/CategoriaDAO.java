package empresa;

import java.sql.*;
import java.util.*;

public class CategoriaDAO {

    public static Map<String, Integer> obtenerCategorias() throws SQLException {
        Map<String, Integer> categorias = new LinkedHashMap<>();
        Connection con = ConexionBD.conectar();
        String sql = "SELECT IDCategoria, Nombre FROM Categorias";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            categorias.put(rs.getString("Nombre"), rs.getInt("IDCategoria"));
        }
        rs.close();
        stmt.close();
        return categorias;
    }
}
