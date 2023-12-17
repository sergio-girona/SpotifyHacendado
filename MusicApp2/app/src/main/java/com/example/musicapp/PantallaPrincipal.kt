package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PantallaPrincipal : AppCompatActivity() {
    private lateinit var btnAfegirPlayList: ImageView
    private lateinit var btnPlayList: ImageView
    private lateinit var crearLlista : CrearLlistaNova
    private lateinit var llistes : ArrayList<PlayList>
    private lateinit var vistaLlistes : GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_principal)

        llistes = ArrayList()
        crearLlista = CrearLlistaNova(this, findViewById(R.id.playlistContainer), llistes)
        btnAfegirPlayList = findViewById(R.id.crearLlista)
        btnPlayList = findViewById(R.id.veureLlista)
        vistaLlistes = findViewById(R.id.playlistContainer)

        btnAfegirPlayList.setOnClickListener{
            crearLlista.show()
        }
    }
}