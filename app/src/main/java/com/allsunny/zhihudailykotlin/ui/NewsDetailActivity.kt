package com.allsunny.zhihudailykotlin.ui

import android.view.Menu
import android.view.MenuItem
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
import com.mob.wrappers.ShareSDKWrapper.share
import cn.sharesdk.onekeyshare.OnekeyShare



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
        mIvHeader = findViewById<ImageView>(R.id.iv_header)
        wv_news.settings.javaScriptEnabled = true

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
        collapsingToolbarLayout.title = "知乎Daily"
    }

    override fun initData() {
        val newsID = intent.getIntExtra("news_id", 9022909)
        getNewsDetail(newsID)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menu_action_share -> {
                showShare()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getNewsDetail(newsID: Int) {
        RetrofitManager.service.getNewsDetail(newsID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newsDetailBean ->

                Glide.with(this)
                    .load(newsDetailBean.image)
                    .error(R.mipmap.image_small_default)           //设置错误图片
                    .placeholder(R.mipmap.image_small_default)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) //缓存
                    .into(mIvHeader)
                tv_title.text = newsDetailBean.title
                tv_source.text = newsDetailBean.image_source

                val mNewsBody = STORY_FORMAT + newsDetailBean.body + "</body></html>"
                wv_news.loadDataWithBaseURL("file:///android_asset/", mNewsBody, "text/html", "UTF-8", null)

                shareTitle = newsDetailBean.title
                shareText = shareTitle +"(分享自@纯净小文App)"
                shareImageUrl = newsDetailBean.image
                shareUrl = newsDetailBean.share_url
            }, { throwable ->
                Logger.e(throwable.toString())
            })
    }

    private var shareTitle =""
    private var shareText =""
    private var shareImageUrl = ""
    private var shareUrl =""

    private fun showShare(){
        val oks = OnekeyShare()
        //关闭sso授权
        oks.disableSSOWhenAuthorize()

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(shareTitle)
        // titleUrl QQ和QQ空间跳转链接
//        oks.setTitleUrl("http://sharesdk.cn")
        // text是分享文本，所有平台都需要这个字段
        oks.text = shareText

        oks.setImageUrl(shareImageUrl)

        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(shareUrl)
        // 启动分享GUI
        oks.show(this)
    }

}