package modelo;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class Periodo {

	private int cvePer;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;

	public int getCvePer() {
		return cvePer;
	}

	public void setCvePer(int cvePer) {
		this.cvePer = cvePer;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getPeridoFormateado() {
		String salida = fechaInicio.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")).toString() + "/"
				+ fechaInicio.getYear();
		salida += fechaFin.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")).toString() + "/"
				+ fechaFin.getYear();
		return salida;
	}

	@Override
	public String toString() {
		return "Periodo [cvePer=" + cvePer + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + "]";
	}

}
