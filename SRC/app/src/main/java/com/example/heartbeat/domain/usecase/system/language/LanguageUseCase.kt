package com.example.heartbeat.domain.usecase.system.language

import javax.inject.Inject

data class LanguageUseCase @Inject constructor(
    val getLanguage: GetLanguageUseCase,
    val updateLanguage: UpdateLanguageUseCase
)