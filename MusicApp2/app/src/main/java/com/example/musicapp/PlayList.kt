package com.example.musicapp

import java.io.Serializable

class PlayList : Serializable{
    private lateinit var nomPlayList :String
    var llista: ArrayList<Canciones> = ArrayList()

    //Métode afegir nom a la playlist
    fun aplicarNom(nom:String){
        nomPlayList = nom
    }

    // Método para agregar una canción a la lista de reproducción
    fun agregarCancion(cancion: Canciones) {
        llista.add(cancion)
    }

    // Método para obtener la lista de canciones
    fun obtenerListaCanciones(): ArrayList<Canciones> {
        return llista
    }

    fun getNom(): String {
        return nomPlayList;
    }
    fun getNumCançons():String{
        return llista.size.toString();
    }
}