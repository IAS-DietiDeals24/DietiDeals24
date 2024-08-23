package com.iasdietideals24.dietideals24.controller

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.ImageHandler
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton

class ControllerCreazioneProfiloFase2 : Controller(R.layout.creazioneprofilofase2) {

    private lateinit var viewModel: ModelRegistrazione

    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var campoImmagine: ShapeableImageView
    private lateinit var biografia: TextInputEditText
    private lateinit var areaGeografica: TextInputEditText
    private lateinit var genere: TextInputEditText
    private lateinit var pulsanteAvanti: MaterialButton

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var selectPhoto: ActivityResultLauncher<String>

    private var listener: OnFragmentBackButton? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentBackButton) {
            listener = requireContext() as OnFragmentBackButton
        }
    }

    override fun onDetach() {
        super.onDetach()

        listener = null
    }

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        pulsanteIndietro = fragmentView.findViewById(R.id.creazioneProfiloFase2_pulsanteIndietro)
        campoImmagine = fragmentView.findViewById(R.id.creazioneProfiloFase2_campoFoto)
        biografia = fragmentView.findViewById(R.id.creazioneProfiloFase2_Biografia)
        areaGeografica = fragmentView.findViewById(R.id.creazioneProfiloFase2_AreaGeografica)
        genere = fragmentView.findViewById(R.id.creazioneProfiloFase2_Genere)
        pulsanteAvanti = fragmentView.findViewById(R.id.creazioneProfiloFase2_pulsanteAvanti)
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteIndietro.setOnClickListener { clickIndietro() }
        pulsanteAvanti.setOnClickListener { clickAvanti() }
        campoImmagine.setOnClickListener { clickImmagine() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity).get(ModelRegistrazione::class)

        requestPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results: Map<String, Boolean> ->
                apriGalleria(results)
            }

        selectPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.immagineProfilo.value = ImageHandler.encodeImage(uri, fragmentContext)
        }
    }

    private fun apriGalleria(results: Map<String, Boolean>) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                when {
                    results.getOrDefault(READ_MEDIA_IMAGES, false) ->
                        selectPhoto.launch("image/*")

                    else ->
                        Snackbar.make(fragmentView, R.string.noMediaAccess, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.arancione, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()
                }
            }

            else -> {
                when {
                    results.getOrDefault(READ_EXTERNAL_STORAGE, false) ->
                        selectPhoto.launch("image/*")

                    else ->
                        Snackbar.make(fragmentView, R.string.noMediaAccess, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.arancione, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()
                }
            }
        }
    }

    @Suppress("SENSELESS_COMPARISON")
    @UIBuilder
    override fun impostaOsservatori() {
        val immagineObserver = Observer<ByteArray> { newByteArray: ByteArray? ->
            when {
                newByteArray == null || newByteArray.isEmpty() -> {
                    campoImmagine.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    campoImmagine.setImageResource(R.drawable.icona_fotocamera)
                }

                newByteArray != null -> {
                    campoImmagine.load(newByteArray) {
                        crossfade(true)
                    }
                    campoImmagine.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }
        viewModel.immagineProfilo.observe(viewLifecycleOwner, immagineObserver)

        val biografiaObserver = Observer<String> { newBiografia ->
            biografia.setText(newBiografia)
        }
        viewModel.biografia.observe(viewLifecycleOwner, biografiaObserver)

        val areaGeograficaObserver = Observer<String> { newAreaGeografica ->
            areaGeografica.setText(newAreaGeografica)
        }
        viewModel.areaGeografica.observe(viewLifecycleOwner, areaGeograficaObserver)

        val genereObserver = Observer<String> { newGenere ->
            genere.setText(newGenere)
        }
        viewModel.genere.observe(viewLifecycleOwner, genereObserver)
    }

    @EventHandler
    private fun clickIndietro() {
        listener?.onFragmentBackButton()
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel.biografia.value = estraiTestoDaElemento(biografia)
        viewModel.areaGeografica.value = estraiTestoDaElemento(areaGeografica)
        viewModel.genere.value = estraiTestoDaElemento(genere)

        navController.navigate(R.id.action_controllerCreazioneProfiloFase2_to_controllerCreazioneProfiloFase3)
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