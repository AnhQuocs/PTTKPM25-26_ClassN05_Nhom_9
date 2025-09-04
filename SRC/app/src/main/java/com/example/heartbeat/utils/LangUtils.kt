package com.example.heartbeat.utils

object LangUtils {
    var currentLang: String = "en"

    fun getLocalizedText(map: Map<String, String>?): String {
        return map?.get(currentLang) ?: map ?.get("en") ?: ""
    }

    fun getLocalizedList(map: Map<String, List<String>>?): List<String> {
        return map?.get(currentLang) ?: emptyList()
    }
}