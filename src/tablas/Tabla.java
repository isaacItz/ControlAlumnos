package tablas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Tabla<T> {

	protected PreparedStatement statement;
	protected String sql;
	protected final String EXITO = "Exito";
	private String tabla;
	protected Connection conexion;

	public Tabla(Connection conexion, String tabla) {
		this.tabla = tabla;
		this.conexion = conexion;
	}

	public abstract String modificar(T dato);

	public abstract String eliminar(T dato);

	public abstract T buscar(T dato);

	public abstract boolean existe(T dato);

	protected String insertar(Object... datos) {
		sql = "insert into " + tabla + " values('";
		int i = 0;
		for (; i < datos.length - 1; i++)
			sql += datos[i] + ("','");

		sql += datos[i] + ("')");

		try {
			System.out.println(sql);
			statement = conexion.prepareStatement(sql);
			statement.executeUpdate(sql);
			return EXITO;
		} catch (SQLException e) {
			return "problema con la tabnla " + tabla.concat(": ") + e.getMessage();
		}
	}

	protected int ejecutarQuery(String sql) throws SQLException {
		statement = conexion.prepareStatement(sql);
		return statement.executeUpdate(sql);

	}

}
