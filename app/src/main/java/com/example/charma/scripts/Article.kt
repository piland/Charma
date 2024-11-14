package com.example.charma.scripts

data class Article(
    val title: String,
    val date: String,
    val excerpt: String,
    val url: String
) {
    override fun toString(): String {
        return "{ \"title\":\"$title\", \"date\": \"$date\", \"excerpt\":\"$excerpt\", \"url\": \"$url\" }"
    }
}
