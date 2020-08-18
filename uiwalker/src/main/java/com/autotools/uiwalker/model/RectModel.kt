package com.autotools.uiwalker.model

import android.graphics.Rect
import com.autotools.uiwalker.data.TestReport
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


import java.io.Serializable

@JacksonXmlRootElement(localName = "RectModel")
class RectModel : Serializable {
    var left: Int = 0
    var right: Int = 0
    var top: Int = 0
    var bottom: Int = 0
    var centerX: Int = 0
        internal set
    var centerY: Int = 0
        internal set
    /**
     * @return the width
     */
    var width: Int = 0
        internal set
    /**
     * @return the height
     */
    var height: Int = 0
        internal set

    constructor(left: Int, top: Int, right: Int, bottom: Int) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
        width = Math.abs(right - left)
        height = Math.abs(bottom - top)
        centerX = (left + right) / 2
        centerY = (top + bottom) / 2
    }

    constructor(rect: Rect) {
        left = rect.left
        top = rect.top
        right = rect.right
        bottom = rect.bottom
        width = Math.abs(right - left)
        height = Math.abs(bottom - top)
        centerX = (left + right) / 2
        centerY = (top + bottom) / 2
    }

    constructor(){}

    fun equals(rect: Rect): Boolean {
        TestReport.i(left.toString() + "-" + rect.left)
        TestReport.i(top.toString() + "-" + rect.top)
        TestReport.i(right.toString() + "-" + rect.right)
        TestReport.i(bottom.toString() + "-" + rect.bottom)
        return (left == rect.left && top == rect.top && right == rect.right
                && bottom == rect.bottom)
    }

    override fun toString(): String {
        return "[$left,$top] [$right,$bottom]"
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as RectModel?
        if (left != other!!.left)
            return false
        if (top != other.top)
            return false

        if (right != other.right)
            return false
        return if (bottom != other.bottom) false else true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + left + top + right + bottom
        return result
    }

    companion object {

        /**
         * 存放空间的四个边界点，以及控件的中心位置
         */
        private const val serialVersionUID = 1853880675108459689L
    }
}
