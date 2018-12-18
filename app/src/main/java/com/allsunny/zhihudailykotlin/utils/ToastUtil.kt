package com.allsunny.zhihudailykotlin.utils

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.widget.Toast
import com.allsunny.zhihudailykotlin.ZhihuDailyApplication


object ToastUtil {

    private var toast: Toast? = null

    fun showToast(msg: String) {
        showToast(ZhihuDailyApplication.context, msg, Toast.LENGTH_SHORT)
    }

    @JvmOverloads
    fun showToast(context: Context, msg: String, duration: Int = Toast.LENGTH_SHORT) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, duration)
        } else {
            toast!!.setText(msg)
        }
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.show()
    }

}
