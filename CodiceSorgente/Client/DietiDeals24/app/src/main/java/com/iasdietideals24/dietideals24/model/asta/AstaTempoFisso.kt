package com.iasdietideals24.dietideals24.model.asta

import com.iasdietideals24.dietideals24.model.Notifica
import com.iasdietideals24.dietideals24.model.account.Account
import com.iasdietideals24.dietideals24.model.offerta.OffertaTempoFisso
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time

class AstaTempoFisso(
    categoria: String,
    nome: String,
    descrizione: String,
    dataScadenza: Date,
    oraScadenza: Time,
    immaginiList: MutableList<String>,
    notificheAssociateList: MutableList<Notifica>,
    proprietario: Account,

    private var sogliaMinima: BigDecimal,
    private var offerteRicevuteList: MutableList<OffertaTempoFisso>
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

    fun getSogliaMinima(): BigDecimal {
        return sogliaMinima
    }

    fun setSogliaMinima(sogliaMinima: BigDecimal) {
        this.sogliaMinima = sogliaMinima
    }

    fun getOfferteRicevuteList(): MutableList<OffertaTempoFisso> {
        return offerteRicevuteList
    }

    fun setOfferteRicevuteList(offerteRicevuteList: MutableList<OffertaTempoFisso>) {
        this.offerteRicevuteList = offerteRicevuteList
    }

    fun addOfferta(offerta: OffertaTempoFisso) {
        offerteRicevuteList.add(offerta)
    }

    fun removeOfferta(offerta: OffertaTempoFisso) {
        offerteRicevuteList.remove(offerta)
    }
}