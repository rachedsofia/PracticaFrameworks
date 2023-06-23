package rached.frameworks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
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
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class Lanterna extends Framework {

	private Screen screen;
	private TextGraphics textGraphics;
	private Window window;
	private WindowBasedTextGUI textGUI;
	private Terminal terminal;
	Panel panel;

	public Lanterna(String ruta) {
		super(ruta);
	}

	public void menu() throws Exception {

		this.terminal = new DefaultTerminalFactory().createTerminal();
		Screen screen = new TerminalScreen(terminal);
		screen.startScreen();

		this.window = new BasicWindow();

		this.panel = new Panel();
		new Label("Bienvenido, estas son sus opciones:").addTo(panel);

		window.setComponent(panel);

		TerminalSize size = new TerminalSize(70, 10);
		CheckBoxList<Accion> checkBox = new CheckBoxList<>(size);

		for (Map.Entry<Integer, Accion> entry : actions.entrySet()) {
			checkBox.addItem(entry.getValue());
		}

		Button btnContinuar = new Button("Continuar", new Runnable() {
			@Override
			public void run() {
				continuar(checkBox.getCheckedItems());
			}
		});

		Button btnSalir = new Button("Salir", new Runnable() {
			@Override
			public void run() {
				System.exit(0);
			}
		});

		panel.addComponent(checkBox);
		panel.addComponent(btnContinuar);
		panel.addComponent(btnSalir);

		MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(),
				new EmptySpace(TextColor.ANSI.BLUE));

		gui.addWindowAndWait(window);

	}

	private void continuar(List<Accion> actionsChecks) {

		Panel info = new Panel();
		info.addComponent(new Label("Se ejecutaran las acciones: " + actionsChecks.toString()).addTo(panel));

		Button btnOk = new Button("Ok", new Runnable() {
			@Override
			public void run() {

				List<AdaptadorCallable> misAcciones = new ArrayList<AdaptadorCallable>();

				for (Accion accion : actionsChecks) {
					misAcciones.add(new AdaptadorCallable(accion));
				}

				ExecutorService executor = Executors.newFixedThreadPool(lector.devolverMaxHilos());

				try {
					executor.invokeAll(misAcciones);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					terminal.close();
					menu();
				} catch (Exception e) {
					new RuntimeException("Error al cerrar terminal y volver a abrir menu.");
				}
			}
		});

		Button btnCancel = new Button("Cancelar", new Runnable() {
			@Override
			public void run() {
				try {
					terminal.close();
					menu();
				} catch (Exception e) {
					new RuntimeException("Error al cancelar.");
				}
			}
		});

		btnOk.addTo(info);
		btnCancel.addTo(info);

		window.setComponent(info);

	}

	public void leerOpcion() throws IOException {

		KeyStroke key;
		boolean salir = false;

		while (!salir) {
			key = screen.readInput();

			if (key.getKeyType() == KeyType.Character) {
				System.out.println("entro.");
				char caracter = key.getCharacter();
				int opcion = Character.getNumericValue(caracter);

				if ((actions.size() + 1) == opcion) {
					salir = true;
					System.out.println("Saliendo.");
				}

				if (!salir && !actions.containsKey(opcion)) {
					System.out.println("Elige una opcion valida.");
				}

				if (actions.containsKey(opcion)) {
					ejecutarAccion(actions.get(opcion));
				}

			}

		}

		screen.stopScreen();

	}

	private void ejecutarAccion(Accion accion) {

		String texto = accion.ejecutar();

		Panel panel = new Panel();
		textGraphics = screen.newTextGraphics();
		textGraphics.putString(0, actions.size() + 4, texto);
		panel.addComponent(new Label(texto))
				.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.END));
		window.setComponent(panel);
		textGUI.addWindowAndWait(window);

		try {
			screen.refresh();
		} catch (IOException e) {
			new RuntimeException("Algo malio sal.");
		}

	}
}
