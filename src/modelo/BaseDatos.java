package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDatos {
	private Connection conexion;
	private String nombreBD;
	private String usuario;
	private String password;
	private String protocolo;
	private String driver;

	public BaseDatos(String nombreBD, String usuario, String password) {
		super();
		this.nombreBD = nombreBD;
		this.usuario = usuario;
		this.password = password;
	}

	public String hacerConexion() {
		try {
//			Class.forName(driver);
			conexion = DriverManager.getConnection(protocolo + nombreBD, usuario, password);
			return "Exito";
		} catch (SQLException esql) {
			return esql.toString();
		}
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getNombreBD() {
		return nombreBD;
	}

	public String getPassword() {
		return password;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setConexion(Connection conexion) {
		this.conexion = conexion;
	}

	public void setNombreBD(String nombreBD) {
		this.nombreBD = nombreBD;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public Connection getConexion() {
		return conexion;
	}

}
