package com.example.heartbeat.domain.entity.recent_search

data class RecentSearch(
    val id: String,
    val title: String,
    val subTitle: String,
    val historyAt: Long
)