package com.autotools.uiwalker.data

import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object TestReport {
    private val OPEN = 0

    private const val INFO = 2
    private const val ERROR = 6
    private const val FAIL = 4
    private const val SUCCESS = 5
    private const val SELF_DEFINE = 1

    private const val CRASH = 1000

    private val logfile_switch = OPEN

    val log_path = PathData.root
    private const val log_file_head = "ui_walker"
    private const val defalt_tag = "ui_walker"


    fun i(tag: String, msg: String): Int {
        return println(INFO, tag, msg)
    }

    fun i(msg: String): Int {
        return println(INFO, defalt_tag, msg)
    }

    fun e(tag: String, msg: String): Int {
        return println(ERROR, tag, msg)
    }

    fun e(msg: String): Int {
        return println(ERROR, defalt_tag, msg)
    }

    fun f(tag: String, msg: String): Int {
        return println(FAIL, tag, msg)
    }

    fun s(tag: String, msg: String): Int {
        return println(SUCCESS, tag, msg)
    }


    private fun checkLogPath(newPath: String?): Boolean {
        if (newPath == null) {
            return true
        }
        val ret: Boolean
        ret = try {
            val logPath = File(newPath)
            if (!logPath.exists()) {
                logPath.mkdirs()
            }
            logPath.isDirectory
        } catch (tr: Throwable) {
            false
        }

        return !ret
    }

    private fun printToFile(priority: Int, tag: String, type: String, buffer: ByteArray, offset: Int, count: Int): Int {
        var mtype = type
        var ret = 0
        var logpath = log_path
        if (priority == CRASH) {
            mtype = "crash"
        }
        if (priority < logfile_switch || checkLogPath(logpath)) {
            if (priority == CRASH) {
                logpath = Environment.getExternalStorageDirectory().path
                mtype = "crash"
                if (checkLogPath(logpath)) {
                    return ret
                }
            } else {
                return ret
            }
        }
        Thread.currentThread()
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val second = cal.get(Calendar.SECOND)
        val millisecond = cal.get(Calendar.MILLISECOND)

        val timeString = String.format("%d-%02d-%02d %02d:%02d:%02d.%d", year, month, day, hour, minute, second, millisecond)
        val headString = String.format("\r\n%s\t(%d)\ttag:%s\tdata:", timeString, priority, tag)
        val headBuffer = headString.toByteArray()

        var fileName: String = when (priority) {
            INFO -> "%s/" + log_file_head + "_Info%d%02d%02d.%s"
            ERROR -> "%s/" + log_file_head + "_Error%d%02d%02d.%s"
            FAIL -> "%s/" + log_file_head + "_Fail%d%02d%02d.%s"
            SUCCESS -> "%s/" + log_file_head + "_Success%d%02d%02d.%s"
            SELF_DEFINE -> "%s/" + tag + "_%d%02d%02d.%s"
            else -> "%s/%d%02d%02d.%s"
        }
        fileName = String.format(fileName, logpath, year, month, day, mtype)
        var fo: FileOutputStream? = null
        try {
            val file = File(fileName)
            fo = FileOutputStream(file, true)
            fo.write(headBuffer)
            fo.write(buffer, offset, count)
            ret = headBuffer.size + count
        } catch (tr: Throwable) {
            ret = 0
        } finally {
            if (fo != null)
                try {
                    fo.close()
                } catch (ignored: IOException) {

                }

        }
        return ret
    }

    private fun printMsgToFile(priority: Int, tag: String, msg: String?): Int {
        var nmsg = msg
        if (nmsg == null) {
            nmsg = "[null]"
        }
        val buffer = nmsg.toByteArray()
        return printToFile(priority, tag, "txt", buffer, 0, buffer.size)
    }

    private fun println(priority: Int, tag: String, msg: String): Int {
        var ret = 0
        if (priority >= OPEN) {
            ret += android.util.Log.println(priority, tag, msg)
        }
        if (priority >= logfile_switch) {
            ret += printMsgToFile(priority, tag, msg)
        }
        return ret
    }

    @Suppress("unused")
    fun selfDefine(tag: String, msg: String): Int {
        return println(SELF_DEFINE, tag, msg)
    }
}
