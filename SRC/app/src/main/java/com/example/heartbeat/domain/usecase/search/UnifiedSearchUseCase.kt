package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.search.SearchResultItem
import javax.inject.Inject

class UnifiedSearchUseCase @Inject constructor(
    private val searchEventsUseCase: SearchEventsUseCase
) {
    suspend operator fun invoke(query: String): List<SearchResultItem> {
        val events = searchEventsUseCase(query).map { SearchResultItem.EventItem(it) }
        return events
    }
}