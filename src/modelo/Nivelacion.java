package modelo;

public class Nivelacion {

	private int cveNiv;
	private int cveGpoMatAlumno;
	private int calificacion;
	private int cveUnidad;

	public int getCveNiv() {
		return cveNiv;
	}

	public void setCveNiv(int cveNiv) {
		this.cveNiv = cveNiv;
	}

	public int getCveGpoMatAlumno() {
		return cveGpoMatAlumno;
	}

	public void setCveGpoMatAlumno(int cveGpoMatAlumno) {
		this.cveGpoMatAlumno = cveGpoMatAlumno;
	}

	public int getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}

	public int getCveUnidad() {
		return cveUnidad;
	}

	public void setCveUnidad(int cveUnidad) {
		this.cveUnidad = cveUnidad;
	}

	@Override
	public String toString() {
		return "Nivelacion [cveNiv=" + cveNiv + ", cveGpoMatAlumno=" + cveGpoMatAlumno + ", calificacion="
				+ calificacion + ", cveUnidad=" + cveUnidad + "]";
	}

}
