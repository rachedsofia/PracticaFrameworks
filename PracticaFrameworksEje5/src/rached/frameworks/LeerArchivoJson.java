package rached.frameworks;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class LeerArchivoJson implements LeerArchivo {

	private int maxThreads;

	@Override
	public Map<Integer, Accion> devolverAcciones(String path) {

		Map<Integer, Accion> acciones = new HashMap<>();

		Gson gson = new GsonBuilder().create();

		try (JsonReader reader = new JsonReader(new FileReader(path))) {

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
				acciones.put((i + 1), accionTemp);
			}

			return acciones;

		} catch (Exception e) {
			throw new RuntimeException("Ocurrio un error con el archivo .json: " + e.getMessage());
		}

	}

	@Override
	public int devolverMaxHilos() {

		return this.maxThreads;
	}

}
