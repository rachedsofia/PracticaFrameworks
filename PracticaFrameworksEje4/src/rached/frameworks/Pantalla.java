package rached.frameworks;

import java.util.List;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class Pantalla {
	private List<Accion> listaAcciones;

	public Pantalla(List<Accion> listaAcciones) {
		this.listaAcciones = listaAcciones;
	}

	public void mostrar() {
		DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
		Screen screen = null;
		Window window = new BasicWindow("Bienvenido, ¿Que desea hacer?");
		WindowBasedTextGUI textGUI = null;
		try {
			screen = terminalFactory.createScreen();
			screen.startScreen();
			textGUI = new MultiWindowTextGUI(screen);
		} catch (Exception e) {

		}

		Panel panel = new Panel();

		for (Accion accion : listaAcciones) {
			panel.addComponent(new Button(accion.nombreItemMenu(), new Runnable() {
				@Override
				public void run() {
					accion.ejecutar();
				}
			}));

			panel.addComponent(new Label(accion.descripcionItemMenu())).setLayoutData(
					GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.END));
		}
		window.setComponent(panel);
		textGUI.addWindowAndWait(window);
	}
}
