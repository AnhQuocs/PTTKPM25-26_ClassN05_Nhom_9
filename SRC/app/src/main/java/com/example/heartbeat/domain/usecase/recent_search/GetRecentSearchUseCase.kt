package com.example.heartbeat.domain.usecase.recent_search

import com.example.heartbeat.domain.entity.recent_search.RecentSearch
import com.example.heartbeat.domain.repository.recent_search.RecentSearchRepository

class GetRecentSearchUseCase(
    private val repository: RecentSearchRepository
) {
    suspend operator fun invoke(userId: String): List<RecentSearch> {
        return repository.getRecentSearch(userId)
    }
}