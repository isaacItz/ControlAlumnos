package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.Alumno;
import modelo.GrupoMateria;
import modelo.GrupoMateriaAlumno;

public class TablaGrupoMateriaAlumno extends Tabla<GrupoMateriaAlumno> {

	public TablaGrupoMateriaAlumno(Connection conexion) {
		super(conexion, "GrupoMateAlum");
	}

	public String agregar(GrupoMateriaAlumno dato) {
		return super.insertar("0", dato.getClaveAlumno(), dato.getClabeGpoMat());
	}

	@Override
	public String modificar(GrupoMateriaAlumno dato) {
		sql = "Update GrupoMateAlum Set cve_alum='" + dato.getClaveAlumno() + "', cve_grumat= '" + dato.getClabeGpoMat()
				+ " Where cve_grumatalum ='" + dato.getClaveGpoMatAlum() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(GrupoMateriaAlumno dato) {
		sql = "DELETE FROM GrupoMateAlum WHERE cve_grumatalum ='" + dato.getClaveGpoMatAlum() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(GrupoMateriaAlumno[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			insertar("0", dato[i].getClaveAlumno(), dato[i].getClabeGpoMat());
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public GrupoMateriaAlumno buscar(GrupoMateriaAlumno dato) {

		String slq = "select * from GrupoMateAlum where cve_alum = '" + dato.getClaveAlumno() + "' and cve_grumat = '"
				+ dato.getClabeGpoMat() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			GrupoMateriaAlumno a = new GrupoMateriaAlumno();
			rs.next();

			a.setClaveGpoMatAlum(rs.getString(1));
			a.setClaveAlumno(rs.getString(2));
			a.setClabeGpoMat(rs.getString(3));
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public GrupoMateria[] getGruposAlum(Alumno a) {
		TablaGrupoMateria tablaGrupoMateria = new TablaGrupoMateria(conexion);
		sql = "select * from grupomatealum where cve_alum = " + a.getNoControl();

		try {
			List<GrupoMateria> lista = new ArrayList<>();
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				GrupoMateria gp = new GrupoMateria();
				gp.setClaveGrupoMateria(rs.getString(3));
				gp = tablaGrupoMateria.getGrupoMateria(gp);
				lista.add(gp);
			}
			return lista.toArray(new GrupoMateria[lista.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getTotalAlumnos(int cveGruMat) {
		String slq = "select count(*) from grupomatealum where cve_grumat = " + cveGruMat;

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean existe(GrupoMateriaAlumno dato) {
		String slq = "select * from GrupoMateAlum where cve_alum = '" + dato.getClaveAlumno() + "' and cve_grumat = '"
				+ dato.getClabeGpoMat() + "'";

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
