package com.autotools.uiwalker.event

import android.graphics.Point
import android.support.test.uiautomator.By
import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.utils.UiUtils
import com.autotools.uiwalker.utils.UiUtils.Companion.uiDevice


/**
 * 对应系统级的操作
 */

class SystemEvent : EventController(true, true) {
    private var left: Point
    private var right: Point
    private var top: Point
    private var bottom: Point

    init {
        val w = uiDevice.displayWidth
        val h = uiDevice.displayHeight
        left = Point((w * 0.2).toInt(), (h * 0.5).toInt())
        right = Point((w * 0.8).toInt(), (h * 0.5).toInt())
        top = Point((w * 0.5).toInt(), (h * 0.2).toInt())
        TestReport.i("SystemEvent_report:  --top($top)x,y")
        if(top.y < 90){
            top = Point(top.x, 90)
        }
        bottom = Point((w * 0.5).toInt(), (h * 0.8).toInt())
        TestReport.i("SystemEvent_report:  --top($top)x,y")
        TestReport.i("SystemEvent_report:  --left($left)")
        TestReport.i("SystemEvent_report:  --right($right)")
        TestReport.i("SystemEvent_report:  --bottom($bottom)")
    }

    fun pressBackXY_Event() {
        uiDevice.click(90, 90)
        report("【点击左上角(90,90) 返回坐标】")
    }

    /**
     * 长按menu键
     */
    fun longPressMenu_Event() {
//        LongpressEvent.longPressMenu()
        report("【长按MENU】")
    }

    /**
     * 短按menu键
     */
    fun pressMenu_Event() {
        uiDevice.pressMenu()
        report("【点击MENU】")
    }

    /**
     * 点击HOME键
     */
    fun pressHome_Event() {
        uiDevice.pressHome()
        report("【点击HOME】")
    }

    /**
     * 点击BACK键
     */
    fun pressBack_Event() {
        uiDevice.pressBack()
        report("【点击BACK】")

    }


    /**
     * 随机点击屏幕
     */
    fun random_click_Event() {
        val x = TestData.random.nextInt(uiDevice.getDisplayWidth())
        var y = TestData.random.nextInt(uiDevice.getDisplayHeight())
        //为了防止点到菜单栏【开发模式】,y<90的，设置成y=90
        if(y < 90){
            report("【随机点击屏幕y<90($x,$y),make y=90】")
            y = 90
        }
        uiDevice.click(x, y)
        report("【随机点击屏幕($x,$y)】")
    }

    fun swipeFromRight_Event() {
        uiDevice.swipe(right.x, right.y, left.x, left.y, 5)
        report("【整屏滑动←】")
    }

    /**
     * 过度遍历后响应事件
     */
    fun overTravelEvent() {
        uiDevice.pressBack()
        //兼容发布等界面返回后出现保存确定等弹窗
        try {
            val x = uiDevice.findObject(By.pkg(TestData.testPkg).text("不保存").clickable(true))
            if (x != null) {
                x.click()
                TestReport.i("SystemEvent_overTravelEvent:点击不保存")
                return
            }
            val y = uiDevice.findObject(By.pkg(TestData.testPkg).text("确定").clickable(true))
            if (y!= null) {
                y.click()
                TestReport.i("SystemEvent_overTravelEvent:点击确定")
                return
            }
            val sure = uiDevice.findObject(By.pkg(TestData.testPkg).text("确认").clickable(true))
            if (sure!= null) {
                sure.click()
                TestReport.i("SystemEvent_overTravelEvent:点击确认")
                return
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        TestReport.i("SystemEvent_overTravelEvent:点击返回上级界面")
        return

    }


    /**
     * 滑动
     */
    fun random_swipe_Event() {
        val r = TestData.random.nextInt(4)
        val step = 5
        when (r) {
            0 -> {
                uiDevice.swipe(left.x, left.y, right.x, right.y, step)
                report("【整屏滑动→】")
            }
            1 -> {
                uiDevice.swipe(right.x, right.y, left.x, left.y, step)
                report("【整屏滑动←】")
            }
            2 -> {
                uiDevice.swipe(top.x, top.y, bottom.x, bottom.y, step)
                report("【整屏滑动↓】")
            }
            3 -> {
                uiDevice.swipe(bottom.x, bottom.y, top.x, top.y, step)
                report("【整屏滑动↑】")
            }
        }
    }

    fun random_swipe_continuity() {
        val step = 20
        report("【连续整屏滑动↑】")
        uiDevice.swipe(bottom.x, bottom.y, top.x, top.y, step)
        report("【整屏滑动↑】")
        uiDevice.swipe(bottom.x, bottom.y, top.x, top.y, step)
        report("【整屏滑动↑】")
        uiDevice.swipe(bottom.x, bottom.y, top.x, top.y, step)
        report("【整屏滑动↑】")
        uiDevice.swipe(bottom.x, bottom.y, top.x, top.y, step)
        report("【整屏滑动↑】")
        uiDevice.swipe(bottom.x, bottom.y, top.x, top.y, step)
        report("【整屏滑动↑】")
        uiDevice.swipe(bottom.x, bottom.y, top.x, top.y, step)
        report("【整屏滑动↑】")

    }

    private fun injectRandom() {
        val r = TestData.random.nextInt(100)
        when (r) {
            in 0..5 -> pressBackXY_Event()
            in 6..9 -> pressHome_Event()
            in 10..19 -> pressBack_Event()
            in 20..29 -> pressMenu_Event()
            in 30..59 -> random_swipe_Event()
            in 60..89 -> random_swipe_continuity()
            in 90..100 -> random_click_Event()
        }

    }

    override fun trigger() {
        if (!this.isActive)
            return
        injectRandom()
    }

    /**
     * 在屏幕随机拖拽
     */
    fun drag_Event() {
        val xStart = TestData.random.nextInt(UiUtils.device_weight)
        val yStart = TestData.random.nextInt(UiUtils.device_height)

        val xEnd = TestData.random.nextInt(UiUtils.device_weight)
        val yEnd = TestData.random.nextInt(UiUtils.device_height)
        uiDevice.drag(xStart, yStart, xEnd, yEnd, 50)
    }

    override fun report(event: String) {

        if (this.isReport) {
            TestReport.i("SystemEvent_report:  ------- $event")
        }
        //需要加sleep时间，如果不sleep，事件流可能因处理不过来而被丢弃
        try {
            Thread.sleep(200)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }


}
