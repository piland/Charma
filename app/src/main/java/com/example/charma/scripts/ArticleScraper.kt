package com.example.charma.scripts

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

object ArticleScraper {
    fun scrapeArticles(): List<Article> {
        val articles = mutableListOf<Article>()
        try {
            val doc: Document = Jsoup.connect("https://inside.charlotte.edu/news-features/").get()
            val articleElements: Elements = doc.select(".card-item-content.card-match-height")

            for (element: Element in articleElements) {
                val titleElement = element.selectFirst(".card-item-title.title a")
                val title = titleElement?.text() ?: ""
                val url = titleElement?.attr("href") ?: ""

                val dateElement = element.selectFirst(".card-item-subtitle.postdate")
                val date = dateElement?.text() ?: ""

                val excerptElement = element.selectFirst(".card-item-body .excerpt p")
                val excerpt = excerptElement?.text() ?: ""

                articles.add(Article(title, date, excerpt, url))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return articles
    }
}
