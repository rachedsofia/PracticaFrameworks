package rached.main;

import rached.frameworks.Frameworks;

public class Main {

	public static void main(String[] args) {
		Frameworks framework = new Frameworks("/accions.properties");
		framework.init();

	}

}
