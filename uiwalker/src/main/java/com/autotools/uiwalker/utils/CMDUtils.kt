package com.autotools.uiwalker.utils

import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.expection.UnexpectedUiException
import com.autotools.uiwalker.utils.UiUtils.Companion.uiDevice
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object CMDUtils {
    class CMD_Result(var resultCode: Int?, var error: String?, var success: String?)

    /**
     * 调用CMD命令
     *
     * @param command     命令
     * @param showCommand 是否记录到log中
     * @return
     */
    fun runCmdByDevice(command: String, showCommand: Boolean): String {
        var info = "unknown"
        if (showCommand) {
            TestReport.i("CMDUtils_runCmdByDevice: $command")
        }
        try {
            info = uiDevice.executeShellCommand(command)
        } catch (e: IOException) {
            val from = "CMDUtils_runCmdByDevice"
            val d = "执行CMD命令出错"
            val ue = UnexpectedUiException(from, d, e, false)
            ue.report()
        }

        return info
    }

    fun runCmdByRuntime(command: String, showCommand: Boolean): CMD_Result? {
        TestReport.i("CMDUtils_runCMD: $command")
//        val cmd = command
        val cmd = arrayOf("/system/bin/sh", "-c", command)
        if (showCommand)
            TestReport.i("runCMD:$command")
        var cmdResult: CMD_Result? = null
        val result: Int
        try {
            val process = Runtime.getRuntime().exec(cmd)
            result = process.waitFor()
            val successMsg = BufferedReader(InputStreamReader(process.inputStream)).readLine()
            val errorMsg = BufferedReader(InputStreamReader(process.errorStream)).readLine()
            cmdResult = CMD_Result(result, errorMsg, successMsg)
        } catch (e: Exception) {
            TestReport.e("run CMD:$command failed")
            e.printStackTrace()
        }

        return cmdResult
    }
}