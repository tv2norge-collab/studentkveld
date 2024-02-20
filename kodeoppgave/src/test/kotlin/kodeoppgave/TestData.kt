package kodeoppgave

val event1 = """
  {
    "id": "eab5e78f-692f-4919-bf4c-60fac33f050c",
    "masterId": null,
    "name": "Nidelv IL - Nyborg Idrettslag",
    "tournamentStageId": "906af52c-a760-46f5-83e6-3b2c8e97626e",
    "venueId": "50efb124-109b-4571-822d-0aa3b4ae5247",
    "venueNeutralGround": false,
    "startDate": 1664110800000,
    "status": "FINISHED",
    "statusDescription": "UNKNOWN",
    "images": [],
    "deleted": false,
    "properties": {
      "NIF:roundNumber": "1",
      "NIF:id": "7621638",
      "NIF:roundName": "Runde 1"
    }
  },
  "fields": {
    "statusDescription": [
      "UNKNOWN"
    ],
    "deleted": [
      false
    ],
    "properties.NIF:id": [
      "7621638"
    ],
    "tournamentStageId": [
      "906af52c-a760-46f5-83e6-3b2c8e97626e"
    ],
    "venueId": [
      "50efb124-109b-4571-822d-0aa3b4ae5247"
    ],
    "name": [
      "Nidelv IL - Nyborg Idrettslag"
    ],
    "venueNeutralGround": [
      false
    ],
    "id": [
      "eab5e78f-692f-4919-bf4c-60fac33f050c"
    ],
    "properties.NIF:roundName": [
      "Runde 1"
    ],
    "properties.NIF:roundNumber": [
      "1"
    ],
    "startDate": [
      1664110800000
    ],
    "status": [
      "FINISHED"
    ]
  }
""".trimIndent()

val incident1 = """
  {
    "id": "4fc2b0af-e48a-4ec4-9f15-533f84966c62",
    "masterId": null,
    "eventId": "eab5e78f-692f-4919-bf4c-60fac33f050c",
    "participantId": "595b07b4-f1c4-4e6f-afc8-9b65aa979fa4",
    "referencedParticipantId": "ce6de8e0-666a-4df1-9283-bee3fe1064f9",
    "incidentType": "SUBSTITUTION_IN",
    "elapsedTime": 75,
    "sortOrder": 2,
    "deleted": false,
    "properties": {
      "ENETPULSE:id": "4036543",
      "ENETPULSE:out_playerFK": "30577"
    }
  },
  "fields": {
    "participantId": [
      "595b07b4-f1c4-4e6f-afc8-9b65aa979fa4"
    ],
    "properties.ENETPULSE:out_playerFK": [
      "30577"
    ],
    "eventId": [
      "e2567142-e70e-4caf-82e2-9261bf898a65"
    ],
    "deleted": [
      false
    ],
    "properties.ENETPULSE:id": [
      "4036543"
    ],
    "sortOrder": [
      2
    ],
    "incidentType": [
      "SUBSTITUTION_IN"
    ],
    "id": [
      "4fc2b0af-e48a-4ec4-9f15-533f84966c62"
    ],
    "referencedParticipantId": [
      "31f74486-50c2-4a00-8db4-cd182b241a73"
    ],
    "elapsedTime": [
      75
    ]
  }
""".trimIndent()

val incident2 = """
  {
    "id": "78a40497-201c-42a9-96fa-c7e48a342f08",
    "masterId": null,
    "eventId": "eab5e78f-692f-4919-bf4c-60fac33f050c",
    "participantId": "ae708c1e-fb33-454d-a5de-538381c24a84",
    "referencedParticipantId": "ce6de8e0-666a-4df1-9283-bee3fe1064f9",
    "incidentType": "SUBSTITUTION_IN",
    "elapsedTime": 62,
    "sortOrder": 1,
    "deleted": false,
    "properties": {
      "ENETPULSE:id": "4038620",
      "ENETPULSE:out_playerFK": "12207"
    }
  },
  "fields": {
    "participantId": [
      "ae708c1e-fb33-454d-a5de-538381c24a84"
    ],
    "properties.ENETPULSE:out_playerFK": [
      "12207"
    ],
    "eventId": [
      "76682f81-8fb2-40d3-bdf9-24c217525a0e"
    ],
    "deleted": [
      false
    ],
    "properties.ENETPULSE:id": [
      "4038620"
    ],
    "sortOrder": [
      1
    ],
    "incidentType": [
      "SUBSTITUTION_IN"
    ],
    "id": [
      "78a40497-201c-42a9-96fa-c7e48a342f08"
    ],
    "referencedParticipantId": [
      "8431280b-c6d9-4d9c-923f-d7e58b63569e"
    ],
    "elapsedTime": [
      62
    ]
  }
""".trimIndent()

