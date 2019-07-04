package modelo;

public class GrupoMateriaAlumno {

	private String claveGpoMatAlum;
	private String claveAlumno;
	private String clabeGpoMat;

	public GrupoMateriaAlumno() {
		super();
	}

	public GrupoMateriaAlumno(String claveGpoMatAlum, String claveAlumno, String clabeGpoMat) {
		super();
		this.claveGpoMatAlum = claveGpoMatAlum;
		this.claveAlumno = claveAlumno;
		this.clabeGpoMat = clabeGpoMat;
	}

	public String getClaveGpoMatAlum() {
		return claveGpoMatAlum;
	}

	public void setClaveGpoMatAlum(String claveGpoMatAlum) {
		this.claveGpoMatAlum = claveGpoMatAlum;
	}

	public String getClaveAlumno() {
		return claveAlumno;
	}

	public void setClaveAlumno(String claveAlumno) {
		this.claveAlumno = claveAlumno;
	}

	public String getClabeGpoMat() {
		return clabeGpoMat;
	}

	public void setClabeGpoMat(String clabeGpoMat) {
		this.clabeGpoMat = clabeGpoMat;
	}

	@Override
	public String toString() {
		return "GrupoMateriaAlumno [claveGpoMatAlum=" + claveGpoMatAlum + ", claveAlumno=" + claveAlumno
				+ ", clabeGpoMat=" + clabeGpoMat + "]";
	}

}
