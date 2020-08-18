package com.autotools.uiwalker.manager


import com.autotools.uiwalker.data.CurrentView
import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.expection.UnexpectedUiException
import com.autotools.uiwalker.model.ActivityModel
import com.autotools.uiwalker.model.UiObjectModel
import com.autotools.uiwalker.utils.DumpUtils
import com.autotools.uiwalker.utils.UiUtils
import com.autotools.uiwalker.utils.XmlModelConvert
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import java.io.File
import java.util.ArrayList


/**
 * 用于Activity级的操作
 */

object ActManager {

    /**
     * 获取当前Activity名称
     *
     * @return Activity名称
     */
     val currentInfo: CurrentView
        get() = DumpUtils.dumpCurrentInfo()

    val currentPkg: String
        get() = currentInfo.pkg

    val currentActName: String
        get() = currentInfo.act


    /**
     * 获取当前Activity的遍历信息
     *
     * @return 当前Activity的遍历信息
     */
    val currentUiList: ArrayList<UiObjectModel>
        get() {

            val list = ArrayList<UiObjectModel>()
            try {
                val input = DumpUtils.dumpUiXml()
                val doc = Jsoup.parse(input, "UTF-8")
                val links = doc.getElementsByTag("node")
                for (link in links) {
                    if (isElementNeeded(link)) {
                        val uiObjectModel = UiObjectModel().initByXml(link)
                        list.add(uiObjectModel)
                    }
                }
            } catch (e: Exception) {
                val from = "ActManager_getCurrentUiList"
                val info = "xml解析成model出错"
                val ue = UnexpectedUiException(from, info, e, true)
                ue.report()
            }

            return list
        }

    /**
     * 获取当前Activity的遍历信息
     * 先根据包名和activity名称获取xml文件,存在则序列化为模型
     * 不存在则先初始化模型
     * @return 当前Activity的遍历信息
     */
    fun getActivityModelByName(pkg: String, actName: String): ActivityModel {

        var model = ActivityModel()
        val file = File(XmlModelConvert.getXmlFilePathByAct(pkg, actName))
        if (file.exists()) {
            model = XmlModelConvert.xmlToAct(file.absolutePath)
        } else {
            model.initCurrentAct()
        }
        return model
    }

    /**
     * 判断当前控件是否需要
     *
     * @param element
     * @return
     */
    private fun isElementNeeded(element: Element): Boolean {
        val packageEnable = element.attr("package") == TestData.testPkg
        if (!packageEnable) { //控件不属于被测包
            return false
        }
        val enabled = element.attr("enabled") == "true"
        if (!enabled) {// 控件不可用
            return false
        }
        val clickable = element.attr("clickable") == "true"
        val scrollable = element.attr("scrollable") == "true"
        if (!clickable && !scrollable) {// 不可点击，不可滑动
            return false
        }
        // 获取控件坐标
        val rString = element.attr("bounds").replace("[", ",")
                .replace("]", ",")
        val r = rString.trim { it <= ' ' }.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val left = Integer.parseInt(r[1])
        // 判断该控件是否在屏幕之外，主要是webview的情况，屏幕之外的控件坐标有的在屏幕之外，有些会都是0
        if (left < 0 || left >= UiUtils.device_weight) {
            return false
        }
        val top = Integer.parseInt(r[2])
        if (top < 0 ) {
            return false
        }
        val right = Integer.parseInt(r[4])
        if (right <= 0) {
            return false
        }
        val bottom = Integer.parseInt(r[5])
        return bottom > 0
    }

}
