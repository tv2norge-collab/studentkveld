package kodeoppgave


import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.*


fun main() {
    run()
}

fun run() =
    StreamsBuilder().apply {

        val eventByEventId = stream(
            "rsEvent",
            Consumed.with(
                Serdes.String(),
                jsonSerdes<RSEvent>()
            )
        )
            .filter { _, v -> !v.deleted }
            .toTable(
                Materialized.with(Serdes.String(), jsonSerdes<RSEvent>())
            )

        val incidentByParticipantId = stream(
            "rsIncident",
            Consumed.with(
                Serdes.String(),
                jsonSerdes<RSIncident>()
            )
        )
            .filter { _, v -> !v.deleted }
            .map { _, v ->
                KeyValue(v.referencedParticipantId, v)
            }
            .toTable(
                Materialized.with(Serdes.String(), jsonSerdes<RSIncident>())
            )

        val participantByParticipantId = stream(
            "rsParticipant",
            Consumed.with(
                Serdes.String(),
                jsonSerdes<RSParticipant>()
            )
        )
            .filter { _, v -> !v.deleted }
            .toTable(
                Materialized.with(Serdes.String(), jsonSerdes<RSParticipant>())
            )

        val incidentParticipant = incidentByParticipantId.join(
            participantByParticipantId,
            { left: RSIncident, right: RSParticipant ->
                IncidentParticipant(left, right)
            },
            Materialized.with(Serdes.String(), jsonSerdes<IncidentParticipant>())
        )


        incidentParticipant.join(
            eventByEventId,
            { left: IncidentParticipant -> left.incident.eventId },
            { left: IncidentParticipant, right: RSEvent ->
                EventIncidentParticipant(right.id, right.name, left)
            },
            Materialized.with(Serdes.String(), jsonSerdes<EventIncidentParticipant>())
        )
            .toStream()
            .groupBy(
                { _, v -> v.eventId },
                Grouped.with(Serdes.String(), jsonSerdes<EventIncidentParticipant>())
            )
            //TODO Create the topic "incidentParticipantByEvent" with IncidentParticipantByEvent messages by aggregating


        // Writes to incidentParticipant-output topic for test purposes, same pattern can be used for making the eventIncidentParticipant topic
        incidentParticipant
            .toStream()
            .to(
                "incidentParticipant",
                Produced.with(Serdes.String(), jsonSerdes<IncidentParticipant>())
            )

    }

data class EventIncidentParticipant(
    val eventId: String,
    val name: String,
    val incidentParticipant: IncidentParticipant
)