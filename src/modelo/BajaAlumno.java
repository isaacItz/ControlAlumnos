package modelo;

public class BajaAlumno {

	private String numBajaAlumno;
	private String motivoBaja;
	private String cveGruMatAlum;

	public String getNumBajaAlumno() {
		return numBajaAlumno;
	}

	public void setNumBajaAlumno(String numBajaAlumno) {
		this.numBajaAlumno = numBajaAlumno;
	}

	public String getMotivoBaja() {
		return motivoBaja;
	}

	public void setMotivoBaja(String motivoBaja) {
		this.motivoBaja = motivoBaja;
	}

	public String getCveGruMatAlum() {
		return cveGruMatAlum;
	}

	public void setCveGruMatAlum(String cveGruMatAlum) {
		this.cveGruMatAlum = cveGruMatAlum;
	}

	@Override
	public String toString() {
		return "BajaAlumno [numBajaAlumno=" + numBajaAlumno + ", motivoBaja=" + motivoBaja + ", cveGruMatAlum="
				+ cveGruMatAlum + "]";
	}

}
