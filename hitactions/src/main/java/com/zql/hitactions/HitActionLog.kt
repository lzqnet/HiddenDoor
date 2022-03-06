package com.zql.hitactions

import android.util.Log
import java.util.logging.Logger


object HitActionLog : ILog {
    private lateinit var logger: ILog
    fun setLogger(logger: ILog) {
        this.logger = logger
    }

    override fun v(tag: String, msg: String): Int {
        if (!this::logger.isInitialized) {
            return Log.v(tag, msg)
        } else {
            return logger.v(tag, msg)
        }
    }

    override fun v(tag: String, msg: String, tr: Throwable): Int {
        if (!this::logger.isInitialized) {
            return Log.v(tag, msg, tr)
        } else {
            return logger.v(tag, msg, tr)
        }
    }

    override fun d(tag: String, msg: String): Int {
        if (!this::logger.isInitialized) {
            return Log.d(tag, msg)
        } else {
            return logger.d(tag, msg)
        }
    }

    override fun d(tag: String, msg: String, tr: Throwable): Int {
        if (!this::logger.isInitialized) {
            return Log.d(tag, msg, tr)
        } else {
            return logger.d(tag, msg, tr)
        }
    }

    override fun i(tag: String, msg: String): Int {
        if (!this::logger.isInitialized) {
            return Log.i(tag, msg)
        } else {
            return logger.i(tag, msg)
        }
    }

    override fun i(tag: String, msg: String, tr: Throwable): Int {
        if (!this::logger.isInitialized) {
            return Log.i(tag, msg, tr)
        } else {
            return logger.i(tag, msg, tr)
        }
    }

    override fun w(tag: String, msg: String): Int {
        if (!this::logger.isInitialized) {
            return Log.w(tag, msg)
        } else {
            return logger.w(tag, msg)
        }
    }

    override fun w(tag: String, msg: String, tr: Throwable): Int {
        if (!this::logger.isInitialized) {
            return Log.w(tag, msg, tr)
        } else {
            return logger.w(tag, msg, tr)
        }
    }

    override fun w(tag: String, tr: Throwable): Int {
        if (!this::logger.isInitialized) {
            return Log.w(tag, tr)
        } else {
            return logger.w(tag, tr)
        }
    }

    override fun e(tag: String, msg: String): Int {
        if (!this::logger.isInitialized) {
            return Log.e(tag, msg)
        } else {
            return logger.e(tag, msg)
        }
    }

    override fun e(tag: String, msg: String, tr: Throwable): Int {
        if (!this::logger.isInitialized) {
            return Log.e(tag, msg, tr)
        } else {
            return logger.e(tag, msg, tr)
        }
    }


}


interface ILog {

    /**
     * Send a [.VERBOSE] log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun v(tag: String, msg: String): Int

    /**
     * Send a [.VERBOSE] log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun v(tag: String, msg: String, tr: Throwable): Int

    /**
     * Send a [.DEBUG] log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun d(tag: String, msg: String): Int

    /**
     * Send a [.DEBUG] log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun d(tag: String, msg: String, tr: Throwable): Int

    /**
     * Send an [.INFO] log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun i(tag: String, msg: String): Int

    /**
     * Send a [.INFO] log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun i(tag: String, msg: String, tr: Throwable): Int

    /**
     * Send a [.WARN] log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun w(tag: String, msg: String): Int

    /**
     * Send a [.WARN] log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun w(tag: String, msg: String, tr: Throwable): Int

    /*
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    fun w(tag: String, tr: Throwable): Int

    /**
     * Send an [.ERROR] log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun e(tag: String, msg: String): Int

    /**
     * Send a [.ERROR] log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun e(tag: String, msg: String, tr: Throwable): Int
}