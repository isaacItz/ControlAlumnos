package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import modelo.GrupoMateria;
import modelo.PaseLista;

public class TablaPaseLista extends Tabla<PaseLista> {

	public TablaPaseLista(Connection conexion) {
		super(conexion, "PaseLista");
	}

	public String agregar(PaseLista dato) {
		return super.insertar("0", dato.getFecha(), dato.getGruMat());
	}

	@Override
	public String modificar(PaseLista dato) {
		sql = "Update PaseLista Set fecha_palis='" + dato.getFecha() + "' Where cve_palis ='" + dato.getClave() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(PaseLista dato) {
		sql = "DELETE FROM PaseLista WHERE cve_palis ='" + dato.getClave() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(PaseLista[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			System.out.println(insertar("0", dato[i].getFecha(), dato[i].getGruMat()));
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public PaseLista buscar(PaseLista dato) {

		String slq = "Select * from PaseLista where fecha_palis = '" + dato.getFecha() + "' and cve_grumat = '"
				+ dato.getGruMat() + "'";

		System.out.println(slq);

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			PaseLista a = new PaseLista();
			rs.next();

			a.setClave(rs.getString(1));
			a.setFecha(rs.getString(2));
			a.setGruMat(rs.getString(3));
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public LocalDate[] getFechasPasesLista(GrupoMateria gpo, LocalDate fechI, LocalDate fechF) {
		sql = "select fecha_palis from paselista where fecha_palis  >= '" + fechI + "' and fecha_palis <= '" + fechF
				+ "' and cve_grumat = " + gpo.getClaveGrupoMateria();
		try {
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			List<LocalDate> lista = new ArrayList<>();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			while (rs.next()) {
				LocalDate l = LocalDate.parse(rs.getString(1), dtf);
				lista.add(l);
			}

			return lista.toArray(new LocalDate[lista.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public PaseLista[] buscarPorFecha(PaseLista dato) {

		String slq = "Select * from PaseLista where fecha_palis = '" + dato.getFecha() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			List<PaseLista> lista = new ArrayList<PaseLista>();

			while (rs.next()) {
				PaseLista a = new PaseLista();
				a.setClave(rs.getString(1));
				a.setFecha(rs.getString(2));
				a.setGruMat(rs.getString(3));
				lista.add(a);
			}

			if (lista.size() == 0) {
				return null;
			}

			return lista.toArray(new PaseLista[lista.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Long[] getAlumnosConFaltas(GrupoMateria gpo) {
		sql = "select pla.cve_alum ,sum(pla.estatus_palisalu) asistencias from paselistaalumno pla where pla.cve_palis in (select cve_palis from paselista where cve_grumat = "
				+ gpo.getClaveGrupoMateria()
				+ ") group by pla.cve_alum having asistencias <= (select count(*) - numero_de_faltas from paselista pa join faltasGrumat fgm on fgm.cve_grumat = pa.cve_grumat where pa.cve_grumat = "
				+ gpo.getClaveGrupoMateria() + ")";
		try {
			statement = conexion.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			List<Long> lista = new ArrayList<>();
			while (result.next()) {
				lista.add(result.getLong(1));
			}
			return lista.toArray(new Long[lista.size()]);
		} catch (Exception e) {
		}
		return null;
	}

	public boolean sePuedePasarLista(GrupoMateria dato) {
		String slq = "select * from paselista where fecha_palis = curdate() and cve_grumat = "
				+ dato.getClaveGrupoMateria();

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			if (!rs.next())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean existe(PaseLista dato) {
		String slq = "Select * from PaseLista where fecha_palis = '" + dato.getFecha() + "' and cve_grumat = '"
				+ dato.getGruMat() + "'";

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
