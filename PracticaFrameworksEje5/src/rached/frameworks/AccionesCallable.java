package rached.frameworks;

import java.util.concurrent.Callable;

public class AccionesCallable implements Callable<Integer> {

	private Accion miAccion;

	public AccionesCallable(Accion unaAccion) {
		this.miAccion = unaAccion;
	}

	@Override
	public Integer call() throws Exception {

		this.miAccion.ejecutar();
		Thread.sleep(700);

		return null;
	}

}
