import javafx.beans.property.SimpleStringProperty
import javafx.scene.media.Media
import src.Mp3Metadata

class Music(path: String){
    private var _path = path
    private var _metadata : Mp3Metadata
    private var _musicMedia : Media
    private var _name : SimpleStringProperty
    private var _duration : SimpleStringProperty

    init {
        _metadata = Mp3Metadata(_path.substringAfter('/').replace("%20", " "))
        _duration = SimpleStringProperty(_metadata.Duration)
        _musicMedia = Media(path)
        _name = SimpleStringProperty("${_metadata.Author} - ${_metadata.Title}")
    }

    fun getName() : String{
        return _name.get()
    }

    fun getMetaData() : Mp3Metadata{
        return _metadata;
    }

//    fun setName(name : String){
//        _name = SimpleStringProperty(name)
//    }

    fun getDuration() : String{
        return _duration.get()
    }

//    fun setDuration(dur : String){
//        _duration = SimpleStringProperty(dur)
//    }

    fun getPath() : String {
        return _path
    }

    fun getMedia() : Media{
        return _musicMedia
    }
}