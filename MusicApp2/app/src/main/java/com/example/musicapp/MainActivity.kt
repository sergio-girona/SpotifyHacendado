package com.example.musicapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var seekVolum: SeekBar
    private lateinit var imatgeAlbum : ImageView
    private lateinit var btnPlay: TextView
    private lateinit var btnAdd: TextView
    private lateinit var btnList: TextView
    private lateinit var btnVolum: TextView
    private var isPlaying: Boolean = false
    private lateinit var handler: Handler
    private lateinit var audioManager: AudioManager
    private lateinit var pathMusic : String
    private lateinit var nomCancion : TextView
    private val REQUEST_CODE_PICK_AUDIO = 101
    private lateinit var listDialogue : ListDialogue
    private var playlist : PlayList? = null
    private lateinit var novaCancion : Canciones
    private lateinit var noMiniatura : ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent: Intent = intent
        playlist = intent.getSerializableExtra("Llista") as? PlayList

        val titolApp : TextView = findViewById(R.id.titolApp)
        if(playlist!!.getNom().isNotBlank())
            titolApp.text = playlist!!.getNom()

        listDialogue = ListDialogue(this, playlist!!, this)
        listDialogue.show()
        listDialogue.dismiss()
        novaCancion = Canciones()
        noMiniatura = tansformByteArray(R.drawable.folagoro)

        pathMusic = "/data/media/0/Music"
        mediaPlayer = MediaPlayer()
        imatgeAlbum = findViewById(R.id.miniaturaMusica)
        btnPlay = findViewById(R.id.btnPlay)
        btnVolum = findViewById(R.id.btnVolum)
        btnAdd = findViewById(R.id.btnAfegir)
        btnList = findViewById(R.id.btnLlista)
        seekBar = findViewById(R.id.seekBar)
        seekVolum = findViewById(R.id.seekBarVolum)
        nomCancion = findViewById(R.id.nom_cancion)
        handler = Handler()
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        seekVolum.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        btnList.setOnClickListener{
            listDialogue.show()
        }

        mediaPlayer.setOnCompletionListener {
            isPlaying = false
            seekBar.progress  = 0
            btnPlay.setBackgroundResource(R.drawable.play_button)
        }
        btnVolum.setOnClickListener {
            toggleSeekBarVisibility()
        }

        btnPlay.setOnClickListener {
            if (!isPlaying) {
                mediaPlayer.start()
                btnPlay.setBackgroundResource(R.drawable.pause_button)
                isPlaying = true
                updateSeekBar()
            } else {
                mediaPlayer.pause()
                btnPlay.setBackgroundResource(R.drawable.play_button)
                isPlaying = false
            }
        }

        // Configura el SeekBar para actualizar su progreso durante la reproducción
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekVolum.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Aquí puedes manejar el cambio de volumen
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnAdd.setOnClickListener {
            // Intent para abrir el explorador de archivos en una ruta específica
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"  // Filtrar solo archivos de audio, puedes ajustar según tus necesidades
            intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse("file://$pathMusic"))

            startActivityForResult(intent, REQUEST_CODE_PICK_AUDIO)

            Log.d("MiTag", "Contenido del array: ${playlist?.llista?.joinToString()}")
        }
    }

    private fun updateSeekBar() {
        // Actualiza la posición del SeekBar cada 1000 ms (1 segundo)
        handler.postDelayed({
            seekBar.progress = mediaPlayer.currentPosition
            updateSeekBar()
        }, 1000)
    }

    private fun toggleSeekBarVisibility() {
        if (seekVolum.visibility == View.VISIBLE) {
            seekVolum.visibility = View.GONE
        } else {
            seekVolum.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }
    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_AUDIO && resultCode == RESULT_OK && data != null) {
            val selectedAudioUri = data.data
            if (selectedAudioUri != null) {
                // Obtener información de la canción (título y miniatura)
                obtenerInformacionCancion(selectedAudioUri)
                // Configurar el MediaPlayer
                configurarMediaPlayer(selectedAudioUri)
            }
        }
    }

    private fun obtenerInformacionCancion(uriCancion: Uri){
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(this, uriCancion)

        val titulo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val miniaturaBytes = retriever.embeddedPicture

        retriever.release()

        if (titulo != null) {
            if (miniaturaBytes != null) {
                novaCancion.crearCancion(uriCancion, titulo,miniaturaBytes)
            }
            else{
                novaCancion.crearCancion(uriCancion,titulo,noMiniatura)
            }
        }

        var existeix = false
        for (i in 0 until playlist?.llista?.size!!){
            val canço = playlist?.llista!![i].nom
            if (novaCancion.nom == canço){
                existeix = true
            }
        }
        if(!existeix){
            playlist?.llista?.add(novaCancion)
            listDialogue.crearLlista(playlist)
        }
        actualizarInterfaz(novaCancion.nom,novaCancion.imatge)
        novaCancion = Canciones()
    }

    fun actualizarInterfaz(titulo: String?, miniaturaBytes: ByteArray?) {
        // Mostrar el título en el TextView
        nomCancion.text = titulo
        nomCancion.isSelected = true
        nomCancion.ellipsize = TextUtils.TruncateAt.MARQUEE
        nomCancion.isSingleLine = true
        nomCancion.marqueeRepeatLimit = -1

        // Mostrar la miniatura en el ImageView si está disponible
        miniaturaBytes?.let { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            imatgeAlbum.setImageBitmap(bitmap)
        }
    }

    private fun tansformByteArray(id: Int) : ByteArray {
        val imatgeByte = this.resources.openRawResource(id)
        return imatgeByte.readBytes()
    }

     fun configurarMediaPlayer(uriCancion: Uri) {
        try {
            if(isPlaying){
                btnPlay.performClick()
            }
            mediaPlayer.reset()
            mediaPlayer.setDataSource(this, uriCancion)
            mediaPlayer.prepare()
            seekBar.max = mediaPlayer.duration
            btnPlay.performClick()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}