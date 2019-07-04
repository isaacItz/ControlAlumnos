package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.PaseListaAlumno;

public class TablaPaseListaAlumno extends Tabla<PaseListaAlumno> {

	public TablaPaseListaAlumno(Connection conexion) {
		super(conexion, "PaseListaAlumno");
	}

	public String agregar(PaseListaAlumno dato) {
		return super.insertar("0", dato.getEstatus(), dato.getPaseLista(), dato.getAlumno());
	}

	@Override
	public String modificar(PaseListaAlumno dato) {
		sql = "Update PaseListaAlumno Set estatus_palisalu='" + dato.getEstatus() + "' Where cve_palis ='"
				+ dato.getPaseLista() + "' and cve_alum = '" + dato.getAlumno() + "'";

		try {
			return String.valueOf(ejecutarQuery(sql));
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(PaseListaAlumno dato) {
		sql = "DELETE FROM PaseListaAlumno WHERE cve_palisalu ='" + dato.getClave() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(PaseListaAlumno[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			System.out.println(insertar("0", dato[i].getEstatus(), dato[i].getPaseLista(), dato[i].getAlumno()));
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public PaseListaAlumno buscar(PaseListaAlumno dato) {

		String slq = "Select * from PaseListaAlumno where cve_palis = '" + dato.getPaseLista() + "' and cve_alum = '"
				+ dato.getAlumno() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			PaseListaAlumno a = new PaseListaAlumno();
			rs.next();

			a.setClave(rs.getString(1));
			a.setEstatus(rs.getString(2));
			a.setPaseLista(rs.getString(3));
			a.setAlumno(rs.getString(4));
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(PaseListaAlumno dato) {
		String slq = "Select * from PaseListaAlumno where cve_palis = '" + dato.getPaseLista() + "' and cve_alum = '"
				+ dato.getAlumno() + "'";

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
