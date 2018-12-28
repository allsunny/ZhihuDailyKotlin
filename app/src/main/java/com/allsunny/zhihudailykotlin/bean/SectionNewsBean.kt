package com.allsunny.zhihudailykotlin.bean

/**
 * Author: allsunny
 * Created on 2018/12/28 10:28
 * Email: 623368886@qq.com
 * Desc:
 */

data class SectionNewsBean(
    val name: String,
    val stories: List<Story>,
    val timestamp: String
)

