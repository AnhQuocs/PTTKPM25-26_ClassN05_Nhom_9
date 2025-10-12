package com.example.heartbeat.domain.usecase.recent_search

class RecentSearchUseCase (
    val addRecentSearchUseCase: AddRecentSearchUseCase,
    val getRecentSearchUseCase: GetRecentSearchUseCase,
    val clearAllRecentSearchUseCase: ClearAllRecentSearchUseCase
)