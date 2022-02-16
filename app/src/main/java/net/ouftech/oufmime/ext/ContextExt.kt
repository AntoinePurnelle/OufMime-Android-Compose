package net.ouftech.oufmime.ext

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.RawRes

fun Context.playSound(@RawRes soundResId: Int): MediaPlayer? =
    MediaPlayer.create(this, soundResId).apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        setOnCompletionListener {
            release()
        }

        start()
    }