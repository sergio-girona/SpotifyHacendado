package com.example.musicapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty

class ListDialogue(context: Context, private var playlist : PlayList, private  var mainActivity: MainActivity) : Dialog(context), View.OnClickListener {
    private lateinit var container : LinearLayout
    private lateinit var llistaCompleta : ArrayList<Canciones>

    override fun onCreate(savedInstanceState: Bundle?){
        setContentView(R.layout.diolgue_list_layout)
        container = findViewById(R.id.llistaCançons)

        llistaCompleta = playlist.llista

        container.setOnClickListener{
            if(container.isNotEmpty()){
                for (i in 0 until container.childCount){
                    val song : View = container.getChildAt(i)
                    if(song is LinearLayout){
                        Log.d("Comprovar", llistaCompleta[i].nom)
                    }
                }
            }
        }

        val closeButton: Button = findViewById(R.id.close_button)
        closeButton.setOnClickListener(this)
    }

    fun crearLlista(playlist1: PlayList?){
        container.removeAllViews()
        if (playlist1!=null){
            pintarCançons(playlist1)
        }
    }

    fun pintarCançons(playlist1: PlayList) {
        val llistaSongs = playlist1.llista
        for (i in 0 until llistaSongs.size){
            crearView(llistaSongs[i])
        }
    }

    fun crearView(cancion : Canciones){
        val fila = LinearLayout(context)
        fila.orientation = LinearLayout.HORIZONTAL
        val params2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params2.setMargins(0,25 , 0, 0)
        fila.layoutParams=params2

        val textView = TextView(context)
        textView.text = cancion.nom
        textView.isSelected = true
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        textView.isSingleLine = true
        textView.marqueeRepeatLimit = -1
        val paramsTxtView = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            2F
        )
        textView.layoutParams=paramsTxtView
        fila.addView(textView)

        val playButton = Button(context)
        playButton.setBackgroundResource(R.drawable.play_button)
        val paramsButton = LinearLayout.LayoutParams(
            100,
            100,
            0.5F
        )
        playButton.layoutParams=paramsButton
        fila.addView(playButton)

        fila.setOnClickListener {
            mainActivity.actualizarInterfaz(cancion.nom , cancion.imatge)
            mainActivity.configurarMediaPlayer(cancion.uri)
        }

        container.addView(fila)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id){
                R.id.close_button -> dismiss()
            }
        }
    }
}