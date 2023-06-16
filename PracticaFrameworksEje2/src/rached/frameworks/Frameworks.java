package rached.frameworks;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Frameworks {
	private String filePath;
	private Map<Integer, Accion> acciones;

	public Frameworks(String path) {
		this.filePath = path;
		this.acciones = new HashMap<>();
	}

	public final void init() {

		this.buscarAccions();

		this.mostrarMenu();
	}

	private void mostrarMenu() {

		System.out.println("Bienvenido, estas son sus opciones:\n");

		for (Map.Entry<Integer, Accion> entry : acciones.entrySet()) {

			System.out.println(entry.getKey() + ". " + entry.getValue().nombreItemMenu() + " ("
					+ entry.getValue().descripcionItemMenu() + ")");

		}

		this.leerOpcionIngresada();

	}

	private void buscarAccions() {

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

	private void leerOpcionIngresada() {
		Scanner scanner = new Scanner(System.in);

		boolean opcionValida = false;

		while (!opcionValida) {

			System.out.print("\nIngrese su opción: _\r\n");
			int opcion = scanner.nextInt();

			try {

				this.acciones.get(opcion).ejecutar();
				System.out.println("Finalizada.\n");
				opcionValida = true;

			} catch (Exception e) {
				System.out.println("Seleccione una opción válida.");
			}
		}

		this.mostrarMenu();
		scanner.close();
	}

}
