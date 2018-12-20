package com.allsunny.zhihudailykotlin.http

import com.allsunny.zhihudailykotlin.api.ApiService
import com.allsunny.zhihudailykotlin.api.UriConstant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

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

                    client = OkHttpClient.Builder()
//                        .addInterceptor()
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