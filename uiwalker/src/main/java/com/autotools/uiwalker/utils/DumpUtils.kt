package com.autotools.uiwalker.utils

import android.os.Build
import com.autotools.uiwalker.data.CurrentView
import com.autotools.uiwalker.data.PathData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.expection.UnexpectedUiException
import com.autotools.uiwalker.utils.UiUtils.Companion.uiDevice


import java.io.File
import java.util.regex.Pattern


/**
 * Created by CrystalChen on 2016/12/30.
 */

object DumpUtils {

    /**
     * dump当前的界面信息
     */
    fun dumpUiXml(): File {
        uiDevice.setCompressedLayoutHeirarchy(true)
        val file = File(PathData.dumpXmlPath)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        try {
            uiDevice.dumpWindowHierarchy(file)
        } catch (e: Exception) {
            val uiException = UnexpectedUiException("DumpUtils_dumpUiXml: ", "DUMP出错", e, true)
            uiException.report()
            uiException.adjust()
        }

        return file
    }

    /**
     * dump当前的界面信息
     */
    fun dumpUiXml(xmlFile: File, isCompressed: Boolean): File {
        uiDevice.setCompressedLayoutHeirarchy(isCompressed)
        if (!xmlFile.parentFile.exists()) {
            xmlFile.parentFile.mkdirs()
        }
        try {
            uiDevice.dumpWindowHierarchy(xmlFile)
        } catch (e: Exception) {
            val uiException = UnexpectedUiException("DumpUtils_dumpUiXml: ", "DUMP出错", e, true)
            uiException.report()
            uiException.adjust()
        }

        return xmlFile
    }

    /**
     * 获取当前进程名及activity名，pkg+act,可以唯一标识一个页面
     */
    fun dumpCurrentInfo(): CurrentView {
        return getCurrentActByActivityRecord()
    }


    /**
     * 获取当前包名
     */
    fun dumpCurrentPkg(): String {
        var pkg = "unknown_pkg"
        try {
            pkg =  uiDevice.currentPackageName
        } catch (e : Exception) {
            TestReport.e("DumpUtils.dumpCurrentPkg: ${e.message}")
            return  pkg
        }
        if (pkg == null) {
            return "unknown_pkg"
        }
        return pkg

    }

    fun getCurrentActByActivityRecord(): CurrentView {
        var act = "unknown_act"
        var pkg = "unknown_pkg"
        var output = ""
        var targetLine = ""
        try {
            //使用adb shell dumpsys window windows获取系统信息
            var cmd = "dumpsys window windows"
            //若当前手机api版本大于29则使用如下命令
            if (Build.VERSION.SDK_INT >=29) {
                cmd = "dumpsys window"
            }
            output = CMDUtils.runCmdByDevice(cmd, false)
            //使用正则匹配出当前activity界面信息
            val pattern = " mFocusedApp=AppWindowToken.*"
            // 创建 Pattern 对象
            val r = Pattern.compile(pattern)
            // 现在创建 matcher 对象
            val m = r.matcher(output)
            //返回最后一个结果对象
            while (m.find()) {
                targetLine = m.group(0)
                //兼容不同Android系统（6以及8等）
                targetLine = if (targetLine.indexOf(",")>-1) {
                    var s1 = targetLine.split(",")[0]
                    var s2 =  s1.split(" ")
                    s2[5]
                } else{
                    var s  = targetLine.split(" ")
                    s[5]
                }
                //文本替换/.为/
                val temp = targetLine.split("/")
                pkg = temp[0]
                act = if (targetLine.contains("/.")) {
                    targetLine.replace("/.", ".")
                } else {
                    targetLine.split("/")[1]
                }

            }
        }  catch (e: Exception) {
            TestReport.e("DumpUtils.getCurrentActByActivityRecord: ${e.message}")
            TestReport.e("DumpUtils.getCurrentActByActivityRecord: $targetLine")
            e.printStackTrace()
        }

        return CurrentView(pkg, act)
    }
}
