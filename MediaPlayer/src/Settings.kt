package src

import javafx.beans.property.SimpleStringProperty
import java.io.File
import java.io.InputStream

class SettingsModel {
    public lateinit var mainMusicDirectory : SimpleStringProperty
    private var toSettingsPath = "Settings/Settings.txt"

    init{
        load()
    }

    fun load(){
        val inputStream: InputStream = File(toSettingsPath).inputStream()
        val lineList = mutableListOf<String>()
        inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }

        mainMusicDirectory = SimpleStringProperty(lineList[0])

        inputStream.close()
    }

    fun save(){
        val outputStream = File(toSettingsPath)
        outputStream.printWriter().use { out ->
            out.println(mainMusicDirectory.get())
        }
    }
}