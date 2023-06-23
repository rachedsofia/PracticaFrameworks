package rached.frameworks;

import java.util.concurrent.Callable;

public class AdaptadorCallable implements Callable<Integer> {

	private Accion accion;

	public AdaptadorCallable(Accion accion) {
		this.accion = accion;
	}

	@Override
	public Integer call() throws Exception {

		System.out.println(accion.ejecutar());
		Thread.sleep(1000);

		return null;
	}

}
