package com.autotools.uiwalker.utils


import com.autotools.uiwalker.data.PathData
import com.autotools.uiwalker.expection.UnexpectedUiException
import com.autotools.uiwalker.model.UiObjectModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


import java.io.File
import java.io.IOException

/**
 * 通过某些特征寻找控件
 */

object UiFinder {

    /**
     * 通过参数获取UiObjectModel,如果有多个对象符合条件，则返回第一个
     *
     * @param condition 存放参数名称是参数值的map
     * @return null 如果找不到
     */
    fun getByAttrs(condition: Map<String, String>): UiObjectModel? {
        val input = File(PathData.dumpXmlPathTemp)
        DumpUtils.dumpUiXml(input, true)
        var doc: Document? = null
        var uiObjectModel: UiObjectModel? = null
        try {
            doc = Jsoup.parse(input, "UTF-8")
            val links = doc!!.getElementsByTag("node")
            for (link in links) {
                var isTarget = true
                for (key in condition.keys) {
                    val value = condition[key]
                    isTarget = isTarget && link.attr(key) == value
                    if (isTarget) {
                        uiObjectModel = UiObjectModel().initByXml(link)
                        break
                    }
                }

            }
        } catch (e: IOException) {
            val from = "UiFinder_getByAttrs"
            val info = "查找指定控件失败"
            val ue = UnexpectedUiException(from, info, e, false)
            ue.report()
        }

        return uiObjectModel

    }

    /**
     * 通过参数的正则表达式获取UiObjectModel，如果有多个对象符合条件，则返回第一个
     *
     * @param condition 存放参数名称是参数值的map
     * @return null 如果找不到
     */
    fun getByAttrsRegExp(condition: Map<String, String>): UiObjectModel? {
        val input = File(PathData.dumpXmlPathTemp)
        DumpUtils.dumpUiXml(input, true)
        var doc: Document? = null
        var uiObjectModel: UiObjectModel? = null
        try {
            doc = Jsoup.parse(input, "UTF-8")
            val links = doc!!.getElementsByTag("node")
            for (link in links) {
                var isTarget = true
                for (key in condition.keys) {
                    val reg = condition[key]
                    isTarget = isTarget && link.attr(key).matches(reg!!.toRegex())
                    if (isTarget) {
                        uiObjectModel = UiObjectModel().initByXml(link)
                        break
                    }
                }

            }
        } catch (e: IOException) {
            val from = "UiFinder_getByAttrs"
            val info = "查找指定控件失败"
            val ue = UnexpectedUiException(from, info, e, false)
            ue.report()
        }

        return uiObjectModel

    }

}
