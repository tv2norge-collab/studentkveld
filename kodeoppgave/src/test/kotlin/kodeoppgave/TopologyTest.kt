package kodeoppgave

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

class TopologyTest : StringSpec({
    "test that a participant and incident are correctly merged" {
        TestDriver().runTest {

            val participant1: RSParticipant = deserializeFromString(participant1)
            rsParticipant.pipeInput(participant1.id, participant1)

            val incident1: RSIncident = deserializeFromString(incident1)
            rsIncident.pipeInput(incident1.id, incident1)

            incidentParticipantOutput.readKeyValuesToMap().entries.single().run {
                value.incident shouldBe incident1
                value.participant shouldBe participant1
            }
        }
    }

    "test that tombstones on participants are ignored and does not lead to exceptions" {
        TestDriver().runTest {

            rsParticipant.pipeInput("ce6de8e0-666a-4df1-9283-bee3fe1064f9", null)

            val incident1: RSIncident = deserializeFromString(incident1)
            rsIncident.pipeInput(incident1.id, incident1)


            incidentParticipantOutput.readKeyValuesToMap().entries.size shouldBe 0
        }
    }

    "test that a participant with 2 corresponding incidents leads to 2 messages" {
        TestDriver().runTest {

            val participant1: RSParticipant = deserializeFromString(participant1)
            rsParticipant.pipeInput(participant1.id, participant1)

            val incident1: RSIncident = deserializeFromString(incident1)
            rsIncident.pipeInput(incident1.id, incident1)

            val incident2: RSIncident = deserializeFromString(incident2)
            rsIncident.pipeInput(incident2.id, incident2)


            incidentParticipantOutput.readKeyValuesToMap().entries.size shouldBe 2
        }
    }

    "test that all incident-participant pairs that shares an event id are joined on that event" {
        TestDriver().runTest {

                val participant1: RSParticipant = deserializeFromString(participant1)
                rsParticipant.pipeInput(participant1.id, participant1)

                val participant2: RSParticipant = deserializeFromString(participant2)
                rsParticipant.pipeInput(participant2.id, participant2)

                val incident1: RSIncident = deserializeFromString(incident1)
                rsIncident.pipeInput(incident1.id, incident1)

                val incident2: RSIncident = deserializeFromString(incident2)
                rsIncident.pipeInput(incident2.id, incident2)

                val incident3: RSIncident = deserializeFromString(incident3)
                rsIncident.pipeInput(incident3.id, incident3)

                val event1: RSEvent = deserializeFromString(event1)
                rsEvent.pipeInput(event1.id, event1)

                incidentParticipantByEventOutput.readKeyValuesToMap().entries.single().run {
                    key shouldBe event1.id
                    value.eventId shouldBe event1.id
                    value.name shouldBe event1.name
                    value.incidents shouldContainExactlyInAnyOrder listOf(
                        IncidentParticipant(incident1, participant1),
                        IncidentParticipant(incident2, participant1),
                        IncidentParticipant(incident3, participant2)
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