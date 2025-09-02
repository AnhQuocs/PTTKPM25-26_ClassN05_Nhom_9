package com.example.heartbeat.domain.usecase.system.language

import com.example.heartbeat.data.preferences.language.LanguagePreferenceManager
import com.example.heartbeat.domain.entity.system.AppLanguage
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val manager: LanguagePreferenceManager
) {
    suspend operator fun invoke(language: AppLanguage) = manager.saveLanguage(language)
}