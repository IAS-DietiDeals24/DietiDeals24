package com.iasdietideals24.backend.utilities;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.shallows.*;

public interface RelationsConverter {

    /**
     * Converte uno Shallow DTO per Profilo nell'entità corrispondente effettivamente salvata nel persistance layer
     */
    Profilo convertProfiloShallowRelation(ProfiloShallowDto profiloShallowDto) throws IdNotFoundException;

    /**
     * Converte uno Shallow DTO per Notifica nell'entità corrispondente effettivamente salvata nel persistance layer
     */
    Notifica convertNotificaShallowRelation(NotificaShallowDto notificaShallowDto) throws IdNotFoundException;

    /**
     * Converte uno Shallow DTO per Asta nell'entità corrispondente effettivamente salvata nel persistance layer
     */
    Asta convertAstaShallowRelation(AstaShallowDto astaShallowDto) throws IdNotFoundException, InvalidTypeException;

    /**
     * Converte uno Shallow DTO per Offerta nell'entità corrispondente effettivamente salvata nel persistance layer
     */
    Offerta convertOffertaShallowRelation(OffertaShallowDto offertaShallowDto) throws IdNotFoundException, InvalidTypeException;

    /**
     * Converte uno Shallow DTO per Offerta nell'entità corrispondente effettivamente salvata nel persistance layer
     */
    Account convertAccountShallowRelation(AccountShallowDto accountShallowDto) throws IdNotFoundException, InvalidTypeException;

    /**
     * Converte uno Shallow DTO per Categoria Asta nell'entità corrispondente effettivamente salvata nel persistance layer
     */
    CategoriaAsta convertCategoriaAstaShallowRelation(CategoriaAstaShallowDto categoriaAstaShallowDto) throws IdNotFoundException;
}
