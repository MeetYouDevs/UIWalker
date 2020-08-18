package com.autotools.uiwalker.utils

import com.autotools.uiwalker.model.ActivityModel
import org.junit.Test

import org.junit.Before
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class XmlModelConvertTest {

    @Before
    fun before(){
        UiUtils.getInstance()
        DocumentBuilderFactory.newInstance()
    }

    fun readXml(): Document {
        val xmlFile = File("./input/items.xml")

        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val xmlInput = InputSource(StringReader(xmlFile.readText()))
        val doc = dBuilder.parse(xmlInput)

        return doc
    }


    @Test
    fun xmlToAct() {
        var act=XmlModelConvert.xmlToAct("/sdcard/1.xml")
        assert(act.actName!!.isNotEmpty())
        assert(act.pkgName!!.isNotEmpty())

    }

    @Test
    fun actToXml() {
        var act = ActivityModel()
        act.initCurrentAct()
        XmlModelConvert.actToXml(act, "/sdcard/1.xml")
    }

    @Test
    fun xmlToFoundUi() {
    }

    @Test
    fun foundUiToXml() {
    }
}