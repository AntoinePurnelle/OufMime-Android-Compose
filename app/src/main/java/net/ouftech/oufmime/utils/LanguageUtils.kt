package net.ouftech.oufmime.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

object LanguageUtils {

    private const val PREFS_KEY = "OufMime_prefs"
    private const val LANGUAGE_KEY = "language"

    fun setLanguage(language: String, context: Context) {
        getSharedPrefs(context).edit().putString(LANGUAGE_KEY, language).apply()
        updateResources(context)
    }

    fun updateResources(context: Context) {
        getSharedPrefs(context).getString(LANGUAGE_KEY, null)?.let { language ->
            context.resources.apply {
                val locale = Locale(language)
                val config = Configuration(configuration)

                context.createConfigurationContext(configuration)
                Locale.setDefault(locale)
                config.setLocale(locale)
                context.resources.updateConfiguration(config, displayMetrics)
            }
        }
    }

    private fun getSharedPrefs(context: Context) =
        context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
}