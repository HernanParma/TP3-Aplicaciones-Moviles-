package com.example.appteca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: AppAdapter
    private var soloFavoritas = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializamos el adaptador con los datos, el click a detalle y el click a favorita
        adapter = AppAdapter(
            Catalogo.apps,
            onAppClick = { app ->
                val intent = Intent(this, DetalleActivity::class.java)
                intent.putExtra("appId", app.id)
                startActivity(intent)
            },
            onFavoritoClick = { app ->
                app.esFavorita = !app.esFavorita
                aplicarFiltros()
            }
        )

        // Configuramos el RecyclerView (como indica la guía paso a paso)
        val rv = findViewById<RecyclerView>(R.id.rvApps)
        rv.layoutManager = LinearLayoutManager(this) // "en columna, de arriba a abajo"[cite: 5]
        rv.adapter = adapter

        // Listener para el buscador en tiempo real
        findViewById<EditText>(R.id.etBuscar).addTextChangedListener {
            aplicarFiltros()
        }

        // Listener para el botón de filtrar solo favoritas
        findViewById<Button>(R.id.btnSoloFav).setOnClickListener {
            soloFavoritas = !soloFavoritas
            aplicarFiltros()
        }

        aplicarFiltros()
    }

    override fun onResume() {
        super.onResume()
        aplicarFiltros() // Asegura que se actualice al volver de la pantalla de detalle
    }

    private fun aplicarFiltros() {
        val q = findViewById<EditText>(R.id.etBuscar).text.toString().trim()
        var lista: List<App> = Catalogo.apps

        // Filtrar por texto de búsqueda (nombre o categoría)
        if (q.isNotEmpty()) {
            lista = lista.filter {
                it.nombre.contains(q, true) || it.categoria.contains(q, true)
            }
        }

        // Filtrar por favoritos si está activo el modo
        if (soloFavoritas) {
            lista = lista.filter { it.esFavorita }
        }

        adapter.actualizarLista(lista)
        findViewById<Button>(R.id.btnSoloFav).text = if (soloFavoritas) "★ Solo favoritas" else "★ Todas"
    }
}