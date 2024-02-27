package kodeoppgave

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TestOutputTopic
import org.apache.kafka.streams.TopologyTestDriver
import java.util.*

data class TestContext(
    val driver: TopologyTestDriver,
    val rsIncident: TestInputTopic<String, RSIncident>,
    val rsEvent: TestInputTopic<String, RSEvent>,
    val rsParticipant: TestInputTopic<String, RSParticipant>,
    val incidentParticipantByEventOutput: TestOutputTopic<String, IncidentParticipantListWrapper>,
    val eventMetadataOutput: TestOutputTopic<String, EventMetadata>
)

class TestDriver() {

    private val driver =
        TopologyTestDriver(
            Topology.run().build(),
            Properties().apply {
                put(
                    StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde::class.java
                )
            })

    private val context = TestContext(
        driver = driver,
        rsIncident = driver.createInputTopic(
            "rsIncident",
            Serdes.String().serializer(),
            jsonSerdes<RSIncident>().serializer()
        ),
        rsEvent = driver.createInputTopic(
            "rsEvent",
            Serdes.String().serializer(),
            jsonSerdes<RSEvent>().serializer()
        ),
        rsParticipant = driver.createInputTopic(
            "rsParticipant",
            Serdes.String().serializer(),
            jsonSerdes<RSParticipant>().serializer()
        ),
        incidentParticipantByEventOutput = driver.createOutputTopic(
            "incidentParticipantByEvent",
            Serdes.String().deserializer(),
            jsonSerdes<IncidentParticipantListWrapper>().deserializer()
        ),
        eventMetadataOutput = driver.createOutputTopic(
            "eventMetadata",
            Serdes.String().deserializer(),
            jsonSerdes<EventMetadata>().deserializer()
        )
    )

    fun runTest(testRunner: TestContext.() -> Unit) {
        context.testRunner()
    }
}

