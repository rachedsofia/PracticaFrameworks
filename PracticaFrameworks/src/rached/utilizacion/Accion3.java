package rached.utilizacion;

import rached.frameworks.Accion;

public class Accion3 implements Accion {

	@Override
	public void ejecutar() {
		// TODO Auto-generated method stub
		System.out.println("Ejecutando 3");
	}

	@Override
	public String nombreItemMenu() {
		// TODO Auto-generated method stub
		return "Accion 3";
	}

	@Override
	public String descripcionItemMenu() {
		// TODO Auto-generated method stub
		return "hola";
	}

}
