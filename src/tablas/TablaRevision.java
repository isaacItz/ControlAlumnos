package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.CriterioUnidad;
import modelo.GrupoMateria;
import modelo.Revision;
import modelo.Unidad;

public class TablaRevision extends Tabla<Revision> {

	public TablaRevision(Connection conexion) {
		super(conexion, "Revision");
	}

	public String agregar(Revision dato) {
		return super.insertar(0, dato.getNumRev(), dato.getNomRev(), dato.getCveCriUni());
	}

	@Override
	public String modificar(Revision dato) {
		sql = "Update Revision Set num_rev='" + dato.getNumRev() + ", nombre_rev = '" + dato.getNomRev()
				+ "' ' Where cve_rev='" + dato.getCveRev() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(Revision dato) {
		sql = "DELETE FROM Revision WHERE cve_rev ='" + dato.getCveRev() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(Revision[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			insertar(0, dato[i].getNumRev(), dato[i].getNomRev(), dato[i].getCveCriUni());
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public Revision buscar(Revision dato) {

		String slq = "Select * from Revision where nombre_rev = '" + dato.getNomRev() + "' and cve_criuni = '"
				+ dato.getCveCriUni() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Revision a = new Revision();

			if (rs.next()) {
				a.setCveRev(rs.getInt(1));
				a.setNumRev(rs.getShort(2));
				a.setNomRev(rs.getString(3));
				a.setCveCriUni(rs.getString(4));
				return a;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Revision[] getRevisionesCriterio(CriterioUnidad cu) {
		sql = "select * from revision where cve_criuni = " + cu.getClave();

		try {
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			List<Revision> lis = new ArrayList<Revision>();
			while (rs.next()) {
				Revision a = new Revision();
				a.setCveRev(rs.getInt(1));
				a.setNumRev(rs.getShort(2));
				a.setNomRev(rs.getString(3));
				a.setCveCriUni(rs.getString(4));
				lis.add(a);
			}

			return lis.toArray(new Revision[lis.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Revision[] getRevisionesPorCriterio(CriterioUnidad cU) {
		sql = "select * from revision where cve_criuni = " + cU.getClave();
		try {
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			List<Revision> lis = new ArrayList<Revision>();
			while (rs.next()) {
				Revision a = new Revision();
				a.setCveRev(rs.getInt(1));
				a.setNumRev(rs.getShort(2));
				a.setNomRev(rs.getString(3));
				a.setCveCriUni(rs.getString(4));
				lis.add(a);
			}

			return lis.toArray(new Revision[lis.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public CriterioUnidad[] getEvaluacionesRealizadas(GrupoMateria gpo, Unidad u) {

		sql = "select x.cve_criuni, x.cve_cri, x.valor_cri, x.cve_uni, x.cve_grumat from (select * from criteriounidad where cve_uni = "
				+ u.getClave() + " and cve_grumat = " + gpo.getClaveGrupoMateria()
				+ ") as x join revision r on r.cve_criuni = x.cve_criuni group by x.cve_criuni";
		System.out.println(sql);

		try {
			statement = conexion.prepareStatement(sql);
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

	public int alumnosEvaluados(CriterioUnidad c) {
		String slq = " select count(*) from Revision where cve_criuni = " + c.getClave();
		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public void borrarRevisiones(CriterioUnidad cri) {

		String slq = "delete from renglonrevision where cve_rev in(select cve_rev from revision where cve_criuni = "
				+ cri.getClave() + ") ";
		try {
			statement = conexion.prepareStatement(slq);
			statement.executeUpdate();
			slq = "Delete from revision where cve_cri = " + cri.getClave();
			statement = conexion.prepareStatement(slq);
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean existeRevision(CriterioUnidad dato) {
		String slq = "Select * from Revision where cve_criuni = '" + dato.getClave() + "' ";
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

	@Override
	public boolean existe(Revision dato) {
		String slq = "Select * from Revision where nombre_rev = '" + dato.getNomRev() + "' and cve_criuni = '"
				+ dato.getCveCriUni() + "' ";
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
