# Backend structure

## Backend package structure

All'interno della directory `src/main/java/` abbiamo la seguente struttura:

1. `com.iasdietideals24.backend` è il root package, che contiene l'entrypoint della Spring API e tutti gli altri packages
    1. `controllers` contiene i REST API Controllers, che si occupano di gestire le richieste HTTP. Gli oggetti JSON che arrivano vengono mappati in DTO. Costituisce il *presentation layer* (è l'interfaccia con il mondo esterno, costituisce il boundary del BCE pattern)
    2. `mappers` contiene le classi utilizzate per mappare i DTO nelle classi rappresentative del dominio (le `entity`), utilizzate dal *service layer*.
    3. `dto` contiene i Data Transfer Object, utilizzati per trasferire i dati dal *presentation layer* al *service layer*.
    4. `services` contiene le classi che si occupano di gestire la business logic e la separazione tra boundary ed entity (costituisce il control del BCE pattern).
    5. `entities` contiene le classi del dominio, che serviranno anche per configurare le entità della JPA. Possono essere utilizzate solo dal *service layer* e come modello per le repository (costituisce l'entity del BCE pattern).
    6. `repositories` contiene le repository interfaces, che sono usate dalla JPA per implementare le operazioni di CRUD per interagire con il database.
    7. `exceptions` contiene exceptions custom.