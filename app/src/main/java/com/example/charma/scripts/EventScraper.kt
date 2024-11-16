package com.example.charma.scripts

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

object EventScraper {
    fun scrapeEvents(): List<Event> {
        val events = mutableListOf<Event>()
        try {
            // Connect to the webpage and parse the document
            val doc: Document = Jsoup.connect("https://campusevents.charlotte.edu/calendar").get()

            // Select all event cards using the base class
            val eventElements: Elements = doc.select("div[class^=em-card]")

            for (element: Element in eventElements) {
                // Extract the event URL from .em-card_image > a
                val imageElement = element.selectFirst(".em-card_image a")
                val url = imageElement?.attr("href").orEmpty()

                // Extract the event title from .em-card_title > a
                val titleElement = element.selectFirst(".em-card_title a")
                val title = titleElement?.text().orEmpty()

                // Extract the event date
                // Use the second occurrence of .em-card_event-text for the updated date format
                val dateElements = element.select(".em-card_event-text")
                val date = if (dateElements.size > 1) {
                    dateElements[1].text().substringBefore('|').trim()
                } else {
                    "No Date"
                }

                // Extract the event location from the last occurrence of .em-card_event-text > a
                val locationElement = element.select(".em-card_event-text a").last()
                val location = locationElement?.text().orEmpty()

                events.add(Event(title, date, location, url))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return events
    }
}
