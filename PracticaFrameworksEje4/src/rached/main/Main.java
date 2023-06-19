package rached.main;

import java.io.IOException;

import rached.frameworks.Frameworks;

public class Main {

	public static void main(String[] args) throws IOException, Exception {
		Frameworks framework = new Frameworks(
				"C:\\Users\\msofi\\OneDrive\\Documentos\\3° año 2023\\Orientación a Objetos 2\\PracticaFrameworksEje4\\resource\\accions.json");
		framework.init();

	}

}
