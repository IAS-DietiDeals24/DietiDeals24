package com.iasdietideals24.dietideals24.controller

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.Creazioneprofilofase2Binding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.ImageHandler
import com.iasdietideals24.dietideals24.utilities.interfaces.OnBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnNextStep

class ControllerCreazioneProfiloFase2 : Controller<Creazioneprofilofase2Binding>() {

    private lateinit var viewModel: ModelRegistrazione

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var selectPhoto: ActivityResultLauncher<String>

    private var listenerBackButton: OnBackButton? = null
    private var listenerNextStep: OnNextStep? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnBackButton) {
            listenerBackButton = requireContext() as OnBackButton
        }
        if (requireContext() is OnNextStep) {
            listenerNextStep = requireContext() as OnNextStep
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerNextStep = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.creazioneProfiloFase2PulsanteIndietro.setOnClickListener { clickIndietro() }
        binding.creazioneProfiloFase2PulsanteAvanti.setOnClickListener { clickAvanti() }
        binding.creazioneProfiloFase2CampoFoto.setOnClickListener { clickImmagine() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelRegistrazione::class]

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

    @UIBuilder
    override fun impostaOsservatori() {
        val immagineObserver = Observer<ByteArray> { newByteArray: ByteArray? ->
            when {
                newByteArray == null || newByteArray.isEmpty() -> {
                    binding.creazioneProfiloFase2CampoFoto.scaleType =
                        ImageView.ScaleType.CENTER_INSIDE
                    binding.creazioneProfiloFase2CampoFoto.setImageResource(R.drawable.icona_fotocamera)
                }

                else -> {
                    binding.creazioneProfiloFase2CampoFoto.load(newByteArray) {
                        crossfade(true)
                    }
                    binding.creazioneProfiloFase2CampoFoto.scaleType =
                        ImageView.ScaleType.CENTER_CROP
                }
            }
        }
        viewModel.immagineProfilo.observe(viewLifecycleOwner, immagineObserver)

        val biografiaObserver = Observer<String> { newBiografia ->
            binding.creazioneProfiloFase2Biografia.setText(newBiografia)
        }
        viewModel.biografia.observe(viewLifecycleOwner, biografiaObserver)

        val areaGeograficaObserver = Observer<String> { newAreaGeografica ->
            binding.creazioneProfiloFase2AreaGeografica.setText(newAreaGeografica)
        }
        viewModel.areaGeografica.observe(viewLifecycleOwner, areaGeograficaObserver)

        val genereObserver = Observer<String> { newGenere ->
            binding.creazioneProfiloFase2Genere.setText(newGenere)
        }
        viewModel.genere.observe(viewLifecycleOwner, genereObserver)
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onBackButton()
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel.biografia.value = estraiTestoDaElemento(binding.creazioneProfiloFase2Biografia)
        viewModel.areaGeografica.value =
            estraiTestoDaElemento(binding.creazioneProfiloFase2AreaGeografica)
        viewModel.genere.value = estraiTestoDaElemento(binding.creazioneProfiloFase2Genere)

        listenerNextStep?.onNextStep(this::class)
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