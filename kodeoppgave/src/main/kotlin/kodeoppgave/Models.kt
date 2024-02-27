package kodeoppgave


data class RSParticipant(
    val id: String,
    val masterId: String?,
    val name: String,
    val gender: String,
    val type: String,
    val country: Country?,
    val images : List<Image>,
    val toBeDecided : Boolean,
)

data class RSEvent(
    val id: String,
    val masterId: String?,
    val name: String,
    val tournamentStageId: String,
    val venueId: String,
    val venueNeutralGround : Boolean,
    val startDate : Long,
    val status : String,
    val statusDescription : String?,
    val images : List<Any>,
)

data class RSIncident(
    val id: String,
    val masterId: String?,
    val eventId: String,
    val participantId: String, // Team ID
    val referencedParticipantId : String?, // Player ID
    val incidentType : String,
    val elapsedTime : Int,
    val sortOrder : Int,
)

data class Country(
    val id:String?,
    val name:String?,
    val images : List<Image>
)

data class Image(
    val url: String
)

data class IncidentParticipantListWrapper(val list: List<IncidentParticipant>)


data class IncidentParticipant(
    val incident: RSIncident,
    val participant: RSParticipant
)

data class EventMetadata(
    val eventId: String,
    val name: String,
    val incidents: List<IncidentParticipant>
)