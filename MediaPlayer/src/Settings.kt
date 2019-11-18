package src

import com.google.gson.GsonBuilder
import javafx.beans.property.SimpleStringProperty
import java.io.File
import java.io.InputStream


class SettingsModel() {
    public var mainMusicDirectory : SimpleStringProperty = SimpleStringProperty()
    private var toSettingsPath : String = "Settings/Settings.json"

    fun save(){
        val outputStream = File(toSettingsPath)
        outputStream.printWriter().use { out ->
            var gsonBuilder = GsonBuilder()
            var gson = gsonBuilder.create()
            out.println(gson.toJson(this))
        }
    }
}