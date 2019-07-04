package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.FaltasGruMat;

public class TablaFaltasGruMat extends Tabla<FaltasGruMat> {

	public TablaFaltasGruMat(Connection conexion) {
		super(conexion, "FaltasGruMat");
	}

	public String agregar(FaltasGruMat dato) {
		return super.insertar("0", dato.getNumFaltas(), dato.getCveGruMat());
	}

	@Override
	public String modificar(FaltasGruMat dato) {
		sql = "Update FaltasGruMat Set numero_de_faltas='" + dato.getNumFaltas() + "' Where cve_faltasGRuMat ='"
				+ dato.getCveFaltasGrumat() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(FaltasGruMat dato) {
		sql = "DELETE FROM FaltasGruMat WHERE cve_faltasGRuMat ='" + dato.getCveFaltasGrumat() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(FaltasGruMat[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			insertar("NULL", dato[i].getNumFaltas(), dato[i].getCveGruMat());
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public FaltasGruMat buscar(FaltasGruMat dato) {

		String slq = "Select * from FaltasGruMat where cve_grumat = '" + dato.getCveGruMat() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			FaltasGruMat a = new FaltasGruMat();

			if (rs.next()) {
				a.setCveFaltasGrumat(rs.getString(1));
				a.setNumFaltas(rs.getInt(2));
				a.setCveGruMat(rs.getString(3));
				return a;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(FaltasGruMat dato) {
		String slq = "Select * from FaltasGruMat where cve_grumat = '" + dato.getCveGruMat() + "'";

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
