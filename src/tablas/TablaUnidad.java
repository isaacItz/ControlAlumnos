package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.CriterioUnidad;
import modelo.GrupoMateria;
import modelo.Unidad;

public class TablaUnidad extends Tabla<Unidad> {

	public TablaUnidad(Connection conexion) {
		super(conexion, "Unidad");
	}

	public String agregar(Unidad dato) {
		return super.insertar(0, dato.getNumeroUnidad(), dato.getNombre(), dato.getClaveMateria());
	}

	@Override
	public String modificar(Unidad dato) {
		sql = "Update Unidad Set nom_uni='" + dato.getNombre() + "' Where cve_uni ='" + dato.getClave() + "'";
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(Unidad dato) {
		sql = "DELETE FROM Unidad WHERE cve_uni ='" + dato.getClave() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(Unidad[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			System.out.println(insertar(0, dato[i].getNombre(), dato[i].getClaveMateria()));
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public Unidad buscar(Unidad dato) {

		String slq = "Select * from Unidad where num_uni = '" + dato.getNumeroUnidad() + "' and cve_mate = '"
				+ dato.getClaveMateria() + "'";
		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Unidad a = new Unidad();
			if (rs.next()) {
				a.setClave(rs.getString(1));
				a.setNumeroUnidad(rs.getString(2));
				a.setNombre(rs.getString(3));
				a.setClaveMateria(rs.getString(4));
				return a;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Unidad[] buscarCoincidencias(Unidad dato) {

		String slq = "Select * from Unidad where cve_mate = '" + dato.getClaveMateria() + "'";

		try {
			statement = conexion.prepareStatement(slq);
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

	public CriterioUnidad[] getCriterios(Unidad u, GrupoMateria gpo) {

		String slq = "select * from criteriounidad where cve_uni = " + u.getClave() + " and cve_grumat = "
				+ gpo.getClaveGrupoMateria();

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();

			List<CriterioUnidad> lis = new ArrayList<CriterioUnidad>();

			while (rs.next()) {
				CriterioUnidad a = new CriterioUnidad();

				a.setClave(rs.getString(1));
				a.setCvecri(rs.getString(2));
				a.setValor(rs.getString(3));
				a.setClaveUnidad(rs.getString(4));
				a.setGrupoMateria(rs.getString(5));

				lis.add(a);
			}

			return lis.toArray(new CriterioUnidad[lis.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Unidad[] getUnidades() {

		String slq = "select * from unidad ";

		try {
			statement = conexion.prepareStatement(slq);
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

	@Override
	public boolean existe(Unidad dato) {
		String slq = "Select * from Unidad where num_uni = '" + dato.getNumeroUnidad() + "' and cve_mate = '"
				+ dato.getClaveMateria() + "'";

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
