package modelo;

public class DocenteGrupoMat {

	int cve_docGM;
	int cve_doc;
	int cve_grumat;

	public int getCve_docGM() {
		return cve_docGM;
	}

	public void setCve_docGM(int cve_docGM) {
		this.cve_docGM = cve_docGM;
	}

	public int getCve_doc() {
		return cve_doc;
	}

	public void setCve_doc(int cve_doc) {
		this.cve_doc = cve_doc;
	}

	public int getCve_grumat() {
		return cve_grumat;
	}

	public void setCve_grumat(int cve_grumat) {
		this.cve_grumat = cve_grumat;
	}

	@Override
	public String toString() {
		return "DocenteGrupoMat [cve_docGM=" + cve_docGM + ", cve_doc=" + cve_doc + ", cve_grumat=" + cve_grumat + "]";
	}

}
