package modelo;

import java.sql.Connection;

import tablas.TablaAlumno;
import tablas.TablaDocente;
import tablas.TablaGrupo;
import tablas.TablaGrupoMateria;
import tablas.TablaGrupoMateriaAlumno;
import tablas.TablaMateria;
import tablas.Tablaperiodo;

public class TablaLista {

	private final String EXITO = "Exito";
	private TablaAlumno tablaAlumno;
	private TablaDocente tablaDocente;
	private TablaMateria tablaMateria;
	private TablaGrupo tablaGrupo;
	private Tablaperiodo tablaperiodo;
	private Connection conexion;

	public TablaLista(Connection conexion) {
		tablaAlumno = new TablaAlumno(conexion);
		tablaDocente = new TablaDocente(conexion);
		tablaGrupo = new TablaGrupo(conexion);
		tablaMateria = new TablaMateria(conexion);
		tablaperiodo = new Tablaperiodo(conexion);
		this.conexion = conexion;
	}

	public String insertar(EstructuraLista cabecera, Alumno[] lista) {

		if (!tablaMateria.existe(cabecera.getMateria()))
			tablaMateria.agregar(cabecera.getMateria());
		if (!tablaDocente.existe(cabecera.getProfesor()))
			tablaDocente.agregar(cabecera.getProfesor());
		if (!tablaGrupo.existe(cabecera.getGrupo()))
			tablaGrupo.agregar(cabecera.getGrupo());
		if (!tablaperiodo.existe(cabecera.getPeriodo()))
			tablaperiodo.agregar(cabecera.getPeriodo());

		tablaAlumno.agregar(lista);
		Materia m = tablaMateria.buscar(cabecera.getMateria());
		Periodo p = tablaperiodo.buscar(cabecera.getPeriodo());

		GrupoMateria gm = new GrupoMateria();
		gm.setNombreGrupo(cabecera.getGrupo().getNombre());
		gm.setCveMateria(m.getClave());
		gm.setCvePeriodo(p.getCvePer());

		TablaGrupoMateria tgm = new TablaGrupoMateria(conexion);
		if (!tgm.existe(gm))
			System.out.println(tgm.agregar(gm));

		String clave = tgm.buscar(gm).getClaveGrupoMateria();

		GrupoMateriaAlumno gma = new GrupoMateriaAlumno();
		gma.setClabeGpoMat(clave);
		TablaGrupoMateriaAlumno tgma = new TablaGrupoMateriaAlumno(conexion);

		for (int i = 0; i < lista.length; i++) {
			gma.setClaveAlumno(lista[i].getNoControl());
			if (!tgma.existe(gma))
				tgma.agregar(gma);
		}

		return EXITO;
	}

}
