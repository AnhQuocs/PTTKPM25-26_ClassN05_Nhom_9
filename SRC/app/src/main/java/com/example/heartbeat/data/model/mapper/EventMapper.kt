package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.EventDto
import com.example.heartbeat.domain.entity.event.Event
import com.google.firebase.Timestamp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
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
        createdAt = createdAt?.toLocalDateTime()
            ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = updatedAt?.toLocalDateTime()
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
        donorCount = donorCount,
        createdAt = createdAt.toTimestamp(),
        updatedAt = updatedAt?.toTimestamp()
    )
}

fun Timestamp.toLocalDateTime(): LocalDateTime {
    return this.toDate().toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .toKotlinLocalDateTime()
}

fun LocalDateTime.toTimestamp(): Timestamp {
    val instant = java.time.LocalDateTime.of(
        year, monthNumber, dayOfMonth, hour, minute, second
    ).atZone(ZoneId.systemDefault()).toInstant()
    return Timestamp(Date.from(instant))
}