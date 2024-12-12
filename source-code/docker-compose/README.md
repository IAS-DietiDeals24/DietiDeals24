# docker-compose

I Docker Compose files qui presenti sono necessari per la corretta configurazione del Production Environment e del Development Environment del **backend**.

In particolare, vi è un file di building comune ai due ambienti, cioè `docker-compose.yaml` a cui vengono poi sovrascritte o aggiunte informazioni in base al tipo di ambiente che si vuole configurare. Tali override vengono fatti da:

- `docker-compose.prod.yaml` per l'ambiente di produzione
- `docker-compose.dev.yaml` per l'ambiente di sviluppo

Se è presente un file `.env`, verranno automaticamente iniettate le variabili d'ambiente settate.

Per maggiori informazioni sul come configurare questi file per avere un ambiente pronto e funzionante, riferirsi al README nella [directory dedicata al Server](../Server/).
