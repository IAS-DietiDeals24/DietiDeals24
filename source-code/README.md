# Note per lo sviluppatore

## Indice

- [Client](#client)
- [Server (Backend e Database)](#server-backend-e-database)
   * [Software necessari](#software-necessari)
   * [Configurare il Production Environment](#configurare-il-production-environment)
   * [Configurare il Development Environment](#configurare-il-development-environment)
      + [IntelliJ IDEA](#intellij-idea)
         - [Clonando la repository del cloud nel Dev container](#clonando-la-repository-del-cloud-nel-dev-container)
         - [Aprendo la repository locale dell'host nel Dev container](#aprendo-la-repository-locale-dellhost-nel-dev-container)
      + [Visual Studio Code](#visual-studio-code)
         - [Clonando la repository del cloud nel Dev container](#clonando-la-repository-del-cloud-nel-dev-container-1)
         - [Aprendo la repository locale dell'host nel Dev container](#aprendo-la-repository-locale-dellhost-nel-dev-container-1)
         - [Recuperare un Dev Container già configurato](#recuperare-un-dev-container-già-configurato)
      + [Command Line Interface](#command-line-interface)
   * [Avviare il backend](#avviare-il-backend)
      + [Building con Maven](#building-con-maven)
      + [Eseguire il file .jar](#eseguire-il-file-jar)
   * [Accedere ai containers](#accedere-ai-containers)
      + [REST API](#rest-api)
      + [pgAdmin](#pgadmin)
- [SonarQube](#sonarqube)
   * [Accedere al SonarQube Server](#accedere-al-sonarqube-server)
   * [SonarQube Maven Scan (backend)](#sonarqube-maven-scan-backend)
   * [SonarQube Gradle Scan (frontedandroid)](#sonarqube-gradle-scan-frontedandroid)

## Client

## Server (Backend e Database)

### Software necessari

- [Docker](https://www.docker.com/get-started/): utilizzato per il setup dell'ambiente di sviluppo dell'applicativo. Inoltre, Docker è utilizzato per astrarre sia la REST API che il database dalla macchina del singolo sviluppatore. Attraverso Docker, vengono installate in automatico tutte le dependency necessarie.
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download) o [Visual Studio Code](https://code.visualstudio.com/Download) per utilizzare il Development Environment.

**ATTENZIONE!** Per utilizzare Docker su sistemi operativi Windows, è necessario installare il [WSL](https://learn.microsoft.com/it-it/windows/wsl/install).

### Configurare il Production Environment

Per eseguire i docker containers utilizzati in Production Environment:

1. Andiamo nella directory `source-code/docker-compose`
2. Eseguiamo il comando:
    
    ```bash
    docker compose -f docker-compose.yaml -f docker-compose.prod.yaml up -d 
    ```

    il flag `-f` specifica i percorsi dei docker compose files (dove il primo è il docker compose file principale) e `-d` esegue i containers in background

E' possibile fermare/rimuovere il docker compose del Production Environment con:

```bash
docker compose -f docker-compose.yaml -f docker-compose.prod.yaml down
```

### Configurare il Development Environment

#### IntelliJ IDEA

Per configurare il Development Environment per il backend con IntelliJ IDEA è necessario avere almeno la versione 2023.2. E' disponibile la [documentazione ufficiale](https://www.jetbrains.com/help/idea/connect-to-devcontainer.html). Possiamo scegliere uno dei seguenti modi per costruire il Dev Container.

##### Clonando la repository del cloud nel Dev container

1. Nella `Welcome page` andare in `Remote Development -> Dev Containers` e creiamo un nuovo devcontainer cliccando su `New Dev Container`.
2. Per clonare una repository, andare nella sezione `From VSC Project`.
3. Selezionare `Docker`.
4. In `Git Repository`, incollare l'indirizzo SSH della repository GitHub.
    - **NOTA:** Se otteniamo l'errore `The authenticity of host 'github.com (140.82.121.4)' can't be established.` allora:
        - Generiamo una SSH key (se non ce l'abbiamo già) e aggiungiamo quella pubblica a GitHub.
        - Aggiungiamo `github.com` agli host conosciuti.
5. Selezionare il `Git Branch` da clonare.
6. In `Detection for devcontainer.json file:` clicchiamo su `Specify Path` e incolliamo il seguente path:

    ```
    source-code/Server/backend/.devcontainer/devcontainer.json
    ```

7. Clicchiamo su `Build Container and Continue`.
8. Una volta creato il devcontainer, dovremo scegliere l'IDE da utilizzare. Scegliamo `IntelliJ IDEA 2024.1.4` (la versione non EAP) e clicchiamo su `Continue`.

In questo modo, cloneremo l'intera repository nel dev container e, in particolare, apriremo il progetto `backend`.

##### Aprendo la repository locale dell'host nel Dev container

Il procedimento è il medesimo di [Clonando la repository del cloud nel Dev container](#clonando-la-repository-del-cloud-nel-dev-container), eccetto che, una volta clonato il repository nell'host tramite git, al punto 2 dovremo scegliere `From Local Project` e in `Path to devcontainer.json` dovremo usare il path del punto 6 a partire dalla main directory del repository. Dopodiché passiamo al punto 7 e 8.

#### Visual Studio Code

Per configurare il Development Environment per il backend con Visual Studio Code è necessaria l'estensione [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) di Microsoft. E' disponibile la [documentazione ufficiale](https://code.visualstudio.com/docs/devcontainers/containers). Una volta installata l'estensione possiamo scegliere uno dei seguenti modi per costruire il Dev Container.

##### Clonando la repository del cloud nel Dev container

Affinché tutto funzioni correttamente è necessario **rimuovere**:

- La proprietà `volumes: - ../..:/myHostDir/DietiDeals24:cached` da `source-code/docker-compose/docker-compose.dev.yaml`
- La proprietà `"workspaceFolder": "/myHostDir/${localWorkspaceFolderBasename}"` da `.devcontainer/devcontainer.json`

Dopodiché:

1. Creiamo un container Volume andando in `View -> Command Palette...` (o semplicemente digitiamo `CTRL+SHIT+P`) e usiamo il comando:

    ```
    Dev Containers: Clone Repository in Named Container Volume...
    ```

2. Selezioniamo la repository e il branch da clonare nel devcontainer
3. Scegliamo il nome da dare al nuovo volume (o utilizziamone uno esistente) e confermiamo.

##### Aprendo la repository locale dell'host nel Dev container

1. Cloniamo il repository nell'host tramite git
2. Affinché tutto funzioni correttamente è necessario **aggiungere**:
    - La proprietà `volumes: - ../..:/myHostDir/DietiDeals24:cached` a `source-code/docker-compose/docker-compose.dev.yaml`
    - La proprietà `"workspaceFolder": "/myHostDir/${localWorkspaceFolderBasename}"` a `.devcontainer/devcontainer.json`
3. Andiamo in `View -> Command Palette...` (o semplicemente digitiamo `CTRL+SHIT+P`) e usiamo il comando:

    ```
    Dev Containers: Open Folder in Container...
    ```

4. Apriamo la main directory della repository, `DietiDeals24`, e confermiamo.

##### Recuperare un Dev Container già configurato

Possiamo riaprire facilmente un devcontainer già configurato andando in `Remote Explorer` e scegliendo il devcontainer da aprire.

#### Command Line Interface

E' possibile interagire con lo stesso Development Environment anche da CLI. Per fare ciò:

1. Dalla main directory del repository andiamo nella directory `source-code/docker-compose`
2. Eseguiamo il comando:

    ```bash
    docker compose -f docker-compose.yaml -f docker-compose.dev.yaml up -d 
    ```

E' possibile fermare/rimuovere il docker compose del Development Environment con:

```bash
docker compose -f docker-compose.yaml -f docker-compose.dev.yaml down
```

### Avviare il backend

#### Building con Maven

Per compilare la REST API, è necessario fare il building dell'applicazione tramite il Maven Wrapper. Per questo è sufficiente recarsi alla source folder del backend (`source-code/Server/backend`) e usare il comando:

```bash
./mvnw clean verify
```

**NOTA:** Se si vuole fare la build del progetto eseguendo anche la scansione SonarQube, riferirsi al capitolo [building con SonarQube Maven Scan](#sonarqube-maven-scan-backend).

#### Eseguire il file .jar

Una volta fatto il building dell'applicazione, è sufficiente eseguire il `file.jar` con appena creato. E' possibile fare ciò con il comando:

```bash
java -jar target/backend-<Versione Jar>.jar
```

### Accedere ai containers

E' possibile configurare un file `.env` per inserire dati sensibili. Per fare ciò, andare in `source-code/docker-compose` copiare il file `.sample-env`, incollarlo nella stessa directory, rinominarlo in `.env` e popolare i campi a proprio piacimento. Di seguito si fa riferimento alle environment variables del file `.env`, a cui è assegnato un valore di default nel caso in cui non sia stata inizializzata nel file `.env`.

#### REST API

Una volta eseguiti i containers, possiamo accedere alla REST API come `localhost`, alla porta specificata dal file `.env` (development: `DEV_BACKEND_PORT` (default: `55511`) | production: `55501`).

#### pgAdmin

Possiamo accedere al database Postgres tramite pgAdmin allo stesso modo della REST API, alla porta specificata dal file `.env` (development: `PGADMIN_PORT` (default: `55512`) | production: `Non disponibile`). pgAdmin verrà automaticamente configurato con la connessione corretta al database tramite il file `source-code/Server/database/servers.json`.

Credenziali di accesso a pgAdmin:

- Email: `PGADMIN_DEFAULT_EMAIL` (default: `admin@dd24.com`)
- Password: `PGADMIN_DEFAULT_PASSWORD` (default: `admin`)

Credenziali di accesso a Postgres:

- User: `dd24-admin`
- Password: `POSTGRES_PASSWORD` (default: `admin`)

**ATTENZIONE!** Utilizzando il Dev Container tramite IntelliJ IDEA è possibile che, per un problema con il binding dei file di configurazione, pgAdmin non si connetta automaticamente ai database Postgresql. In tal caso, dopo aver fatto l'accesso a pgAdmin, è possibile collegarsi al database di backend in questo modo:

1. `Right Click` su `Servers`
2. `Register -> Server...`

    In `General`:

    - In `Name` inseriamo il nome da dare al server a cui ci stiamo connettendo

    In `Connection`:

    - In `Hostname/address` inseriamo `backend-database`
    - In `Port` inseriamo `5432`
    - In `Maintenance database` inseriamo `backend`
    - In `Username` inseriamo `dd24-admin`
    - In `Password` inseriamo quella assegnata a `POSTGRES_PASSWORD`

3. `Save`

## SonarQube

**NOTA:** Per abilitare la scansione del progetto dall'interno del DevContainer, bisogna recarsi al file `source-code/docker-compose/docker-compose.dev.yaml` e togliere il commento alle seguenti righe di codice al di sotto dell'attributo top-level `networks:`:

```Yaml
external:
    name: sonarqube-compose_sonarqubeNetwork
```

Una volta fatto questo, è necessario re-buildare il DevContainer **SOLO DOPO AVER CREATO IL CONTAINER PER SONARQUBE**.

Il docker compose file per SonarQube e il file di configurazione `.env` si trovano nella directory `source-code/sonarqube`. Possiamo avviare il docker compose file per SonarQube in due modi:

1. Cliccando sull'apposito script
    1. In Linux, clicchiamo su `run-sonarqube.sh`
    2. In Windows, clicchiamo su `run-sonarqube.cmd`
2. Tramite terminale, recandoci nella directory `source-code/sonarqube` e inserendo il comando:

    ```bash
    docker compose -f docker-compose.sonarqube.yaml up -d
    ```

In ogni caso, stoppiamo i container con il comando:

```bash
docker compose -f docker-compose.sonarqube.yaml down
```

### Accedere al SonarQube Server

Possiamo accedere a SonarQube alla porta specificata dal file `.env` (default: `55513`).

Al primo accesso, useremo le seguenti credenziali:

- Login: `admin`
- Password: `admin`

Dopodiché ci verrà chiesto di modificare la password.

Una volta entrati nella dashboard SonarQube, generiamo uno User Token (ci servirà per automatizzare la scansione SonarQube del progetto):

1. Clicchiamo sull'icona del proprio profilo
2. Clicchiamo su `My Account`
3. Andiamo in `Security`
4. In `Generate Tokens` selezioniamo il tipo `User Token`
5. Inseriamo un `Nome` e scegliamo il periodo di `Expire`
6. Clicchiamo su `Generate` e usiamo il token come valore della environment variable `SONAR_TOKEN` del file `source-code/docker-compose/.env`
7. Re-buildiamo il DevContainer dell'ambiente di sviluppo

### SonarQube Maven Scan (backend)

**NOTA:** Prima di eseguire questi passaggi, è necessario aver settato la environment variable `SONAR_TOKEN`.

Per eseguire la build del progetto Maven `backend` eseguendo anche la scansione con SonarQube è sufficiente recarsi nella directory `source-code/Server/backend` ed eseguire questo comando:

```Bash
./mvnw clean verify sonar:sonar -Dsonar.token=${SONAR_TOKEN}
```

### SonarQube Gradle Scan (frontedandroid)

Per eseguire la scansione del progetto Gradle `frontendandroid` (dall'interno del DevContainer) è necessario recarsi nella directory `source-code/Client/frontendandroid` ed eseguire il comando:

```Bash
./gradlew sonar \
  -Dsonar.projectKey=dietideals24-frontendandroid \
  -Dsonar.projectName='dietideals24-frontendandroid' \
  -Dsonar.host.url=http://sonarqube:9000 \
  -Dsonar.token=<SonarQubeToken>
```

Nel caso in cui la scansione avvenga dall'esterno del DevContainer, è necessario usare l'url: `http://localhost:55513`.