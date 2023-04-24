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
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.RawRes

fun Context.playSound(@RawRes soundResId: Int): MediaPlayer? = createMediaPlayer(soundResId).apply { this?.start() }

fun Context.createMediaPlayer(@RawRes soundResId: Int): MediaPlayer? = try {
    MediaPlayer.create(this, soundResId).apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        setOnCompletionListener { release() }
    }
} catch (_: UnsupportedOperationException) {
    null
}