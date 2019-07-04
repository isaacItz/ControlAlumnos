package pruebas;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import modelo.Alumno;
import modelo.BaseDatos;
import modelo.Utileria;

public class PruebasBD {

	public static void main(String[] args) {

		BaseDatos bd = new BaseDatos("escuela", "root", "maracana");
		bd.setDriver("com.mysql.jdbc.Driver");
		bd.setProtocolo("jdbc:mysql://localhost/");
		System.out.println(bd.hacerConexion());
		Connection conexion = bd.getConexion();
		File archivo = Utileria.getArchivo("C:\\Users\\JORGE\\eclipse-workspace\\Listas\\src\\recursos");

		String lista = null;
		try {
			lista = Utileria.leerPDF(archivo);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		EstructuraLista estruct = Utileria.geEstructuraLista(lista);

		String datos[] = lista.split("\n");

		PreparedStatement statement;

		for (int i = 8; i < datos.length - 1; i++) {
			String temp[] = Utileria.generarAlumno(datos[i]);
			Alumno tem = Utileria.generarAlumno(temp);
			try {
				String sql = "Update Alumno set nom_alum = '" + tem.getNombre() + "' where cve_alum = "
						+ tem.getNoControl();
				System.out.println(sql);
				statement = conexion.prepareStatement(sql);
				statement.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
