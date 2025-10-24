package com.jamel.compuzoneapp.controlador;

public class Validaciones {
    public static boolean esCantidadValida(int cantidad) {
        return cantidad >= 0;
    }

    public static boolean esPrecioValido(double precio) {
        return precio > 0.0;
    }

    public static boolean esNombreCompleto(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }
}
