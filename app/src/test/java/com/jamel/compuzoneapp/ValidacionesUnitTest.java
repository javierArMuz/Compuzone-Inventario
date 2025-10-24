package com.jamel.compuzoneapp;

import com.jamel.compuzoneapp.controlador.Validaciones;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidacionesUnitTest {

    // Prueba para la validación de nombre (no vacío)
    @Test
    public void nombre_debeSerCompleto() {
        assertTrue(Validaciones.esNombreCompleto("Teclado Mecánico"));
        assertFalse(Validaciones.esNombreCompleto(" "));
        assertFalse(Validaciones.esNombreCompleto(null));
        assertFalse(Validaciones.esNombreCompleto(""));
    }

    // Prueba para la validación de cantidad (cero o positivo)
    @Test
    public void cantidad_debeSerCeroOPositiva() {
        assertTrue(Validaciones.esCantidadValida(10)); // Positivo
        assertTrue(Validaciones.esCantidadValida(0));  // Cero
        assertFalse(Validaciones.esCantidadValida(-5)); // Negativo (Falla)
    }

    // Prueba para la validación de precio (mayor a cero)
    @Test
    public void precio_debeSerMayorACero() {
        assertTrue(Validaciones.esPrecioValido(199.99)); // Mayor a cero
        assertTrue(Validaciones.esPrecioValido(0.01));  // Mínimo válido
        assertFalse(Validaciones.esPrecioValido(0.0));  // Cero (Falla)
        assertFalse(Validaciones.esPrecioValido(-10.0)); // Negativo (Falla)
    }
}
