# Note per lo sviluppatore

## Indice

- [Backend e Database](#backend-e-database)
   * [Software necessari](#software-necessari)
   * [Building del Production Environment](#building-del-production-environment)
      + [Command Line Interface](#command-line-interface)
   * [Building del Development Environment](#building-del-development-environment)
      + [IntelliJ IDEA](#intellij-idea)
         - [Clonando la repository del cloud nel Dev container](#clonando-la-repository-del-cloud-nel-dev-container)
         - [Aprendo la repository locale dell'host nel Dev container](#aprendo-la-repository-locale-dellhost-nel-dev-container)
      + [Visual Studio Code](#visual-studio-code)
         - [Clonando la repository del cloud nel Dev container](#clonando-la-repository-del-cloud-nel-dev-container-1)
         - [Aprendo la repository locale dell'host nel Dev container](#aprendo-la-repository-locale-dellhost-nel-dev-container-1)
         - [Recuperare un Dev Container già configurato](#recuperare-un-dev-container-già-configurato)
      + [Command Line Interface](#command-line-interface-1)
   * [Configurare e accedere ai containers](#configurare-e-accedere-ai-containers)
      + [Backend](#backend)
         - [AWS Cognito](#aws-cognito)
      + [pgAdmin](#pgadmin)
   * [Compilazione ed esecuzione del backend](#compilazione-ed-esecuzione-del-backend)
      + [Building con Maven](#building-con-maven)
      + [Eseguire il file .jar](#eseguire-il-file-jar)
   * [SonarQube Maven Scan](#sonarqube-maven-scan)

## Backend e Database

### Software necessari

- [Docker](https://www.docker.com/get-started/): utilizzato per il setup dell'ambiente di sviluppo dell'applicativo. Inoltre, Docker è utilizzato per astrarre sia la REST API che il database dalla macchina del singolo sviluppatore. Attraverso Docker, vengono installate in automatico tutte le dependency necessarie.
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download) o [Visual Studio Code](https://code.visualstudio.com/Download) per utilizzare il Development Environment.

**ATTENZIONE!** Per utilizzare Docker su sistemi operativi Windows, è necessario installare il [WSL](https://learn.microsoft.com/it-it/windows/wsl/install).

### Building del Production Environment

#### Command Line Interface

Per buildare i docker containers utilizzati in Production Environment:

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

E' possibile eseguire comodamente questi comandi tramite gli appositi [scripts](../scripts/prodcompose/) per *Windows* o *Linux*.

### Building del Development Environment

#### IntelliJ IDEA

Per buildare il Development Environment per il backend con IntelliJ IDEA è necessario avere almeno la versione 2023.2. E' disponibile la [documentazione ufficiale](https://www.jetbrains.com/help/idea/connect-to-devcontainer.html). Possiamo scegliere uno dei seguenti modi per costruire il Dev Container.

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

Per buildare il Development Environment per il backend con Visual Studio Code è necessaria l'estensione [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) di Microsoft. E' disponibile la [documentazione ufficiale](https://code.visualstudio.com/docs/devcontainers/containers). Una volta installata l'estensione possiamo scegliere uno dei seguenti modi per costruire il Dev Container.

##### Clonando la repository del cloud nel Dev container

Affinché tutto funzioni correttamente è necessario **rimuovere**:

- La proprietà `volumes: - ../..:/myHostDir/DietiDeals24:cached` da `source-code/docker-compose/docker-compose.dev.yaml`
- La proprietà `"workspaceFolder": "/myHostDir/${localWorkspaceFolderBasename}"` da `.devcontainer/devcontainer.json`

Dopodiché:

1. Creiamo il container andando in `View -> Command Palette...` (o semplicemente digitiamo `CTRL+SHIT+P`) e usiamo il comando:

    ```
    Dev Containers: Clone Repository in Named Container Volume...
    ```

2. Selezioniamo la repository e il branch da clonare nel devcontainer
3. Scegliamo il nome da dare al nuovo volume per il DevEnvironment (o utilizziamone uno esistente) e confermiamo.

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

E' possibile interagire con lo stesso Development Environment anche da CLI.

Per eseguire i docker containers utilizzati in Development Environment:

1. Andiamo nella directory `source-code/docker-compose`
2. Eseguiamo il comando:

    ```bash
    docker compose -f docker-compose.yaml -f docker-compose.dev.yaml up -d 
    ```

    il flag `-f` specifica i percorsi dei docker compose files (dove il primo è il docker compose file principale) e `-d` esegue i containers in background

E' possibile fermare/rimuovere il docker compose del Development Environment con:

```bash
docker compose -f docker-compose.yaml -f docker-compose.dev.yaml down
```

E' possibile eseguire comodamente questi comandi tramite gli appositi [scripts](../scripts/devcompose/) per *Windows* o *Linux*.

### Configurare e accedere ai containers

E' possibile configurare un file `.env` per inserire dati sensibili. Per fare ciò, andare in `source-code/docker-compose` copiare il file `.sample-env`, incollarlo nella stessa directory, rinominarlo in `.env` e popolare i campi con i dati sensibili richiesti. Di seguito si fa riferimento alle environment variables del file `.env`, a cui è assegnato un valore di default nel caso in cui non sia stata inizializzata nel file `.env`.

#### Backend

Possiamo accedere alla REST API con `localhost`, alla porta specificata dal file `.env` (development: `DEV_BACKEND_PORT` (default: `55511`) | production: `55501`).

##### AWS Cognito

E' necessario inserire anche le informazioni necessarie per permettere alla REST API di autenticare gli utenti tramite AWS Cognito. Tutte le informazioni devono essere recuperate dalla dashboard di [AWS Cognito](https://eu-north-1.console.aws.amazon.com/cognito/v2/idp/user-pools?region=eu-north-1).

Nel file `.env` dare un valore a:

- `AUTH_COGNITO_URI`, ottenibile dalla sezione `Branding -> Domain`, alla voce `Cognito domain`
- `OAUTH2_JWT_JWK_SET_URI`, ottenibile dalla sezione `Overview`, alla voce `Token signing key URL`.
- `OAUTH2_JWT_ISSUER_URI` è `OAUTH2_JWT_JWK_SET_URI` ma senza la parte finale dell'URL (`/.well-known/jwks.json`)
- `OAUTH2_JWT_CLIENTID`, ottenibile dalla sezione `Applications -> App clients`, cliccando sull'app client di cui si vuole ottenere l'ID, alla voce `Client ID`
- `OAUTH2_JWT_CLIENTSECRET`, ottenibile dalla sezione `App clients`, cliccando sull'app client di cui si vuole ottenere il secret, alla voce `Client secret`

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

### Compilazione ed esecuzione del backend

#### Building con Maven

Per compilare il backend, è necessario fare il building dell'applicazione tramite il Maven Wrapper. Per questo è sufficiente recarsi alla source folder del backend (`source-code/Server/backend`) e usare il comando:

```bash
./mvnw clean verify
```

**NOTA:** Se si vuole fare la build del progetto eseguendo anche la scansione SonarQube, riferirsi al capitolo [building con SonarQube Maven Scan](#sonarqube-maven-scan).

#### Eseguire il file .jar

Una volta fatto il building dell'applicazione, è sufficiente eseguire il `file.jar` con appena creato. E' possibile fare ciò con il comando:

```bash
java -jar target/backend-<Versione Jar>.jar
```

### SonarQube Maven Scan

Riferirsi alle seguenti istruzioni solo dopo aver seguito quelle riportate nel README nella [directory dedicata a SonarQube](../sonarqube/).

1. Avviamo il container per SonarQube
2. Assicuriamoci di aver settato l'environment variable `SONAR_TOKEN` nel file `source-code/docker-compose/.env` come riportato nelle istruzioni dedicate a SonarQube
3. Per abilitare la scansione del progetto dall'interno del DevContainer, bisogna recarsi al file `source-code/docker-compose/docker-compose.dev.yaml` e togliere il commento alle seguenti righe di codice al di sotto dell'attributo top-level `networks:`:

    ```Yaml
    external:
        name: sonarqube-compose_sonarqubeNetwork
    ```

4. Re-buildiamo il DevContainer dell'ambiente di sviluppo
5. Scansioniamo il progetto Maven `backend` recandoci nella directory `source-code/Server/backend` ed eseguendo il comando:

    ```Bash
    ./mvnw clean verify sonar:sonar -Dsonar.token=${SONAR_TOKEN}
    ```
