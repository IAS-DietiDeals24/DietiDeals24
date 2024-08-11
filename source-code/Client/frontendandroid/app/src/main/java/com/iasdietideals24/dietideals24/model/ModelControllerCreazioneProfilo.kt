package com.iasdietideals24.dietideals24.model

import android.net.Uri
import com.iasdietideals24.dietideals24.utilities.annotations.Validation
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import java.sql.Date

class ModelControllerCreazioneProfilo {

    private var _nomeUtente: String = ""

    private var _nome: String = ""

    private var _cognome: String = ""

    private var _dataNascita: Date? = null

    private var _immagineProfilo: Uri = Uri.EMPTY

    private var _biografia: String = ""

    private var _areaGeografica: String = ""

    private var _genere: String = ""

    private var _linkPersonale: String = ""

    private var _linkInstagram: String = ""

    private var _linkFacebook: String = ""

    private var _linkGitHub: String = ""

    private var _linkX: String = ""

    var nomeUtente: String
        get() = _nomeUtente
        set(valore) {
            _nomeUtente = valore
        }

    var nome: String
        get() = _nome
        set(valore) {
            _nome = valore
        }

    var cognome: String
        get() = _cognome
        set(valore) {
            _cognome = valore
        }

    var dataNascita: Date?
        get() = _dataNascita
        set(valore) {
            _dataNascita = valore
        }

    var immagineProfilo: Uri
        get() = _immagineProfilo
        set(valore) {
            _immagineProfilo = valore
        }

    var biografia: String
        get() = _biografia
        set(valore) {
            _biografia = valore
        }

    var areaGeografica: String
        get() = _areaGeografica
        set(valore) {
            _areaGeografica = valore
        }

    var genere: String
        get() = _genere
        set(valore) {
            _genere = valore
        }

    var linkPersonale: String
        get() = _linkPersonale
        set(valore) {
            _linkPersonale = valore
        }

    var linkInstagram: String
        get() = _linkInstagram
        set(valore) {
            _linkInstagram = valore
        }

    var linkFacebook: String
        get() = _linkFacebook
        set(valore) {
            _linkFacebook = valore
        }

    var linkGitHub: String
        get() = _linkGitHub
        set(valore) {
            _linkGitHub = valore
        }

    var linkX: String
        get() = _linkX
        set(valore) {
            _linkX = valore
        }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    fun validate() {
        nomeUtente()
        nome()
        cognome()
        dataNascita()
    }

    @Throws(EccezioneCampiNonCompilati::class)
    private fun nomeUtente() {
        if (_nomeUtente.isEmpty())
            throw EccezioneCampiNonCompilati("Nome utente non compilato.")
    }

    @Throws(EccezioneCampiNonCompilati::class)
    private fun nome() {
        if (_nome.isEmpty())
            throw EccezioneCampiNonCompilati("Nome non compilato.")
    }

    @Throws(EccezioneCampiNonCompilati::class)
    private fun cognome() {
        if (_cognome.isEmpty())
            throw EccezioneCampiNonCompilati("Cognome non compilato.")
    }

    @Throws(EccezioneCampiNonCompilati::class)
    private fun dataNascita() {
        if (_dataNascita == null)
            throw EccezioneCampiNonCompilati("Data di nascita non compilata.")
    }
}