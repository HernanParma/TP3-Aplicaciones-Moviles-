package com.example.appteca

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetalleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val appId = intent.getIntExtra("appId", -1)
        val app = Catalogo.apps.find { it.id == appId }

        if (app == null) {
            finish()
            return
        }

        // Asignamos los textos de la app encontrada
        findViewById<TextView>(R.id.tvDetNombre).text = app.nombre
        findViewById<TextView>(R.id.tvDetCategoria).text = app.categoria
        findViewById<TextView>(R.id.tvDetDescripcion).text = app.descripcion

        val btnFavorito = findViewById<Button>(R.id.btnFavoritoDetalle)

        // Función auxiliar para actualizar el texto del botón según el estado
        fun actualizarBoton() {
            btnFavorito.text = if (app.esFavorita) "★ Quitar de favoritos" else "☆ Marcar como favorita"
        }

        // Estado inicial del botón al abrir la pantalla
        actualizarBoton()

        // Acción al hacer clic en el botón de favorito
        btnFavorito.setOnClickListener {
            app.esFavorita = !app.esFavorita
            actualizarBoton()
        }
    }
}