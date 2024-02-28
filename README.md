# Kodeoppgave TV2

I denne oppgaven skal du fikse og legge til funksjonalitet i en kafka streams applikasjon som er skrevet i Kotlin og Java. 
Du skal kun trenge å gjøre endringer Topology, som betyr at du kun trenger å skrive Java-kode. Du må forholde deg til 
kotlin-klassene i Models.kt, de kompileres til java-kode, og får automatisk get-funksjoner for hvert felt slik at du kan 
bruke disse direkte i java-koden din.
Den enkleste måten å kjøre applikasjonen på er å bruke [Intellij Community Edition](https://www.jetbrains.com/idea/download/) 
og åpne prosjektet der siden det kommer med kotlin og java innebygget. Community edition er gratis. 

Kafka er en meldingsplattform for strømming av data. Et kafka cluster er typisk delt opp i flere kafka topics, der hvert 
topic inneholder en type data. Det er veldig raskt og skalerbart, og gjør at man kan jobbe med data i sanntid etterhvert 
som det kommer inn. 

Kafka streams er et bibliotek for å jobbe med slike kafka-strømmer, og lar deg feks aggregere, filtrere og joine sammen 
data fra forskjellige topics, og sende det videre til et nytt topic. Dette topicet kan igjen kan leses av en annen 
applikasjon som krever mer bearbeidet data, eller prosesseres videre i kafka streams.

## Viktige prinsipper i kafka streams

### Streams
Et kafka topic er en kø av key-value par, i kafka streams leses et topic inn i et stream objekt. Dette er en strøm av 
key-value par, noe som vil si at duplikate keys er lov.
```java
var someStream = builder.stream(
        "streamName",
        Consumed.with(
            Serdes.String(),
            new JsonSerdes<>(SomeObject.class)
        )
    );
```
Legg merke til at vi her bruker en Serde (Serializer/Deserializer) for å deserialisere bytesene fra kafka topic til et 
objekt vi kan jobbe med i java.

### Tables
En stream er som sagt en strøm av key-value par som kan inneholde duplikate keys. Man ønsker ofte å gjøre denne strømmen 
om til et table for å kunne gjøre mer forutsigbare joins og aggregasjoner. Dette er fordi et table kun tillater unike 
keys, og oppnår dette ved å kun ta vare på siste entry av hver key. Et table og operasjonene man kan gjøre på det kan 
minne om tables i databaser
```java
var someTable = builder.stream(
        "streamName",
        Consumed.with(
            Serdes.String(),
            new JsonSerdes<>(SomeObject.class)
        )
    )
    .toTable(
        Materialized.with(
            Serdes.String(),
            new JsonSerdes<>(SomeObject.class)
        )
    );
```
I et table må vi huske å serialisere dataene igjen, gjort med `Materialized.with(Serdes.String(), jsonSerdes<SomeObject>())`

### Tombstones
Kafka er kjent for å være raskt og skalerbart, men det medfører noen ulemper. En av disse er at det er vanskelig å 
slette data. For å komme seg rundt dette har kafka en konvensjon for å slette data, nemlig tombstones. En tombstone er 
en melding som ikke inneholder noen verdi. Hvis siste verdi for en key er `null`, vil man ved en toTable-operasjon kun 
ta vare på denne verdien for keyen. Hvordan man håndterer en tombstone vil variere fra applikasjon til applikasjon, men 
hvis man kun ønsker å ikke ta med denne keyen videre ned i applikasjonen kan man bruke `filter`-operasjonen på en strøm.
```java
var someStream = builder.stream(
        "streamName",
        Consumed.with(
            Serdes.String(),
            new JsonSerdes<>(SomeObject.class)
        )
    )
    .filter((key, value) -> value != null)
```

### Joins
En join-operasjon i kafka streams er en operasjon som tar to tables og slår de sammen til et nytt. Joinen skjer på 
meldingene sin key. Hvordan 2 meldinger skal slås sammen bestemmes av en lambda-funksjon som tar inn meldingene fra 
begge tables.
```java
table1.join(
    table2,
    (left, right) -> new NewObject(left, right),
    Materialized.with(
        Serdes.String(),
        new JsonSerdes<>(NewObject.class)
    )
);
```

### Foreign-key joins
Mye det samme som en join, men table1 sin key blir ikke brukt, her må man også oppgi en lambda funksjon som bestemmer 
hvordan man skal velge foreign-key fra table1
```
table1.join(
    table2,
    (left) -> left.getSomeField(),
    (left, right) -> new NewObject(left, right),
    Materialized.with(
        Serdes.String(),
        new JsonSerdes<>(NewObject.class)
    )
);
```

### Output topics
Når man har et table man har lyst til å skrive til et nytt topic så det kan brukes av andre applikasjoner gjør man slik:
```java
someTable
    .toStream()
    .to(
        "outputTopicName",
        Produced.with(
            Serdes.String(),
            new JsonSerdes<>(OutputObject.class)
        )
    );
```
Merk at vi ikke kan produsere output topics av et table, så vi må først gjøre det til en stream.

### Aggregasjoner
Streams kan grupperes sammen og aggregeres opp. Dette gjøres med `groupByKey` og `aggregate`-operasjonene. 
```java
stream1
    .groupBy(
        (key, value) -> value.getSomeField(),
        Grouped.with(
            Serdes.String(),
            new JsonSerdes<>(SomeObject.class)
        )
    )
    .aggregate(
        () -> "",
        (key, value, aggregate) -> aggregate + value.getSomeString(),
        Materialized.with(
            Serdes.String(),
            Serdes.String()
        )
    );
```
I groupBy er første parameter en lambda som forteller hva som skal grupperes på. I aggregate initialiserer første 
parameter det aggregerte objektet, andre parameter er en lambda som forteller hvordan dette objektet skal oppdateres for 
hver melding i gruppen.


## Oppgaven
Applikasjonen du skal jobbe med er en kafka streams applikasjon som skal ta inn data fra 3 forskjellige topics som inneholder 
sportsarrangementer, hendelser fra sportsarrangmeneter, og metadata om deltakere. Disse dataene skal slås sammen slik at 
hver melding inneholder metadata om et sportsarrangement, alle hendelser fra et arrangement, og metadata om deltakerne 
som utløste hver hendelse. Det er flere tester som feiler, noe som tilsier at det er noe som ikke fungerer som det skal.

### Hvordan kjøre prosjektet
Prosjektets main funksjon er ment å blir kjørt fra et cluster som har tilgang til det relevante kafka-clusteret. Det har 
vi ikke lokalt, derfor kjører man heller testene som setter opp sitt eget kafka cluster, og populerer det med 
sportsdata-meldinger. Dette gjøres ved å høyreklikke på kodeoppgave-directoriet og velge "Run tests". Hver oppgave 
korresponderer med en test som feiler.


### Oppgave 1
Applikasjonen vår støter på problemer fordi topicet med deltakere inneholder tombstones som vi ikke håndterer. Klarer 
du å filtrere disse vekk?

### Oppgave 2
Kafka-topicet med sportsarrangement-metadata må kunsumeres slik at vi kan populere samlingene med hendelser per 
sportsarrangment med mer metadata. Konsumer topicet "rsEvent" som en stream og gjør det deretter om til et table.

### Oppgave 3
Vi har allerede et table med samlinger av hendelser per sportsarrangement, fra oppgave 2 har vi nå også et table med 
sportsarrangement. Bruk en join for å sette disse sammen og lage et output topic med navn "eventMetadata" som produserer 
meldinger på formen EventMetadata.

### Oppgave 4
Den som har skrevet denne koden keyer hendelser på spiller-IDen til den som utløste hendelsen for å joine hendelser og
spillere sammen. Dette fungerer ikke fordi samme spiller kan utløse flere hendelser, og vi får derfor flere meldinger
for samme spiller som vi mister pga at vi kun tar vare på siste melding for hver key. Kan du fikse dette?
(Hint: fjern koden som keyer hendelser på spiller-id, bruk heller foreign-key join på spiller-id feltet)
