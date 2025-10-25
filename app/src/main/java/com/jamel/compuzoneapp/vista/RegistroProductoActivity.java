package com.jamel.compuzoneapp.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jamel.compuzoneapp.R;
import com.jamel.compuzoneapp.database.DatabaseHelper;
import com.jamel.compuzoneapp.model.Producto;
import com.jamel.compuzoneapp.controlador.Validaciones;

public class RegistroProductoActivity extends AppCompatActivity {
    private EditText etNombre, etCantidad, etPrecio;
    private DatabaseHelper dbHelper;
    private int productoId = -1;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_producto);
        dbHelper = new DatabaseHelper(this); // Instanciar Acceso a Datos

        etNombre = findViewById(R.id.et_nombre);
        etCantidad = findViewById(R.id.et_cantidad);
        etPrecio = findViewById(R.id.et_precio);

        // Inicializar el Botón <--- ¡ESTO DEBE IR ANTES DE USARLO!
        btnGuardar = findViewById(R.id.btn_guardar);

        // Lógica de Edición/Creación (donde se usa setText)
        Intent intent = getIntent();
        productoId = intent.getIntExtra("ID_PRODUCTO_EDITAR", -1);

        if (productoId != -1) {
            // Estamos en modo edición
            cargarDatosProducto(productoId);
            btnGuardar.setText("ACTUALIZAR PRODUCTO");
        } else {
            // Estamos en modo creación
            btnGuardar.setText("GUARDAR PRODUCTO");
        }

        // Configurar el botón para llamar a la función de guardado/actualización
        btnGuardar.setOnClickListener(v -> guardarOActualizarProducto());
    }

    // -----------------------------------------------------------------
// Método para cargar los datos del producto
// -----------------------------------------------------------------
    private void cargarDatosProducto(int id) {
        Producto producto = dbHelper.obtenerProducto(id);
        if (producto != null) {
            etNombre.setText(producto.getNombre());
            etCantidad.setText(String.valueOf(producto.getCantidad()));
            etPrecio.setText(String.valueOf(producto.getPrecio()));
        }
    }


    // -----------------------------------------------------------------
// Método unificado para GUARDAR o ACTUALIZAR
// -----------------------------------------------------------------
    private void guardarOActualizarProducto() {
        // Limpiar errores previos
        etNombre.setError(null);
        etCantidad.setError(null);
        etPrecio.setError(null);

        String nombre = etNombre.getText().toString().trim();
        String sCantidad = etCantidad.getText().toString().trim();
        String sPrecio = etPrecio.getText().toString().trim();

        // =========================================================
        // 1. VALIDACIÓN USANDO LA CLASE Validaciones.java
        // =========================================================

        // 1.1. Validar nombre (no vacío)
        if (!Validaciones.esNombreCompleto(nombre)) {
            etNombre.setError("El nombre del producto no puede estar vacío.");
            return;
        }

        // 1.2. Validar que Cantidad y Precio no estén vacíos antes de la conversión
        if (sCantidad.isEmpty()) {
            etCantidad.setError("La cantidad es obligatoria.");
            return;
        }
        if (sPrecio.isEmpty()) {
            etPrecio.setError("El precio es obligatorio.");
            return;
        }

        // Variables numéricas
        int cantidad = 0;
        double precio = 0.0;

        try {
            // Validación de formato: Intentar la conversión
            cantidad = Integer.parseInt(sCantidad);
            precio = Double.parseDouble(sPrecio);
        } catch (NumberFormatException e) {
            // La validación de formato debe quedar aquí, ya que necesita el try-catch.
            Toast.makeText(this, "Formato numérico inválido en Cantidad o Precio.", Toast.LENGTH_LONG).show();
            return;
        }

        // 1.3. Validación de Cantidad (no negativa)
        if (!Validaciones.esCantidadValida(cantidad)) {
            etCantidad.setError("La cantidad no puede ser negativa.");
            return;
        }

        // 1.4. Validación de Precio (mayor a cero)
        if (!Validaciones.esPrecioValido(precio)) {
            etPrecio.setError("El precio debe ser mayor a cero.");
            return;
        }

        // =========================================================
        // 2. CREACIÓN/ACTUALIZACIÓN Y ALMACENAMIENTO
        // =========================================================

        Producto producto = new Producto(nombre, cantidad, precio);

        // ... (Lógica final de guardado/actualización) ...
        // ... (El resto del método sigue igual) ...

        boolean resultado;

        if (productoId != -1) {
            producto.setId(productoId);
            resultado = dbHelper.actualizarProducto(producto);
        } else {
            resultado = dbHelper.insertarProducto(producto);
        }

        if (resultado) {
            Toast.makeText(this, (productoId != -1) ? "Producto actualizado." : "Producto guardado.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al procesar la operación en la base de datos.", Toast.LENGTH_LONG).show();
        }
    }
}
