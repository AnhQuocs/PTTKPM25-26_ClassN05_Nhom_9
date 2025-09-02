package com.example.heartbeat.data.preferences.language

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.heartbeat.domain.entity.system.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class LanguagePreferenceManager(context: Context) {

    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("language_pref")
    }

    private val dataStore = context.languageDataStore

    val languageFlow: Flow<AppLanguage> = dataStore.data
        .catch { exception ->
            if(exception is IOException) emit (emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val code = preferences[LANGUAGE_KEY] ?: AppLanguage.ENGLISH.code
            AppLanguage.fromCode(code)
        }

    suspend fun saveLanguage(language: AppLanguage) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.code
        }
    }
}