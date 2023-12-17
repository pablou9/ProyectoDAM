package es.ifp.petprotech.bd;

public interface BaseDeDatos<T> {

    T conectar();
    void desconectar();

}
