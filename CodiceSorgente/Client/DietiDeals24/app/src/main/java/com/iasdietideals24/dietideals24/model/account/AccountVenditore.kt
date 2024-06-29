package com.iasdietideals24.dietideals24.model.account

import com.iasdietideals24.dietideals24.model.Notifica
import com.iasdietideals24.dietideals24.model.Profilo
import com.iasdietideals24.dietideals24.model.asta.AstaVenditore
import com.iasdietideals24.dietideals24.model.offerta.OffertaVenditore

class AccountVenditore(
    email: String,
    password: String,
    profilo: Profilo,
    notificheInviate: MutableList<Notifica>,
    notificheRicevute: MutableList<Notifica>,

    private var astePosseduteList: MutableList<AstaVenditore>,
    private var offerteCollegateList: MutableList<OffertaVenditore>
) : Account(email, password, profilo, notificheInviate, notificheRicevute) {

    fun getAstePosseduteList(): MutableList<AstaVenditore> {
        return astePosseduteList
    }

    fun getOfferteCollegateList(): MutableList<OffertaVenditore> {
        return offerteCollegateList
    }

    fun setAstePosseduteList(astePosseduteList: MutableList<AstaVenditore>) {
        this.astePosseduteList = astePosseduteList
    }

    fun setOfferteCollegateList(offerteCollegateList: MutableList<OffertaVenditore>) {
        this.offerteCollegateList = offerteCollegateList
    }

    fun addAstaPosseduta(astaVenditore: AstaVenditore) {
        astePosseduteList.add(astaVenditore)
    }

    fun addOffertaCollegata(offertaVenditore: OffertaVenditore) {
        offerteCollegateList.add(offertaVenditore)
    }

    fun removeAstaPosseduta(astaVenditore: AstaVenditore) {
        astePosseduteList.remove(astaVenditore)
    }


    fun removeOffertaCollegata(offertaVenditore: OffertaVenditore) {
        offerteCollegateList.remove(offertaVenditore)
    }

}