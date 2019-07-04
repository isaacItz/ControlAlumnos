package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.CriterioUnidad;

public class TablaCriterioUnidad extends Tabla<CriterioUnidad> {

	public TablaCriterioUnidad(Connection conexion) {
		super(conexion, "CriterioUnidad");
	}

	public String agregar(CriterioUnidad dato) {
		return super.insertar("0", dato.getCvecri(), dato.getValor(), dato.getClaveUnidad(), dato.getGrupoMateria());
	}

	@Override
	public String modificar(CriterioUnidad dato) {
		sql = "Update CriterioUnidad Set cve_cri='" + dato.getCvecri() + "', valor_cri = '" + dato.getValor()
				+ "' Where cve_criuni ='" + dato.getClave() + "'";

		try {
			System.out.println(sql);
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(CriterioUnidad dato) {
		sql = "DELETE FROM CriterioUnidad WHERE cve_criuni ='" + dato.getClave() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(CriterioUnidad[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			insertar("0", dato[i].getCvecri(), dato[i].getValor(), dato[i].getClaveUnidad(), dato[i].getGrupoMateria());
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public CriterioUnidad buscar(CriterioUnidad dato) {

		String slq = "Select * from CriterioUnidad where cve_grumat = '" + dato.getGrupoMateria() + "' and cve_uni = '"
				+ dato.getClaveUnidad() + "' and cve_cri = '" + dato.getCvecri() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			CriterioUnidad a = new CriterioUnidad();
			if (rs.next()) {
				a.setClave(rs.getString(1));
				a.setCvecri(rs.getString(2));
				a.setValor(rs.getString(3));
				a.setClaveUnidad(rs.getString(4));
				a.setGrupoMateria(rs.getString(5));
				return a;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public CriterioUnidad getCriterioUnidad(CriterioUnidad dato) {

		String slq = "Select * from CriterioUnidad where cve_criuni = " + dato.getClave();

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			CriterioUnidad a = new CriterioUnidad();
			if (rs.next()) {
				a.setClave(rs.getString(1));
				a.setCvecri(rs.getString(2));
				a.setValor(rs.getString(3));
				a.setClaveUnidad(rs.getString(4));
				a.setGrupoMateria(rs.getString(5));
				return a;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public CriterioUnidad[] getCriterios() {

		String slq = "Select * from CriterioUnidad ";

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

	@Override
	public boolean existe(CriterioUnidad dato) {
		String slq = "Select * from CriterioUnidad where cve_grumat = '" + dato.getGrupoMateria() + "' and cve_uni = '"
				+ dato.getClaveUnidad() + "' and cve_cri = '" + dato.getCvecri() + "'";

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
