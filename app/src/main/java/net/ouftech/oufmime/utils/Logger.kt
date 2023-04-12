package net.ouftech.oufmime.utils

import android.util.Log

class Logger {

    private val callerClass: String
        get() {
            val caller = Thread.currentThread().stackTrace[4]
            return "${caller.simpleName()}:${caller.methodName}"
        }

    private fun StackTraceElement.simpleName() = className.substring(className.lastIndexOf('.') + 1).trim()

    fun d(msg: String) {
        Log.d(callerClass, msg)
    }

    fun e(t: Throwable) {
        Log.e(callerClass, t.toString())
    }

}