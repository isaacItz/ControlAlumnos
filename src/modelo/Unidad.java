package modelo;

public class Unidad {
	private String clave;
	private String numeroUnidad;
	private String nombre;
	private String claveMateria;

	public Unidad(String clave, String nombre, String claveMateria) {
		super();
		this.clave = clave;
		this.nombre = nombre;
		this.claveMateria = claveMateria;
	}

	public Unidad() {
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getClaveMateria() {
		return claveMateria;
	}

	public String getNumeroUnidad() {
		return numeroUnidad;
	}

	public void setNumeroUnidad(String numeroUnidad) {
		this.numeroUnidad = numeroUnidad;
	}

	public void setClaveMateria(String claveMateria) {
		this.claveMateria = claveMateria;
	}

	@Override
	public String toString() {
		return "Unidad [clave=" + clave + ", numeroUnidad=" + numeroUnidad + ", nombre=" + nombre + ", claveMateria="
				+ claveMateria + "]";
	}

}
