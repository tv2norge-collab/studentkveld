package kodeoppgave

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

class TopologyTest : StringSpec({
    "test that a participant and incident are correctly merged and joined on eventId" {
        TestDriver().runTest {
            val participant1: RSParticipant = deserializeFromString(participant1)
            rsParticipant.pipeInput(participant1.id, participant1)
            val participant2: RSParticipant = deserializeFromString(participant2)
            rsParticipant.pipeInput(participant2.id, participant2)

            val incident1: RSIncident = deserializeFromString(incident1)
            rsIncident.pipeInput(incident1.id, incident1)
            val incident3: RSIncident = deserializeFromString(incident3)
            rsIncident.pipeInput(incident3.id, incident3)

            incidentParticipantByEventOutput.readKeyValuesToMap().entries.single().run {
                value shouldBe IncidentParticipantListWrapper(
                    listOf(
                        IncidentParticipant(incident1, participant1),
                        IncidentParticipant(incident3, participant2)
                    )
                )
            }
        }
    }

    "test that tombstones in rsParticipant are handled" {
        TestDriver().runTest {
            val participant1: RSParticipant = deserializeFromString(participant1)
            rsParticipant.pipeInput(participant1.id, null)

            val incident1: RSIncident = deserializeFromString(incident1)
            rsIncident.pipeInput(incident1.id, incident1)

            incidentParticipantByEventOutput.readKeyValuesToMap().entries.size shouldBe 0
        }
    }

    "test that rsEvent is consumed" {
        TestDriver().runTest {
            val event1: RSEvent = deserializeFromString(event1)
            rsEvent.pipeInput(event1.id, event1)
        }
    }

    "test that events and incidentParticipant produces EventMetadata messages" {
        TestDriver().runTest {
            val event1: RSEvent = deserializeFromString(event1)
            rsEvent.pipeInput(event1.id, event1)

            val participant1: RSParticipant = deserializeFromString(participant1)
            rsParticipant.pipeInput(participant1.id, participant1)

            val incident1: RSIncident = deserializeFromString(incident1)
            rsIncident.pipeInput(incident1.id, incident1)

            eventMetadataOutput.readKeyValuesToMap().entries.single().run {
                value.run {
                    eventId shouldBe event1.id
                    name shouldBe event1.name
                    incidents.shouldContainExactlyInAnyOrder(
                        IncidentParticipant(incident1, participant1)
                    )
                }
            }
        }
    }

    "test that 2 incidents caused by the same participant in one event leads to 2 entries in the output" {
        TestDriver().runTest {
            val incident1: RSIncident = deserializeFromString(incident1)
            rsIncident.pipeInput(incident1.id, incident1)

            val incident2: RSIncident = deserializeFromString(incident2)
            rsIncident.pipeInput(incident2.id, incident2)

            val participant1: RSParticipant = deserializeFromString(participant1)
            rsParticipant.pipeInput(participant1.id, participant1)

            incidentParticipantByEventOutput.readKeyValuesToMap().entries.single().run {
                value.list.shouldContainExactlyInAnyOrder(
                    IncidentParticipant(incident1, participant1),
                    IncidentParticipant(incident2, participant1)
                )
            }
        }
    }
})

inline fun <reified T> deserializeFromString(message: String): T =
    JsonSerdes(T::class.java)
        .deserializer()
        .deserialize(
            "topic",
            message.toByteArray()
        )