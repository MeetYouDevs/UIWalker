package com.autotools.uiwalker.manager

import com.autotools.uiwalker.listener.ScreenListener
import com.autotools.uiwalker.listener.TestTimeListener
import com.autotools.uiwalker.listener.UnexpectedUiListener


/**
 * 用于各类子线程的任务管理
 */

object TaskManager {
    // 任务类型
    enum class TaskType {
        TestTime, UnexpectedUi, All
    }

    fun startTask(taskType: TaskType) {
        when (taskType) {
            TaskManager.TaskType.TestTime -> TestTimeListener.startWatching()
            TaskManager.TaskType.UnexpectedUi -> {
                UnexpectedUiListener.startWatching()
                TestTimeListener.startWatching()
                ScreenListener.startWatching()

            }
            TaskManager.TaskType.All -> {
                TestTimeListener.startWatching()
                UnexpectedUiListener.startWatching()
                ScreenListener.startWatching()
            }
        }
    }

    fun stopTask(taskType: TaskType) {
        when (taskType) {
            TaskManager.TaskType.TestTime -> TestTimeListener.stopWatching()
            TaskManager.TaskType.UnexpectedUi -> {
                UnexpectedUiListener.stopWatching()
                TestTimeListener.stopWatching()
                ScreenListener.stopWatching()
            }
            TaskManager.TaskType.All -> {
                TestTimeListener.stopWatching()
                UnexpectedUiListener.stopWatching()
                ScreenListener.stopWatching()
            }
        }
    }
}
