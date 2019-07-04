package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.Periodo;

public class Tablaperiodo extends Tabla<Periodo> {

	private Periodo periodoActual;

	public Tablaperiodo(Connection conexion) {
		super(conexion, "Periodo");
		periodoActual = null;
	}

	public String agregar(Periodo dato) {
		return super.insertar("0", dato.getFechaInicio(), dato.getFechaFin());
	}

	@Override
	public String modificar(Periodo dato) {
		sql = "Update Periodo Set fecha_iniPer ='" + dato.getFechaInicio() + "'  and fecha_finPer = '"
				+ dato.getFechaFin() + "' Where cve_per ='" + dato.getCvePer() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(Periodo dato) {
		sql = "DELETE FROM Periodo WHERE cve_per ='" + dato.getCvePer() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public Periodo buscar(Periodo dato) {

		String slq = "Select * from periodo where fecha_iniPer = '" + dato.getFechaInicio() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Periodo a = new Periodo();
			rs.next();

			a.setCvePer(rs.getInt(1));
			a.setFechaInicio(LocalDate.parse(rs.getDate(2).toString()));
			a.setFechaFin(LocalDate.parse(rs.getDate(3).toString()));
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Periodo[] getPeriodos() {

		String slq = "Select * from periodo ";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();

			List<Periodo> lis = new ArrayList<>();

			while (rs.next()) {
				Periodo a = new Periodo();
				a.setCvePer(rs.getInt(1));
				a.setFechaInicio(LocalDate.parse(rs.getString(2)));
				a.setFechaFin(LocalDate.parse(rs.getString(3)));
				lis.add(a);
			}

			return lis.toArray(new Periodo[lis.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Periodo getPeriodoActual() {
		if (periodoActual != null) {
			return periodoActual;
		} else {
			String slq = "select * from periodo where fecha_finPer = (select max(fecha_finPer) from periodo)";
			try {
				statement = conexion.prepareStatement(slq);
				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					periodoActual = new Periodo();
					periodoActual.setCvePer(rs.getInt(1));
					periodoActual.setFechaInicio(LocalDate.parse(rs.getString(2)));
					periodoActual.setFechaFin(LocalDate.parse(rs.getString(3)));
				}
				return periodoActual;
			} catch (Exception e) {
				return null;
			}
		}

	}

	@Override
	public boolean existe(Periodo dato) {
		String slq = "Select * from periodo where fecha_iniPer = '" + dato.getFechaInicio() + "'";

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
