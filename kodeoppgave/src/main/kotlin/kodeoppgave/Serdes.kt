package kodeoppgave

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer

val objectMapper = jacksonObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .setDateFormat(StdDateFormat())
    .registerModule(JavaTimeModule())!!

inline fun <reified T> jsonSerdes() = JsonSerdes(T::class.java)

open class JsonSerdes<T>(private val type: Class<T>) :
    Serde<T> {
    override fun serializer(): Serializer<T> =
        Serializer<T> { _, state ->
            state?.let { objectMapper.writeValueAsBytes(state) }
        }

    override fun deserializer(): Deserializer<T> =
        Deserializer<T> { _, bytes ->
            bytes?.let {
                objectMapper.readValue(bytes, type)
            }
        }
}
