package com.allsunny.zhihudailykotlin.ui

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.allsunny.zhihudailykotlin.R
import com.allsunny.zhihudailykotlin.bean.Story
import com.allsunny.zhihudailykotlin.http.RetrofitManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.allsunny.zhihudailykotlin.api.UriConstant
import com.allsunny.zhihudailykotlin.bean.SectionNewsBean
import com.allsunny.zhihudailykotlin.bean.NewsBean
import com.allsunny.zhihudailykotlin.utils.ToastUtil


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var newsListAdapter: BaseQuickAdapter<Story, BaseViewHolder>? = null
    private var newsListData: List<Story>? = null
    private lateinit var currentDate: String

    private var column: Int = 0

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val view = nav_view.getHeaderView(0)
        val intent = Intent(this, AboutActivity::class.java)
        view.findViewById<View>(R.id.constraintLayout).setOnClickListener {
            startActivity(intent)
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        swr_refresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swr_refresh.setOnRefreshListener {
            if (swr_refresh.isRefreshing) {
                getLastNews()
            }
        }
    }

    override fun initData() {

        newsListAdapter = object : BaseQuickAdapter<Story, BaseViewHolder>(R.layout.item_story, newsListData) {
            override fun convert(helper: BaseViewHolder?, item: Story?) {
                helper!!.setText(R.id.tv_title, item!!.title)

                Glide.with(mContext)
                        .load(item.images[0])
                        .error(R.mipmap.image_small_default)           //设置错误图片
                        .placeholder(R.mipmap.image_small_default)     //设置占位图片
//                    .dontAnimate()                           //解决Glide加载图片的变形问题
                        .diskCacheStrategy(DiskCacheStrategy.RESULT) //缓存
                        .into(helper.getView(R.id.iv_story))

                helper.addOnClickListener(R.id.cv_item)

                if (item.multipic) {
                    helper.setVisible(R.id.iv_multi_pic, true)
                } else {
                    helper.setVisible(R.id.iv_multi_pic, false)
                }

                helper.setOnClickListener(R.id.cv_item, View.OnClickListener {
                    val intent = Intent(mContext, NewsDetailActivity::class.java)
                    intent.putExtra("news_id", item.id)
                    startActivity(intent)
                })

            }
        }
        newsListAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
        rv_data.layoutManager = linearLayoutManager
        rv_data.adapter = newsListAdapter
        /**
         * object:实现匿名内部类
         */
        rv_data.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = linearLayoutManager.itemCount
                val lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                if (lastVisibleItem == totalItemCount - 1 && dy > 0) {
                    when {
                        column == UriConstant.COLUMN_HOME ->
                            getBeforeNews(currentDate)
                        column == UriConstant.COLUMN_BLIND_TALK ->
                            getSectionBeforeNews(currentDate)
                        column == UriConstant.COLUMN_BIG_MISTAKE ->
                            getSectionBeforeNews(currentDate)
                        column == UriConstant.COLUMN_LITTLE_THING ->
                            getSectionBeforeNews(currentDate)
                    }
                }

            }
        })

        getLastNews()
    }

    private fun getLastNews() {
        RetrofitManager.service.getNewsData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ newsBean: NewsBean? ->
                    newsListData = newsBean?.stories
                    newsListAdapter?.setNewData(newsListData)
                    currentDate = newsBean?.date.toString()
                    if (newsListData!!.size < 8) {
                        getBeforeNews(currentDate)
                    }
                    if (swr_refresh.isRefreshing) {
                        swr_refresh.isRefreshing = false
                    }
                }, { throwable: Throwable? ->
                    if (swr_refresh.isRefreshing) {
                        swr_refresh.isRefreshing = false
                    }
                    Logger.e(throwable.toString())
                })
    }

    private fun getBeforeNews(date: String) {
        RetrofitManager.service.getBeforeNews(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ newsBean: NewsBean? ->
                    newsListAdapter?.addData(newsBean!!.stories)
                    currentDate = newsBean?.date.toString()
                }, { throwable: Throwable? ->
                    Logger.e(throwable.toString())
                })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_change_style -> {
                setTheme(R.style.NightTheme)
                return true
            }
            R.id.action_settings -> return true
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                column = UriConstant.COLUMN_HOME
                getLastNews()
                swr_refresh.isEnabled = true
                toolbar.title = "首页"
            }
            R.id.nav_blind_talk -> {
                column = UriConstant.COLUMN_BLIND_TALK
                getSectionNews()
                swr_refresh.isEnabled = false
                toolbar.title = "瞎扯"

            }
            R.id.nav_big_mistake -> {
                column = UriConstant.COLUMN_BIG_MISTAKE
                getSectionNews()
                swr_refresh.isEnabled = false
                toolbar.title = "大误"

            }
            R.id.nav_little_thing -> {
                column = UriConstant.COLUMN_LITTLE_THING
                getSectionNews()
                swr_refresh.isEnabled = false
                toolbar.title = "小事"

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getSectionNews() {
        RetrofitManager.service.getSectionNews(column)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bean: SectionNewsBean? ->
                    newsListData = bean?.stories
                    newsListAdapter?.setNewData(newsListData)
                    currentDate = bean!!.timestamp
                    if (newsListData!!.size < 8) {
                        getSectionBeforeNews(currentDate)
                    }
                    if (swr_refresh.isRefreshing) {
                        swr_refresh.isRefreshing = false
                    }
                }, { throwable: Throwable? ->
                    if (swr_refresh.isRefreshing) {
                        swr_refresh.isRefreshing = false
                    }
                    Logger.e(throwable.toString())
                })
    }

    private fun getSectionBeforeNews(timestamp: String) {
        RetrofitManager.service.getSectionBeforeNews(column, timestamp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { newsBean: SectionNewsBean? ->
                            newsListAdapter?.addData(newsBean!!.stories)
                            currentDate = newsBean!!.timestamp
                        }, { throwable: Throwable? ->
                    Logger.e(throwable.toString())
                })
    }

    private var firstTime: Long = 0L
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                ToastUtil.showToast("再按一次退出")
                firstTime = System.currentTimeMillis()
            } else {
                finish()
                System.exit(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
