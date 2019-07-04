package modelo;

public class CriterioUnidad {
	private String clave;
	private String cvecri;
	private String valor;
	private String grupoMateria;
	private String claveUnidad;

	public CriterioUnidad(String clave, String cvecri, String valor, String grupoMateria, String claveUnidad) {
		super();
		this.clave = clave;
		this.cvecri = cvecri;
		this.valor = valor;
		this.grupoMateria = grupoMateria;
		this.claveUnidad = claveUnidad;
	}

	public CriterioUnidad() {
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getCvecri() {
		return cvecri;
	}

	public void setCvecri(String cvecri) {
		this.cvecri = cvecri;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getGrupoMateria() {
		return grupoMateria;
	}

	public void setGrupoMateria(String grupoMateria) {
		this.grupoMateria = grupoMateria;
	}

	public String getClaveUnidad() {
		return claveUnidad;
	}

	public void setClaveUnidad(String claveUnidad) {
		this.claveUnidad = claveUnidad;
	}

	@Override
	public String toString() {
		return "CriterioUnidad [clave=" + clave + ", cvecri=" + cvecri + ", valor=" + valor + ", grupoMateria="
				+ grupoMateria + ", claveUnidad=" + claveUnidad + "]";
	}

}
