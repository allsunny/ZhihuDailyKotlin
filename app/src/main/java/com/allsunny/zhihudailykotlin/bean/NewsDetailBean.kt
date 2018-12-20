package com.allsunny.zhihudailykotlin.bean

/**
 * Author: Xavier
 * Created on 2018/12/20 10:50
 * Email: 1620101514@qq.com
 * Desc:
 */

data class NewsDetailBean(
    val body: String,
    val css: List<String>,
    val ga_prefix: String,
    val id: Int,
    val image: String,
    val image_source: String,
    val images: List<String>,
    val js: List<String>,
    val share_url: String,
    val title: String,
    val type: Int
)
