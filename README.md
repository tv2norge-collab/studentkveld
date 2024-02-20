# Kodeoppgave TV2

I denne oppgaven skal du fikse og legge til funksjonalitet i en kafka streams applikasjon som er skrevet i Kotlin. 
Den enkleste måten å kjøre applikasjonen på er å bruke [Intellij Community Edition](https://www.jetbrains.com/idea/download/) 
og åpne prosjektet der siden det kommer med kotlin innebygget. Community edition er gratis. 

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
```kotlin
val someStream = stream(
    "topicName",
    Consumed.with(
        Serdes.String(),
        jsonSerdes<SomeObject>()
    )
)
```
Legg merke til at vi her bruker en Serde (Serializer/Deserializer) for å deserialisere bytesene fra kafka topic til et 
objekt vi kan jobbe med i kotlin.

### Tables
En stream er som sagt en strøm av key-value par som kan inneholde duplikate keys. Man ønsker ofte å gjøre denne strømmen 
om til et table for å kunne gjøre mer forutsigbare joins og aggregasjoner. Dette er fordi et table kun tillater unike 
keys, og oppnår dette ved å kun ta vare på siste entry av hver key. 
```kotlin
val someStream = stream(
    "topicName",
    Consumed.with(
        Serdes.String(),
        jsonSerdes<SomeObject>()
    )
)
    .toTable(
        Materialized.with(Serdes.String(), jsonSerdes<SomeObject>())
    )
```
I et table må vi huske å serialisere dataene, gjort med `Materialized.with(Serdes.String(), jsonSerdes<SomeObject>())`

### Tombstones
Kafka er kjent for å være raskt og skalerbart, men det medfører noen ulemper. En av disse er at det er vanskelig å 
slette data. For å komme seg rundt dette har kafka en konvensjon for å slette data, nemlig tombstones. En tombstone er 
en melding som ikke inneholder noen verdi. Hvis siste verdi for en key er `null`, vil man ved en toTable-operasjon kun 
ta vare på denne verdien for keyen. Hvordan man håndterer en tombstone vil variere fra applikasjon til applikasjon, men 
hvis man kun ønsker å ikke ta med denne keyen videre ned i applikasjonen kan man bruke `filter`-operasjonen på en strøm.
```kotlin
val someStream = stream(
    "topicName",
    Consumed.with(
        Serdes.String(),
        jsonSerdes<SomeObject>()
    )
)
    .filter { _, v -> v != null }
```

### Joins
En join-operasjon i kafka streams er en operasjon som tar to tables og slår de sammen til et nytt. Joinen skjer på 
meldingene sin key. Hvordan 2 meldinger skal slås sammen bestemmes av en lambda-funksjon som tar inn meldingene fra 
begge tables.
```
table1.join(
    table2, 
    { left: Object1, right: Object2 -> 
        Object3(
            name = left.name, 
            duration = right.duration
        )
    }, 
    Materialized.with(Serdes.String(), jsonSerdes<Object3>())
)
```

### Foreign-key joins
Mye det samme som en join, men table1 sin key blir ikke brukt, her må man også oppgi en lambda funksjon som bestemmer 
hvordan man skal velge foreign-key fra table1
```
table1.join(
    table2, 
    { left: Object1 -> left.someValue }
    { left: Object1, right: Object2 -> 
        Object3(
            name = left.name, 
            duration = right.duration
        )
    }, 
    Materialized.with(Serdes.String(), jsonSerdes<Object3>())
)
```

### Aggregasjoner
Streams kan grupperes sammen og aggregeres opp. Dette gjøres med `groupByKey` og `aggregate`-operasjonene. 
```kotlin
stream1
    .groupBy(
        { _, v -> v.someField },
        Grouped.with(Serdes.String(), jsonSerdes<Object1>())
    )
    .aggregate(
        { Object2("", "", emptyList()) },
        { _: String, v: Object1, agg: Object2 ->
            Object2(v.field1, v.field2, agg.field3List + v.field3)
        },
        Materialized.with(Serdes.String(), jsonSerdes<Object2>())
    )
```
I groupBy er første parameter en lambda som forteller hva som skal grupperes på. I aggregate initialiserer første 
parameter det aggregerte objektet, andre parameter er en lambda som forteller hvordan dette objektet skal oppdateres for 
hver melding i gruppen.


## Oppgaven
Applikasjonen du skal jobbe med er en kafka streams applikasjon som tar inn data fra 3 forskjellige topics som inneholder 
sportsarrangementer, hendelser fra sportsarrangmeneter, og metadata om deltakere. Disse dataene skal du slå sammen slik at 
hver melding inneholder alle hendelser fra et arrangement, og metadata om deltakerne som utløste hver hendelse. Det er 
flere tester som feiler, noe som tilsier at det er noe som ikke fungerer som det skal.


### Oppgave 1
Applikasjonen vår støter på problemer fordi topicet med deltakere inneholder tombstones som vi ikke håndterer. Klarer 
du å filtrere disse vekk?

### Oppgave 2
Den som har skrevet denne koden keyer hendelser på spiller-IDen til den som utløste hendelsen for å joine hendelser og 
spillere sammen. Dette fungerer ikke fordi samme spiller kan utløse flere hendelser, og vi får derfor flere meldinger 
for samme spiller som vi mister pga at vi kun tar vare på siste melding for hver key. Kan du fikse dette? 
(Hint: bruk foreign-key join)

### Oppgave 3
Den som har skrevet koden har begynt å joine sammen hendelser, spillere og sportsarrangement, og gruppert de etter 
hvilket sportsarrangement de tilhører. Kan du aggregere opp meldingene slik at vi får en melding per sportsarrangement, 
og skrive dette til et output topic? (Hint: incidentParticipant skrives allerede til et output topic)