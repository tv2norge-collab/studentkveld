package kodeoppgave

data class IncidentParticipant(
    val incident: RSIncident,
    val participant: RSParticipant
)

data class IncidentParticipantByEvent(
    val eventId: String,
    val name: String,
    val incidents: List<IncidentParticipant>
)
