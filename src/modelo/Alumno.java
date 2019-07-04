package modelo;

public class Alumno {
	private String noControl;
	private String nombre;

	public String getNoControl() {
		return noControl;
	}

	public void setNoControl(String noControl) {
		this.noControl = noControl;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Alumno [noControl=" + noControl + ", nombre=" + nombre + "]";
	}

}