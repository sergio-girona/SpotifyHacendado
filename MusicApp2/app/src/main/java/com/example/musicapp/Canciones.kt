package com.example.musicapp

import android.graphics.Bitmap
import android.net.Uri

class Canciones {
    lateinit var uri : Uri
    lateinit var nom :String
    lateinit var imatge : ByteArray

    //Mètode per crear canço
    fun crearCancion(uriCancion: Uri, nomCancion :String, imatgeCancion:ByteArray){
        uri = uriCancion
        nom = nomCancion
        imatge = imatgeCancion
    }
}
