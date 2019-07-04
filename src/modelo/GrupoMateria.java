package modelo;

public class GrupoMateria {

	private String claveGrupoMateria;
	private String nombreGrupo;
	private String cveMateria;
	private int cvePeriodo;

	public GrupoMateria() {
		super();
	}

	public GrupoMateria(String claveGrupoMateria, String nombreGrupo, String cveMateria, int cvePeriodo) {
		super();
		this.claveGrupoMateria = claveGrupoMateria;
		this.nombreGrupo = nombreGrupo;
		this.cveMateria = cveMateria;
		this.cvePeriodo = cvePeriodo;
	}

	public String getClaveGrupoMateria() {
		return claveGrupoMateria;
	}

	public void setClaveGrupoMateria(String claveGrupoMateria) {
		this.claveGrupoMateria = claveGrupoMateria;
	}

	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	public String getCveMateria() {
		return cveMateria;
	}

	public void setCveMateria(String cveMateria) {
		this.cveMateria = cveMateria;
	}

	public int getCvePeriodo() {
		return cvePeriodo;
	}

	public void setCvePeriodo(int cvePeriodo) {
		this.cvePeriodo = cvePeriodo;
	}

	@Override
	public String toString() {
		return "GrupoMateria [claveGrupoMateria=" + claveGrupoMateria + ", nombreGrupo=" + nombreGrupo + ", cveMateria="
				+ cveMateria + ", cvePeriodo=" + cvePeriodo + "]";
	}

}
