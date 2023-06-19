package rached.frameworks;

public class Salir implements Accion {

	@Override
	public void ejecutar() {
		System.exit(0);
	}

	@Override
	public String nombreItemMenu() {
		return "Salir";
	}

	@Override
	public String descripcionItemMenu() {
		return "Esta accion cerrara el progama...";
	}

}
