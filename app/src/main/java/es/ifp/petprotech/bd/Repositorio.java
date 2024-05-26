package es.ifp.petprotech.bd;

import java.util.List;
import java.util.Map;

public interface Repositorio<T extends Entidad> {
    boolean crear(T t);
    boolean asociarAEstaEntidad(T entidad, Entidad asociar);
    boolean asociarMuchos(T entidad, Entidad asociar);
    List<T> seleccionarTodo();
    T seleccionarPorId(long id);
    List<T> seleccionarPor(String atributo, String valor);
    List<T> seleccionarEn(long[] ids);
    <E extends Entidad> Map<Long, List<E>> seleccionarMuchosAUno(Class<E> claseAsociacion, long[] where);
    <E extends Entidad> Map<Long, List<E>> seleccionarUnoAMuchos(Class<E> claseAsociacion, long[] idsPropias);
    <E extends Entidad> Map<Long, List<E>> seleccionarMuchosAMuchos(Class<E> claseAsociacion, long[] idsPropias);
    boolean actualizar(T t);
    boolean eliminar(T t);

    void setRepositoriosAsociados(Map<Class<? extends Entidad>, Repositorio<? extends Entidad>> contiene);
}
