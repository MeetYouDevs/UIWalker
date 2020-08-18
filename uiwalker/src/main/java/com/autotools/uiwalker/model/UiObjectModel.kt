package com.autotools.uiwalker.model

import com.autotools.uiwalker.manager.ActManager
import com.autotools.uiwalker.utils.UiUtils
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import org.jsoup.nodes.Element
import java.io.Serializable

/**
 * 控件信息
 */
/**
 * 从dump出的xml中解析出element元素，并且存入到UiObjectModle中
 *
 * @param element xml中解析出来的元素
 */
@JacksonXmlRootElement(localName = "UiObjectModel")
class UiObjectModel : Serializable {
    var id: String = ""
    var actName: String = ""
    var className: String = ""
    var contentDescript: String = ""
    var pkgName: String = ""
    var text: String = ""
    var isClickable: Boolean = false
    var isCheckable: Boolean = false
    var isChecked: Boolean = false
    var isEnabled: Boolean = false
    var isFocusable: Boolean = false
    var isLongClickable: Boolean = false
    var isScrollable: Boolean = false
    var isSelected: Boolean = false
    var isFocused: Boolean = false
    var isPassword: Boolean = false
    var bounds: RectModel = RectModel(0, 0, 0, 0)

    fun initByXml(element: Element): UiObjectModel {
        val uiObj = UiObjectModel()
        uiObj.actName = ActManager.currentActName
        val enabled = element.attr("enabled") == "true"
        val clickable = element.attr("clickable") == "true"
        val scrollable = element.attr("scrollable") == "true"
        val rString = element.attr("bounds").replace("[", ",")
                .replace("]", ",")
        val r = rString.trim { it <= ' ' }.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val left = Integer.parseInt(r[1])
        val top = Integer.parseInt(r[2])
        var right = Integer.parseInt(r[4])
        var bottom = Integer.parseInt(r[5])
        val rect = RectModel(left, top, right, bottom)
        uiObj.bounds = rect
        uiObj.isEnabled = enabled
        uiObj.isClickable = clickable
        uiObj.isScrollable = scrollable
        uiObj.className = element.attr("class")
        uiObj.text = element.attr("text")
        uiObj.id = element.attr("resource-id")
        uiObj.pkgName = element.attr("package")
        uiObj.contentDescript = element.attr("content-desc")
        uiObj.isCheckable = element.attr("checkable").equals("true")
        uiObj.isChecked = element.attr("checked").equals("true")
        uiObj.isFocusable = element.attr("focusable").equals("true")
        uiObj.isFocused = element.attr("focused").equals("true")
        uiObj.isLongClickable = element.attr("long-clickable").equals("true")
        uiObj.isPassword = element.attr("isPassword").equals("true")
        uiObj.isSelected = element.attr("selected").equals("true")
        return uiObj
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val myother = other as UiObjectModel?
        // TestReport.e(this.toString() + " equals(Object obj) "
        // + other.toString());

        if (!id!!.isEmpty() && !myother!!.id!!.isEmpty()) {
            if (id != myother.id) {
                return false
            }
        }
        if (!className!!.isEmpty() && !myother!!.className!!.isEmpty()) {
            if (className != myother.className) {
                return false
            }
        }

        if (!pkgName!!.isEmpty() && !myother!!.pkgName!!.isEmpty()) {
            if (pkgName != myother.pkgName) {
                return false
            }
        }
        if (!contentDescript!!.isEmpty() && !myother!!.contentDescript!!.isEmpty()) {
            // webview输入框的内容会体现为contentDescript，输入文字后该内容会变化，实际上为同一控件，故不能用不等来判断
            if (contentDescript == myother.contentDescript) {
                return true
            }
        }
        if (!text!!.isEmpty() && !myother!!.text!!.isEmpty()) {
            // 有些控件点击后显示的文字状态会变化，导致text不一样，实际是一样的
            if (text == myother.text) {
                return true
            }
        }
        return bounds == myother!!.bounds
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1

        result = prime * result + (id?.hashCode() ?: 0)
        result = prime * result + (className?.hashCode() ?: 0)
        result = prime * result + (contentDescript?.hashCode() ?: 0)
        result = prime * result + (pkgName?.hashCode() ?: 0)
        result = prime * result + (text?.hashCode() ?: 0)
        result = prime * result + if (isClickable == false) 0 else 1
        result = prime * result + if (isCheckable == false) 0 else 1
        result = prime * result + if (isChecked == false) 0 else 1
        result = prime * result + if (isFocusable == false) 0 else 1
        result = prime * result + if (isLongClickable == false) 0 else 1
        result = prime * result + if (isScrollable == false) 0 else 1
        result = prime * result + if (isSelected == false) 0 else 1
        result = prime * result + if (isFocused == false) 0 else 1
        result = prime * result + if (isPassword == false) 0 else 1
        result = (prime * result + bounds.left + bounds.top + bounds.right
                + bounds.bottom)

        return result
    }

    override fun toString(): String {
        return "$className $bounds "
    }
}