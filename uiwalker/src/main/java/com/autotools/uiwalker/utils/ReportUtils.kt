package com.autotools.uiwalker.utils

import com.autotools.uiwalker.data.PathData
import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.data.TestReport
import java.io.File
import org.json.JSONObject


object ReportUtils {
    fun getReportList() {
        TestReport.i("ReportUtils.getReportList: 保存遍历结果至${PathData.report}")
        val fileList = File(PathData.xmlFolder).listFiles() ?: return
        var result = ""
        var testMsg = "------------------测试参数------------------\n"
        var testApp = "被测应用：${TestData.appName}\n"
        var actCycle = "Act最大遍历次数：${TestData.maxCycle}\n"
        var runTime = "测试时长：${TestData.runTime_min} \n"
        var seed = "遍历顺序seed值：${TestData.seed}\n"
        var systemEventRate = "系统事件比：${TestData.systemEventRate}\n"
        var whiteActivity = "Activity白名单：${TestData.activityReg}\n"
        result = result + testMsg + testApp + actCycle + runTime + seed + systemEventRate + whiteActivity
        var actMsg = "------------------覆盖activity列表------------------\n"
        result += actMsg
        for (file in fileList) {
            var act = XmlModelConvert.xmlToAct(file.absolutePath)
            val obj = JSONObject()
            obj.put("ActName", act.actName)
            TestReport.i("ReportUtils.getReportList: ${obj}")
            result = result + obj.toString() + "\n"
        }
        File(PathData.report).writeText(result.toString())


    }
}