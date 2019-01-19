package com.allsunny.zhihudailykotlin.http

import com.allsunny.zhihudailykotlin.ZhihuDailyApplication
import com.allsunny.zhihudailykotlin.api.ApiService
import com.allsunny.zhihudailykotlin.api.UriConstant
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * Author: allsunny
 * Created on 2018/12/18 16:00
 * Email: 623368886@qq.com
 * Desc:
 */

object RetrofitManager {

    private var client: OkHttpClient? = null
    private var retrofit: Retrofit? = null


    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }

    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(RetrofitManager::class.java) {
                if (retrofit == null) {

                    // 指定缓存路径,缓存大小100Mb
                    val cache = Cache(
                        File(ZhihuDailyApplication.context.cacheDir, "HttpCache"),
                        (1024 * 1024 * 1000).toLong()
                    )

                    client = OkHttpClient.Builder()
                        .cache(cache)
                        .addInterceptor(CustomHttpLoggingInterceptor())
                        .build()

                    retrofit = Retrofit.Builder()
                        .baseUrl(UriConstant.BASE_URL)
                        .client(client!!)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
        }

        return retrofit
    }


}