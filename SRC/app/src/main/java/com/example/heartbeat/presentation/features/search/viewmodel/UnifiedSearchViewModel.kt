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
    internal val unifiedSearchUseCase: UnifiedSearchUseCase,
    internal val unifiedSuggestionUseCase: UnifiedSuggestionUseCase,
    internal val recentSearchUseCase: RecentSearchUseCase
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
            try {
                val normalized = normalize(newQuery)
                val result = if (newQuery.isBlank()) {
                    Result.success(emptyList<SearchSuggestionItem>())
                } else {
                    unifiedSuggestionUseCase(normalized)
                }
                suggestions = result.getOrDefault(emptyList())
            } catch (e: Throwable) {
                suggestions = emptyList()
            }
        }
    }

    fun onSearch() {
        isLoading = true
        viewModelScope.launch {
            try {
                val normalized = normalize(query)
                val result = unifiedSearchUseCase(normalized)
                searchResults = result.getOrDefault(emptyList())
            } catch (e: Throwable) {
                searchResults = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun onSuggestionClicked(suggestion: SearchSuggestionItem.EventSuggestion) {
        val event = suggestion.event
        query = event.name
        showSuggestions = false

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) return
        
        // Match the test Branch 2: user != null but uid is "empty" (interpreted as invalid)
        val uid = user.uid.takeIf { it.isNotBlank() } ?: return

        viewModelScope.launch {
            try {
                recentSearchUseCase.addRecentSearchUseCase(
                    uid,
                    RecentSearch(
                        id = event.id,
                        title = event.name,
                        subTitle = event.locationId,
                        historyAt = System.currentTimeMillis()
                    )
                )
            } catch (e: Throwable) {
                // Ignore failure in history
            }
        }
    }

    fun clearQuery() {
        query = ""
        suggestions = emptyList()
    }

    private fun normalize(text: String): String =
        text.lowercase().replace("\\s+".toRegex(), " ").trim()
}
