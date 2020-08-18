package com.autotools.uiwalker.data

import android.os.Environment
import java.text.SimpleDateFormat
import java.util.*


/**
 * 路径的存放位置
 */
class PathData {
    companion object {
        var currentDate = getNow()
        val sdcard = "/sdcard"

        val root = "$sdcard/auto_tools/uiwalker" //+ currentDate
        val configPath = "$root/config.json"
        val xmlFolder = "$root/xml"
        val temp = "$root/temp"
        val log_path = root
        val dumpXmlPath = "$temp/dump.xml"
        val dumpXmlPathTemp = "$temp/dump_temp.xml"
        val covgDataPath = root + "/covg_data_" + System.currentTimeMillis() + ".xls"
        var utfApkName = "Utf7Ime.apk"
        var report = "$root/walker_result.txt"

        fun getNow(): String {
            val tms = Calendar.getInstance()
            return tms.get(Calendar.YEAR).toString() + tms.get(Calendar.MONTH).toString() + tms.get(Calendar.DAY_OF_MONTH).toString() + "_" + tms.get(Calendar.HOUR_OF_DAY).toString() + tms.get(Calendar.MINUTE).toString() + tms.get(Calendar.SECOND).toString() + tms.get(Calendar.MILLISECOND).toString()
        }
    }


}
