# Dienstwagendatenbank

## Projektbeschreibung

Dieses Java-Projekt implementiert eine einfache Datenbankverwaltung für Dienstwagen, Fahrer und deren Fahrten. Es ermöglicht das Importieren von Daten aus einer Textdatei und bietet verschiedene Suchfunktionen, die über Kommandozeilenargumente gesteuert werden. Das Projekt ist für die Lehrveranstaltung "Programmierung mit Java" an der DHBW Stuttgart konzipiert.

## Funktionen

- **Datenimport:** Liest Fahrer-, Fahrzeug- und Fahrtdaten aus der `vehicleDB.txt` Datei ein und parst diese in entsprechende Java-Objekte.
- **Fahrersuche:** Findet Fahrer anhand von Teilen ihres Vor- oder Nachnamens.
- **Fahrzeugsuche:** Sucht Fahrzeuge nach Hersteller, Modell oder Kennzeichen.
- **"Geblitzt":** Ermittelt, welcher Fahrer zu einem bestimmten Zeitpunkt ein Fahrzeug mit einem gegebenen Kennzeichen genutzt hat.
- **"Liegen gelassen":** Findet alle anderen Fahrer, die am selben Tag wie ein angegebener Fahrer dieselben Fahrzeuge genutzt haben.
- **Logging:** Ein einfacher `ApplicationLogger` gibt Status- und Fehlermeldungen auf der Konsole aus.

## Projektstruktur

```
Dienstwagendatenbank/
├───src/
│   ├───app/
│   │   ├───ApplicationLogger.java  # Logging-System
│   │   ├───Import.java             # Datenimport und Suchlogik
│   │   ├───Main.java               # Einstiegspunkt und Argumenten-Parsing
│   │   └───ProjektTester.java      # End-to-End-Tests für die Features
│   ├───Data/
│   │   ├───Driver.java             # Datenmodell für Fahrer
│   │   ├───Trip.java               # Datenmodell für Fahrten
│   │   └───Vehicle.java            # Datenmodell für Fahrzeuge
│   └───test/
│       └───ImportTest.java         # JUnit-Tests für die Import-Klasse
├───.gitignore
├───2025-Projekt.pdf                # Projektaufgabenstellung
├───class_diagram.puml              # UML-Klassendiagramm (PlantUML)
├───vehicleDB.txt                   # Rohdatendatei
└───...
```

## Installation und Setup

### Voraussetzungen
- **Java Development Kit (JDK):** Version 11 oder höher.
- **JUnit 5:** Für die Ausführung der Unit-Tests wird die `junit-platform-console-standalone` JAR-Datei benötigt.

### Kompilierung
1.  Navigieren Sie in das Hauptverzeichnis des Projekts.
2.  Erstellen Sie ein Verzeichnis für die kompilierten Klassen (z.B. `out`).
    ```bash
    mkdir out
    ```
3.  Kompilieren Sie die Java-Dateien. Passen Sie den Pfad zur JUnit-JAR-Datei an, falls diese für den Classpath benötigt wird.
    ```bash
    # Windows
    javac -d out -cp ".;path\to\junit-jupiter-api.jar" src\app\*.java src\Data\*.java src\test\*.java

    # Linux / macOS
    javac -d out -cp ".:path/to/junit-jupiter-api.jar" src/app/*.java src/Data/*.java src/test/*.java
    ```

## Nutzung

Die Anwendung wird über die Kommandozeile aus dem Hauptverzeichnis des Projekts gestartet.

### Verfügbare Befehle

Die Befehle werden als Argumente an die `Main`-Klasse übergeben.

1.  **Fahrersuche:**
    *   Format: `--fahrersuche="<Suchbegriff>"`
    *   Beispiel: `java -cp out app.Main --fahrersuche="Hoffmann"`

2.  **Fahrzeugsuche:**
    *   Format: `--fahrzeugsuche="<Suchbegriff>"`
    *   Beispiel: `java -cp out app.Main --fahrzeugsuche="S-BC-4566"`

3.  **Geblitzt (Fahrer zu Zeitpunkt finden):**
    *   Format: `--fahrerZeitpunkt="<Kennzeichen>;<DatumUhrzeit>"`
    *   Beispiel: `java -cp out app.Main --fahrerZeitpunkt="S-MN-9932;2024-02-14T13:57:43"`

4.  **Liegen gelassen (Andere Nutzer finden):**
    *   Format: `--fahrerDatum="<FahrerID>;<Datum>"`
    *   Beispiel: `java -cp out app.Main --fahrerDatum="F003;2024-08-13"`

## Tests

Das Projekt enthält zwei Arten von Tests:

1.  **End-to-End-Tests (`ProjektTester`):**
    Diese Klasse testet die Hauptfeatures durch Aufruf der `main`-Methode mit vordefinierten Argumenten und überprüft die Konsolenausgabe.
    ```bash
    java -cp out app.ProjektTester
    ```

2.  **Unit-Tests (`ImportTest`):**
    Diese Klasse verwendet JUnit 5, um die Methoden der `Import`-Klasse zu testen. Zur Ausführung wird die JUnit Platform Console Standalone JAR benötigt.
    ```bash
    # Ersetzen Sie <VERSION> durch Ihre Version der JAR
    # Windows
    java -jar junit-platform-console-standalone-<VERSION>.jar -cp "out;." --scan-classpath

    # Linux / macOS
    java -jar junit-platform-console-standalone-<VERSION>.jar -cp "out:." --scan-classpath
    ```

```
