package kodeoppgave;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;

import java.util.ArrayList;

public class Topology {

    public static StreamsBuilder run() {
        var builder = new StreamsBuilder();

        //TODO consume messages from the topic rsEvent
        KTable<String, RSEvent> events = null;

        var incidents = builder.stream(
                "rsIncident",
                Consumed.with(
                    Serdes.String(),
                    new JsonSerdes<>(RSIncident.class)
                )
            )
            .map((key, value) -> new KeyValue<>(value.getReferencedParticipantId(), value))
            .toTable(
                Materialized.with(
                    Serdes.String(),
                    new JsonSerdes<>(RSIncident.class)
                )
            );

        var participants = builder.stream(
                "rsParticipant",
                Consumed.with(
                    Serdes.String(),
                    new JsonSerdes<>(RSParticipant.class)
                )
            )
            .filter((key, value) -> value.getCountry() != null)
            .toTable(
                Materialized.with(
                    Serdes.String(),
                    new JsonSerdes<>(RSParticipant.class)
                )
            );

        var incidentParticipant = incidents.join(
                participants,
                (left, right) -> new IncidentParticipant(left, right),
                Materialized.with(
                    Serdes.String(),
                    new JsonSerdes<>(IncidentParticipant.class)
                )
            )
            .toStream()
            .groupBy(
                (key, value) -> value.getIncident().getEventId(),
                Grouped.with(
                    Serdes.String(),
                    new JsonSerdes<>(IncidentParticipant.class)
                )
            )
            .aggregate(
                () -> new IncidentParticipantListWrapper(new ArrayList<>()),
                (key, value, aggregate) -> {
                    aggregate.getList().add(value);
                    return new IncidentParticipantListWrapper(new ArrayList<>(aggregate.getList()));
                },
                Materialized.with(
                    Serdes.String(),
                    new JsonSerdes<>(IncidentParticipantListWrapper.class)
                )
            );

        incidentParticipant
            .toStream()
            .to(
                "incidentParticipantByEvent",
                Produced.with(
                    Serdes.String(),
                    new JsonSerdes<>(IncidentParticipantListWrapper.class)
                )
            );

        //TODO join events and incidentParticipant to create the output topic with name eventMetadata that produces EventMetadata objects

        return builder;
    }

}
