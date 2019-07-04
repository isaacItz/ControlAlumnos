package modelo;

public class Institucion extends Elemento {

	private String subdireccion;

	public Institucion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Institucion(String clave, String nombre, String subdireccion) {
		super(clave, nombre);
		this.subdireccion = subdireccion;

	}

	public String getSubdireccion() {
		return subdireccion;
	}

	public void setSubdireccion(String subdireccion) {
		this.subdireccion = subdireccion;
	}

	@Override
	public String toString() {
		return "Institucion [subdireccion=" + subdireccion + "]";
	}

}
