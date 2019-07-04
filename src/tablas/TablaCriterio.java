package tablas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.Criterio;

public class TablaCriterio extends Tabla<Criterio> {

	public TablaCriterio(Connection conexion) {
		super(conexion, "Criterio");
	}

	public String agregar(Criterio dato) {
		return super.insertar("0", dato.getNombre());
	}

	@Override
	public String modificar(Criterio dato) {
		sql = "Update Criterio Set nom_Cri='" + dato.getNombre() + "' Where cve_cri ='" + dato.getClave() + "'";

		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public String eliminar(Criterio dato) {
		sql = "DELETE FROM Criterio WHERE cve_cri ='" + dato.getClave() + "'";
		try {
			ejecutarQuery(sql);
			return EXITO;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public String agregar(Criterio[] dato) {
		int i = 0;
		for (; i < dato.length; i++) {
			System.out.println(insertar(dato[i].getClave(), dato[i].getNombre()));
		}
		return i == dato.length ? EXITO : "Error";

	}

	@Override
	public Criterio buscar(Criterio dato) {

		String slq = "Select * from Criterio where nom_cri = '" + dato.getNombre() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Criterio a = new Criterio();

			if (rs.next()) {
				a.setClave(rs.getString(1));
				a.setNombre(rs.getString(2));
			}

			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Criterio getCriterio(Criterio dato) {

		String slq = "Select * from Criterio where cve_cri = '" + dato.getClave() + "'";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			Criterio a = new Criterio();

			if (rs.next()) {
				a.setClave(rs.getString(1));
				a.setNombre(rs.getString(2));
			}

			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Criterio[] getCriterios() {

		String slq = "Select * from Criterio ";

		try {
			statement = conexion.prepareStatement(slq);
			ResultSet rs = statement.executeQuery();
			List<Criterio> list = new ArrayList<>();
			while (rs.next()) {

				Criterio a = new Criterio();
				a.setClave(rs.getString(1));
				a.setNombre(rs.getString(2));
				list.add(a);
			}

			return list.toArray(new Criterio[list.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existe(Criterio dato) {
		String slq = "Select * from Criterio where nom_cri = '" + dato.getNombre() + "'";

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
