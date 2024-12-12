# Backend

## Backend package structure

All'interno della directory `src/main/java/` abbiamo la seguente struttura:

1. `com.iasdietideals24.backend` è il root package, che contiene l'entrypoint della Spring API e tutti gli altri packages
    1. `controllers` contiene i REST API Controllers, che si occupano di gestire le richieste HTTP. Gli oggetti JSON che arrivano vengono mappati in DTO. Costituisce il *presentation layer* (è l'interfaccia con il mondo esterno, costituisce il boundary del BCE pattern)
    2. `mapstruct` contiene le classi utilizzate per sviluppare un layer di isolamento tra *presentation layer* e *service layer*.
       1. `dto` contiene i Data Transfer Object, utilizzati come mezzo di comunicazione dei dati dal *presentation layer* al *service layer*.
       2. `mappers` contiene le classi utilizzate per mappare i Data Transfer Object nelle classi rappresentative del dominio, cioè le `entities`.
    3. `services` contiene le classi che si occupano di gestire la business logic e la separazione tra boundary ed entity (costituisce il control del BCE pattern).
    4. `entities` contiene le classi del dominio, che serviranno anche per configurare le entità della JPA. Possono essere utilizzate solo dal *service layer* e come modello per le repository (costituisce l'entity del BCE pattern).
    5. `repositories` contiene le repository interfaces, che sono usate dalla JPA per implementare le operazioni di CRUD per interagire con il database.
    6. `exceptions` contiene exceptions custom.
    7. `config` contiene le classi di configurazione, che contengono alcuni Beans iniettati.
    8. `scheduled` contiene le classi con metodi eseguiti a intervalli di tempo regolari.

## REST API

Gli endpoint esposti dalla DietiDeals24 REST API sono consultabili alla pagina di [documentazione generata tramite Postman](https://documenter.getpostman.com/view/37147881/2sAYBd67Sj).
