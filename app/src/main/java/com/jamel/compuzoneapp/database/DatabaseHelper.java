package com.jamel.compuzoneapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jamel.compuzoneapp.model.Producto;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "drogueria.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PRODUCTOS = "productos";

    // Columnas...
    // Define la tabla y las columnas
    private static final String CREATE_TABLE_PRODUCTOS =
            "CREATE TABLE " + TABLE_PRODUCTOS + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT,"
                    + "cantidad INTEGER,"
                    + "precio REAL"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
    }

    // --- Métodos CRUD (La lógica de Acceso a Datos) ---

    // CREATE (INSERT)
    public boolean insertarProducto(Producto producto) {
        // 1. Obtener la base de datos en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Crear un contenedor de valores (ContentValues) para los datos
        ContentValues values = new ContentValues();

        // Asignar valores a las columnas (el ID es autoincremental)
        // Asegúrarse que los nombres de las columnas coincidan con los de tu tabla
        values.put("nombre", producto.getNombre());
        values.put("cantidad", producto.getCantidad());
        values.put("precio", producto.getPrecio());

        // 3. Insertar la nueva fila. El metodo insert retorna el ID de la fila insertada (long).
        // Si retorna -1, la inserción falló.
        long resultado = db.insert("productos", null, values);

        // 4. Cerrar la conexión y retornar el resultado
        db.close();

        // Retorna true si el ID insertado no fue -1
        return resultado != -1;
    }

    // READ All
    public List<Producto> obtenerTodosProductos() {
        List<Producto> listaProductos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para obtener todos los productos
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTOS, null);

        if (cursor.moveToFirst()) {
            do {
                Producto p = new Producto(
                        cursor.getInt(0), // id
                        cursor.getString(1), // nombre
                        cursor.getInt(2), // cantidad
                        cursor.getDouble(3) // precio
                );

                // Crea y añade el objeto Producto a la lista
                listaProductos.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaProductos;
    }

    // UPDATE
    public boolean actualizarProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Los campos que se van a actualizar
        values.put("nombre", producto.getNombre());
        values.put("cantidad", producto.getCantidad());
        values.put("precio", producto.getPrecio());

        // Ejecutar la actualización: WHERE id = ?
        int filasAfectadas = db.update(
                "productos",
                values,
                "id = ?",
                new String[]{String.valueOf(producto.getId())}
        );

        db.close();

        // Si filasAfectadas es mayor que 0, la actualización fue exitosa
        return filasAfectadas > 0;
    }

    // READ One
    public Producto obtenerProducto(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "productos",
                new String[]{"id", "nombre", "cantidad", "precio"},
                "id=?",
                new String[]{String.valueOf(id)},
                null, null, null, null
        );

        Producto producto = null;

        if (cursor != null && cursor.moveToFirst()) {
            producto = new Producto(
                    cursor.getInt(0),      // id
                    cursor.getString(1),   // nombre
                    cursor.getInt(2),      // cantidad
                    cursor.getDouble(3)    // precio
            );
        }

        if (cursor != null) {
            cursor.close();
        }

        return producto;
    }

    // DELETE
    public boolean eliminarProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // El metodo delete devuelve el número de filas afectadas.
        // Tabla: "productos"
        // Cláusula WHERE: "id = ?"
        // Argumentos de selección: String.valueOf(id)
        int filasEliminadas = db.delete(
                "productos",
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        // Retorna true si se eliminó al menos una fila (filasEliminadas > 0)
        return filasEliminadas > 0;
    }
}
