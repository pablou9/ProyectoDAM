package es.ifp.petprotech.bd;

public interface Repositorio<T extends Entidad> {
    boolean crear(T t);
    T seleccionarConId(long id);
    T seleccionar();
    boolean actualizar(T t);
    boolean eliminar(T t);
}
