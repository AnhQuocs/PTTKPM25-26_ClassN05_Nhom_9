package com.example.heartbeat.domain.repository.recent_search

import com.example.heartbeat.domain.entity.recent_search.RecentSearch

interface RecentSearchRepository {
    suspend fun addRecentSearch(userId: String, recent: RecentSearch)
    suspend fun getRecentSearch(userId: String): List<RecentSearch>
    suspend fun clearAllRecentSearch(userId: String)
}