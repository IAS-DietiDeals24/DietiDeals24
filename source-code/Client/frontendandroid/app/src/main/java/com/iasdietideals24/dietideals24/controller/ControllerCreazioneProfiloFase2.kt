package com.iasdietideals24.dietideals24.controller

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.net.Uri
import android.os.Build
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelControllerCreazioneProfilo
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder

class ControllerCreazioneProfiloFase2 : Controller(R.layout.creazioneprofilofase2) {
    private var viewModel: ModelControllerCreazioneProfilo? = null

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var selectPhoto: ActivityResultLauncher<String>

    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var campoImmagine: ShapeableImageView
    private lateinit var biografia: TextInputEditText
    private lateinit var areaGeografica: TextInputEditText
    private lateinit var genere: TextInputEditText
    private lateinit var pulsanteAvanti: MaterialButton

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        pulsanteIndietro = findViewById(R.id.creazioneProfiloFase2_pulsanteIndietro)
        campoImmagine = findViewById(R.id.creazioneProfiloFase2_campoFoto)
        biografia = findViewById(R.id.creazioneProfiloFase2_Biografia)
        areaGeografica = findViewById(R.id.creazioneProfiloFase2_AreaGeografica)
        genere = findViewById(R.id.creazioneProfiloFase2_Genere)
        pulsanteAvanti = findViewById(R.id.creazioneProfiloFase2_pulsanteAvanti)
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteIndietro.setOnClickListener { clickIndietro() }
        pulsanteAvanti.setOnClickListener { clickAvanti() }
        campoImmagine.setOnClickListener { clickImmagine() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = recuperaViewModel()

        requestPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results: Map<String, Boolean> ->

                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                        when {
                            results.getOrDefault(READ_MEDIA_IMAGES, false) ->
                                selectPhoto.launch("image/*")

                            else ->
                                Toast.makeText(
                                    applicationContext,
                                    "Non hai concesso l'autorizzazione di accesso ai media.",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    }

                    else -> {
                        when {
                            results.getOrDefault(READ_EXTERNAL_STORAGE, false) ->
                                selectPhoto.launch("image/*")

                            else ->
                                Toast.makeText(
                                    applicationContext,
                                    "Non hai concesso l'autorizzazione di accesso ai media.",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    }
                }
            }

        selectPhoto =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

                when {
                    uri == null -> {
                        campoImmagine.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        campoImmagine.setImageResource(R.drawable.icona_fotocamera)
                        viewModel?.immagineProfilo = Uri.EMPTY
                    }

                    uri != null -> {
                        campoImmagine.load(uri) {
                            crossfade(true)
                        }
                        viewModel?.immagineProfilo = uri
                        campoImmagine.scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                }
            }
    }

    @EventHandler
    private fun clickIndietro() {
        setContentView(R.layout.creazioneprofilofase1)
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel?.biografia = estraiTestoDaElemento(biografia)
        viewModel?.areaGeografica = estraiTestoDaElemento(areaGeografica)
        viewModel?.genere = estraiTestoDaElemento(genere)

        cambiaAttivita(ControllerCreazioneProfiloFase3::class.java, viewModel!!)
    }

    @EventHandler
    private fun clickImmagine() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }
}