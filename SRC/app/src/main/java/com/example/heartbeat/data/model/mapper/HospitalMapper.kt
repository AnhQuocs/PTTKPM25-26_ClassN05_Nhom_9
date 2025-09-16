package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.HospitalDto
import com.example.heartbeat.domain.entity.hospital.Hospital
import com.example.heartbeat.utils.LangUtils

fun HospitalDto.toDomain(id: String): Hospital {
    return Hospital (
        hospitalId = id,
        hospitalName = LangUtils.getLocalizedText(hospitalName),
        imgUrl = imgUrl.orEmpty(),
        address = LangUtils.getLocalizedText(address),
        phone = phone.orEmpty() ,
        province = LangUtils.getLocalizedText(province),
        district = LangUtils.getLocalizedText(district)
    )
}