package com.example.heartbeat.domain.entity.search

import com.example.heartbeat.domain.entity.event.Event

sealed class SearchResultItem {
    data class EventItem(val event: Event) : SearchResultItem()
}

sealed class SearchSuggestionItem {
    data class EventSuggestion(val event: Event) : SearchSuggestionItem()
}