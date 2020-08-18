package com.autotools.uiwalker.event


import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.utils.CMDUtils
import com.autotools.uiwalker.utils.Utf7ImeHelper


/**
 * @author cxq 定义各种事件，如点击，输入，滑动
 */
internal object BaseEvents {

    /**
     * 点击指定坐标
     *
     * @param x 点击的x坐标
     * @param y 点击的y坐标
     */
    fun click(x: Int, y: Int) {
        var tmp_y = y
        //防止点到 黄栏开发者模式
        if(y < 90){
            tmp_y = 90
            TestReport.i("BaseEvents_click,$y<90, make y=90")
        }
        val command = "input tap $x $tmp_y"
        CMDUtils.runCmdByDevice(command, false)
    }


    /**
     * 长按指定坐标
     */
    fun longClick(x: Int, y: Int) {
        var tmp_y = y
        //防止点到 黄栏开发者模式
        if(y < 90){
            tmp_y = 90
            TestReport.i("BaseEvents_longClick,$y<90, make y=90")
        }
        val command = ("input swipe " + x + " " + tmp_y + " " + x + " " + tmp_y
                + " 1000")
        CMDUtils.runCmdByDevice(command, false)
    }


    /**
     * 输入指定内容
     */
    fun inputText(x: Int, y: Int, text: String) {
        click(x, y)
        val textString = Utf7ImeHelper.e(text)
        val command = "input text $textString"
        CMDUtils.runCmdByDevice(command, false)
    }


    /**
     * 滑动，耗时1s
     */
    fun swipe(startX: Float, startY: Float, endX: Float, endY: Float) {
        val command = ("input swipe " + startX + " " + startY + " " + endX
                + " " + endY + " 1000")
        CMDUtils.runCmdByDevice(command, false)
    }


}
