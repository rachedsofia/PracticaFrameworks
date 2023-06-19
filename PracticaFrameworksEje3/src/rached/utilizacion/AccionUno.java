package rached.utilizacion;

import rached.frameworks.Accion;

public class AccionUno implements Accion {

	@Override
	public void ejecutar() {
		// TODO Auto-generated method stub
		System.out.println("Ejecutando Accion 1");
	}

	@Override
	public String nombreItemMenu() {
		// TODO Auto-generated method stub
		return "Accion 1";
	}

	@Override
	public String descripcionItemMenu() {
		// TODO Auto-generated method stub
		return "Ver peliculas de Dispney+";
	}

}
