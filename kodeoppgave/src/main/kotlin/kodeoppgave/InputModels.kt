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
    val deleted : Boolean,
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
    val deleted : Boolean
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
    val deleted : Boolean
)

data class Country(
    val id:String?,
    val name:String?,
    val images : List<Image>
)

data class Image(
    val url: String
)