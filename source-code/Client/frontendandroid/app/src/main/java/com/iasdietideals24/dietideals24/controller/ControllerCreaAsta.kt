package com.iasdietideals24.dietideals24.controller

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelAsta
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.ImageHandler
import com.iasdietideals24.dietideals24.utilities.classes.toLocalDate
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.classes.toMillis
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalTime

class ControllerCreaAsta : Controller(R.layout.creaasta) {

    private lateinit var viewModel: ModelAsta

    private lateinit var titolo: MaterialTextView
    private lateinit var campoData: TextInputLayout
    private lateinit var data: TextInputEditText
    private lateinit var campoOra: TextInputLayout
    private lateinit var ora: TextInputEditText
    private lateinit var layoutPrezzo: ConstraintLayout
    private lateinit var etichettaPrezzo: MaterialTextView
    private lateinit var campoPrezzo: TextInputLayout
    private lateinit var prezzo: TextInputEditText
    private lateinit var immagine: ShapeableImageView
    private lateinit var campoNome: TextInputLayout
    private lateinit var nome: TextInputEditText
    private lateinit var campoCategoria: TextInputLayout
    private lateinit var categoria: TextInputEditText
    private lateinit var campoDescrizione: TextInputLayout
    private lateinit var descrizione: TextInputEditText
    private lateinit var pulsanteCrea: MaterialButton

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var selectPhoto: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tipoAccount = runBlocking { caricaPreferenzaStringa("tipoAccount") }

