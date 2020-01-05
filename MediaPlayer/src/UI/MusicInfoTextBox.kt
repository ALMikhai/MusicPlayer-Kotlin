package src.UI

import Music
import javafx.scene.control.TextArea
import javafx.scene.text.Font
import java.net.URL

class Sender() {
    companion object {
        fun send(url : String) : String {
            return try {
                URL(url).openStream().bufferedReader().use{ it.readText() }
            } catch (e : Exception) {
                "Not found."
            }
        }
    }
}

class MusicInfoTextBox() {
    companion object{
        public fun init(music: Music) : TextArea {
            var aboutAuthor = Sender.send("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=${music.getMetaData().Author}&api_key=2747adef6721be32886e85a913b2bbb6&format=json")
            var text = aboutAuthor.substringAfter("\"summary\":\"").substringBefore("\"}}}").replace("\\n", "\n")
            var lyrics = Sender.send("https://orion.apiseeds.com/api/music/lyric/${music.getMetaData().Author}/${music.getMetaData().Title}?apikey=Ohh8O9q1szkWVKNuvoYzjAn1UYYdN8qJdUE7GBpxDIW4WxVsadS35TbNKRkv0fY1 ")
            text += "\n\nLyrics\n\n" + lyrics.substringAfter("\"text\":\"").substringBefore("\",\"lang\"").replace("\\n", "\n")
            var textArea = TextArea(text)
            textArea.isEditable = false
            textArea.font = Font("Arial", 17.0)
            return textArea
        }
    }
}