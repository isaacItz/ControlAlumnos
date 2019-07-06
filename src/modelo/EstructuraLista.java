package modelo;

public class EstructuraLista {

	private Materia materia;
	private Docente profesor;
	private Periodo periodo;
	private String clave;
	private Grupo grupo;
	private int alumnos;
	private String departamento;

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public EstructuraLista() {

	}

	public EstructuraLista(Materia materia, Docente profesor, Periodo periodo, String clave, Grupo grupo, int alumnos) {
		super();
		this.materia = materia;
		this.profesor = profesor;
		this.periodo = periodo;
		this.clave = clave;
		this.grupo = grupo;
		this.alumnos = alumnos;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	public Docente getProfesor() {
		return profesor;
	}

	public void setProfesor(Docente profesor) {
		this.profesor = profesor;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public int getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(int alumnos) {
		this.alumnos = alumnos;
	}

	@Override
	public String toString() {
		return "EstructuraLista [materia=" + materia + ", profesor=" + profesor + ", periodo=" + periodo + ", clave="
				+ clave + ", grupo=" + grupo + ", alumnos=" + alumnos + "]";
	}

}
