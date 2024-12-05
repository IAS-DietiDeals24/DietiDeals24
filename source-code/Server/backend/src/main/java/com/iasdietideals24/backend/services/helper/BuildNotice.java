package com.iasdietideals24.backend.services.helper;

import com.iasdietideals24.backend.entities.OffertaInversa;
import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import com.iasdietideals24.backend.entities.OffertaTempoFisso;

import java.util.Set;

public interface BuildNotice {

    void notifyNuovaOfferta(OffertaInversa offertaInversa);

    void notifyNuovaOfferta(OffertaSilenziosa offertaSilenziosa);

    void notifyNuovaOfferta(OffertaTempoFisso offertaTempoFisso);

    void notifyOffertaSilenziosaRifiutata(OffertaSilenziosa offertaSilenziosa);

    void notifyOffertaSilenziosaRifiutata(Set<OffertaSilenziosa> offertaSilenziosa);

    void notifyOffertaSilenziosaAccettata(OffertaSilenziosa offertaSilenziosa);
}
