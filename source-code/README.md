# Note per lo sviluppatore

## Client

## Server

### Software necessari

- [Docker](https://www.docker.com/get-started/): utilizzato per il setup dell'ambiente di sviluppo dell'applicativo. Inoltre, Docker è utilizzato per astrarre sia la REST API che il database dalla macchina del singolo sviluppatore. Attraverso Docker, vengono installate in automatico tutte le dependency necessarie.

**ATTENZIONE!** Per utilizzare Docker su sistemi operativi Windows, è necessario installare il [WSL](https://learn.microsoft.com/it-it/windows/wsl/install).

### Configurare il Development Environment

#### IntelliJ IDEA

Per configurare il Development Environment con IntelliJ IDEA è necessario avere almeno la versione 2023.2. E' disponibile la [documentazione ufficiale](https://www.jetbrains.com/help/idea/connect-to-devcontainer.html), ma qui di seguito vengono riportati i passaggi da fare:

1. Nella `Welcome page` andare in `Remote Development -> Dev Containers` e creiamo un nuovo devcontainer cliccando su `New Dev Container`.
2. Per clonare una repository, andare nella sezione `From VSC Project`.
3. Selezionare `Docker`.
4. In `Git Repository`, incollare l'indirizzo SSH della repository GitHub.
    - **NOTA:** Se otteniamo l'errore `The authenticity of host 'github.com (140.82.121.4)' can't be established.` allora:
        - Generiamo una SSH key (se non ce l'abbiamo già) e aggiungiamo quella pubblica a GitHub.
        - Aggiungiamo `github.com` agli host conosciuti.
5. Selezionare il `Git Branch` da clonare.
6. In `Detection for devcontainer.json file:` clicchiamo su `Specify Path` e incolliamo questo path:
    ```
    CodiceSorgente/Server/server-DietiDeals24/.devcontainer/devcontainer.json
    ```
7. Clicchiamo su `Build Container and Continue`.
8. Una volta creato il devcontainer, dovremo scegliere l'IDE da utilizzare. Scegliamo `IntelliJ IDEA 2024.1.4` (la versione non EAP) e clicchiamo su `Continue`.

#### Visual Studio Code

Per configurare il Development Environment con Visual Studio Code è necessaria l'estensione [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) di Microsoft. Una volta installata l'estensione:

1. Creiamo un container Volume andando in `View -> Command Palette...` (o semplicemente digitiamo `CTRL+SHIT+P`) e usiamo il comando:

    ```
    Dev Containers: Clone Repository in Named Container Volume...
    ```

2. Selezioniamo la repository e il branch da clonare nel devcontainer
3. Scegliamo il nome da dare al nuovo volume (o utilizziamone uno esistente) e confermiamo

