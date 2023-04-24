/*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package net.ouftech.oufmime.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

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

    private fun getSharedPrefs(context: Context) = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
}