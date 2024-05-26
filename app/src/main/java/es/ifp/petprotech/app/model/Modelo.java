package es.ifp.petprotech.app.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import es.ifp.petprotech.bd.BaseDeDatos;
import es.ifp.petprotech.bd.Entidad;
import es.ifp.petprotech.bd.Repositorio;
import es.ifp.petprotech.bd.RepositorioSQLite;
import es.ifp.petprotech.centros.datos.CentrosProfesionalesRepositorio;
import es.ifp.petprotech.centros.model.CentroProfesional;
import es.ifp.petprotech.mascotas.datos.ContratoMascotas;
import es.ifp.petprotech.mascotas.datos.ContratoMascotasVeterinarios;
import es.ifp.petprotech.mascotas.datos.MascotasRepositorio;
import es.ifp.petprotech.mascotas.model.Mascota;
import es.ifp.petprotech.veterinarios.datos.ContratoVeterinarios;
import es.ifp.petprotech.veterinarios.datos.VeterinariosRepositorio;
import es.ifp.petprotech.veterinarios.model.Veterinario;

public enum Modelo {

    MASCOTA(Mascota.class, Modelo::repositorioMascotas),
    VETERINARIO(Veterinario.class, Modelo::repositorioVeterinarios, Modelo::asociacionesVeterinarios),
    CENTRO_PROFESIONAL(CentroProfesional.class, Modelo::repositorioCentroProfesional, Modelo::asociacionesCentrosProfesionales);

    private final Function<BaseDeDatos<SQLiteDatabase>, Repositorio<? extends Entidad>> constructorRepositorio;
    private final Consumer<Repositorio<? extends Entidad>> asociadorRepositorios;

    private Repositorio<? extends Entidad> repositorio;

    // Para Testing
    private final Class<? extends Entidad> clase;

    Modelo(Class<? extends Entidad> clase,
           Function<BaseDeDatos<SQLiteDatabase>, Repositorio<? extends Entidad>> constructorRepositorio)
    {
        this(clase, constructorRepositorio, null);
    }

    Modelo(Class<? extends Entidad> clase,
           Function<BaseDeDatos<SQLiteDatabase>, Repositorio<? extends Entidad>> constructorRepositorio,
           Consumer<Repositorio<? extends Entidad>> asociadorRepositorios)
    {
        this.clase = clase;
        this.constructorRepositorio = constructorRepositorio;
        this.asociadorRepositorios = asociadorRepositorios;
    }

    public void initRepositorio(BaseDeDatos<SQLiteDatabase> bd) {
        repositorio = constructorRepositorio.apply(bd);
    }

    public void ejecutarAsociacionesRepositorio() {
        if (asociadorRepositorios != null)
            asociadorRepositorios.accept(repositorio);
    }

    public Class<? extends Entidad> getClase() {
        return clase;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entidad> Repositorio<T> getRepositorio(Modelo modelo, Class<T> clase) {
        return (Repositorio<T>) modelo.repositorio;
    }

    private static Repositorio<Mascota> repositorioMascotas(BaseDeDatos<SQLiteDatabase> bd) {
        MascotasRepositorio repositorio = new MascotasRepositorio(bd);

        repositorio.anadirAsociacionesAMuchos(Map.of(
                Veterinario.class, new RepositorioSQLite.AsociaciacionMuchosAMuchos(
                        ContratoVeterinarios.NOMBRE_TABLA,
                        ContratoMascotasVeterinarios.NOMBRE_TABLA,
                        ContratoMascotasVeterinarios.Columnas.ID_MASCOTA,
                        ContratoMascotasVeterinarios.Columnas.ID_VETERINARIO
                )));

        return repositorio;
    }

    private static Repositorio<Veterinario> repositorioVeterinarios(BaseDeDatos<SQLiteDatabase> bd) {
        VeterinariosRepositorio repositorio = new VeterinariosRepositorio(bd);

        repositorio.anadirAsociacionesAMuchos(Map.of(
                Mascota.class, new RepositorioSQLite.AsociaciacionMuchosAMuchos(
                        ContratoMascotas.NOMBRE_TABLA,
                        ContratoMascotasVeterinarios.NOMBRE_TABLA,
                        ContratoMascotasVeterinarios.Columnas.ID_VETERINARIO,
                        ContratoMascotasVeterinarios.Columnas.ID_MASCOTA
                )));

        return repositorio;
    }

    private static void asociacionesVeterinarios(Repositorio<? extends Entidad> repositorio) {
        repositorio.setRepositoriosAsociados(Map.of(
            Mascota.class, MASCOTA.repositorio,
            CentroProfesional.class, CENTRO_PROFESIONAL.repositorio
        ));
    }

    private static Repositorio<CentroProfesional> repositorioCentroProfesional(BaseDeDatos<SQLiteDatabase> bd) {
        CentrosProfesionalesRepositorio repositorio = new CentrosProfesionalesRepositorio(bd);

        repositorio.anadirAsociaciones(Map.of(
                Veterinario.class, new RepositorioSQLite.AsociaciacionUnoAMuchos(
                    ContratoVeterinarios.NOMBRE_TABLA, ContratoVeterinarios.Columnas.ID_CENTRO
                )
        ));

        repositorio.anadirAsociacionesAMuchos(Map.of(
                Mascota.class, new RepositorioSQLite.AsociaciacionMuchosAMuchos(
                        ContratoMascotas.NOMBRE_TABLA,
                        ContratoMascotasVeterinarios.NOMBRE_TABLA,
                        ContratoMascotasVeterinarios.Columnas.ID_VETERINARIO,
                        ContratoMascotasVeterinarios.Columnas.ID_MASCOTA
                )));



        return repositorio;
    }

    private static void asociacionesCentrosProfesionales(Repositorio<? extends Entidad> repositorio) {
        repositorio.setRepositoriosAsociados(Map.of(
                Veterinario.class, VETERINARIO.repositorio,
                Mascota.class, MASCOTA.repositorio));
    }
}