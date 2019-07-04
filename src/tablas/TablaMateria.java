package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.Materia;
import modelo.Unidad;

public class TablaMateria extends Tabla<Materia> {

	private Tablaperiodo tablaperiodo;

	public TablaMateria(Connection conexion) {
		super(conexion, "Materia");
		tablaperiodo = new Tablaperiodo(conexion);
	}

	public String agregar(Materia dato) {
		return super.insertar("0", dato.getNombre());
	}

	@Override
	public String modificar(Materia dato) {
		sql = "Update Materia Set nom_mate='" + dato.getNombre() + "' Where cve_mate ='" + dato.getClave() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(Materia dato) {
		sql = "DELETE FROM Materia WHERE cve_mate ='" + dato.getClave() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(Materia[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			insertar("NULL", dato[i].getNombre());
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public Materia buscar(Materia dato) {

		String slq = "Select * from materia where nom_mate = '" + dato.getNombre() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Materia a = new Materia();
			if (rs.next()) {
				a.setClave(rs.getString(1));
				a.setNombre(rs.getString(2));
				return a;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Materia getMateria(String cveMateria) {
		String slq = "Select * from materia where cve_mate = '" + cveMateria + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Materia a = new Materia();
			if (rs.next()) {
				a.setClave(rs.getString(1));
				a.setNombre(rs.getString(2));
			}

			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Unidad[] getUnidades(Materia materia) {
		sql = "Select * from Unidad where cve_mate = " + materia.getClave();
		try {
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			List<Unidad> lis = new ArrayList<>();
			while (rs.next()) {
				Unidad a = new Unidad();
				a.setClave(rs.getString(1));
				a.setNumeroUnidad(rs.getString(2));
				a.setNombre(rs.getString(3));
				a.setClaveMateria(rs.getString(4));
				lis.add(a);
			}

			return lis.toArray(new Unidad[lis.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Materia[] getMaterias() {

		String slq = "Select * from materia where cve_per = " + tablaperiodo.getPeriodoActual().getCvePer();

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();

			List<Materia> lis = new ArrayList<>();

			while (rs.next()) {
				Materia a = new Materia();
				a.setClave(rs.getString(1));
				a.setNombre(rs.getString(2));
				lis.add(a);
			}

			return lis.toArray(new Materia[lis.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Materia[] getMateriasConGrupo() {

		String slq = "select * from materia where cve_mate in(select cve_mate from grupomateria where cve_per in (select cve_per from periodo where status = 1))";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();

			List<Materia> lis = new ArrayList<>();

			while (rs.next()) {
				Materia a = new Materia();
				a.setClave(rs.getString(1));
				a.setNombre(rs.getString(2));
				lis.add(a);
			}

			return lis.toArray(new Materia[lis.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(Materia dato) {
		String slq = "Select * from materia where nom_mate = '" + dato.getNombre() + "'";

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
