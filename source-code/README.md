# source-code

In questa directory sono contenuti i file di implementazione del progetto.

## Contenuti della directory

- In [Client](./Client/) vi sono:
    - Il progetto [frontend Android](./Client/frontendandroid/) sviluppato in *Kotlin*.
- In [Server](./Server/) vi sono:
    - Il progetto [backend](./Server/backend/) sviluppato in *Java* con *Spring Framework*. In particolare, ci siamo avvalsi delle Spring Dependencies offerte da *Spring Boot*
    - La directory [database](./Server/database/), contenente file utili per la costruzione del database
- In [docker-compose](./docker-compose/) vi sono:
    - I file di build automatica per l'ambiente di sviluppo e l'ambiente di produzione
- In [scripts](./scripts/) vi sono:
    - Gli scripts per costruire e rimuovere rapidamente [l'ambiente di sviluppo](./scripts/devcompose/) e [l'ambiente di produzione](./scripts/prodcompose/). Per entrambi vi sono sia la versione per *Windows* che per sistemi *Unix* che utilizzano *bash*
- In [sonarqube](./sonarqube/) vi sono:
    - I file di build automatica per un container contenente ciò che è necessario per far funzionare *SonarQube*
    - Gli scripts per costruire rapidamente i container per *SonarQube*