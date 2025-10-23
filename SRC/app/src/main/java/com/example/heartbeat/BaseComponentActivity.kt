package com.example.heartbeat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.heartbeat.data.preferences.language.LanguagePreferenceManager
import com.example.heartbeat.domain.entity.system.AppLanguage
import com.example.heartbeat.utils.LangUtils
import com.example.heartbeat.utils.LanguageManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

open class BaseComponentActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val updatedContext = runBlocking {
            val manager = LanguagePreferenceManager(newBase)
            val lang = manager.languageFlow.firstOrNull() ?: AppLanguage.ENGLISH
            val contextWithLocale = LanguageManager.setAppLocale(newBase, lang)

            LangUtils.currentLang = lang.code

            contextWithLocale
        }
        super.attachBaseContext(updatedContext)
    }
}