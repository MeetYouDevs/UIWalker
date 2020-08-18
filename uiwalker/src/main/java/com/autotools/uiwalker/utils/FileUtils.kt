package com.autotools.uiwalker.utils


import android.util.Log
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.expection.UnexpectedUiException
import com.autotools.uiwalker.utils.UiUtils.Companion.context


import java.io.*
import java.text.SimpleDateFormat
import java.util.*


object FileUtils {

    private val sd = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())

    /**
     * 将asset文件，例如CopyAssets("/sdcard/xxx/", "xxx.txt");
     *
     * @param dir      目的路径
     * @param fileName 需要被复制的文件
     */
    fun copyAssets(dir: String, fileName: String) {
        val mWorkingPath = File(dir)
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {
                Log.e("--copyAssets--", "cannot create directory.")
            }
        }
        try {
            val context = context
            val `in` = context.resources.assets.open(fileName)
            val outFile = File(mWorkingPath, fileName)
            val out = FileOutputStream(outFile)
            // Transfer bytes from in to out
            val buf = ByteArray(1024)
            var len: Int = `in`.read(buf)
            while (len > 0) {
                out.write(buf, 0, len)
                len = `in`.read(buf)
            }
            `in`.close()
            out.close()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

    }

}
