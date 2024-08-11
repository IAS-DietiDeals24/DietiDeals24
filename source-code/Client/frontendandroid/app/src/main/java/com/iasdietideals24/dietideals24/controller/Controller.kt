package com.iasdietideals24.dietideals24.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.DateFormat
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.APIController
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.annotations.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Method
import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

abstract class Controller(
    private val layout: Int = 0
) : FragmentActivity() {

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
    protected fun <Model> eseguiChiamataREST(methodName: String, vararg args: Any?): Model? {
        var returned: Model? = null

        val argTypes: Array<Class<Any>?> = args.map { it?.javaClass }.toTypedArray()
        val method: Method = APIController.instance.javaClass.getMethod(methodName, *argTypes)

        @Suppress("UNCHECKED_CAST")
        val call: Call<Model> = method.invoke(APIController.instance, *args) as Call<Model>

        call.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                if (response.isSuccessful) {
                    returned = response.body()
                    onRESTSuccess<Model>(call, response)
                }
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
                onRESTFailure<Model>(call, t)
            }
        })

        return returned
    }

    @Utility
    protected open fun <Model> onRESTSuccess(call: Call<Model>, response: Response<Model>) {
        // Non fare nulla
    }

    @Utility
    protected open fun <Model> onRESTFailure(call: Call<Model>, t: Throwable) {
        // Non fare nulla
    }

    @Utility
    protected fun estraiTestoDaElemento(elemento: TextInputEditText): String {
        val testoElemento = elemento.text
        return testoElemento.toString()
    }

    @Utility
    protected fun estraiDataDaElemento(elemento: TextInputEditText): Date? {
        val testoElemento = elemento.text
        val stringaData = testoElemento.toString()

        return try {
            val inFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
            inFormat.isLenient = false
            val date = inFormat.parse(stringaData)

            @SuppressLint("SimpleDateFormat")
            val outFormat = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = outFormat.format(date)
            Date.valueOf(formattedDate)
        } catch (e: ParseException) {
            null
        }
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
    protected fun <controllerClass : Controller> cambiaAttivita(controller: Class<controllerClass>) {
        val nuovaAttivita = Intent(this, controller)
        startActivity(nuovaAttivita)
    }

    @Utility
    protected fun <controllerClass : Controller> cambiaAttivita(
        controller: Class<controllerClass>,
        viewModel: Any
    ) {
        val gson = Gson()
        val json = gson.toJson(viewModel)
        val nuovaAttivita = Intent(this, controller)
        nuovaAttivita.putExtra("viewModel", json)
        startActivity(nuovaAttivita)
    }

    @Utility
    protected inline fun <reified ViewModel> recuperaViewModel(): ViewModel {
        val gson = Gson()
        val json = intent.getStringExtra("viewModel").toString()
        return gson.fromJson(json, ViewModel::class.java)
    }

    @Utility
    protected fun salvaPreferenzaStringa(name: String, value: String) {
        val sharedPreferences =
            getSharedPreferences("com.iasdietideals24.dietideals24.preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(name, value)
        editor.apply()
    }

    @Utility
    protected fun caricaPreferenzaStringa(name: String): String? {
        val sharedPreferences =
            getSharedPreferences("com.iasdietideals24.dietideals24.preferences", MODE_PRIVATE)
        return sharedPreferences.getString(name, "")
    }
}