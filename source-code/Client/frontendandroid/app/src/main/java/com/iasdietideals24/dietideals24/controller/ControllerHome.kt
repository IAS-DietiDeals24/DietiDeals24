package com.iasdietideals24.dietideals24.controller

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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
import kotlinx.coroutines.withContext

class ControllerHome : Controller(R.layout.home) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var campoRicerca: TextInputLayout
    private lateinit var ricerca: TextInputEditText
    private lateinit var campoFiltro: TextInputLayout
    private lateinit var filtro: MaterialAutoCompleteTextView

    private var jobRecupero: Job? = null

    override fun onPause() {
        super.onPause()

        jobRecupero?.cancel()
    }

    override fun onResume() {
        super.onResume()

        if (ricerca.text.isNullOrEmpty()) {
            jobRecupero = lifecycleScope.launch {
                while (isActive) {
                    recuperaAste()

                    delay(10000)
                }
            }
        }
    }

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
                if (!s.isNullOrEmpty()) {
                    jobRecupero?.cancel()

                    jobRecupero = lifecycleScope.launch {
                        delay(1000)

                        while (isActive) {
                            recuperaAste()

                            delay(10000)
                        }
                    }
                } else if (s.isNullOrEmpty() && lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    jobRecupero = lifecycleScope.launch {
                        while (isActive) {
                            recuperaAste()

                            delay(10000)
                        }
                    }
                }
            }
        })
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        filtro.setText(R.string.category_no_category)
        filtro.setSimpleItems(resources.getStringArray(R.array.home_categorieAsta))
        filtro.setOnItemClickListener { _, _, _, _ ->
            jobRecupero?.cancel()

            jobRecupero = lifecycleScope.launch {
                while (isActive) {
                    recuperaAste()

                    delay(10000)
                }
            }
        }
    }

    private suspend fun recuperaAste() {
        val result: Array<DatiAnteprimaAsta>? = withContext(Dispatchers.IO) {
            if (ricerca.text.isNullOrEmpty() && filtro.text.toString() == getString(R.string.category_no_category))
                eseguiChiamataREST(
                    "recuperaAste",
                    CurrentUser.id
                )
            else
                eseguiChiamataREST(
                    "ricercaAste",
                    CurrentUser.id, ricerca.text.toString(), filtro.text.toString()
                )
        }

        withContext(Dispatchers.Main) {
            if (result != null)
                recyclerView.adapter = AdapterHome(result, resources)
            else
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
        }
    }
}