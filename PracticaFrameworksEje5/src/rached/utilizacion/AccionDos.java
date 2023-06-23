package rached.utilizacion;

import rached.frameworks.Accion;

public class AccionDos implements Accion {
	@Override
	public String ejecutar() {
		// TODO Auto-generated method stub
		return "Se esta ejecutando Accion 2";
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
