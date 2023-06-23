package rached.frameworks;

import java.util.Map;

public interface LeerArchivo {

	Map<Integer, Accion> devolverAcciones(String path);

	int devolverMaxHilos();

}
