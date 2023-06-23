package rached.frameworks;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class LectorArchivos {

	String pathAcciones;
	private Map<Integer, Accion> acciones;
	private int maxThreads;

	public LectorArchivos(String path) {
		this.pathAcciones = path;
		this.acciones = new HashMap<>();
	}

	public Map<Integer, Accion> devolverAcciones() {

		if (isJson())
			this.leerArchivoJSON();
		else
			this.leerArchivoComun();
		return acciones;
	}

	public int devolverMaxHilos() {
		return this.maxThreads;
	}

	private boolean isJson() {

		File file = new File(this.pathAcciones);
		String fileName = file.getName();

		if (fileName.endsWith(".json"))
			return true;
		else
			return false;
	}

	/* Metodo para leer un archivo .json */
	private void leerArchivoJSON() {
		Gson gson = new GsonBuilder().create();

		try (JsonReader reader = new JsonReader(new FileReader(this.pathAcciones))) {

			JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

			/* Obtengo el array de acciones que contiene el archivo de configuracion dado */
			JsonArray jsonArray = jsonObject.getAsJsonArray("acciones");

			/*
			 * Obtengo el valor de hilos maximos que pueden ejecutarse indicados en el
			 * archivo de configuracion
			 */
			this.maxThreads = jsonObject.get("max-threads").getAsInt();

			for (int i = 0; i < jsonArray.size(); i++) {

				Accion accionTemp = (Accion) Class.forName(jsonArray.get(i).getAsString()).getDeclaredConstructor()
						.newInstance();
				this.acciones.put((i + 1), accionTemp);
			}

		} catch (Exception e) {
			throw new RuntimeException("Ocurrio un error con el archivo .json: " + e.getMessage());
		}
	}

	/* Metodo para leer un archivo .config normal */
	private void leerArchivoComun() {

		Properties prop = new Properties();

		try (InputStream configFile = getClass().getResourceAsStream(this.pathAcciones);) {

			prop.load(configFile);
			String clase = prop.getProperty("accions");
			String[] clases = clase.split(";");

			for (int i = 0; i < clases.length; i++) {

				Accion accionTemp = (Accion) Class.forName(clases[i]).getDeclaredConstructor().newInstance();
				this.acciones.put((i + 1), accionTemp);
			}

		} catch (Exception e) {
			throw new RuntimeException("Ocurrio un error con el archivo .config: " + e.getMessage());
		}

	}

}
