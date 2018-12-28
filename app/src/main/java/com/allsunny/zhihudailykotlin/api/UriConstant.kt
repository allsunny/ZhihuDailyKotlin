package com.allsunny.zhihudailykotlin.api


object UriConstant {
    val BASE_URL = "https://news-at.zhihu.com/api/4/"
    val STORY_FORMAT =
        "<!doctype html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width,user-scalable=no\"><link href=\"news_qa.min.css\" rel=\"stylesheet\"></head><body className=\"%s\">"
    val STORY_LARGE_FONT = "$STORY_FORMAT<script src=\"large-font.js\"></script>"
    val STORY_FORMAT_NIGHT = "$STORY_FORMAT<script src=\"night.js\"></script>"

    val COLUMN_HOME = 0
    val COLUMN_BLIND_TALK = 2
    val COLUMN_BIG_MISTAKE = 29
    val COLUMN_LITTLE_THING = 35
}