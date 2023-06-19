package rached.frameworks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class Frameworks {
	private String filePath;
	private Map<Integer, Accion> acciones;

	public Frameworks(String path) {
		this.filePath = path;
		this.acciones = new HashMap<>();
	}

	public final void init() throws IOException, Exception {
		if (esJson()) {
			this.buscarAccionesJSON();
		} else {
			this.buscarAcciones();
		}
		try {
			Terminal terminal = new DefaultTerminalFactory().createTerminal();
			Screen screen = new TerminalScreen(terminal);
			screen.startScreen();

			mostrarMenu(screen);

			screen.stopScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean esJson() {
		if (this.filePath.endsWith(".json")) {
			return true;
		}
		return false;
	}

	private void mostrarMenu(Screen screen) throws IOException {
		screen.clear();

		TextGraphics textGraphics = screen.newTextGraphics();
		textGraphics.putString(0, 0, "Bienvenido, estas son sus opciones:");
		textGraphics.putString(0, 0, "Bienvenido, estas son sus opciones:");
		int row = 2;
		for (Map.Entry<Integer, Accion> entry : acciones.entrySet()) {
			String optionText = entry.getKey() + ". " + entry.getValue().nombreItemMenu() + " ("
					+ entry.getValue().descripcionItemMenu() + ")";
			textGraphics.putString(0, row++, optionText);
		}

		screen.refresh();

		leerOpcionIngresada(screen);
	}

	private void leerOpcionIngresada(Screen screen) throws IOException {
		KeyStroke keyStroke;

		boolean opcionValida = false;

		while (!opcionValida) {
			keyStroke = screen.readInput();

			if (keyStroke.getKeyType() == KeyType.Character) {
				char character = keyStroke.getCharacter();
				int opcion = Character.getNumericValue(character);

				if (acciones.containsKey(opcion)) {
					acciones.get(opcion).ejecutar();
					System.out.println("Finalizada.\n");
					opcionValida = true;
				} else {
					System.out.println("Seleccione una opción válida.");
				}
			}
		}

		mostrarMenu(screen);
	}

	private HashMap<Integer, Accion> buscarAccionesJSON() throws IOException, Exception {
		HashMap<Integer, Accion> lista = new HashMap<>();
		Gson gson = new GsonBuilder().create();
		int i = 0;

		try (BufferedReader reader = new BufferedReader(new FileReader(this.filePath))) {
			JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
			JsonArray jsonArray = jsonObject.getAsJsonArray("acciones");
			for (JsonElement jsonElement : jsonArray) {
				String clase = jsonElement.getAsString();
				Accion nuevaAccion = (Accion) Class.forName(clase).getDeclaredConstructor().newInstance();
				lista.put(i, nuevaAccion);
				i++;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();

		}
		return lista;
	}

	private void buscarAcciones() {
		Properties prop = new Properties();

		try (InputStream configFile = getClass().getResourceAsStream(this.filePath);) {

			prop.load(configFile);

			String clase = prop.getProperty("accions");

			String[] clases = clase.split(";");

			for (int i = 0; i < clases.length; i++) {

				Accion accionTemp = (Accion) Class.forName(clases[i]).getDeclaredConstructor().newInstance();

				this.acciones.put((i + 1), accionTemp);
			}

			this.acciones.put(clases.length + 1, new Salir());

		} catch (Exception e) {
			throw new RuntimeException("Ocurrio un error con el archivo .config: " + e.getMessage());
		}
	}

}
