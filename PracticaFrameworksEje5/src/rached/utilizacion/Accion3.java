package rached.utilizacion;

import rached.frameworks.Accion;

public class Accion3 implements Accion {

	@Override
	public String ejecutar() {
		return "Se ejecuto accion 3";
	}

	@Override
	public String nombreItemMenu() {
		// TODO Auto-generated method stub
		return "Accion 3";
	}

	@Override
	public String descripcionItemMenu() {
		// TODO Auto-generated method stub
		return "Hola viedma";
	}

}