val incident3 = """
  {
    "id": "d9ea885f-6784-403c-9868-bb2476bdfa3a",
    "masterId": null,
    "eventId": "eab5e78f-692f-4919-bf4c-60fac33f050c",
    "participantId": "fac35b79-1e86-444c-a3c2-aee9439bc016",
    "referencedParticipantId": "093cc969-cdd9-4f32-9721-8e871a1923ef",
    "incidentType": "REGULAR_GOAL",
    "elapsedTime": 3490,
    "sortOrder": 1,
    "deleted": false,
    "properties": {
      "ENETPULSE:EmptyNet": "yes",
      "ENETPULSE:id": "4013906"
    }
  },
  "fields": {
    "participantId": [
      "fac35b79-1e86-444c-a3c2-aee9439bc016"
    ],
    "eventId": [
      "a457b24f-9d7a-44f7-9c25-024ae9c2e931"
    ],
    "properties.ENETPULSE:EmptyNet": [
      "yes"
    ],
    "deleted": [
      false
    ],
    "properties.ENETPULSE:id": [
      "4013906"
    ],
    "sortOrder": [
      1
    ],
    "incidentType": [
      "REGULAR_GOAL"
    ],
    "id": [
      "d9ea885f-6784-403c-9868-bb2476bdfa3a"
    ],
    "referencedParticipantId": [
      "c935ed3b-fd66-4483-ac68-8828f48042e2"
    ],
    "elapsedTime": [
      3490
    ]
  }
""".trimIndent()

val participant1 = """
    {
    "id": "ce6de8e0-666a-4df1-9283-bee3fe1064f9",
    "masterId": null,
    "name": "Marcelo Messiah",
    "gender": "MALE",
    "type": "ATHLETE",
    "country": {
      "id": "3221efd9-e4b0-4439-a326-a87783e794d1",
      "name": "El Salvador",
      "images": []
    },
    "images": [],
    "toBeDecided": false,
    "deleted": false,
    "properties": {
      "ENETPULSE:status": "active",
      "ENETPULSE:date_of_birth": "1981-11-09",
      "ENETPULSE:height": "182",
      "ENETPULSE:id": "329792",
      "ENETPULSE:position": "defender"
    }
  },
  "fields": {
    "gender": [
      "MALE"
    ],
    "toBeDecided": [
      false
    ],
    "country.id": [
      "3221efd9-e4b0-4439-a326-a87783e794d1"
    ],
    "name.keyword": [
      "Marcelo Messiah"
    ],
    "properties.ENETPULSE:position": [
      "defender"
    ],
    "type": [
      "ATHLETE"
    ],
    "deleted": [
      false
    ],
    "properties.ENETPULSE:height": [
      "182"
    ],
    "properties.ENETPULSE:id": [
      "329792"
    ],
    "name": [
      "Marcelo Messiah"
    ],
    "country.name": [
      "El Salvador"
    ],
    "id": [
      "ce6de8e0-666a-4df1-9283-bee3fe1064f9"
    ],
    "properties.ENETPULSE:status": [
      "active"
    ],
    "properties.ENETPULSE:date_of_birth": [
      "1981-11-09"
    ]
  }
""".trimIndent()

val participant2 = """
  {
    "id": "093cc969-cdd9-4f32-9721-8e871a1923ef",
    "masterId": null,
    "name": "Faris Zeljkovic",
    "gender": "MALE",
    "type": "ATHLETE",
    "country": {
      "id": "d57598c4-6955-493b-a993-6d17c169dd69",
      "name": "Bosnia-Hercegovina",
      "images": []
    },
    "images": [],
    "toBeDecided": false,
    "deleted": false,
    "properties": {
      "ENETPULSE:status": "active",
      "ENETPULSE:date_of_birth": "1993-08-09",
      "ENETPULSE:id": "402020",
      "ENETPULSE:position": "defender"
    }
  },
  "fields": {
    "gender": [
      "MALE"
    ],
    "toBeDecided": [
      false
    ],
    "country.id": [
      "d57598c4-6955-493b-a993-6d17c169dd69"
    ],
    "name.keyword": [
      "Faris Zeljkovic"
    ],
    "properties.ENETPULSE:position": [
      "defender"
    ],
    "type": [
      "ATHLETE"
    ],
    "deleted": [
      false
    ],
    "properties.ENETPULSE:id": [
      "402020"
    ],
    "name": [
      "Faris Zeljkovic"
    ],
    "country.name": [
      "Bosnia-Hercegovina"
    ],
    "id": [
      "093cc969-cdd9-4f32-9721-8e871a1923ef"
    ],
    "properties.ENETPULSE:status": [
      "active"
    ],
    "properties.ENETPULSE:date_of_birth": [
      "1993-08-09"
    ]
  }
""".trimIndent()

