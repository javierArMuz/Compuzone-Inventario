// Archivo: ProductoAdapter.java
package com.jamel.compuzoneapp.vista;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.jamel.compuzoneapp.R; // Asegúrate de que R sea importado correctamente
import com.jamel.compuzoneapp.model.Producto;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<Producto> {

    private Context context;
    private List<Producto> productos;

    public ProductoAdapter(Context context, List<Producto> productos) {
        super(context, 0, productos);
        this.context = context;
        this.productos = productos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Obtener el ítem actual (el objeto Producto)
        Producto producto = productos.get(position);

        // 2. Inflar el layout si convertView es nulo
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        }

        // 3. Obtener referencias a los TextViews del item_producto.xml
        TextView tvNombre = convertView.findViewById(R.id.tv_item_nombre);
        TextView tvCantidad = convertView.findViewById(R.id.tv_item_cantidad);
        TextView tvPrecio = convertView.findViewById(R.id.tv_item_precio);

        // 4. Rellenar los datos con la información del objeto Producto
        if (producto != null) {
            tvNombre.setText(producto.getNombre());
            tvCantidad.setText("Cant: " + producto.getCantidad());

            // Formatear el precio para que se vea como moneda
            String precioFormateado = String.format("$ %.2f", producto.getPrecio());
            tvPrecio.setText(precioFormateado);
        }

        return convertView;
    }
}