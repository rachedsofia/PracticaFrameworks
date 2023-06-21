package rached.frameworks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class Frameworks {
	private String filePath;
	private Map<Integer, Accion> acciones;
	private MaxThreads maxThreads;
	private Pantalla pantalla;

	public Frameworks(String path) {
		this.filePath = path;
		this.acciones = new HashMap<>();
		this.maxThreads = new MaxThreads(2);

	}

	public final void init() throws IOException, Exception {
		if (esJson()) {
			this.acciones = this.buscarAccionesJSON();
			maxThreads.agregarAcciones(this.acciones);
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
		this.pantalla = new Pantalla(convertirALista());
		this.pantalla.mostrar();
		leerOpcionIngresada(screen);
	}

	private List<Accion> convertirALista() {
		List<Accion> lista = new ArrayList<>();

		for (int i = 0; i < this.acciones.size(); i++) {
			lista.add(this.acciones.get(i));
		}

		return lista;
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
			JsonArray jsonArray = jsonObject.getAsJsonArray("accions");
			for (JsonElement jsonElement : jsonArray) {
				String clase = jsonElement.getAsString();
				maxThreads.agregarAcciones(lista);
				Accion nuevaAccion = (Accion) Class.forName(clase).getDeclaredConstructor().newInstance();
				maxThreads.agregarAcciones(lista);
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
//
//}
