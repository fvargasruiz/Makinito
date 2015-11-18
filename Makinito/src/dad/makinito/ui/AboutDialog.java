package dad.makinito.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import dad.makinito.Config;
import dad.makinito.ui.resources.Icons;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {
	
	public AboutDialog(Container padre) {
		initDialog();
		initComponents();
		setLocationRelativeTo(padre);
	}
	
	private void initDialog() {
		setTitle("Acerca de Makinito");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(320, 300);
		setModal(true);
	}
	
	private void initComponents() {
		
		JLabel aboutLabel = new JLabel(new ImageIcon(Icons.LOGO_GRANDE));
		aboutLabel.setHorizontalTextPosition(JLabel.CENTER);
		aboutLabel.setVerticalTextPosition(JLabel.BOTTOM);
		aboutLabel.setText(
				"<html><center>"
				+ "<h1>Makinito</h1>"
				+ "Versión " + Config.getVersion() + "<br/>"
				+ "Simulador de la arquitectura Von Neumann<br/>"
				+ "Autor: <u>Francisco Vargas Ruiz</u>"
				+ "</center></html>"
				);
		
		getContentPane().add(aboutLabel, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		new AboutDialog(null).setVisible(true);
	}
	
}
