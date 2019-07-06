/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneradorPDF {
	private static EstructuraLista estructura;
	private static Font fuenteTitulo;
	private static Font fuenteNormal;

	public static void createPDF(Object[][] data, EstructuraLista estru, String tipoLista, File pdfNewFile,
			String... cols) {
		estructura = estru;

		fuenteTitulo = new Font();
		fuenteTitulo.setFamily("Calibri");
		fuenteTitulo.setColor(0, 0, 0);
		fuenteTitulo.setSize(10);
		fuenteTitulo.setStyle(Font.BOLD);

		fuenteNormal = new Font();
		fuenteNormal.setFamily("Calibri");
		fuenteNormal.setColor(0, 0, 0);
		fuenteNormal.setSize(10);
		fuenteNormal.setStyle(Font.NORMAL);
		try {
			Document document = new Document();
			try {

				PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));
				String rutaPDF = pdfNewFile.getAbsolutePath();
				try {

					File path = new File(rutaPDF);
					Desktop.getDesktop().open(path);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(null, "No se pudo encontrar el archivo", "Error",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}

			} catch (FileNotFoundException fileNotFoundException) {
				JOptionPane.showMessageDialog(null, fileNotFoundException);
			}
			document.open();

			String subEncebezado = "Lista de: " + tipoLista;
			String itz = "Instituto Tecnol\u00F3gico de Zit\u00E1cuaro";
			Paragraph paragraph = new Paragraph(new Phrase(Chunk.NEWLINE));

			Font fuenteHeader = new Font();
			fuenteHeader.setFamily("Calibri");
			fuenteHeader.setColor(0, 0, 0);
			fuenteHeader.setSize(16);
			fuenteHeader.setStyle(Font.BOLD);
			Phrase titulo = new Phrase(itz);
			titulo.setFont(fuenteTitulo);

			Paragraph parrafoTitulo = new Paragraph();
			parrafoTitulo.add(new Phrase(itz, fuenteHeader));
			Image img = Image.getInstance(".\\src\\recursos\\headerITz.png");
			img.scaleAbsoluteWidth(560);
			img.scaleAbsoluteHeight(70);
			document.add(img);

			String departamento = "                                     "
					+ "Departamento de Sistemas y Computaci\u00F3n";
			paragraph.add(new Phrase(departamento));
			paragraph.add(new Phrase(Chunk.NEWLINE));
			paragraph.add(new Phrase(Chunk.NEWLINE));
			paragraph.add(new Phrase(subEncebezado));
			paragraph.add(new Phrase(Chunk.NEWLINE));
			paragraph.add(new Phrase(Chunk.NEWLINE));

			Paragraph saltos = new Paragraph();
			saltos.add(new Phrase(Chunk.NEWLINE));
			saltos.add(new Phrase(Chunk.NEWLINE));

			PdfPTable table = new PdfPTable(cols.length);

			PdfPCell columnHeader = null;

			float[] valoresColums = new float[cols.length];
			valoresColums[0] = (15f);
			valoresColums[1] = (40f);
			valoresColums[2] = (120f);

			int tam = 155 / (data[0].length - 3);

			for (int i = 3; i < cols.length; i++) {
				valoresColums[i] = ((float) tam);
			}

			String[] titulos = { "#", "Nº Control", "Nombre del Alumno" };
			for (int column = 0; column < cols.length; column++) {
				if (column < titulos.length) {
					columnHeader = new PdfPCell(new Phrase(titulos[column], fuenteTitulo));
				} else
					columnHeader = new PdfPCell(new Phrase(cols[column], fuenteTitulo));

				columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
				columnHeader.setBorderColor(BaseColor.DARK_GRAY);
				table.addCell(columnHeader);
			}
			table.setHeaderRows(1);

			float porciento = 0;
			float acreditados = 0;

			for (int row = 0; row < data.length; row++) {

				for (int cell = 0; cell < data[row].length; cell++) {
					PdfPCell celda = new PdfPCell(new Phrase(data[row][cell].toString(), fuenteNormal));
					celda.setBorderColor(BaseColor.DARK_GRAY);
					if (row % 2 == 0) {
						celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
					}
					table.addCell(celda);
				}
				int dato = Integer.parseInt(data[row][data[row].length - 1].toString());
				if (dato >= 70) {
					acreditados++;
				}
			}

			porciento = (acreditados / data.length) * 100;

			table.setWidthPercentage(100);
			table.setWidths(valoresColums);

			document.add(parrafoTitulo);
			document.add(paragraph);
			document.add(getTablaEncabezado(estructura));
			document.add(saltos);
			document.add(table);
			Paragraph paragraphx = new Paragraph(new Phrase("Pocentaje de acreditacion " + (int) porciento + "%"));
			document.add(paragraphx);
			document.close();

			document.close();
			System.out.println("Exito al Generar PDF");
		} catch (DocumentException documentException) {
			System.out.println("error al generar un documento: " + documentException);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static PdfPTable getTablaEncabezado(EstructuraLista estruct) {

		PdfPTable tabla = new PdfPTable(4);
		Phrase p = new Phrase();
		PdfPCell cell = new PdfPCell();
		Font fontNor = new Font();
		fontNor.setFamily("Calibri");
		fontNor.setColor(0, 0, 0);
		fontNor.setSize(10);
		fontNor.setStyle(Font.NORMAL);

		float[] values = new float[4];
		values[0] = 70;
		values[1] = 230;
		values[2] = 35;
		values[3] = 15;
		try {
			tabla.setWidths(values);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase("Departamento:", fuenteTitulo));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase(estruct.getDepartamento(), fontNor));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase("Folio:", fuenteTitulo));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase("--", fontNor));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase("Materia:", fuenteTitulo));
		tabla.addCell(cell);
		p.setFont(fuenteTitulo);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase(estruct.getMateria().getNombre(), fontNor));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase("Clave:", fuenteTitulo));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase(estruct.getMateria().getClave(), fontNor));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase("Profesor:", fuenteTitulo));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase(estruct.getProfesor().getNombre(), fontNor));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase("Grupo:", fuenteTitulo));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase(estruct.getGrupo().getNombre(), fontNor));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase("Periodo:", fuenteTitulo));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase(estruct.getPeriodo().getPeridoFormateado().toUpperCase(), fontNor));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase("Alumnos:", fuenteTitulo));
		tabla.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(0);
		cell.addElement(new Phrase(String.valueOf(estruct.getAlumnos()), fontNor));
		tabla.addCell(cell);

		tabla.setWidthPercentage(100);

		return tabla;

	}

}
