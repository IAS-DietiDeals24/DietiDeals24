package com.iasdietideals24.dietideals24.model.account

import com.iasdietideals24.dietideals24.model.Notifica
import com.iasdietideals24.dietideals24.model.Profilo
import com.iasdietideals24.dietideals24.model.asta.AstaCompratore
import com.iasdietideals24.dietideals24.model.offerta.OffertaCompratore

class AccountCompratore(
    email: String,
    password: String,
    profilo: Profilo,
    notificheInviate: MutableList<Notifica>,
    notificheRicevute: MutableList<Notifica>,

    private var astePosseduteList: MutableList<AstaCompratore>,
    private var offerteCollegateList: MutableList<OffertaCompratore>
) : Account(email, password, profilo, notificheInviate, notificheRicevute) {

    fun getAstePosseduteList(): MutableList<AstaCompratore> {
        return astePosseduteList
    }

    fun getOfferteCollegateList(): MutableList<OffertaCompratore> {
        return offerteCollegateList
    }

    fun setAstePosseduteList(astePosseduteList: MutableList<AstaCompratore>) {
        this.astePosseduteList = astePosseduteList
    }

    fun setOfferteCollegateList(offerteCollegateList: MutableList<OffertaCompratore>) {
        this.offerteCollegateList = offerteCollegateList
    }

    fun addAstaPosseduta(astaCompratore: AstaCompratore) {
        astePosseduteList.add(astaCompratore)
    }

    fun addOffertaCollegata(offertaCompratore: OffertaCompratore) {
        offerteCollegateList.add(offertaCompratore)
    }

    fun removeAstaPosseduta(astaCompratore: AstaCompratore) {
        astePosseduteList.remove(astaCompratore)
    }


    fun removeOffertaCollegata(offertaCompratore: OffertaCompratore) {
        offerteCollegateList.remove(offertaCompratore)
    }

}