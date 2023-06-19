package rached.utilizacion;

import rached.frameworks.Accion;

public class AccionDos implements Accion {

	@Override
	public void ejecutar() {
		System.out.println("Ejecutando Accion 2");
	}

	@Override
	public String nombreItemMenu() {
		// TODO Auto-generated method stub
		return "Accion 2";
	}

	@Override
	public String descripcionItemMenu() {
		// TODO Auto-generated method stub
		return "Ver catalogo de peliculas y series en netflix";
	}

}
