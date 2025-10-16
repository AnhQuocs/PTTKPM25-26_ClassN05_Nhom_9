package com.example.heartbeat.domain.usecase.recent_viewed

import com.example.heartbeat.domain.repository.recent_viewed.RecentViewedRepository

class ClearRecentViewedUseCase (
    private val repository: RecentViewedRepository
) {
    suspend operator fun invoke(userId: String) {
        return repository.clearRecentViewed(userId)
    }
}