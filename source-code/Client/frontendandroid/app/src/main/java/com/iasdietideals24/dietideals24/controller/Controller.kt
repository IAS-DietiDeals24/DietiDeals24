package com.iasdietideals24.dietideals24.controller

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.DateFormat
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.iasdietideals24.dietideals24.utilities.APIController
import com.iasdietideals24.dietideals24.utilities.DataStore.dataStore
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.annotations.Utility
import com.iasdietideals24.dietideals24.utilities.classes.APIMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
) : Fragment(layout) {

    /**
     * Una proprietà che chiama requireContext() nel fragment.
     */
    val fragmentContext: Context
        get() = requireContext()

    /**
     * Una proprietà che chiama requireView() nel fragment.
     */
    val fragmentView: View
        get() = requireView()

    /**
     * Una proprietà che chiama findNavController() nel fragment.
     */
    val navController: NavController
        get() = findNavController()

    /**
     * Una proprietà che chiama requireActivity() nel fragment.
     */
    val fragmentActivity: FragmentActivity
        get() = requireActivity()

    /**
     * Esegue tutti i compiti principali per la creazione della vista, quali eventi di click, inizializzazione di messaggio, eccetera.
     */
    @UIBuilder
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trovaElementiInterfaccia()

        impostaMessaggiCorpo()

        impostaEventiClick()

        impostaEventiDiCambiamentoCampi()

        elaborazioneAggiuntiva()

        impostaOsservatori()
    }

    /**
     * Usato per trovare gli elementi di interfaccia all'interno della vista.
     */
    @UIBuilder
    protected open fun trovaElementiInterfaccia() {
        // Non fare nulla
    }

    /**
     * Usato per inizializzare i messaggi nella vista.
     */
    @UIBuilder
    protected open fun impostaMessaggiCorpo() {
        // Non fare nulla
    }

    /**
     * Usato per inizializzare gli eventi di click nella vista.
     */
    @UIBuilder
    protected open fun impostaEventiClick() {
        // Non fare nulla
    }

    /**
     * Usato per inizializzare gli eventi di cambio di campo nella vista.
     */
    @UIBuilder
    protected open fun impostaEventiDiCambiamentoCampi() {
        // Non fare nulla
    }

    /**
     * Usato per inizializzare gli osservatori dei dati in tempo reale per il ViewModel della vista.
     */
    @UIBuilder
    protected open fun impostaOsservatori() {
        // Non fare nulla
    }

    /**
     * Usato effettuare eventuali operazioni aggiuntive alla fine della creazione della vista.
     */
    @UIBuilder
    protected open fun elaborazioneAggiuntiva() {
        // Non fare nulla
    }

    /**
     * Effettua una chiamata REST e ne restituisce la risposta JSON deserializzata in un oggetto.
     * @param methodName Il nome del metodo da chiamare.
     * @param args Gli argomenti da passare al metodo.
     * @return Una classe generica che contiene il risultato della chiamata REST.
     */
    @Utility
    protected fun <Model> eseguiChiamataREST(methodName: String, vararg args: Any?): Model? {
        var returned: Model? = null

        val argTypes: Array<Class<Any>?> = args.map { it?.javaClass }.toTypedArray()
        val method: Method =
            APIController.instance.javaClass.getMethod(methodName, Int::class.java, *argTypes)

        @Suppress("UNCHECKED_CAST")
        val call: Call<Model> =
            method.invoke(APIController.instance, APIMode.DEBUG.ordinal, *args) as Call<Model>

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

    /**
     * Usata per effettuare una operazione quando la chiamata REST ha avuto successo.
     * @param call Un wrapper con i dati della richiesta REST.
     * @param response Un wrapper con i dati della risposta REST.
     */
    @Utility
    protected open fun <Model> onRESTSuccess(call: Call<Model>, response: Response<Model>) {
        // Non fare nulla
    }

    /**
     * Usata per effettuare una operazione quando la chiamata REST non ha avuto successo.
     * @param call Un wrapper con i dati della richiesta REST.
     * @param t L'eccezione o errore che ha causato la fallimento della chiamata REST.
     */
    @Utility
    protected open fun <Model> onRESTFailure(call: Call<Model>, t: Throwable) {
        // Non fare nulla
    }

    /**
     * Usata per estrarre il testo da un campo di testo della vista.
     * @param elemento Il campo di testo dal quale estrarre il testo.
     * @return Il testo estratto dal campo di testo.
     */
    @Utility
    protected fun estraiTestoDaElemento(elemento: TextInputEditText): String {
        val testoElemento = elemento.text
        return testoElemento.toString()
    }

    /**
     * Usata per estrarre la data da un campo di testo della vista.
     * @param elemento Il campo di testo dal quale estrarre la data.
     * @return La data estratta dal campo di testo.
     */
    @SuppressLint("SimpleDateFormat")
    @Utility
    protected fun estraiDataDaElemento(elemento: TextInputEditText): Date? {
        val testoElemento = elemento.text
        val stringaData = testoElemento.toString()

        return try {
            val inFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
            inFormat.isLenient = false
            val date = inFormat.parse(stringaData)

            val outFormat = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = outFormat.format(date)
            Date.valueOf(formattedDate)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * Usata per formattare una data nel formato predefinito per la località attuale.
     * @param data La data da formattare.
     * @return La data formattata.
     */
    protected fun formattaData(data: Date): String {
        val formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        return formatter.format(data)
    }

    /**
     * Usata per estrarre il testo da un messaggio della vista.
     * @param elemento Il campo di testo dal quale estrarre il testo.
     * @return Il testo estratto dal campo di testo.
     */
    @Utility
    protected fun estraiTestoDaElemento(elemento: TextView): String {
        val testoElemento = elemento.text
        return testoElemento.toString()
    }

    /**
     * Usata per rimuovere il messaggio di errore da un campo di testo della vista.
     * @param campiEvidenziati L'elenco dei campi ai quali rimuovere lo stato di errore.
     */
    @Utility
    protected fun rimuoviErroreCampo(vararg campiEvidenziati: TextInputLayout) {
        for (campo in campiEvidenziati)
            campo.error = null
    }

    /**
     * Usata per impostare il messaggio di errore su un campo di testo della vista.
     * @param messaggioErrore Il messaggio di errore da impostare.
     * @param campiEvidenziati L'elenco dei campi ai quali impostare lo stato di errore.
     */
    @Utility
    protected fun erroreCampo(messaggioErrore: Int, vararg campoErrore: TextInputLayout) {
        for (campo in campoErrore)
            campo.error = getString(messaggioErrore)
    }

    /**
     * Salva in maniera asincrona una preferenza nel datastore dell'applicazione.
     * @param name La chiave della preferenza da salvare.
     * @param value Il valore della preferenza da salvare.
     */
    @Utility
    protected suspend fun salvaPreferenzaStringa(name: String, value: String) {
        fragmentContext.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(name)] = value
        }
    }

    /**
     * Carica in maniera asincrona una preferenza dal datastore dell'applicazione.
     * @param name La chiave della preferenza da caricare.
     * @param value Il valore della preferenza da caricare.
     */
    @Utility
    protected suspend fun caricaPreferenzaStringa(name: String): String {
        return fragmentContext.dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey(name)] ?: ""
            }.first()
    }
}