package modelo;

public class PaseLista {
	private String clave;
	private String fecha;
	private String gruMat;

	public PaseLista(String clave, String fecha, String gruMat) {
		super();
		this.clave = clave;
		this.fecha = fecha;
		this.gruMat = gruMat;
	}

	public PaseLista() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getGruMat() {
		return gruMat;
	}

	public void setGruMat(String gruMat) {
		this.gruMat = gruMat;
	}

	@Override
	public String toString() {
		return "PaseLista [clave=" + clave + ", fecha=" + fecha + ", gruMat=" + gruMat + "]";
	}

}
