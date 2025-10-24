// Archivo: ListaProductosActivity.java
package com.jamel.compuzoneapp.vista;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog; // Opcional, si usas AppCompat
//import android.app.AlertDialog;           // Si usas el diálogo base de Android
import android.widget.Toast;              // Necesario para los mensajes

import com.jamel.compuzoneapp.R;
import com.jamel.compuzoneapp.database.DatabaseHelper;
import com.jamel.compuzoneapp.model.Producto;

import java.util.ArrayList;
import java.util.List;

public class ListaProductosActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    private List<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.lv_productos);
        FloatingActionButton fab = findViewById(R.id.fab_agregar_producto);

        // Configurar el botón FAB para ir a la actividad de registro
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ListaProductosActivity.this, RegistroProductoActivity.class);
            startActivity(intent);
        });
        // AÑADIDO: Configurar el clic en el ListView (EDITAR)
        // *****************************************************************
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Obtenemos el objeto Producto completo en la posición clickeada
            Producto productoAEditar = listaProductos.get(position);

            // Creamos el Intent para ir a RegistroProductoActivity
            Intent intent = new Intent(ListaProductosActivity.this, RegistroProductoActivity.class);

            // Enviamos el ID del producto como extra
            intent.putExtra("ID_PRODUCTO_EDITAR", productoAEditar.getId());

            startActivity(intent);
        });
        // *****************************************************************
        // AÑADIDO: Configurar el CLIC LARGO en el ListView (ELIMINAR)
        // *****************************************************************
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Obtenemos el ID del producto que se ha pulsado
            Producto productoSeleccionado = listaProductos.get(position);
            mostrarDialogoOpciones(productoSeleccionado);
            return true; // Importante retornar true para consumir el clic largo
        });

        cargarProductos();
    }

    // Este método asegura que la lista se refresque cada vez que volvemos a esta pantalla
    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos();
    }

    private void cargarProductos() {
        // 1. Obtener la lista de la Capa de Acceso a Datos (DatabaseHelper)
        listaProductos = dbHelper.obtenerTodosProductos();

        // 2. Crear el ADAPTADOR PERSONALIZADO y asignarlo al ListView
        // Ya no necesitamos la lista de Strings (datosAMostrar)

        ProductoAdapter adapter = new ProductoAdapter(
                this,
                listaProductos
        );

        listView.setAdapter(adapter);

        // Opcional: Mostrar mensaje si la lista está vacía
        if (listaProductos.isEmpty()) {
            // Podrías poner un TextView en el layout principal para mostrar "No hay productos"
            Toast.makeText(this, "No hay productos registrados.", Toast.LENGTH_LONG).show();
        }
    }

    // Dentro de ListaProductosActivity.java

    private void mostrarDialogoOpciones(Producto producto) {
        // Las opciones que mostraremos en el diálogo
        final String[] opciones = {"Editar Producto", "Eliminar Producto"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones para " + producto.getNombre());

        builder.setItems(opciones, (dialog, which) -> {
            if (which == 0) {
                // Opción 0: Editar Producto
                // Usamos la misma lógica que el clic normal
                Intent intent = new Intent(ListaProductosActivity.this, RegistroProductoActivity.class);
                intent.putExtra("ID_PRODUCTO_EDITAR", producto.getId());
                startActivity(intent);

            } else if (which == 1) {
                // Opción 1: Eliminar Producto
                mostrarDialogoConfirmarEliminar(producto);
            }
        });
        builder.show();
    }

    private void mostrarDialogoConfirmarEliminar(Producto producto) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar el producto: " + producto.getNombre() + "?")
                .setPositiveButton("Sí, Eliminar", (dialog, which) -> {
                    boolean eliminado = dbHelper.eliminarProducto(producto.getId());
                    if (eliminado) {
                        Toast.makeText(ListaProductosActivity.this, "Producto eliminado exitosamente.", Toast.LENGTH_SHORT).show();
                        // Refrescar la lista después de eliminar
                        cargarProductos();
                    } else {
                        Toast.makeText(ListaProductosActivity.this, "Error al eliminar el producto.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}