package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.BajaAlumno;

public class TablaBajaAlumno extends Tabla<BajaAlumno> {

	public TablaBajaAlumno(Connection conexion) {
		super(conexion, "BajaAlumno");
	}

	public String agregar(BajaAlumno dato) {
		return super.insertar("0", dato.getMotivoBaja(), dato.getCveGruMatAlum());
	}

	@Override
	public String modificar(BajaAlumno dato) {
		sql = "Update BajaAlumno Set motivo_baja='" + dato.getMotivoBaja() + "' Where num_bajaAlumno ='"
				+ dato.getNumBajaAlumno() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(BajaAlumno dato) {
		sql = "DELETE FROM BajaAlumno WHERE num_bajaAlumno ='" + dato.getNumBajaAlumno() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(BajaAlumno[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			insertar("NULL", dato[i].getMotivoBaja(), dato[i].getCveGruMatAlum());
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public BajaAlumno buscar(BajaAlumno dato) {

		String slq = "Select * from BajaAlumno where cve_grumatalum = '" + dato.getCveGruMatAlum() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			BajaAlumno a = new BajaAlumno();

			if (rs.next()) {
				a.setNumBajaAlumno(rs.getString(1));
				a.setMotivoBaja(rs.getString(2));
				a.setCveGruMatAlum(rs.getString(3));
				return a;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(BajaAlumno dato) {
		String slq = "Select * from BajaAlumno where cve_grumatalum = '" + dato.getCveGruMatAlum() + "'";

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
