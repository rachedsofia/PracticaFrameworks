package rached.utilizacion;

import rached.frameworks.Accion;

public class AccionUno implements Accion {

	@Override
	public String ejecutar() {
		// TODO Auto-generated method stub
		return "Se esta ejecutando Accion 1";
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
