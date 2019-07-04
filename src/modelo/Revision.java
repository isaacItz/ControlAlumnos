package modelo;

public class Revision {

	private int cveRev;
	private short numRev;
	private String nomRev;
	private String cveCriUni;

	public int getCveRev() {
		return cveRev;
	}

	public void setCveRev(int cveRev) {
		this.cveRev = cveRev;
	}

	public short getNumRev() {
		return numRev;
	}

	public void setNumRev(short numRev) {
		this.numRev = numRev;
	}

	public String getNomRev() {
		return nomRev;
	}

	public void setNomRev(String nomRev) {
		this.nomRev = nomRev;
	}

	public String getCveCriUni() {
		return cveCriUni;
	}

	public void setCveCriUni(String cveCriUni) {
		this.cveCriUni = cveCriUni;
	}

	@Override
	public String toString() {
		return "Revision [cveRev=" + cveRev + ", numRev=" + numRev + ", nomRev=" + nomRev + ", cveCriUni=" + cveCriUni
				+ "]";
	}

}
