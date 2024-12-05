package com.iasdietideals24.backend.services.helper;

import com.iasdietideals24.backend.entities.*;

import java.util.Set;

public interface BuildNotice {

    void notifyNuovaOfferta(OffertaInversa offertaInversa);

    void notifyNuovaOfferta(OffertaSilenziosa offertaSilenziosa);

    void notifyNuovaOfferta(OffertaTempoFisso offertaTempoFisso);

    void notifyOffertaSilenziosaRifiutata(OffertaSilenziosa offertaSilenziosa);

    void notifyOffertaSilenziosaRifiutata(Set<OffertaSilenziosa> offertaSilenziosa);

    void notifyOffertaSilenziosaAccettata(OffertaSilenziosa offertaSilenziosa);

    void notifyAstaInversaScaduta(AstaInversa astaInversa);

    void notifyAstaSilenziosaScaduta(AstaSilenziosa astaSilenziosa);

    void notifyAstaTempoFissoScaduta(AstaTempoFisso astaTempoFisso);

    void notifyOffertaInversaVincitrice(OffertaInversa offertaVincitrice);

    void notifyOffertaInversaPerdente(Set<OffertaInversa> offertePerdeti);

    void notifyOffertaTempoFissoVincitrice(OffertaTempoFisso offertaVincitrice);

    void notifyOffertaTempoFissoPerdente(Set<OffertaTempoFisso> offertePerdeti);
}
