package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.RecentSearchDto
import com.example.heartbeat.domain.entity.recent_search.RecentSearch

fun RecentSearch.toDto() = RecentSearchDto (
    id = id,
    title = title,
    subTitle = subTitle,
    historyAt = historyAt
)

fun RecentSearchDto.toDomain() = RecentSearch (
    id = id,
    title = title,
    subTitle = subTitle,
    historyAt = historyAt
)