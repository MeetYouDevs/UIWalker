package com.autotools.uiwalker.model

import com.autotools.uiwalker.data.PathData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.manager.ActManager
import com.autotools.uiwalker.utils.XmlModelConvert


import java.io.File
import java.io.Serializable
import java.util.ArrayList
import java.util.LinkedHashSet


/**
 * 页面信息
 */
class FoundUiModel
/**
 * 存储当前的页面信息
 */
internal constructor() : Serializable {
    // 页面的已操作控件
    var foundUiList: ArrayList<UiObjectModel>? = null
        private set
    private val datFile = File(PathData.xmlFolder + "/foundUis.xml")
    var newUiCount = 0
        private set

    /**
     * 从文件中获取所有属性
     */
    private val foundUiByFile: FoundUiModel
        get() = XmlModelConvert.xmlToFoundUi(datFile.getAbsolutePath())

    init {
        foundUiList = ArrayList()
        if (!datFile.getParentFile().exists()) {
            datFile.getParentFile().mkdirs()
        }
        saveToFile()
    }

    /**
     * 将所有属性保存到.xml文件中持久化
     */
    private fun saveToFile() {
        XmlModelConvert.foundUiToXml(this, datFile.getAbsolutePath())
    }

    fun checkNew() {
        val oldList = foundUiByFile.foundUiList
        val newList = ActManager.currentUiList
        var allList = ArrayList<UiObjectModel>()
        allList.addAll(oldList!!)
        allList.addAll(newList)
        allList = ArrayList(LinkedHashSet(allList))
        foundUiList = allList
        TestReport.i("FoundUiModel_checkNew: 当前发现UI数量" + foundUiList!!.size)
        newUiCount = foundUiList!!.size - oldList.size
        saveToFile()

    }

    companion object {
        var instance = FoundUiModel()
    }
}
