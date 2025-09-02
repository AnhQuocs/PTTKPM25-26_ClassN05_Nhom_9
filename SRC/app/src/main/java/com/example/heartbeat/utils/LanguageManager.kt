package com.example.heartbeat.utils

import android.content.Context
import android.os.Build
import android.os.LocaleList
import com.example.heartbeat.domain.entity.system.AppLanguage
import java.util.Locale

object LanguageManager {
    fun setAppLocale(context: Context, language: AppLanguage): Context {
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val config = context.resources.configuration

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        } else {
            config.locale = locale
        }

        return context.createConfigurationContext(config)
    }
}