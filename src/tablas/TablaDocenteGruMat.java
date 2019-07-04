package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.DocenteGrupoMat;

public class TablaDocenteGruMat extends Tabla<DocenteGrupoMat> {

	public TablaDocenteGruMat(Connection conexion) {
		super(conexion, "DocenteGrupoMat");
	}

	public String agregar(DocenteGrupoMat dato) {
		return super.insertar(dato.getCve_docGM(), dato.getCve_doc(), dato.getCve_grumat());
	}

	@Override
	public String modificar(DocenteGrupoMat dato) {
		sql = "Update DocenteGrupoMat set cve_doc = '" + dato.getCve_doc() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(DocenteGrupoMat dato) {
		sql = "DELETE FROM DocenteGrupoMat WHERE cve_docGM ='" + dato.getCve_docGM() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(DocenteGrupoMat[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			System.out.println(insertar(dato[i].getCve_docGM(), dato[i].getCve_doc(), dato[i].getCve_grumat()));
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public DocenteGrupoMat buscar(DocenteGrupoMat dato) {

		String slq = "Select * from DocenteGrupoMat where nom_gru = '" + dato.getCve_doc() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			DocenteGrupoMat a = new DocenteGrupoMat();
			rs.next();

			a.setCve_docGM(rs.getInt(1));
			a.setCve_docGM(rs.getInt(2));
			a.setCve_grumat(rs.getInt(3));
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public DocenteGrupoMat[]  buscarDoc(DocenteGrupoMat dato) {

		String slq = "Select * from DocenteGrupoMat where cve_grumat = '" + dato.getCve_grumat() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();

			List<DocenteGrupoMat> lis = new ArrayList<DocenteGrupoMat>();

			while (rs.next()) {
				DocenteGrupoMat a = new DocenteGrupoMat();
				a.setCve_docGM(rs.getInt(1));
				a.setCve_docGM(rs.getInt(2));
				a.setCve_grumat(rs.getInt(3));
				lis.add(a);

			}

			return lis.toArray(new DocenteGrupoMat[lis.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(DocenteGrupoMat dato) {
		String slq = "Select * from DocenteGrupoMat where nom_gru = '" + dato.getCve_doc() + "'";

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
