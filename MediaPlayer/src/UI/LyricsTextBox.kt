package src.UI

import Music
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.text.Font
import java.net.URL

class Sender() {
    companion object {
        fun send(url : String) : String {
            return try {
                URL(url).openStream().bufferedReader().use{ it.readText() }
            } catch (e : Exception) {
                "not correct url"
            }
        }
    }
}

class LyricsTextBox() {
    companion object{
        public fun init(music: Music) : TextArea {
            var json = Sender.send("https://orion.apiseeds.com/api/music/lyric/${music.getMetaData().Author}/${music.getMetaData().Title}?apikey=Ohh8O9q1szkWVKNuvoYzjAn1UYYdN8qJdUE7GBpxDIW4WxVsadS35TbNKRkv0fY1 ")
            var text = json.substringAfter("\"text\":\"").substringBefore("\",\"lang\"").replace("\\n", "\n")
            var textArea = TextArea(text)
            textArea.isEditable = false
            textArea.font = Font("Arial", 20.0)
            return textArea
        }
    }
}