package com.allsunny.zhihudailykotlin.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.allsunny.zhihudailykotlin.ZhihuDailyApplication

/**
 * Author: allsunny
 * Created on 2018/12/18 14:58
 * Email: 623368886@qq.com
 * Desc:
 */

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initView()
        initData()
    }


    /**
     *  加载布局
     */
    abstract fun layoutId(): Int


    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * 初始化数据
     */
    abstract fun initData()


    override fun onDestroy() {
        super.onDestroy()
        ZhihuDailyApplication.getRefWatcher(this)?.watch(this)
    }

}