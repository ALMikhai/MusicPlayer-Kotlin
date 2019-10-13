import javafx.beans.property.Property
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.scene.media.Media
import javafx.scene.media.MediaException
import javafx.scene.media.MediaPlayer

class Music(path: String){
    private var _path = path
    private lateinit var _musicMedia : Media
    private lateinit var _name : SimpleStringProperty
    private lateinit var _duration : SimpleStringProperty
    private var _isLoaded = false

    init {
        try {
            _duration = SimpleStringProperty("Loading...")
            _musicMedia = Media(path)
            _name = SimpleStringProperty(path.replace("%20", " ").substringAfterLast('/'))
        }catch (e: MediaException){
            e.printStackTrace()
        }
    }

    fun getName() : String{
        return _name.get()
    }

    fun getDuration() : String{
        return _duration.get()
    }

    fun setDuration(dur : String){
        _duration = SimpleStringProperty(dur)
    }

    fun getPath() : String {
        return _path
    }

    fun getMedia() : Media{
        return _musicMedia
    }
}