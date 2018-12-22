package com.allsunny.zhihudailykotlin.api

import com.allsunny.zhihudailykotlin.bean.NewsBean
import com.allsunny.zhihudailykotlin.bean.NewsDetailBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Author: allsunny
 * Created on 2018/12/18 14:32
 * Email: 623368886@qq.com
 * Desc:
 */
interface ApiService {

    /**
     * 首页内容列表数据
     */
    @GET("news/latest")
    fun getNewsData(): Observable<NewsBean>


    @GET("news/before/{date}")
    fun getBeforeNews(@Path("date") date: String): Observable<NewsBean>

    @GET("news/{id}")
    fun getNewsDetail(@Path("id") id: Int): Observable<NewsDetailBean>

}
