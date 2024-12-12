# SonarQube

## Indice

- [Configurare SonarQube](#configurare-sonarqube)
   * [Configurare pgAdmin4 per SonarQube](#configurare-pgadmin4-per-sonarqube)
- [Accedere al SonarQube Server](#accedere-al-sonarqube-server)
- [Generare uno User Token](#generare-uno-user-token)

## Configurare SonarQube

Possiamo avviare il docker compose file per SonarQube in due modi:

1. Cliccando sull'apposito script
    1. In *Linux*, clicchiamo su `run-sonarqube.sh`
    1. In *Windows*, clicchiamo su `run-sonarqube.cmd`
1. Tramite terminale, recandoci nella directory `source-code/sonarqube` ed eseguendo il comando:

    ```bash
    docker compose -f docker-compose.sonarqube.yaml up -d
    ```

In ogni caso, stoppiamo ed eliminiamo i container con il comando:

```bash
docker compose -f docker-compose.sonarqube.yaml down
```

### Configurare pgAdmin4 per SonarQube

Se si desidera configurare pgAdmin4 in modo da accedere al database dedicato a SonarQube, ecco i passaggi da seguire:

1. Andare nel file `docker-compose.sonarqube.yaml` e uncommentare tutte le linee di codice dedicate al container pgAdmin4. Di default, si accede a pgAdmin4 alla porta `55555`, con email `admin@dd24.com` e password `admin`.
2. Se si è configurato il file `.env`, bisogna settare il file `servers.json` in questo modo:
    1. Come valore di `"MaintenanceDB":` scrivere il valore della environment variable `SONAR_DB`.
    2. Come valore di `"Username":` scrivere il valore della environment variable `SONAR_JDBC_USERNAME`.
    3. All'accesso al database in pgAdmin4, come valore della password scrivere il valore della environment variable `SONAR_JDBC_PASSWORD`.

Se, per qualche motivo, il server non dovesse autoconfigurarsi, bisogna seguire i seguenti passaggi. Una volta connessi a pgAdmin4:

1. `Right Click` su `Servers`
2. `Register -> Server...`

    In `General`:

    - In `Name` inseriamo il nome da dare al server a cui ci stiamo connettendo

    In `Connection`:

    - In `Hostname/address` inseriamo `sonarqube-database`
    - In `Port` inseriamo `5432`
    - In `Maintenance database` inseriamo quella assegnata a `SONAR_DB`
    - In `Username` inseriamo quella assegnata a `SONAR_JDBC_USERNAME`
    - In `Password` inseriamo quella assegnata a `SONAR_JDBC_PASSWORD`

3. `Save`

## Accedere al SonarQube Server

Possiamo accedere a SonarQube alla porta specificata dal file `.env` (default: `55513`).

Al primo accesso, useremo le seguenti credenziali:

- Login: `admin`
- Password: `admin`

Dopodiché ci verrà chiesto di modificare la password.

## Generare uno User Token

Una volta entrati nella dashboard SonarQube, generiamo uno User Token (ci servirà per automatizzare la scansione SonarQube del progetto):

1. Clicchiamo sull'icona del proprio profilo
2. Clicchiamo su `My Account`
3. Andiamo in `Security`
4. In `Generate Tokens` selezioniamo il tipo `User Token`
5. Inseriamo un `Nome` e scegliamo il periodo di `Expire`
6. Clicchiamo su `Generate` e usiamo il token come valore della environment variable `SONAR_TOKEN` del file `source-code/docker-compose/.env`
