package com.iasdietideals24.dietideals24.controller

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterHome
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiAnteprimaAsta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class ControllerHome : Controller(R.layout.home) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var campoRicerca: TextInputLayout
    private lateinit var ricerca: TextInputEditText
    private lateinit var campoFiltro: TextInputLayout
    private lateinit var filtro: MaterialAutoCompleteTextView

    private val mutex: Mutex = Mutex()
    private var jobRecupero: Job? = null
    private var jobRicerca: Job? = null

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        recyclerView = fragmentView.findViewById(R.id.home_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(fragmentContext)

        campoRicerca = fragmentView.findViewById(R.id.home_campoRicerca)
        ricerca = fragmentView.findViewById(R.id.home_ricerca)
        campoFiltro = fragmentView.findViewById(R.id.home_campoFiltro)
        filtro = fragmentView.findViewById(R.id.home_filtro)
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        ricerca.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Non fare niente
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Non fare niente
            }

            override fun afterTextChanged(s: Editable?) {
                jobRicerca?.cancel()

                jobRicerca = lifecycleScope.launch {
                    delay(500)

                    if (isActive) {
                        mutex.withLock {
                            eseguiRicerca(s.toString())
                        }
                    }
                }
            }

        })
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        filtro.setSimpleItems(resources.getStringArray(R.array.home_categorieAsta))

        jobRecupero = lifecycleScope.launch {
            while (isActive) {
                mutex.withLock {
                    aggiornaAste()
                }

                delay(30000)
            }
        }
    }

    private suspend fun eseguiRicerca(query: String) {
        val result: Array<DatiAnteprimaAsta>? = withContext(Dispatchers.IO) {
            eseguiChiamataREST(
                "ricercaAste",
                CurrentUser.id, query, filtro.text.toString()
            )
        }

        withContext(Dispatchers.Main) {
            if (result != null)
                recyclerView.adapter = AdapterHome(result, resources)
        }
    }

    private suspend fun aggiornaAste() {
        val result: Array<DatiAnteprimaAsta>? = withContext(Dispatchers.IO) {
            eseguiChiamataREST(
                "recuperaAste",
                CurrentUser.id
            )
        }

        withContext(Dispatchers.Main) {
            if (result != null)
                recyclerView.adapter = AdapterHome(result, resources)
        }
    }
}