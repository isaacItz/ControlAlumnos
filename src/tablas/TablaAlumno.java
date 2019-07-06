package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.Alumno;
import modelo.GrupoMateria;

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

	public Alumno[] getAlumnos(GrupoMateria gpo) {
		sql = "select * from alumno where exists(select * from grupomatealum where cve_alum = alumno.cve_alum)";
		if (gpo != null) {
			sql += "where cve_alum in (select cve_alum from grupomatealum where cve_grumat = "
					+ gpo.getClaveGrupoMateria() + ")";
		}
		try {
			List<Alumno> lista = new ArrayList<Alumno>();
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Alumno al = new Alumno();
				al.setNoControl(rs.getString(1));
				al.setNombre(rs.getString(2));
				lista.add(al);
			}
			return lista.toArray(new Alumno[lista.size()]);
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
