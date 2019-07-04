package modelo;

import java.time.LocalDate;

public class RenglonRevision {

	private String numRengRev;
	private String calfRenRev;
	private String cveAlum;
	private LocalDate fechaRenRev;
	private String cveRev;

	public String getNumRengRev() {
		return numRengRev;
	}

	public void setNumRengRev(String numRengRev) {
		this.numRengRev = numRengRev;
	}

	public String getCalfRenRev() {
		return calfRenRev;
	}

	public void setCalfRenRev(String calfRenRev) {
		this.calfRenRev = calfRenRev;
	}

	public LocalDate getFechaRenRev() {
		return fechaRenRev;
	}

	public void setFechaRenRev(LocalDate fechaRenRev) {
		this.fechaRenRev = fechaRenRev;
	}

	public String getCveRev() {
		return cveRev;
	}

	public void setCveRev(String cveRev) {
		this.cveRev = cveRev;
	}

	public String getCveAlum() {
		return cveAlum;
	}

	public void setCveAlum(String cveAlum) {
		this.cveAlum = cveAlum;
	}

	@Override
	public String toString() {
		return "RenglonRevision [numRengRev=" + numRengRev + ", calfRenRev=" + calfRenRev + ", cveAlum=" + cveAlum
				+ ", fechaRenRev=" + fechaRenRev + ", cveRev=" + cveRev + "]";
	}

}