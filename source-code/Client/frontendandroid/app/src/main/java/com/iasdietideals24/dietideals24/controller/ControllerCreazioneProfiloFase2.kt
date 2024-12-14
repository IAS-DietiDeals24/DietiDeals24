package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.lifecycle.Observer
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.Creazioneprofilofase2Binding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnNextStep
import com.iasdietideals24.dietideals24.utilities.tools.ImageHandler
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ControllerCreazioneProfiloFase2 : Controller<Creazioneprofilofase2Binding>() {

    // ViewModel
    private val viewModel: ModelRegistrazione by activityViewModel()

    private lateinit var selectPhoto: ActivityResultLauncher<PickVisualMediaRequest>

    // Listeners
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
        selectPhoto = registerForActivityResult(PickVisualMedia()) { uri: Uri? ->
            viewModel.immagineProfilo.value = ImageHandler.comprimiByteArray(uri, fragmentContext)
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
        selectPhoto.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }
}