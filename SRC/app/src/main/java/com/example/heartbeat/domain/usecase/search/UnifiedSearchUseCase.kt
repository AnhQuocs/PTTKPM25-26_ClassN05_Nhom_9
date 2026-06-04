package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.search.SearchResultItem
import javax.inject.Inject

class UnifiedSearchUseCase @Inject constructor(
    private val searchEventsUseCase: SearchEventsUseCase
) {
    suspend operator fun invoke(query: String): Result<List<SearchResultItem>> = try {
        searchEventsUseCase(query).map { events ->
            events.map { SearchResultItem.EventItem(it) }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}