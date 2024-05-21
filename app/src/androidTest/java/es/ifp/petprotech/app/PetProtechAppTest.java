package es.ifp.petprotech.app;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.ifp.petprotech.app.model.FabricaViewModel;
import es.ifp.petprotech.app.model.Modelo;

@RunWith(AndroidJUnit4.class)
public class PetProtechAppTest {

    @Test
    public void inicializaTodosLosRepositoriosAlLanzarLaApp() {
        // La aplicación es lanzada por AndroidJUnit, no es necesaria ninguna otra acción

        for (Modelo modelo : Modelo.values())
            Assert.assertNotNull(Modelo.getRepositorio(modelo, modelo.getClase()));
    }

    @Test
    public void inicializaTodosLasFabricasViewModelAlLanzarLaApp() {
        // La aplicación es lanzada por AndroidJUnit, no es necesaria ninguna otra acción

        for (FabricaViewModel fabrica : FabricaViewModel.values())
            Assert.assertNotNull(fabrica.getFabrica());
    }

}
