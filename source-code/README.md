# Note per lo sviluppatore

## Client

## Server (Backend e Database)

### Software necessari

- [Docker](https://www.docker.com/get-started/): utilizzato per il setup dell'ambiente di sviluppo dell'applicativo. Inoltre, Docker è utilizzato per astrarre sia la REST API che il database dalla macchina del singolo sviluppatore. Attraverso Docker, vengono installate in automatico tutte le dependency necessarie.
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download) o [Visual Studio Code](https://code.visualstudio.com/Download) per utilizzare il Development Environment.

**ATTENZIONE!** Per utilizzare Docker su sistemi operativi Windows, è necessario installare il [WSL](https://learn.microsoft.com/it-it/windows/wsl/install).

### Configurare il Production Environment

Per eseguire i docker containers utilizzati in Production Environment:

1. Andiamo nella directory `source-code`
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

Affinché tutto funzioni correttamente è necessario rimuovere:

- La proprietà `volumes: - ..:/myHostDir/DietiDeals24:cached` da `source-code/docker-compose.override.yaml`
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
2. Affinché tutto funzioni correttamente è necessario aggiungere:
    - La proprietà `volumes: - ..:/myHostDir/DietiDeals24:cached` a `source-code/docker-compose.override.yaml`
    - La proprietà `"workspaceFolder": "/myHostDir/${localWorkspaceFolderBasename}"` a `.devcontainer/devcontainer.json`
3. Andiamo in `View -> Command Palette...` (o semplicemente digitiamo `CTRL+SHIT+P`) e usiamo il comando:

    ```
    Dev Containers: Open Folder in Container...
    ```
4. Apriamo la main direcotory della repository, `DietiDeals24`, e confermiamo.

##### Recuperare un Dev Container già configurato

Possiamo riaprire facilmente un devcontainer già configurato andando in `Remote Explorer` e scegliendo il devcontainer da aprire.

#### Command Line Interface

E' possibile interagire con lo stesso Development Environment anche da CLI. Per fare ciò:

1. Dalla main directory del repository andiamo nella directory `source-code`
2. Eseguiamo il comando:

    ```bash
    docker compose up -d
    ```

    in automatico, verrà esteso il file `docker-compose.yaml` con le informazioni contenute in `docker-compose.override.yaml`.

E' possibile fermare/rimuovere il docker compose del Development Environment con:

```bash
docker compose down
```

### Avviare il backend

#### Building con Maven

Per compilare la REST API, è necessario fare il building dell'applicazione tramite il Maven Wrapper. Per questo è sufficiente recarsi alla source folder del backend (`source-code/Server/backend`) e usare il comando:

```bash
./mvnw clean verify
```

**NOTA:** SonarQube è stato configurato in modo da essere parte integrante della build. Riferirsi al [building con SonarQube Maven Scan](#sonarqube-maven-scan-backend).

#### Eseguire il file .jar

Una volta fatto il building dell'applicazione, è sufficiente eseguire il `file.jar` con appena creato. E' possibile fare ciò con il comando:

```bash
java -jar target/backend-<Versione Jar>.jar
```

### Accedere ai containers

E' necessario configurare un file `.env`. Per fare ciò, copiare il file `.sample-env`, rinominarlo in `.env` e modificare i campi a proprio piacimento. Di seguito si fa riferimento alle environment variables del file `.env`.

#### REST API

Una volta eseguiti i containers, possiamo accedere alla REST API come `localhost`, alla porta specificata dal file `.env` (development: `DEV_BACKEND_PORT` | production: `55501`).

#### pgAdmin

Possiamo accedere al database Postgres tramite pgAdmin allo stesso modo della REST API, alla porta specificata dal file `.env` (development: `PGADMIN_PORT` | production: `Non disponibile`). pgAdmin verrà automaticamente configurato con la connessione corretta al database.

Credenziali di accesso a pgAdmin:

- Email: `PGADMIN_DEFAULT_EMAIL`
- Password: `PGADMIN_DEFAULT_PASSWORD`

Credenziali di accesso a Postgres:

- User: `dd24-admin`
- Password: `POSTGRES_PASSWORD`

**ATTENZIONE!** Utilizzando il Dev container tramite IntelliJ IDEA è possibile che, per un problema con il binding dei file di configurazione, pgAdmin non si connetta automaticamente a postgres. In tal caso, dopo aver fatto l'accesso a pgAdmin, è possibile collegarsi in questo modo:

1. `Right Click` su `Servers`
2. `Register -> Server...`

    In `General`:

    - In `Name` inseriamo il nome da dare al server a cui ci stiamo connettendo

    In `Connection`:

    - In `Hostname/address` inseriamo `database`
    - In `Port` inseriamo `5432`
    - In `Maintenance database` inseriamo il nome del database a cui vogliamo collegarci, cioé `dietideals24-backend`
    - In `Username` inseriamo `dd24-admin`
    - In `Password` inseriamo quella assegnata a `POSTGRES_PASSWORD`

3. `Save`

## SonarQube

### Accedere al SonarQube Server

Possiamo accedere a SonarQube alla porta specificata dal compose file (development: `55513` | production: `Non disponibile`).

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
6. Clicchiamo su `Generate` e usiamo il token come valore della environment variable `SONAR_TOKEN` del file `.env`
7. Re-buildiamo il docker compose (ad esempio, con `docker compose up`)

### SonarQube Maven Scan (backend)

**NOTA:** Prima di eseguire questi passaggi, è necessario aver settato la environment variable `SONAR_TOKEN`.

Per eseguire la build del progetto Maven `backend` eseguendo anche la scansione con SonarQube è sufficiente recarsi nella directory `source-code/Server/backend` ed eseguire questo comando:

```Bash
./mvnw clean verify -Dsonar.token=${SONAR_TOKEN}
```

### SonarQube Gradle Scan (frontedandroid)

Per eseguire la scansione del progetto Gradle `frontendandroid` è necessario recarsi nella directory `source-code/Client/frontendandroid` ed eseguire questo comando:

```Bash
./gradlew sonar \
  -Dsonar.projectKey=dietideals24-frontendandroid \
  -Dsonar.projectName='dietideals24-frontendandroid' \
  -Dsonar.host.url=http://sonarqube:9000 \
  -Dsonar.token=<SonarQubeToken>
```