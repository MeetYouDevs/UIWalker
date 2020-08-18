package com.autotools.uiwalker.model


import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.manager.ActManager
import com.autotools.uiwalker.utils.XmlModelConvert
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

import java.io.File
import java.io.Serializable
import java.util.ArrayList


/**
 * 页面信息
 */
@JacksonXmlRootElement(localName = "ActivityModel")
class ActivityModel : Serializable {
    // 页面的已操作控件
    @JacksonXmlProperty
    private var operatedUiList = ArrayList<UiObjectModel>()

    // 页面对应的名称
    @JacksonXmlProperty
    lateinit var actName: String

    // 页面对应的包名
    @JacksonXmlProperty
    lateinit var pkgName: String

    // 用于保存当前页面的数据
    @JacksonXmlProperty
    lateinit var datFile: File


    //已经被遍历完毕的次数(不够准确，判定条件为可点击控件为null的时候认为被页面遍历的一次)
    @JacksonXmlProperty
    var overTraveledCount = 0

    //是否过度遍历
    @JacksonXmlProperty
    var overTraveled = false

    //历史遍历过的最大控件数量
    @JacksonXmlProperty
    var maxFoundedUiCount = 0

    /**
     * 存储当前的页面信息
     */
    fun initCurrentAct() {
        operatedUiList = ArrayList()
        pkgName = ActManager.currentPkg
        actName = ActManager.currentActName
        val actXmlPath = XmlModelConvert.getXmlFilePathByAct(pkgName, actName)
        datFile = File(actXmlPath)
        if (!datFile.parentFile.exists()) {
            datFile.parentFile.mkdirs()
        }
        saveActDat()
    }

    /**
     * 将所有属性保存到.xml文件中持久化
     */
    private fun saveActDat() {

        XmlModelConvert.actToXml(this, datFile.absolutePath)
    }

    fun getOperatedUiList(): ArrayList<UiObjectModel>? {
        return operatedUiList
    }


    fun addOperatedUiObject(operatedUi: UiObjectModel) {
        if (operatedUiList == null) {
            operatedUiList = ArrayList<UiObjectModel>()
        }
        operatedUiList.add(operatedUi)
        val newCount = operatedUiList.size
        if (newCount > maxFoundedUiCount) {
            maxFoundedUiCount = newCount
        }

        saveActDat()
    }

    /**
     * 完成一次遍历
     */
    fun travelFinished() {
        overTraveledCount++
        if (overTraveledCount >= TestData.maxCycle) {
            if (!TestData!!.whiteActivitys.contains(actName)) {
                overTraveled = true
                saveActDat()
                //先保存过度遍历的所有控件，再clear
                operatedUiList.clear()
            } else {
                TestReport.i("ActivityModel_travelFinished:${actName}当前Activity白名单,不计入过度遍历")
                operatedUiList.clear()
                saveActDat()
            }
        }
        else{
            operatedUiList.clear()
            saveActDat()
        }

        TestReport.i("ActivityModel_travelFinished:当前Activity控件已遍历完成,清除列表")
    }


}
