package com.allsunny.zhihudailykotlin.ui

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.allsunny.zhihudailykotlin.R
import com.allsunny.zhihudailykotlin.bean.Story
import com.allsunny.zhihudailykotlin.http.RetrofitManager
import com.allsunny.zhihudailykotlin.utils.ToastUtil
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
import android.widget.Toast


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var newsListAdapter: BaseQuickAdapter<Story, BaseViewHolder>? = null
    private var newsListData: List<Story>? = null

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
    }

    override fun initData() {

        newsListAdapter = object : BaseQuickAdapter<Story, BaseViewHolder>(R.layout.item_story, newsListData) {
            override fun convert(helper: BaseViewHolder?, item: Story?) {
                helper!!.setText(R.id.tv_title, item!!.title)

                Glide.with(mContext)
                    .load(item.images[0])
                    .error(R.mipmap.ic_launcher)           //设置错误图片
                    .placeholder(R.mipmap.ic_launcher)     //设置占位图片
                    .dontAnimate()                           //解决Glide加载图片的变形问题
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) //缓存
                    .into(helper.getView(R.id.iv_story))

                helper.addOnClickListener(R.id.cv_item)

                helper.setOnClickListener(R.id.cv_item, View.OnClickListener {
                    ToastUtil.showToast(item!!.title)
                })

            }
        }

        rv_data.layoutManager = linearLayoutManager
        rv_data.adapter = newsListAdapter

        RetrofitManager.service.getNewsData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newsBean ->
                newsListData = newsBean.stories
                newsListAdapter!!.setNewData(newsListData)
            }, { throwable ->
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
                ToastUtil.showToast("Hello")
                return true
            }
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
