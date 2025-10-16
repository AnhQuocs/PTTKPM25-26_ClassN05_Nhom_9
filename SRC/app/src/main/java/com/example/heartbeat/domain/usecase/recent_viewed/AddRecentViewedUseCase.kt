package com.example.heartbeat.domain.usecase.recent_viewed

import com.example.heartbeat.domain.entity.recent_viewed.RecentViewed
import com.example.heartbeat.domain.repository.recent_viewed.RecentViewedRepository

class AddRecentViewedUseCase (
    private val repository: RecentViewedRepository
) {
    suspend operator fun invoke(userId: String, recentViewed: RecentViewed) {
        return repository.addRecentViewed(userId, recentViewed)
    }
}