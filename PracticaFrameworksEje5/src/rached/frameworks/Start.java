package rached.frameworks;

public class Start {

	private Framework framework;

	public Start(String ruta) {
		this.framework = new Lanterna(ruta);
	}

	public void init() throws Exception {
		this.framework.init();
	}
}
