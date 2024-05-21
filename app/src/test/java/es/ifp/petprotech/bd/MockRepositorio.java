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
    public boolean asociar(T entidad, Entidad asociar) {
        return false;
    }

    @Override
    public boolean asociarMuchos(T entidad, Entidad asociar) {
        return false;
    }

    @Override
    public List<T> seleccionarPorAsociacion(Entidad asociacion) {
        return null;
    }

    @Override
    public List<T> seleccionarPorAsociacion(Entidad asociacion, long[] en) {
        return null;
    }

    @Override
    public Map<Long,List<T>> seleccionarPorAsociacion(Class<?> claseAsociacion, long[] en) {
        return null;
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
    public List<T> seleccionarPor(String atributo, String valor) {
        throw new UnsupportedOperationException();
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
