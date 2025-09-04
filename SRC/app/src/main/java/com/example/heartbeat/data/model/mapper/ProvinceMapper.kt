package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.ProvinceDto
import com.example.heartbeat.domain.entity.system.Province
import com.example.heartbeat.utils.LangUtils

fun ProvinceDto.toProvince(id: String): Province {
    return Province(
        id = id,
        name = LangUtils.getLocalizedText(name)
    )
}