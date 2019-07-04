package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import modelo.RenglonRevision;
import modelo.Revision;

public class TablaRenglonRevision extends Tabla<RenglonRevision> {

	public TablaRenglonRevision(Connection conexion) {
		super(conexion, "RenglonRevision");
	}

	public String agregar(RenglonRevision dato) {
		return super.insertar(0, dato.getCalfRenRev(), dato.getCveAlum(), dato.getFechaRenRev(), dato.getCveRev());
	}

	@Override
	public String modificar(RenglonRevision dato) {
		sql = "Update RenglonRevision Set calf_renRev='" + dato.getCalfRenRev() + "' Where num_renRev='"
				+ dato.getNumRengRev() + "'";

		try {
			System.out.println(sql);
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(RenglonRevision dato) {
		sql = "DELETE FROM RenglonRevision WHERE num_renRev ='" + dato.getNumRengRev() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String eliminarRenglones(Revision dato) {
		sql = "DELETE FROM RenglonRevision WHERE cve_Rev ='" + dato.getCveRev() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(RenglonRevision[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			insertar(0, dato[i].getCalfRenRev(), dato[i].getCveAlum(), dato[i].getFechaRenRev(), dato[i].getCveRev());
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public RenglonRevision buscar(RenglonRevision dato) {

		String slq = "Select * from RenglonRevision where cve_alum = '" + dato.getCveAlum() + "' and cve_rev = '"
				+ dato.getCveRev() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			RenglonRevision a = new RenglonRevision();

			if (rs.next()) {
				a.setNumRengRev(rs.getString(1));
				a.setCalfRenRev(rs.getString(2));
				a.setCveAlum(rs.getString(3));
				a.setFechaRenRev(LocalDate.parse(rs.getString(4)));
				a.setCveRev(rs.getString(5));
				return a;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(RenglonRevision dato) {
		String slq = "Select * from RenglonRevision where cve_alum = '" + dato.getCveAlum() + "' and cve_rev = '"
				+ dato.getCveRev() + "'";

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
