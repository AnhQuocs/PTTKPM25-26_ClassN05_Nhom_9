package com.example.heartbeat.domain.usecase.recent_search

import com.example.heartbeat.domain.repository.recent_search.RecentSearchRepository

class ClearAllRecentSearchUseCase(
    private val repository: RecentSearchRepository
) {
    suspend operator fun invoke(userId: String) {
        return repository.clearAllRecentSearch(userId)
    }
}