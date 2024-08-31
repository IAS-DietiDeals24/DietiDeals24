package com.iasdietideals24.dietideals24.controller

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.ImageHandler
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import java.time.LocalDate


class ControllerModificaProfilo : Controller<ModificaprofiloBinding>() {

    private lateinit var viewModel: ModelProfilo

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var selectPhoto: ActivityResultLauncher<String>

    private var listenerProfile: OnGoToProfile? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnGoToProfile) {
            listenerProfile = requireContext() as OnGoToProfile
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerProfile = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.modificaProfiloPulsanteIndietro.setOnClickListener { clickIndietro() }

        binding.modificaProfiloPulsanteConferma.setOnClickListener { clickConferma() }

        binding.modificaProfiloPulsante.setOnClickListener { clickPulsanteImmagine() }

        binding.modificaProfiloFacebook.setOnClickListener { clickFacebook() }

        binding.modificaProfiloInstagram.setOnClickListener { clickInstagram() }

        binding.modificaProfiloGithub.setOnClickListener { clickGitHub() }

        binding.modificaProfiloX.setOnClickListener { clickX() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelProfilo::class]

        requestPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results: Map<String, Boolean> ->
                apriGalleria(results)
            }

        selectPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.immagineProfilo.value = ImageHandler.encodeImage(uri, fragmentContext)
        }
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val nomeUtenteObserver = Observer<String> { newNomeUtente ->
            binding.modificaProfiloNomeUtente.text = newNomeUtente
        }
        viewModel.nomeUtente.observe(viewLifecycleOwner, nomeUtenteObserver)

        val immagineObserver = Observer<ByteArray> { newByteArray: ByteArray? ->
            when {
                newByteArray == null || newByteArray.isEmpty() -> {
                    binding.modificaProfiloImmagineUtente.scaleType =
                        ImageView.ScaleType.CENTER_INSIDE
                    binding.modificaProfiloImmagineUtente.setImageResource(R.drawable.icona_profilo)
                }

                else -> {
                    binding.modificaProfiloImmagineUtente.load(newByteArray) {
                        crossfade(true)
                    }
                    binding.modificaProfiloImmagineUtente.scaleType =
                        ImageView.ScaleType.CENTER_CROP
                }
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

    @EventHandler
    private fun clickIndietro() {
        viewModel.clear()

        listenerProfile?.onGoToProfile(CurrentUser.id, ControllerModificaProfilo::class)
    }

    @EventHandler
    private fun clickConferma() {
        try {
            viewModel.validate()

            val returned: Boolean? =
                eseguiChiamataREST("aggiornaProfilo", viewModel.toProfilo())

            when (returned) {
                null -> Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                true -> {
                    Snackbar.make(
                        fragmentView,
                        R.string.modificaProfilo_successoModifica,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()

                    listenerProfile?.onGoToProfile(
                        CurrentUser.id,
                        ControllerModificaProfilo::class
                    )
                }

                else -> Snackbar.make(
                    fragmentView,
                    R.string.modificaProfilo_fallimentoModifica,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        } catch (_: EccezioneCampiNonCompilati) {
            erroreCampo(
                R.string.registrazione_erroreCampiObbligatoriNonCompilati,
                binding.modificaProfiloCampoNome,
                binding.modificaProfiloCampoCognome,
                binding.modificaProfiloCampoEmail,
                binding.modificaProfiloCampoDataNascita,
                binding.modificaProfiloCampoGenere
            )
        } catch (_: EccezioneEmailNonValida) {
            erroreCampo(
                R.string.registrazione_erroreFormatoEmail,
                binding.modificaProfiloCampoEmail
            )
        } catch (_: EccezioneEmailUsata) {
            erroreCampo(
                R.string.registrazione_erroreEmailGiàUsata,
                binding.modificaProfiloCampoEmail
            )
        }
    }

    @EventHandler
    private fun clickPulsanteImmagine() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }

    @EventHandler
    private fun clickFacebook() {
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

    @EventHandler
    private fun clickInstagram() {
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

    @EventHandler
    private fun clickGitHub() {
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

    @EventHandler
    private fun clickX() {
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
}