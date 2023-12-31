package rached.frameworks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MaxThreads {
	private Integer limite;
	private HashMap<Integer, Accion> acciones;
	private ExecutorService executor;

	public MaxThreads(int limite) {
		this.limite = limite;
		this.executor = Executors.newFixedThreadPool(limite);
		this.acciones = new HashMap<Integer, Accion>(); // Esto no estaba, por eso daba null pointer al tratar de
														// hacer un put.
	}

//	public void agregarAcciones(Queue<Accion> acciones) {
//		while (cuposDisponibles() > 0 && !acciones.isEmpty())
//			this.acciones.add(acciones.poll());
//	}
	public void agregarAcciones(Map<Integer, Accion> acciones) {
		int cuposDisponibles = cuposDisponibles();

		for (Map.Entry<Integer, Accion> entry : acciones.entrySet()) {
			int key = entry.getKey();
			Accion accion = entry.getValue();

			if (cuposDisponibles > 0) {
				this.acciones.put(key, accion);
				cuposDisponibles--;
			}
		}
	}

	public void ejecutar() {

		List<Callable<String>> callables = new ArrayList<>();

		for (Accion accion : this.acciones.values()) {
			callables.add(() -> {
				accion.ejecutar();
				return "Listo";
			});
		}

		try {
			this.executor.invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private int cuposDisponibles() {
		return limite - this.acciones.size();
	}

}
