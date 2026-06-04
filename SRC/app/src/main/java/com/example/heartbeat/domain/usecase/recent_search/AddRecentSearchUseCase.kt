package com.example.heartbeat.domain.usecase.recent_search

import com.example.heartbeat.domain.entity.recent_search.RecentSearch
import com.example.heartbeat.domain.repository.recent_search.RecentSearchRepository

class AddRecentSearchUseCase(
    private val repository: RecentSearchRepository
) {
    suspend operator fun invoke(userId: String, recent: RecentSearch): Result<Unit> = try {
        repository.addRecentSearch(userId, recent)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}