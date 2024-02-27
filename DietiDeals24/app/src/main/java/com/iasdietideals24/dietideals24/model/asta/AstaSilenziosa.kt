package com.iasdietideals24.dietideals24.model.asta

import android.graphics.Bitmap
import com.iasdietideals24.dietideals24.model.Notifica
import com.iasdietideals24.dietideals24.model.account.Account
import com.iasdietideals24.dietideals24.model.offerta.OffertaSilenziosa
import java.sql.Date
import java.sql.Time

class AstaSilenziosa(
    categoria: String,
    nome: String,
    descrizione: String,
    dataScadenza: Date,
    oraScadenza: Time,
    immaginiList: MutableList<String>,
    notificheAssociateList: MutableList<Notifica>,
    proprietario: Account,

    private var offerteRicevuteList: MutableList<OffertaSilenziosa>
) : AstaVenditore(
    categoria,
    nome,
    descrizione,
    dataScadenza,
    oraScadenza,
    immaginiList,
    notificheAssociateList,
    proprietario
) {

    fun getOfferteRicevuteList(): MutableList<OffertaSilenziosa> {
        return offerteRicevuteList
    }

    fun setOfferteRicevuteList(offerteRicevuteList: MutableList<OffertaSilenziosa>) {
        this.offerteRicevuteList = offerteRicevuteList
    }

    fun addOffertaRicevuta(offerta: OffertaSilenziosa) {
        offerteRicevuteList.add(offerta)
    }

    fun removeOffertaRicevuta(offerta: OffertaSilenziosa) {
        offerteRicevuteList.remove(offerta)
    }
}