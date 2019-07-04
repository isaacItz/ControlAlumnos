package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.Nivelacion;

public class TablaNivelacion extends Tabla<Nivelacion> {

	public TablaNivelacion(Connection conexion) {
		super(conexion, "Nivelacion");
	}

	public String agregar(Nivelacion dato) {
		return super.insertar(dato.getCveNiv(), dato.getCveGpoMatAlumno(), dato.getCalificacion(), dato.getCveUnidad());
	}

	@Override
	public String modificar(Nivelacion dato) {
		sql = "Update Nivelacion Set calificacion='" + dato.getCalificacion() + "' Where cve_niv ='" + dato.getCveNiv()
				+ "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(Nivelacion dato) {
		sql = "DELETE FROM Nivelacion WHERE cve_niv ='" + dato.getCveNiv() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(Nivelacion[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			System.out.println(insertar(dato[i].getCveNiv(), dato[i].getCveGpoMatAlumno(), dato[i].getCalificacion(),
					dato[i].getCveUnidad()));
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public Nivelacion buscar(Nivelacion dato) {

		String slq = "Select * from Nivelacion where cve_uni = '" + dato.getCveUnidad() + "' and cve_grumatalum = '"
				+ dato.getCveGpoMatAlumno() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Nivelacion a = new Nivelacion();
			rs.next();

			a.setCveNiv(rs.getInt(1));
			a.setCveGpoMatAlumno(rs.getInt(2));
			a.setCalificacion(rs.getInt(3));
			a.setCveUnidad(rs.getInt(4));
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(Nivelacion dato) {
		String slq = "Select * from Nivelacion where cve_uni = '" + dato.getCveUnidad() + "' and cve_grumatalum = '"
				+ dato.getCveGpoMatAlumno() + "'";

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
