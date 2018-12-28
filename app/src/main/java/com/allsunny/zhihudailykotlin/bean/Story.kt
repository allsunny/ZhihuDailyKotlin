package com.allsunny.zhihudailykotlin.bean

/**
 * Author: Xavier
 * Created on 2018/12/28 10:37
 * Email: 1620101514@qq.com
 * Desc:
 */

data class Story(
    val ga_prefix: String,
    val id: Int,
    val images: List<String>,
    val title: String,
    val type: Int,
    val multipic: Boolean,

    val date: String,
    val display_date: String
)