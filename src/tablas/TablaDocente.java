package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.Docente;

public class TablaDocente extends Tabla<Docente> {

	public TablaDocente(Connection conexion) {
		super(conexion, "Docente");
	}

	public String agregar(Docente dato) {
		return super.insertar("0", dato.getNombre());
	}

	@Override
	public String modificar(Docente dato) {
		sql = "Update Docente Set nom_doc='" + dato.getNombre() + "' Where cve_doc ='" + dato.getClave() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(Docente dato) {
		sql = "DELETE FROM Docente WHERE nom_doc ='" + dato.getClave() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(Docente[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			insertar("NULL", dato[i].getNombre());
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public Docente buscar(Docente dato) {

		String slq = "Select * from Docente where nom_doc = '" + dato.getNombre() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Docente a = new Docente();
			rs.next();

			a.setClave(rs.getString(1));
			a.setNombre(rs.getString(2));
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Docente getPrimero() {
		String slq = "Select * from Docente limit 1";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Docente a = new Docente();
			rs.next();

			a.setClave(rs.getString(1));
			a.setNombre(rs.getString(2));
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(Docente dato) {
		String slq = "Select * from docente where nom_doc = '" + dato.getNombre() + "'";

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
