package modelo;

public abstract class Elemento {

	private String clave;
	private String nombre;

	public Elemento() {
		super();
	}

	public Elemento(String clave, String nombre) {
		this.clave = clave;
		this.nombre = nombre;
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

	@Override
	public String toString() {
		return "clave=" + clave + ", nombre=" + nombre;
	}

}
