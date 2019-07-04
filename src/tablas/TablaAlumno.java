package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.Alumno;

public class TablaAlumno extends Tabla<Alumno> {

	public TablaAlumno(Connection conexion) {
		super(conexion, "Alumno");
	}

	public String agregar(Alumno dato) {
		return super.insertar(dato.getNoControl(), dato.getNombre());
	}

	@Override
	public String modificar(Alumno dato) {
		sql = "Update Alumno Set nombre='" + dato.getNombre() + "' Where clave ='" + dato.getNoControl() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(Alumno dato) {
		sql = "DELETE FROM Alumno WHERE clave ='" + dato.getNoControl() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(Alumno[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			System.out.println(insertar(dato[i].getNoControl(), dato[i].getNombre()));
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public Alumno buscar(Alumno dato) {

		String slq = "Select * from alumno where numerodecontrol_alum = '" + dato.getNoControl() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Alumno a = new Alumno();
			rs.next();

			a.setNoControl(rs.getString(1));
			a.setNombre(rs.getString(2));

			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(Alumno dato) {
		String slq = "Select * from alumno where cve_alum = '" + dato.getNoControl() + "'";

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
