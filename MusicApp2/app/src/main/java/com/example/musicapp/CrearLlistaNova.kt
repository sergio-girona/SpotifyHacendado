package com.example.musicapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginLeft

class CrearLlistaNova(context: Context, private val pantallaPrincipal: GridLayout , private val llistes: ArrayList<PlayList>) : Dialog(context), View.OnClickListener {
    private lateinit var nom: String
    private lateinit var nomTextView: EditText
    private lateinit var novaPlayList: PlayList
    private var numllista = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.crear_llista)

        nomTextView = findViewById(R.id.nomPlayList)
        novaPlayList = PlayList()

        val cancelarButton: Button = findViewById(R.id.cancelar)
        cancelarButton.setOnClickListener(this)

        val confirmButton: Button = findViewById(R.id.confirmButton)
        confirmButton.setOnClickListener(this)
    }

    private fun crearPlaylist() {
        nom = nomTextView.text.toString()
        novaPlayList.aplicarNom(nom)

        llistes.add(novaPlayList)
        numllista = llistes.indexOf(novaPlayList)

        AfegirLlista()

        dismiss()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.cancelar -> dismiss()
                R.id.confirmButton -> crearPlaylist()
            }
        }
    }

    fun AfegirLlista(){
        val container = LinearLayout(context)
        container.orientation = LinearLayout.HORIZONTAL
        val params2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params2.setMargins(0,25 , 0, 0)
        container.layoutParams=params2
        //container.id= numllista;

        val nomView = TextView(context)
        nomView.text = novaPlayList.getNom()
        nomView.textSize = 25F
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1F
        )
        params.setMargins(25,0 , 0, 0)
        nomView.layoutParams = params
        container.addView(nomView)

        val numCançonView = TextView(context)
        val numero = novaPlayList.getNumCançons()
        numCançonView.text = "Cançons: $numero"
        numCançonView.textSize = 25F
        val params1 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1F
        )
        params1.setMargins(60, 0, 0, 0)
        numCançonView.layoutParams = params1
        container.addView(numCançonView)

        val imageView = ImageView(context)
        imageView.setBackgroundResource(R.drawable.play_button)
        val params3 = LinearLayout.LayoutParams(
            135,
            135,
            1F
        )
        params3.setMargins(90, 0, 0, 0)
        imageView.layoutParams = params3
        container.addView(imageView)

        container.setOnClickListener{
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("Llista",llistes[numllista] )
            context.startActivity(intent)
        }

        pantallaPrincipal.addView(container)
    }
}