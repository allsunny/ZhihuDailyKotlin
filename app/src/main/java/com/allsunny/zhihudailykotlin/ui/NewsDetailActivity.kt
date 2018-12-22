package com.allsunny.zhihudailykotlin.ui

import android.widget.ImageView
import com.allsunny.zhihudailykotlin.R
import com.allsunny.zhihudailykotlin.api.UriConstant.STORY_FORMAT
import com.allsunny.zhihudailykotlin.api.UriConstant.STORY_FORMAT_NIGHT
import com.allsunny.zhihudailykotlin.http.RetrofitManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

/**
 * Author: allsunny
 * Created on 2018/12/21 9:25
 * Email: 623368886@qq.com
 * Desc:
 */

class NewsDetailActivity : BaseActivity() {

    private lateinit var mIvHeader:ImageView

    override fun layoutId(): Int {
        return R.layout.activity_news_detail
    }

    override fun initView() {
        mIvHeader = findViewById(R.id.iv_header) as ImageView
        wv_news.settings.javaScriptEnabled = true

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
        collapsingToolbarLayout.title = "知乎Daily"
    }

    override fun initData() {
        val newsID = intent.getIntExtra("news_id", 9022909);
        getNewsDetail(newsID)
    }

    private fun getNewsDetail(newsID: Int) {
        RetrofitManager.service.getNewsDetail(newsID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newsDetailBean ->

                Glide.with(this)
                    .load(newsDetailBean.image)
                    .error(R.mipmap.ic_launcher)           //设置错误图片
                    .placeholder(R.mipmap.ic_launcher)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) //缓存
                    .into(mIvHeader)
                tv_title.setText(newsDetailBean.title)
                tv_source.setText(newsDetailBean.image_source)

                val mNewsBody = STORY_FORMAT + newsDetailBean.body + "</body></html>";
                wv_news.loadDataWithBaseURL("file:///android_asset/", mNewsBody, "text/html", "UTF-8", null)
            }, { throwable ->
                Logger.e(throwable.toString())
            })
    }

}