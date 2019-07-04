package modelo;

public class PaseListaAlumno {
	private String clave;
	private String estatus;
	private String alumno;
	private String paseLista;

	public PaseListaAlumno(String clave, String estatus, String alumno, String paseLista) {
		super();
		this.clave = clave;
		this.estatus = estatus;
		this.alumno = alumno;
		this.paseLista = paseLista;
	}

	public PaseListaAlumno() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getAlumno() {
		return alumno;
	}

	public void setAlumno(String alumno) {
		this.alumno = alumno;
	}

	public String getPaseLista() {
		return paseLista;
	}

	public void setPaseLista(String paseLista) {
		this.paseLista = paseLista;
	}

	@Override
	public String toString() {
		return "PaseListaAlumno [clave=" + clave + ", estatus=" + estatus + ", alumno=" + alumno + ", paseLista="
				+ paseLista + "]";
	}

}
