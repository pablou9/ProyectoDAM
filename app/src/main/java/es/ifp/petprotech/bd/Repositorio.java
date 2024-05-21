package es.ifp.petprotech.bd;

import java.util.List;
import java.util.Map;

public interface Repositorio<T extends Entidad> {
    boolean crear(T t);
    boolean asociar(T entidad, Entidad asociar);
    boolean asociarMuchos(T entidad, Entidad asociar);
    List<T> seleccionarTodo();
    T seleccionarPorId(long id);
    List<T> seleccionarPor(String atributo, String valor);
    List<T> seleccionarEn(long[] ids);
    Map<Long, List<T>> seleccionarPorAsociacion(Class<?> claseAsociacion, long[] where);
    Map<Long, List<T>> seleccionarPorAsociacionAMuchos(Class<?> claseAsociacion, long[] where);
    boolean actualizar(T t);
    boolean eliminar(T t);
}
