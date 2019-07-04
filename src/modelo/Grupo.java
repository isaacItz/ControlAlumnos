package modelo;

public class Grupo {

	private String nombre;

	public Grupo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Grupo(String nombre) {
		super();
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Grupo [nombre=" + nombre + "]";
	}

}
