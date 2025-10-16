package com.example.heartbeat.data.model.mapper

import com.example.heartbeat.data.model.dto.RecentViewedDto
import com.example.heartbeat.domain.entity.recent_viewed.RecentViewed

fun RecentViewed.toDto() = RecentViewedDto (
    id = id,
    viewedAt = viewedAt
)

fun RecentViewedDto.toDomain() = RecentViewed (
    id = id,
    viewedAt = viewedAt
)