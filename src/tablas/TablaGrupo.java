package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.Grupo;

public class TablaGrupo extends Tabla<Grupo> {

	public TablaGrupo(Connection conexion) {
		super(conexion, "Grupo");
	}

	public String agregar(Grupo dato) {
		return super.insertar(dato.getNombre());
	}

	@Override
	public String modificar(Grupo dato) {
		sql = "Update Grupo Set nombre='" + dato.getNombre() + "' Where nombre ='" + dato.getNombre() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(Grupo dato) {
		sql = "DELETE FROM Grupo WHERE nombre ='" + dato.getNombre() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(Grupo[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			insertar(dato[i].getNombre());
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public Grupo buscar(Grupo dato) {

		String slq = "Select * from grupo where nom_gru = '" + dato.getNombre() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Grupo a = new Grupo();
			rs.next();

			a.setNombre(rs.getString(1));
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(Grupo dato) {
		String slq = "Select * from grupo where nom_gru = '" + dato.getNombre() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			if (rs.next())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
