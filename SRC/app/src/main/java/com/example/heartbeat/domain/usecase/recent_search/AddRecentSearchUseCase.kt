package com.example.heartbeat.domain.usecase.recent_search

import com.example.heartbeat.domain.entity.recent_search.RecentSearch
import com.example.heartbeat.domain.repository.recent_search.RecentSearchRepository

class AddRecentSearchUseCase(
    private val repository: RecentSearchRepository
) {
    suspend operator fun invoke(userId: String, recent: RecentSearch) {
        return repository.addRecentSearch(userId, recent)
    }
}