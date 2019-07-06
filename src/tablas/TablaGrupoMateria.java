package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.CriterioUnidad;
import modelo.GrupoMateria;
import modelo.Unidad;

public class TablaGrupoMateria extends Tabla<GrupoMateria> {

	private Tablaperiodo tablaperiodo;

	public TablaGrupoMateria(Connection conexion) {
		super(conexion, "GrupoMateria");
		tablaperiodo = new Tablaperiodo(conexion);
	}

	public String agregar(GrupoMateria dato) {
		return super.insertar("0", dato.getNombreGrupo(), dato.getCveMateria(), dato.getCvePeriodo());
	}

	@Override
	public String modificar(GrupoMateria dato) {
		sql = "Update GrupoMateria Set nom_gru='" + dato.getNombreGrupo() + "', cve_mate= '" + dato.getCveMateria()
				+ "', cve_per = '" + dato.getCvePeriodo() + "' Where cve_grumat ='" + dato.getClaveGrupoMateria() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(GrupoMateria dato) {
		sql = "DELETE FROM GrupoMateria WHERE clave ='" + dato.getClaveGrupoMateria() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(GrupoMateria[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			insertar("0", dato[i].getNombreGrupo(), dato[i].getCveMateria(), dato[i].getCvePeriodo());
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public GrupoMateria buscar(GrupoMateria dato) {

		String slq = "select * from grupomateria where nom_gru = '" + dato.getNombreGrupo() + "' and cve_mate = '"
				+ dato.getCveMateria() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			GrupoMateria a = new GrupoMateria();
			rs.next();

			a.setClaveGrupoMateria(rs.getString(1));
			a.setNombreGrupo(rs.getString(2));
			a.setCveMateria(rs.getString(3));
			a.setCvePeriodo(rs.getInt(4));
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getTotalAlumnos(String cveGruMat) {
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

	public CriterioUnidad[] getCriteriosUnidad(GrupoMateria gpo, Unidad u) {
		sql = "select * from criteriounidad where cve_grumat = " + gpo.getClaveGrupoMateria() + " and cve_uni = "
				+ u.getClave();
		try {
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			List<CriterioUnidad> lis = new ArrayList<>();

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

	public GrupoMateria[] getCurrentGroups() {

		String slq = "select cve_grumat,nom_gru,cve_mate,p.cve_per from  grupomateria gm join periodo p on p.cve_per = gm.cve_per where gm.cve_per = "
				+ tablaperiodo.getPeriodoActual().getCvePer();

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();

			List<GrupoMateria> lis = new ArrayList<>();

			while (rs.next()) {
				GrupoMateria a = new GrupoMateria();

				a.setClaveGrupoMateria(rs.getString(1));
				a.setNombreGrupo(rs.getString(2));
				a.setCveMateria(rs.getString(3));
				a.setCvePeriodo(rs.getInt(4));
				lis.add(a);
			}

			return lis.toArray(new GrupoMateria[lis.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public GrupoMateria getGrupoMateria(GrupoMateria gpo) {

		sql = "select * from grupomateria where cve_grumat = " + gpo.getClaveGrupoMateria();
		try {
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			GrupoMateria a = new GrupoMateria();
			if (rs.next()) {
				a.setClaveGrupoMateria(rs.getString(1));
				a.setNombreGrupo(rs.getString(2));
				a.setCveMateria(rs.getString(3));
				a.setCvePeriodo(rs.getInt(4));
				return a;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(GrupoMateria dato) {
		String slq = "select * from grupomateria where nom_gru = '" + dato.getNombreGrupo() + "' and cve_mate = '"
				+ dato.getCveMateria() + "' and cve_per = '" + dato.getCvePeriodo() + "'";

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
