package com.example.heartbeat.domain.usecase.recent_viewed

class RecentViewedUseCase (
    val addRecentViewedUseCase: AddRecentViewedUseCase,
    val getRecentViewedUseCase: GetRecentViewedUseCase,
    val clearRecentViewedUseCase: ClearRecentViewedUseCase
)