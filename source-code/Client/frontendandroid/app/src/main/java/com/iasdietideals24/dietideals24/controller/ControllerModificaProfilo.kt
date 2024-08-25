package com.iasdietideals24.dietideals24.controller

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.ModificaprofiloBinding
import com.iasdietideals24.dietideals24.model.ModelProfilo
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.ImageHandler
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import java.time.LocalDate


class ControllerModificaProfilo : Controller<ModificaprofiloBinding>() {

    private lateinit var viewModel: ModelProfilo

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var selectPhoto: ActivityResultLauncher<String>

    private var listenerBackButton: OnFragmentBackButton? = null
    private var listenerConfirmButton: OnGoToProfile? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentBackButton) {
            listenerBackButton = requireContext() as OnFragmentBackButton
        }
        if (requireContext() is OnGoToProfile) {
            listenerConfirmButton = requireContext() as OnGoToProfile
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerConfirmButton = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.modificaProfiloPulsanteIndietro.setOnClickListener {
            listenerBackButton?.onFragmentBackButton()
        }

        binding.modificaProfiloPulsanteConferma.setOnClickListener {
            var returned: Boolean? = eseguiChiamataREST("aggiornaProfilo", viewModel.toProfilo())

            if (returned == null)
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            else if (returned == true) {
                Snackbar.make(
                    fragmentView,
                    R.string.modificaProfilo_successoModifica,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                listenerConfirmButton?.onGoToProfile(
                    CurrentUser.id,
                    ControllerModificaProfilo::class
                )
            } else
                Snackbar.make(
                    fragmentView,
                    R.string.modificaProfilo_fallimentoModifica,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
        }

        binding.modificaProfiloPulsante.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
            } else {
                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
            }
        }

        binding.modificaProfiloFacebook.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popuplinksocial, view as ViewGroup?, false)
            val input: TextInputEditText = viewInflated.findViewById(R.id.popupLinkSocial_campoLink)
            input.setText(viewModel.linkFacebook.value)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.crea_titoloPopupTipoAsta)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ ->
                    viewModel.linkFacebook.value = input.getText().toString()
                }
                .setNegativeButton(R.string.annulla) { _, _ -> }
                .show()
        }

        binding.modificaProfiloInstagram.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popuplinksocial, view as ViewGroup?, false)
            val input: TextInputEditText = viewInflated.findViewById(R.id.popupLinkSocial_campoLink)
            input.setText(viewModel.linkInstagram.value)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.crea_titoloPopupTipoAsta)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ ->
                    viewModel.linkInstagram.value = input.getText().toString()
                }
                .setNegativeButton(R.string.annulla) { _, _ -> }
                .show()
        }

        binding.modificaProfiloGithub.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popuplinksocial, view as ViewGroup?, false)
            val input: TextInputEditText = viewInflated.findViewById(R.id.popupLinkSocial_campoLink)
            input.setText(viewModel.linkGitHub.value)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.crea_titoloPopupTipoAsta)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ ->
                    viewModel.linkGitHub.value = input.getText().toString()
                }
                .setNegativeButton(R.string.annulla) { _, _ -> }
                .show()
        }

        binding.modificaProfiloX.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popuplinksocial, view as ViewGroup?, false)
            val input: TextInputEditText = viewInflated.findViewById(R.id.popupLinkSocial_campoLink)
            input.setText(viewModel.linkX.value)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.crea_titoloPopupTipoAsta)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ ->
                    viewModel.linkX.value = input.getText().toString()
                }
                .setNegativeButton(R.string.annulla) { _, _ -> }
                .show()
        }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity).get(ModelProfilo::class)

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
        val nomeUtenteObserver = Observer<String> { newNomeUtente ->
            binding.modificaProfiloNomeUtente.text = newNomeUtente
        }
        viewModel.nomeUtente.observe(viewLifecycleOwner, nomeUtenteObserver)

        val immagineObserver = Observer<ByteArray> { newByteArray: ByteArray? ->
            if (newByteArray?.isNotEmpty() == true)
                binding.modificaProfiloImmagineUtente.load(newByteArray) {
                    crossfade(true)
                }
        }
        viewModel.immagineProfilo.observe(viewLifecycleOwner, immagineObserver)

        val nomeObserver = Observer<String> { newNome ->
            binding.modificaProfiloNome.setText(newNome)
        }
        viewModel.nome.observe(viewLifecycleOwner, nomeObserver)

        val cognomeObserver = Observer<String> { newCognome ->
            binding.modificaProfiloCognome.setText(newCognome)
        }
        viewModel.cognome.observe(viewLifecycleOwner, cognomeObserver)

        val dataNascitaObserver = Observer<LocalDate> { newData ->
            binding.modificaProfiloDataNascita.setText(newData.toLocalStringShort())
        }
        viewModel.dataNascita.observe(viewLifecycleOwner, dataNascitaObserver)

        val genereObserver = Observer<String> { newGenere ->
            binding.modificaProfiloGenere.setText(newGenere)
        }
        viewModel.genere.observe(viewLifecycleOwner, genereObserver)

        val areaGeograficaObserver = Observer<String> { newAreaGeografica ->
            binding.modificaProfiloAreaGeografica.setText(newAreaGeografica)
        }
        viewModel.areaGeografica.observe(viewLifecycleOwner, areaGeograficaObserver)

        val biografiaObserver = Observer<String> { newBiografia ->
            binding.modificaProfiloBiografia.setText(newBiografia)
        }
        viewModel.biografia.observe(viewLifecycleOwner, biografiaObserver)

        val linkPersonaleObserver = Observer<String> { newLinkPersonale ->
            binding.modificaProfiloLinkPersonale.setText(newLinkPersonale)
        }
        viewModel.linkPersonale.observe(viewLifecycleOwner, linkPersonaleObserver)
    }
}