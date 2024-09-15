# SonarQube

## Configurare pgAdmin4 per SonarQube

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