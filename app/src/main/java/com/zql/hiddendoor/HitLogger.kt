package com.zql.hiddendoor

import android.util.Log
import com.zql.hitactions.ILog

class HitLogger : ILog {
    override fun v(tag: String, msg: String): Int {
        Log.v(tag, msg)
        return 0
    }

    override fun v(tag: String, msg: String, tr: Throwable): Int {
        Log.v(tag, msg, tr)
        return 0
    }

    override fun d(tag: String, msg: String): Int {
        Log.d(tag, msg)
        return 0
    }

    override fun d(tag: String, msg: String, tr: Throwable): Int {
        Log.d(tag, msg, tr)
        return 0

    }

    override fun i(tag: String, msg: String): Int {
        Log.i(tag, msg)
        return 0

    }

    override fun i(tag: String, msg: String, tr: Throwable): Int {
        Log.i(tag, msg, tr)
        return 0

    }

    override fun w(tag: String, msg: String): Int {
        Log.w(tag, msg)
        return 0

    }

    override fun w(tag: String, msg: String, tr: Throwable): Int {
        Log.w(tag, msg, tr)
        return 0

    }

    override fun w(tag: String, tr: Throwable): Int {
        Log.w(tag, tr.toString())
        return 0

    }

    override fun e(tag: String, msg: String): Int {
        Log.e(tag, msg)
        return 0

    }

    override fun e(tag: String, msg: String, tr: Throwable): Int {
        Log.e(tag, msg,tr)
        return 0
    }

}