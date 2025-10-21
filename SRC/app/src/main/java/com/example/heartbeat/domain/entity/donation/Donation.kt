package com.example.heartbeat.domain.entity.donation

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import java.time.LocalDateTime

object LocalDateTimeParceler : Parceler<LocalDateTime> {
    override fun create(parcel: Parcel): LocalDateTime {
        val str = parcel.readString()!!
        return LocalDateTime.parse(str)
    }

    override fun LocalDateTime.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.toString())
    }
}

@Parcelize
@TypeParceler<LocalDateTime, LocalDateTimeParceler>
data class Donation(
    val donationId: String,
    val donorId: String,
    val eventId: String,
    val citizenId: String,
    val status: String,
    val donationVolume: String,
    val createAt: LocalDateTime,
    val donatedAt: String
) : Parcelable