package com.iasdietideals24.dietideals24.model.asta


import com.iasdietideals24.dietideals24.model.Notifica
import com.iasdietideals24.dietideals24.model.account.Account
import com.iasdietideals24.dietideals24.model.offerta.OffertaInversa
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time

class AstaInversa(
    categoria: String,
    nome: String,
    descrizione: String,
    dataScadenza: Date,
    oraScadenza: Time,
    immaginiList: MutableList<String>,
    notificheAssociateList: MutableList<Notifica>,
    proprietario: Account,

    private var sogliaIniziale: BigDecimal,
    private var offerteRicevuteList: MutableList<OffertaInversa>
) : AstaCompratore(
    categoria,
    nome,
    descrizione,
    dataScadenza,
    oraScadenza,
    immaginiList,
    notificheAssociateList,
    proprietario
) {

    fun getSogliaIniziale(): BigDecimal {
        return sogliaIniziale
    }

    fun setSogliaIniziale(sogliaIniziale: BigDecimal) {
        this.sogliaIniziale = sogliaIniziale
    }

    fun getOfferteRicevuteList(): MutableList<OffertaInversa> {
        return offerteRicevuteList
    }

    fun setOfferteRicevuteList(offerteRicevuteList: MutableList<OffertaInversa>) {
        this.offerteRicevuteList = offerteRicevuteList
    }

    fun addOffertaRicevuta(offerta: OffertaInversa) {
        offerteRicevuteList.add(offerta)
    }

    fun removeOffertaRicevuta(offerta: OffertaInversa) {
        offerteRicevuteList.remove(offerta)
    }
}