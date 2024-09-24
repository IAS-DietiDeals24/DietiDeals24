package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.annotations.Utility
import com.iasdietideals24.dietideals24.utilities.classes.DataStore.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.ParameterizedType
import kotlin.reflect.full.createInstance

abstract class Controller<bindingType : ViewBinding> : Fragment() {

    private var _binding: bindingType? = null

    /**
     * Una istanza di una classe generata a tempo di compilazione dai file XML delle viste, che
     * contiene tutti gli elementi della vista come campi della classe stessa.
     */
    protected val binding get() = _binding!!

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
     * Una proprietà che chiama requireActivity() nel fragment.
     */
    val fragmentActivity: FragmentActivity
        get() = requireActivity()

    /**
     * Effettua il binding delle classi generate dalla build.
     */
    @Suppress("UNCHECKED_CAST")
    @UIBuilder
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val classe =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        val metodo = classe.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        _binding = metodo.invoke(null, inflater, container, false) as bindingType
        val view = binding.root
        return view
    }

    /**
     * Esegue tutti i compiti principali per la creazione della vista, quali eventi di click, inizializzazione di messaggio, eccetera.
     */
    @UIBuilder
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        impostaMessaggiCorpo()

        impostaEventiClick()

        impostaEventiDiCambiamentoCampi()

        elaborazioneAggiuntiva()

        impostaOsservatori()
    }

    /**
     * Scollega il binding della vista per aiutare la garbage collection.
     */
    @UIBuilder
    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
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
     * @param call Wrapper con la funzione da chiamare.
     * @return Una classe generica [Dto] che contiene il risultato della chiamata REST.
     */
    protected inline fun <reified Dto : Any> chiamaAPI(call: Call<Dto>): Dto {
        var ret: Dto? = null

        call.enqueue(object : Callback<Dto> {
            override fun onResponse(call: Call<Dto>, response: Response<Dto>) {
                if (response.isSuccessful) {
                    ret = response.body()
                    onRESTSuccess(call, response)
                } else if (!response.isSuccessful)
                    onRESTUnsuccess(call, response)
            }

            override fun onFailure(call: Call<Dto>, t: Throwable) {
                onRESTFailure(call, t)
            }
        })

        return ret ?: Dto::class.createInstance()
    }

    /**
     * Usata per effettuare una operazione quando la chiamata REST ha avuto successo.
     * @param call Un wrapper con i dati della richiesta REST.
     * @param response Un wrapper con i dati della risposta REST.
     */
    @Utility
    protected open fun <Dto> onRESTSuccess(call: Call<Dto>, response: Response<Dto>) {
        // Non fare nulla
    }

    /**
     * Usata per effettuare una operazione quando la chiamata REST non ha avuto successo.
     * @param call Un wrapper con i dati della richiesta REST.
     * @param response Un wrapper con i dati della risposta REST.
     */
    @Utility
    protected open fun <Dto> onRESTUnsuccess(call: Call<Dto>, response: Response<Dto>) {
        Snackbar.make(
            fragmentView,
            R.string.responseError.toString() + response.code().toString(),
            Snackbar.LENGTH_SHORT
        )
            .setBackgroundTint(resources.getColor(R.color.blu, null))
            .setTextColor(resources.getColor(R.color.grigio, null))
            .show()
    }

    /**
     * Usata per effettuare una operazione quando la chiamata REST ha registrato un errore.
     * @param call Un wrapper con i dati della richiesta REST.
     * @param t L'eccezione o errore che ha causato la fallimento della chiamata REST.
     */
    @Utility
    protected open fun <Dto> onRESTFailure(call: Call<Dto>, t: Throwable) {
        Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(R.color.blu, null))
            .setTextColor(resources.getColor(R.color.grigio, null))
            .show()
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
     * @param campoErrore Campi ai quali impostare lo stato di errore.
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
     * @return Il valore della preferenza caricata.
     */
    @Utility
    protected suspend fun caricaPreferenzaStringa(name: String): String {
        return fragmentContext.dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey(name)] ?: ""
            }.first()
    }
}