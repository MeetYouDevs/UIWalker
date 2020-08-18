package com.autotools.uiwalker.utils

import com.autotools.uiwalker.data.PathData
import com.autotools.uiwalker.model.ActivityModel
import com.autotools.uiwalker.model.FoundUiModel
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File
import java.util.regex.Pattern


object XmlModelConvert {

    /**
     * 解析xml文件的内容，转化成ActivityModel对象
     */
    fun xmlToAct(xmlFilePath: String): ActivityModel {
        val xmlMapper = XmlMapper()
        val file = File(xmlFilePath)
        if (!file.exists())
            return ActivityModel()
        val obj = xmlMapper.readValue(file, ActivityModel::class.java)
        return obj

    }

    /**
     * 将ActivityModel对象存放到文件中
     */
    fun actToXml(act: ActivityModel, xmlFilePath: String) {
        val xmlMapper = XmlMapper()
        xmlMapper.writeValue(File(xmlFilePath), act)
    }

    /**
     * 解析xml文件的内容，转化成FoundUiModel对象
     */
    fun xmlToFoundUi(xmlFilePath: String): FoundUiModel {
        val xmlMapper = XmlMapper()
        val obj = xmlMapper.readValue(File(xmlFilePath), FoundUiModel::class.java)
        return obj
    }

    /**
     * 将FoundUiModel对象存放到文件中
     */
    fun foundUiToXml(act: FoundUiModel, xmlFilePath: String) {
        val xmlMapper = XmlMapper()
        xmlMapper.writeValue(File(xmlFilePath), act)
    }

    fun getXmlFilePathByAct(pkgName: String, actName: String): String {
        val actXmlPath = PathData.xmlFolder +
                "/" + pkgName.replace(".", "-") +
                "@" +
                actName.replace(".", "-") +
                ".xml"
        return actXmlPath
    }


}
