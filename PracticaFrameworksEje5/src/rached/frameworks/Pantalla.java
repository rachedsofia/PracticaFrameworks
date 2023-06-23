package rached.frameworks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.CheckBoxList;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class Pantalla {
	private List<Accion> listaAcciones;

	private Terminal terminalFactory;
	private Panel panel;
	private LeerArchivo archivo;
	private Window window;
	private WindowBasedTextGUI textGUI;
	private Screen screen;

	public Pantalla(List<Accion> listaAcciones) {
		this.listaAcciones = listaAcciones;
	}

	public void mostrar() throws IOException {
		this.terminalFactory = new DefaultTerminalFactory().createTerminal(); // acá lo cambie
		screen = new TerminalScreen(terminalFactory);
		screen.startScreen();

		this.window = new BasicWindow();
		this.panel = new Panel();
		new Label("Bienvenido, estas son sus opciones:").addTo(panel);

		TerminalSize size = new TerminalSize(70, 10);
		CheckBoxList<Integer> checkBoxList = new CheckBoxList<Integer>(size);
		window.setComponent(panel);

		for (Accion accion : listaAcciones) {
			panel.addComponent(new Button(accion.nombreItemMenu(), new Runnable() {
				@Override
				public void run() {
					accion.ejecutar();
					confirmarEjecucionAcciones(checkBoxList.getCheckedItems());
				}
			}));

			panel.addComponent(new Label(accion.descripcionItemMenu())).setLayoutData(
					GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.END));
		}

		panel.addComponent(new Button(listaAcciones.size() + 1 + ". Salir"));
		window.setComponent(panel);
		// textGUI.addWindowAndWait(window);

		MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(),
				new EmptySpace(TextColor.ANSI.BLUE));
		gui.addWindowAndWait(window);
	}

	private void confirmarEjecucionAcciones(List<Integer> actionsChecks) {

		Panel info = new Panel();
		info.addComponent(new Label("Se ejecutaran las acciones: " + actionsChecks.toString()).addTo(panel));

		Button btnOk = new Button("Ok", new Runnable() {
			@Override
			public void run() {

				// List<MaxThreads> misAcciones = new ArrayList<MaxThreads>(); // aca esta el
				// error.

				List<AccionesCallable> misAcciones = new ArrayList<AccionesCallable>();
				for (Integer key : actionsChecks) {
					misAcciones.add(new AccionesCallable(listaAcciones.get(key)));
				}

				ExecutorService executor = Executors.newFixedThreadPool(archivo.devolverMaxHilos());

				try {
					executor.invokeAll(misAcciones);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					terminalFactory.close();
					mostrar();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Button btnCancel = new Button("Cancelar", new Runnable() {
			@Override
			public void run() {
				try {
					terminalFactory.close();
					mostrar();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnOk.addTo(info);
		btnCancel.addTo(info);

		window.setComponent(info);
	}

}
