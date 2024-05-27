package es.ifp.petprotech.bd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MockRepositorio<T extends Entidad> implements Repositorio<T> {
    private final List<T> datos = new ArrayList<>();

    @Override
    public boolean crear(T t) {
        return datos.add(t);
    }

    @Override
    public boolean asociarAEstaEntidad(T entidad, Entidad asociar) {
        return false;
    }

    @Override
    public boolean asociarMuchos(T entidad, Entidad asociar) {
        return false;
    }

    @Override
    public <E extends Entidad> Map<Long, List<E>> seleccionarMuchosAUno(Class<E> claseAsociacion, long[] where) {
        return null;
    }

    @Override
    public <E extends Entidad> Map<Long, List<E>> seleccionarMuchosAMuchos(Class<E> claseAsociacion, long[] where) {
        return null;
    }

    @Override
    public void setRepositoriosAsociados(Map<Class<? extends Entidad>, Repositorio<? extends Entidad>> contiene) {

    }

    @Override
    public List<T> seleccionarTodo() {
        return datos;
    }

    @Override
    public T seleccionarPorId(long id) {
        return datos
                .stream()
                .filter(entidad -> entidad.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<T> seleccionarPorNombre(String nombre) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E extends Entidad> Map<Long, List<E>> seleccionarUnoAMuchos(Class<E> claseAsociacion, long[] idsPropias) {
        return null;
    }

    @Override
    public List<T> seleccionarEn(long[] ids) {
        return null;
    }

    @Override
    public boolean actualizar(T t) {
        T reemplazada = datos.set(datos.indexOf(t), t);
        return reemplazada != null;
    }

    @Override
    public boolean eliminar(T t) {
        return datos.remove(t);
    }
}
