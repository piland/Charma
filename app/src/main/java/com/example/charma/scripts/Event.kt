package com.example.charma.scripts

data class Event(
    val title: String,
    val date: String,
    val location: String,
    val url: String
) {
    override fun toString(): String {
        return "{ \"title\":\"$title\", \"date\": \"$date\", \"location\":\"$location\", \"url\": \"$url\" }"
    }
}
