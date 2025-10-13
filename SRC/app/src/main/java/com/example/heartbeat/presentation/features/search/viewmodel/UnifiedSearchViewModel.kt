package com.example.heartbeat.presentation.features.search.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.recent_search.RecentSearch
import com.example.heartbeat.domain.entity.search.SearchResultItem
import com.example.heartbeat.domain.entity.search.SearchSuggestionItem
import com.example.heartbeat.domain.usecase.recent_search.RecentSearchUseCase
import com.example.heartbeat.domain.usecase.search.UnifiedSearchUseCase
import com.example.heartbeat.domain.usecase.search.UnifiedSuggestionUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnifiedSearchViewModel @Inject constructor(
    private val unifiedSearchUseCase: UnifiedSearchUseCase,
    private val unifiedSuggestionUseCase: UnifiedSuggestionUseCase,
    private val recentSearchUseCase: RecentSearchUseCase
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    var suggestions by mutableStateOf<List<SearchSuggestionItem>>(emptyList())
        private set

    var searchResults by mutableStateOf<List<SearchResultItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var showSuggestions by mutableStateOf(false)
        private set

    fun onQueryChanged(newQuery: String) {
        query = newQuery
        showSuggestions = newQuery.isNotBlank()

        viewModelScope.launch {
            suggestions = if (newQuery.isBlank()) {
                emptyList()
            } else {
                unifiedSuggestionUseCase(normalize(newQuery))
            }
        }
    }

    fun onSearch() {
        viewModelScope.launch {
            isLoading = true
            searchResults = unifiedSearchUseCase(normalize(query))
            isLoading = false
        }
    }

    fun onSuggestionClicked(suggestion: SearchSuggestionItem.EventSuggestion) {
        val event = suggestion.event
        query = event.name
        showSuggestions = false

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            recentSearchUseCase.addRecentSearchUseCase(
                userId,
                RecentSearch(
                    id = event.id,
                    title = event.name,
                    subTitle = event.locationId,
                    historyAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun clearQuery() {
        query = ""
        suggestions = emptyList()
    }

    private fun normalize(text: String): String =
        text.lowercase().replace("\\s+".toRegex(), " ").trim()
}