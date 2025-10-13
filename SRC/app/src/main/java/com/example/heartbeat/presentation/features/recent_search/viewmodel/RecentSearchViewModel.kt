package com.example.heartbeat.presentation.features.recent_search.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartbeat.domain.entity.recent_search.RecentSearch
import com.example.heartbeat.domain.usecase.recent_search.RecentSearchUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentSearchViewModel @Inject constructor(
    private val recentSearchUseCase: RecentSearchUseCase
) : ViewModel() {

    var recentList by mutableStateOf<List<RecentSearch>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadRecentSearch()
    }

    fun loadRecentSearch() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            recentList = recentSearchUseCase.getRecentSearchUseCase(userId)
        }
    }

    fun addRecentSearch(id: String, title: String, subTitle: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            isLoading = true
            val recent = RecentSearch(
                id = id,
                title = title,
                subTitle = subTitle,
                historyAt = System.currentTimeMillis()
            )

            recentSearchUseCase.addRecentSearchUseCase(userId, recent)
            loadRecentSearch()
            isLoading = false
        }
    }

    fun clearRecentSearch() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            isLoading = true
            recentList = emptyList()
            recentSearchUseCase.clearAllRecentSearchUseCase(userId)
            loadRecentSearch()
            isLoading = false
        }
    }
}