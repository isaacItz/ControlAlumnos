package modelo;

public class FaltasGruMat {

	private String cveFaltasGrumat;
	private int numFaltas;
	private String cveGruMat;

	public String getCveFaltasGrumat() {
		return cveFaltasGrumat;
	}

	public void setCveFaltasGrumat(String cveFaltasGrumat) {
		this.cveFaltasGrumat = cveFaltasGrumat;
	}

	public int getNumFaltas() {
		return numFaltas;
	}

	public void setNumFaltas(int numFaltas) {
		this.numFaltas = numFaltas;
	}

	public String getCveGruMat() {
		return cveGruMat;
	}

	public void setCveGruMat(String cveGruMat) {
		this.cveGruMat = cveGruMat;
	}

	@Override
	public String toString() {
		return "FaltasGruMar [cveFaltasGrumat=" + cveFaltasGrumat + ", numFaltas=" + numFaltas + ", cveGruMat="
				+ cveGruMat + "]";
	}

}
