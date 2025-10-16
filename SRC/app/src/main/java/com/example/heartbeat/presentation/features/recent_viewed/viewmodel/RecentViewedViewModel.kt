package com.example.heartbeat.presentation.features.recent_viewed.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heartbeat.domain.entity.recent_viewed.RecentViewed
import com.example.heartbeat.domain.usecase.recent_viewed.RecentViewedUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewedViewModel @Inject constructor(
    private val recentViewedUseCase: RecentViewedUseCase
) : ViewModel() {
    var recentList by mutableStateOf<List<RecentViewed>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadRecent()
    }

    private fun loadRecent() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            isLoading = true
            recentList = recentViewedUseCase.getRecentViewedUseCase(userId)
            isLoading = false
        }
    }

    fun addRecentViewed(id: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            isLoading = true

            val recent = RecentViewed(
                id = id,
                viewedAt = System.currentTimeMillis()
            )

            recentViewedUseCase.addRecentViewedUseCase(userId, recent)
            loadRecent()

            isLoading = false
        }
    }

    fun clearRecentViewed() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            isLoading = true
            recentList = emptyList()
            recentViewedUseCase.clearRecentViewedUseCase(userId)
            isLoading = false
        }
    }
}