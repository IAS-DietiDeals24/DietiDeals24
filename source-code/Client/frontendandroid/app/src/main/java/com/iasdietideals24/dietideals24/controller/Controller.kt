package com.iasdietideals24.dietideals24.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.APIController
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.annotations.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Method

abstract class Controller(
    private val layout: Int = 0
) : Activity() {

    private val userId: Int = 0

    @UIBuilder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layout)

        trovaElementiInterfaccia()

        impostaMessaggiCorpo()

        impostaEventiClick()

        impostaEventiDiCambiamentoCampi()

        elaborazioneAggiuntiva()
    }

    @UIBuilder
    protected abstract fun trovaElementiInterfaccia()

    @UIBuilder
    protected open fun impostaMessaggiCorpo() {
        // Non fare nulla
    }

    @UIBuilder
    protected abstract fun impostaEventiClick()

    @UIBuilder
    protected open fun impostaEventiDiCambiamentoCampi() {
        // Non fare nulla
    }

    @UIBuilder
    protected open fun elaborazioneAggiuntiva() {
        // Non fare nulla
    }

    @Utility
    protected fun <model> eseguiChiamataREST(methodName: String, vararg args: Any?): model? {
        var returned: model? = null

        val argTypes: Array<Class<Any>?> = args.map { it?.javaClass }.toTypedArray()
        val method: Method = APIController.instance.javaClass.getMethod(methodName, *argTypes)

        @Suppress("UNCHECKED_CAST")
        val call: Call<model> = method.invoke(APIController.instance, *args) as Call<model>

        call.enqueue(object : Callback<model> {
            override fun onResponse(call: Call<model>, response: Response<model>) {
                if (response.isSuccessful) {
                    returned = response.body()
                    onRESTSuccess<model>(call, response)
                }
            }

            override fun onFailure(call: Call<model>, t: Throwable) {
                onRESTFailure<model>(call, t)
            }
        })

        return returned;
    }

    @Utility
    protected open fun <model> onRESTSuccess(call: Call<model>, response: Response<model>) {
        // Non fare nulla
    }

    @Utility
    protected open fun <model> onRESTFailure(call: Call<model>, t: Throwable) {
        // Non fare nulla
    }

    @Utility
    protected fun estraiTestoDaElemento(elemento: TextInputEditText): String {
        val testoElemento = elemento.text
        return testoElemento.toString()
    }

    @Utility
    protected fun estraiTestoDaElemento(elemento: TextView): String {
        val testoElemento = elemento.text
        return testoElemento.toString()
    }

    @Utility
    protected fun rimuoviErroreCampo(vararg campiEvidenziati: TextInputLayout) {
        for (campo in campiEvidenziati)
            campo.error = null
    }

    @Utility
    protected fun rimuoviMessaggioErrore(
        layoutDoveEliminareMessaggio: LinearLayout,
        indiceMessaggioErrore: Int
    ) {
        layoutDoveEliminareMessaggio.removeViewAt(indiceMessaggioErrore)
    }

    @Utility
    protected fun evidenziaCampiErrore(vararg campiDaEvidenziare: TextInputLayout) {
        for (campo in campiDaEvidenziare)
            campo.error = getString(R.string.erroreCampi)
    }

    @Utility
    protected fun erroreCampo(
        layout: ViewGroup,
        messaggioErrore: String,
        indiceMessaggioErrore: Int,
        vararg campiEvidenziati: TextInputLayout
    ) {
        creaMessaggioErroreCampi(layout, messaggioErrore, indiceMessaggioErrore)
        evidenziaCampiErrore(*campiEvidenziati)
    }

    @Utility
    private fun creaMessaggioErroreCampi(
        layout: ViewGroup,
        messaggioErrore: String,
        indiceMessaggioErrore: Int
    ) {
        val testoMessaggio = messaggioErrore
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layout.addView(messaggioDiErrore, indiceMessaggioErrore)
    }

    @Utility
    private fun creaMessaggioErrore(testoMessaggio: String): MaterialTextView {
        val messaggio = MaterialTextView(this)

        messaggio.text = testoMessaggio

        messaggio.textSize = 20f

        val grigio = resources.getColor(R.color.grigio, theme)
        messaggio.setBackgroundColor(grigio)

        val rosso = resources.getColor(R.color.rosso, theme)
        messaggio.setTextColor(rosso)

        val parametriLayoutTextView =
            LayoutParams(420 * resources.displayMetrics.density.toInt(), MATCH_PARENT)
        messaggio.layoutParams = parametriLayoutTextView

        messaggio.setBackgroundResource(R.drawable.errore)

        messaggio.setPadding(60, 0, 0, 0)

        return messaggio
    }

    @Utility
    protected fun cambiaAttivita(controller: Class<*>) {
        val nuovaAttivita = Intent(this, controller)
        startActivity(nuovaAttivita)
    }

    @Utility
    protected fun cambiaAttivita(controller: Class<*>, vararg extras: Pair<String, String>) {
        val nuovaAttivita = Intent(this, controller)
        for (extra in extras)
            nuovaAttivita.putExtra(extra.first, extra.second)
        startActivity(nuovaAttivita)
    }
}