package com.example.heartbeat.domain.repository.recent_viewed

import com.example.heartbeat.domain.entity.recent_viewed.RecentViewed

interface RecentViewedRepository {
    suspend fun addRecentViewed(userId: String, recentViewed: RecentViewed)
    suspend fun getRecentViewed(userId: String): List<RecentViewed>
    suspend fun clearRecentViewed(userId: String)
}