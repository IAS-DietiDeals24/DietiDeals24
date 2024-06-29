package com.iasdietideals24.dietideals24.model.asta

import com.iasdietideals24.dietideals24.model.Notifica
import java.sql.Date
import java.sql.Time

abstract class Asta protected constructor(
    private var categoria: String,
    private var nome: String,
    private var descrizione: String,
    private var dataScadenza: Date,
    private var oraScadenza: Time,
    private var immaginiList: MutableList<String>,
    private var notificheAssociateList: MutableList<Notifica>
) {
    fun getCategoria(): String {
        return categoria
    }

    fun setCategoria(categoria: String) {
        this.categoria = categoria
    }

    fun getNome(): String {
        return nome
    }

    fun setNome(nome: String) {
        this.nome = nome
    }

    fun getDescrizione(): String {
        return descrizione
    }

    fun setDescrizione(descrizione: String) {
        this.descrizione = descrizione
    }

    fun getDataScadenza(): Date {
        return dataScadenza
    }

    fun setDataScadenza(dataScadenza: Date) {
        this.dataScadenza = dataScadenza
    }

    fun getOraScadenza(): Time {
        return oraScadenza
    }

    fun setOraScadenza(oraScadenza: Time) {
        this.oraScadenza = oraScadenza
    }

    fun getImmaginiList(): MutableList<String> {
        return immaginiList
    }

    fun setImmaginiList(immaginiList: MutableList<String>) {
        this.immaginiList = immaginiList
    }

    fun addImmagine(immagine: String) {
        immaginiList.add(immagine)
    }

    fun removeImmagine(immagine: String) {
        immaginiList.remove(immagine)
    }

    fun getNotificheAssociateList(): MutableList<Notifica> {
        return notificheAssociateList
    }

    fun setNotificheAssociateList(notificheAssociateList: MutableList<Notifica>) {
        this.notificheAssociateList = notificheAssociateList
    }

    fun addNotifica(notifica: Notifica) {
        notificheAssociateList.add(notifica)
    }

    fun removeNotifica(notifica: Notifica) {
        notificheAssociateList.remove(notifica)
    }
}