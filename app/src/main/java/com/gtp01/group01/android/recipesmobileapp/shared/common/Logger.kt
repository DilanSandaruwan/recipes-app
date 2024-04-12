package com.gtp01.group01.android.recipesmobileapp.shared.common

import android.util.Log


/**
 * Logger class for logging errors.
 */
class Logger {
    /**
     * Logs an error message with optional throwable.
     * @param tag The tag to associate with the log message.
     * @param message The error message.
     * @param throwable Optional throwable to log.
     */
    fun error(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}