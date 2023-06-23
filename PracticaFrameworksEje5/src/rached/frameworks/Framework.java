package rached.frameworks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Framework {
	private String fileConfig;
	protected Map<Integer, Accion> actions;
	protected AdaptadorCallable maxThreads;
	protected Lector lector;

	public Framework(String ruta) {
		this.fileConfig = ruta;
		this.lector = new Lector(ruta);
		this.actions = new HashMap<>();

	}

	public void init() throws Exception {

		this.actions = lector.cargarAcciones();
		this.menu();

	}

	public abstract void menu() throws Exception;

	public abstract void leerOpcion() throws IOException;

	private void cargarAcciones() {

		Properties properties = new Properties();
		try (InputStream configFile = getClass().getResourceAsStream(fileConfig)) {

			properties.load(configFile);
			String acciones = properties.getProperty("accions");
			List<String> nombresClases = Arrays.asList(acciones.split(";"));

			int opcion = 1;
			for (String nombre : nombresClases) {

				Accion accion = (Accion) Class.forName(nombre).getDeclaredConstructor().newInstance();

				actions.put(opcion, accion);
				opcion++;
			}

		} catch (Exception e) {
			new RuntimeException("Error al cargar Acciones.");
		}

	}

	private void accionesJson() throws Exception {

		// HashMap<Integer, Accion> lista = new HashMap<>();
		Gson gson = new GsonBuilder().create();

		try (BufferedReader reader = new BufferedReader(new FileReader(this.fileConfig))) {

			JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
			JsonArray jsonArray = jsonObject.getAsJsonArray("accions");
			int i = 1;
			for (JsonElement jsonElement : jsonArray) {
				String clase = jsonElement.getAsString();
				Accion nuevaAccion = (Accion) Class.forName(clase).getDeclaredConstructor().newInstance();
				actions.put(i, nuevaAccion);
				i++;
			}
		} catch (IOException e) {
			new RuntimeException("Es aca.");
		}

	}

}
