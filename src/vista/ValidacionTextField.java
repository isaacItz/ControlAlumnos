package vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class ValidacionTextField {

	public static boolean estaVacio(JTextField t) {
		if (t.getText().isEmpty())
			return true;
		t.requestFocus();
		return false;
	}

	public static boolean longitudText(JTextField t, int tamanio) {
		if (t.getText().length() == tamanio)
			return true;
		t.requestFocus();
		return false;
	}

	public static boolean esNumerico(JTextField t) {
		try {
			Integer.parseInt(t.getText());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean esCadena(JTextField t) {
		String cadena = t.getText();
		for (int i = 0; i < cadena.length(); i++)
			if (Character.isDigit(cadena.charAt(i)))
				return false;
		return true;
	}

	public static void soloTexto(JTextField t) {
		t.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isLetter(e.getKeyChar()))
					e.consume();
			}
		});

	}

	public static void soloNum(JTextField t) {
		t.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar()))
					e.consume();
			}
		});

	}

	public static void limitarTextF(JTextField t, int limit) {
		t.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (t.getText().length() > limit - 1)
					e.consume();
			}
		});

	}

	public static void aniadirEvtCajaVacia(JTextField t, String mensaje) {
		t.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (t.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,mensaje);
					t.requestFocus();
				}

			}
		});

	}
}
