package com.example.heartbeat.domain.usecase.search

import com.example.heartbeat.domain.entity.search.SearchResultItem
import com.example.heartbeat.domain.entity.search.SearchSuggestionItem
import javax.inject.Inject

class UnifiedSuggestionUseCase @Inject constructor(
    private val unifiedSearchUseCase: UnifiedSearchUseCase
) {
    suspend operator fun invoke(query: String): Result<List<SearchSuggestionItem>> = try {
        unifiedSearchUseCase(query).map { results ->
            results.map {
                when (it) {
                    is SearchResultItem.EventItem -> SearchSuggestionItem.EventSuggestion(it.event)
                }
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}