// Archivo: MainActivity.kt

package com.jamel.compuzoneapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
// Asegúrate de importar la actividad a la que quieres ir
import com.jamel.compuzoneapp.vista.ListaProductosActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Crear el Intent para ir a ListaProductosActivity
        val intent = Intent(this, ListaProductosActivity::class.java)

        // 2. Iniciar la actividad
        startActivity(intent)

        // 3. Terminar la MainActivity para que el usuario no pueda volver a ella
        finish()

        // NO USAR setContent {} o setContentView() aquí si la actividad se va a cerrar inmediatamente
    }
}