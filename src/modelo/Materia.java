package modelo;

public class Materia extends Elemento {

	private String claveDepartamento;

	public Materia() {
		super();
	}

	public Materia(String clave, String nombre, String claveDepartamento) {
		super(clave, nombre);
		this.claveDepartamento = claveDepartamento;
	}

	public String getClaveDepartamento() {
		return claveDepartamento;
	}

	public void setClaveDepartamento(String claveDepartamento) {
		this.claveDepartamento = claveDepartamento;
	}

	@Override
	public String toString() {
		return "Materia [claveDepartamento=" + claveDepartamento + ", getClave()=" + getClave() + ", getNombre()="
				+ getNombre() + "]";
	}

}