        val asteCompratore = arrayOf(getString(R.string.tipoAsta_astaInversa))
        val asteVenditore = arrayOf(
            getString(R.string.tipoAsta_astaTempoFisso),
            getString(R.string.tipoAsta_astaSilenziosa)
        )
        var selezionato: Int = -1

        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.crea_titoloPopupTipoAsta)
            .setSingleChoiceItems(
                when (tipoAccount) {
                    "compratore" -> asteCompratore
                    "venditore" -> asteVenditore
                    else -> arrayOf()
                },
                selezionato,
                { _, which ->
                    selezionato = which
                }
            )
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                when (tipoAccount) {
                    "compratore" -> viewModel.tipo.value = asteCompratore[selezionato]
                    "venditore" -> viewModel.tipo.value = asteVenditore[selezionato]
                }
            }
            .show()
    }

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        titolo = fragmentView.findViewById(R.id.crea_tipoAsta)
        campoData = fragmentView.findViewById(R.id.crea_campoDataScadenza)
        data = fragmentView.findViewById(R.id.crea_dataScadenza)
        campoOra = fragmentView.findViewById(R.id.crea_campoOra)
        ora = fragmentView.findViewById(R.id.crea_ora)
        layoutPrezzo = fragmentView.findViewById(R.id.crea_constraintLayout3)
        etichettaPrezzo = fragmentView.findViewById(R.id.crea_etichettaPrezzo)
        campoPrezzo = fragmentView.findViewById(R.id.crea_campoPrezzo)
        prezzo = fragmentView.findViewById(R.id.crea_prezzo)
        immagine = fragmentView.findViewById(R.id.crea_campoFoto)
        campoNome = fragmentView.findViewById(R.id.crea_campoNome)
        nome = fragmentView.findViewById(R.id.crea_nome)
        campoCategoria = fragmentView.findViewById(R.id.crea_campoCategoria)
        categoria = fragmentView.findViewById(R.id.crea_categoria)
        campoDescrizione = fragmentView.findViewById(R.id.crea_campoDescrizione)
        descrizione = fragmentView.findViewById(R.id.crea_descrizione)
        pulsanteCrea = fragmentView.findViewById(R.id.crea_pulsanteCrea)
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (viewModel.tipo.value) {
            "Inversa" -> {
                titolo.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaInversa)
                )
                etichettaPrezzo.text = getString(
                    R.string.crea_prezzo,
                    getString(R.string.crea_prezzoPartenza)
                )
            }

            "Tempo fisso" -> {
                titolo.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaTempoFisso)
                )
                etichettaPrezzo.text = getString(
                    R.string.crea_prezzo,
                    getString(R.string.crea_prezzoMinimo)
                )
            }

            "Silenziosa" -> {
                titolo.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaSilenziosa)
                )
                layoutPrezzo.visibility = ConstraintLayout.GONE
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteCrea.setOnClickListener {
            clickCrea()
        }

        campoData.setEndIconOnClickListener {
            clickDataScadenza()
        }

        campoOra.setEndIconOnClickListener {
            clickOraScadenza()
        }
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        data.addTextChangedListener {
            rimuoviErroreCampi()
        }

        ora.addTextChangedListener {
            rimuoviErroreCampi()
        }

        prezzo.addTextChangedListener {
            rimuoviErroreCampi()
        }

        nome.addTextChangedListener {
            rimuoviErroreCampi()
        }

        categoria.addTextChangedListener {
            rimuoviErroreCampi()
        }

        descrizione.addTextChangedListener {
            rimuoviErroreCampi()
        }
    }

    private fun rimuoviErroreCampi() {
        rimuoviErroreCampo(
            campoData,
            campoOra,
            campoPrezzo,
            campoNome,
            campoCategoria,
            campoDescrizione
        )
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity).get(ModelAsta::class)

        requestPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results: Map<String, Boolean> ->
                apriGalleria(results)
            }

        selectPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.immagine.value = ImageHandler.encodeImage(uri, fragmentContext)
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
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
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
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()
                }
            }
        }
    }

    @Suppress("SENSELESS_COMPARISON")
    @UIBuilder
    override fun impostaOsservatori() {
        val dataFineObserver = Observer<LocalDate> { newData ->
            if (newData != LocalDate.MIN)
                data.setText(newData.toLocalStringShort())
            else
                data.setText("")
        }
        viewModel.dataFine.observe(viewLifecycleOwner, dataFineObserver)

        val oraFineObserver = Observer<LocalTime> { newOra ->
            ora.setText(newOra.toString())
        }
        viewModel.oraFine.observe(viewLifecycleOwner, oraFineObserver)

        val prezzoObserver = Observer<Double> { newPrezzo ->
            prezzo.setText(newPrezzo.toString())
        }
        viewModel.prezzo.observe(viewLifecycleOwner, prezzoObserver)

        val immagineObserver = Observer<ByteArray> { newByteArray: ByteArray? ->
            when {
                newByteArray == null || newByteArray.isEmpty() -> {
                    immagine.setImageResource(R.drawable.icona_fotocamera)
                }

                newByteArray != null -> {
                    immagine.load(newByteArray) {
                        crossfade(true)
                    }
                }
            }
        }
        viewModel.immagine.observe(viewLifecycleOwner, immagineObserver)

        val nomeObserver = Observer<String> { newNome ->
            nome.setText(newNome)
        }
        viewModel.nome.observe(viewLifecycleOwner, nomeObserver)

        val categoriaObserver = Observer<String> { newCategoria ->
            categoria.setText(newCategoria)
        }
        viewModel.categoria.observe(viewLifecycleOwner, categoriaObserver)

        val descrizioneObserver = Observer<String> { newDescrizione ->
            descrizione.setText(newDescrizione)
        }
        viewModel.descrizione.observe(viewLifecycleOwner, descrizioneObserver)
    }

    @EventHandler
    private fun clickCrea() {
        val retuned: Long? = eseguiChiamataREST("creaAsta")

        if (retuned != null) {
            viewModel.clear()

            Snackbar.make(fragmentView, R.string.crea_astaCreataConSuccesso, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()

            navController.navigate(R.id.action_controllerCreaAsta_to_controllerHome)
        } else {
            Snackbar.make(fragmentView, R.string.crea_astaNonCreata, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
        }

    }

    @EventHandler
    private fun clickDataScadenza() {
        val calendarConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .setFirstDayOfWeek(Calendar.MONDAY)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.crea_titoloPopupData)
            .setCalendarConstraints(calendarConstraints.build())
            .setSelection(
                if (viewModel.dataFine.value == LocalDate.MIN)
                    MaterialDatePicker.todayInUtcMilliseconds()
                else
                    viewModel.dataFine.value?.toMillis()
            )
            .setTheme(R.style.DatePicker)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            if (datePicker.selection != null) {
                val localDate = datePicker.selection!!.toLocalDate()

                viewModel.dataFine.value = localDate

                data.setText(localDate.toLocalStringShort())
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
    }

    @EventHandler
    private fun clickOraScadenza() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.crea_titoloPopupOra)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(
                if (viewModel.oraFine.value == null)
                    LocalTime.now().hour
                else
                    viewModel.oraFine.value!!.hour
            )
            .setMinute(
                if (viewModel.oraFine.value == null)
                    LocalTime.now().minute
                else
                    viewModel.oraFine.value!!.minute
            )
            .setTheme(R.style.DatePicker)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            viewModel.oraFine.value = LocalTime.of(timePicker.hour, timePicker.minute)
        }

        timePicker.show(requireActivity().supportFragmentManager, "timePicker")
    }
}
