# Note per lo sviluppatore

## Indice

- [Frontend Android](#frontend-android)
   * [SonarQube Gradle Scan](#sonarqube-gradle-scan)

## Frontend Android

### SonarQube Gradle Scan

Riferirsi alle seguenti istruzioni solo dopo aver seguito quelle riportate nel README nella [directory dedicata a SonarQube](../sonarqube/).

1. Avviamo il container per SonarQube
2. Scansioniamo il progetto Gradle `frontendandroid` recandoci nella directory `source-code/Client/frontendandroid` ed eseguendo il comando:

    ```Bash
    ./gradlew sonar \
    -Dsonar.projectKey=dietideals24-frontendandroid-app \
    -Dsonar.projectName='dietideals24-frontendandroid-app' \
    -Dsonar.host.url=http://localhost:55513 \
    -Dsonar.token=<SONAR_TOKEN>
    ```
