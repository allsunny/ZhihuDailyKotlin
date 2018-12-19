package com.allsunny.zhihudailykotlin.ui

import com.allsunny.zhihudailykotlin.R
import kotlinx.android.synthetic.main.app_bar_main.*

/**
 * Author: allsunny
 * Created on 2018/12/18 14:58
 * Email: 623368886@qq.com
 * Desc:
 */

class AboutActivity : BaseActivity() {


    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

    }

    override fun initData() {
    }

    override fun layoutId(): Int {
        return R.layout.activity_about
    }


}