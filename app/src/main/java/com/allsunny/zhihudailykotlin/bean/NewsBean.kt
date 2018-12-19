package com.allsunny.zhihudailykotlin.bean

/**
 * Author: allsunny
 * Created on 2018/12/18 15:56
 * Email: 623368886@qq.com
 * Desc:
 */
 
data class NewsBean(
    val date: String,
    val stories: List<Story>,
    val top_stories: List<TopStory>
)

data class Story(
    val ga_prefix: String,
    val id: Int,
    val images: List<String>,
    val title: String,
    val type: Int
)

data class TopStory(
    val ga_prefix: String,
    val id: Int,
    val image: String,
    val title: String,
    val type: Int
)