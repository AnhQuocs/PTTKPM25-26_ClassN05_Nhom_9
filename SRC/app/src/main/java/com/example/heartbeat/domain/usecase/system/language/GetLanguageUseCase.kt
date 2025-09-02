package com.example.heartbeat.domain.usecase.system.language

import com.example.heartbeat.data.preferences.language.LanguagePreferenceManager
import com.example.heartbeat.domain.entity.system.AppLanguage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val manager: LanguagePreferenceManager
) {
    operator fun invoke(): Flow<AppLanguage> = manager.languageFlow
}