package modelo;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class Utileria {
	public static String leerPDF(File file) throws IOException {
		String text = new String();
		try (PDDocument doc = PDDocument.load(file)) {
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(doc);
			return text;
		}
	}

	public static Alumno generarAlumno(String linea[]) {

		Alumno a = new Alumno();
		a.setNoControl(linea[1]);
		a.setNombre(linea[2]);

		return a;
	}

	public static String[] generarAlumno(String linea) {
		String[] aux = linea.split(" ");
		System.out.println(linea);
		String[] salida = new String[6];
		salida[0] = aux[0];
		salida[1] = aux[1];
		salida[4] = " ";
		int i = 4;

		for (; i < aux.length - 2; i++) {
			if (!aux[i].equalsIgnoreCase("curso")) {
				if (aux[i].equals(" "))
					continue;
				salida[4] += aux[i];
				if (i < aux.length - 3)
					salida[4] = salida[4].concat(" ");
			}

		}

		salida[2] = aux[2];
		salida[3] = aux[3].trim();

		salida[2] += " ".concat(salida[3]).concat(salida[4]);

		salida[5] = (aux[aux.length - 2].concat(" ").concat(aux[aux.length - 1]));
		return salida;

	}

	public LocalDate toLocalDate(Date date) {
		LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return ld;
	}

	public static EstructuraLista geEstructuraLista(String lista) {
		String cabecera[] = lista.split("\n", 8);

		EstructuraLista estructura = new EstructuraLista();

		String aux[] = cabecera[4].split(" ");
		String auxn = "";
		for (int i = 1; i < aux.length - 2; i++) {
			auxn += aux[i] + " ";
		}
		estructura.setMateria(new Materia(null, auxn, null));
		estructura.setClave((aux[aux.length - 1].trim()));

		auxn = "";
		aux = cabecera[5].split(" ");
		for (int i = 1; i < aux.length - 2; i++) {
			auxn += aux[i] + " ";
		}
		estructura.setProfesor(new Docente(null, auxn.trim()));
		estructura.setGrupo(new Grupo(aux[aux.length - 1].trim()));

		auxn = "";
		aux = cabecera[6].split(" ");
		for (int i = 1; i < aux.length - 2; i++) {
			auxn += aux[i] + " ";
		}
		String mesi = auxn.split(" ")[0];
		String mesf = auxn.split(" ")[2].split("/")[0];
		String año = auxn.split("/")[1];
		Periodo p = new Periodo();
		Locale l = new Locale("es", "ES");
		SimpleDateFormat sd = new SimpleDateFormat("dd-MMMM-yyyy", l);
		SimpleDateFormat sd2 = new SimpleDateFormat("dd-MM-yyyy", l);
		String fe = "01-" + mesi.toLowerCase().trim() + "-" + año;
		String fe2 = "01-" + mesf.toLowerCase().trim() + "-" + año;

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy", l);
		try {
			String s = sd2.format(sd.parse(fe));
			String s2 = sd2.format(sd.parse(fe2));
			LocalDate feI = LocalDate.parse(s, dtf);
			LocalDate feF = LocalDate.parse(s2, dtf);
			p.setFechaInicio(feI);
			p.setFechaFin(feF);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		estructura.setPeriodo(p);
		estructura.setAlumnos(Integer.parseInt(aux[aux.length - 1].trim()));

		return estructura;
	}

	public static File getArchivo(String rutaDefecto) {
		JFileChooser selector = new JFileChooser(rutaDefecto);
		FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("pdf", "pdf");
		selector.setFileFilter(filtroImagen);
		int op = selector.showOpenDialog(null);

		if (op == JFileChooser.APPROVE_OPTION) {
			return selector.getSelectedFile();
		}
		return null;

	}

	public static void escribir(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje);
	}

}
