package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.EventDto
import com.example.heartbeat.domain.entity.event.Event
import com.google.firebase.Timestamp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import java.util.Date

fun EventDto.toDomain(id: String): Event {
    return Event(
        id = id,
        locationId = locationId.orEmpty(),
        name = name.orEmpty(),
        description = description.orEmpty(),
        date = date.orEmpty(),
        time = time.orEmpty(),
        deadline = deadline?.toDate()
            ?.toInstant()
            ?.let { Instant.fromEpochMilliseconds(it.toEpochMilli()) }
            ?.toLocalDateTime(TimeZone.currentSystemDefault())
            ?: LocalDateTime(1970, 1, 1, 0, 0, 0),
        donorList = donorList ?: emptyList(),
        capacity = capacity ?: 0,
        donorCount = donorCount ?: 0,
    )
}

fun Event.toDto(): EventDto {
    return EventDto(
        locationId = locationId,
        name = name,
        description = description,
        date = date,
        time = time,
        deadline = Timestamp(
            Date.from(
                deadline?.toInstant(TimeZone.currentSystemDefault())
                    ?.toJavaInstant()
            )
        ),
        donorList = donorList,
        capacity = capacity,
        donorCount = donorCount
    )
}