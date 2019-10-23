import UI.SlidersGeneration
import UI.MusicTableGeneration
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.media.MediaException
import javafx.scene.media.MediaPlayer
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.util.Duration
import java.io.File

open class Model() {
    internal var primaryStage = Stage()
    internal var controller = Controller(this, primaryStage)

    internal var musicPlaying : Music? = null
    internal var musicSelected : Music? = null
    internal var observableList : ObservableList<Music> = FXCollections.observableArrayList()
    internal var selectedFile: File? = null
    internal var mPlayer: MediaPlayer? = null
    private var checkDurationMediaPlayer : MediaPlayer? = null

    val fileChooser = FileChooser()
    val folderChooser = DirectoryChooser()

    internal var windowHeight = 800.0
    internal var windowWidth = 600.0
    internal var tableViewMusic : TableView<Music> = MusicTableGeneration.init(observableList)
    internal var musicTimer : Text = Text()
    internal var musicSlider = SlidersGeneration.musicSlider(this)
    internal var volumeSlider = SlidersGeneration.volumeSlider(this)

    fun addNewMusic(url:String) : Music{
        var music = Music(url)
        observableList.add(music)
        return music
    }

    fun checkMusicDuration(musics: ArrayList<Music>){
        if(musics.isNotEmpty()){
            var music = musics.first()
            checkDurationMediaPlayer = MediaPlayer(music.getMedia())

            checkDurationMediaPlayer!!.onReady = Runnable {
                var duration = checkDurationMediaPlayer!!.stopTime.toSeconds()
                music.setDuration((duration / 60).toInt().toString() + '.' + (duration % 60).toInt().toString())
                musics.remove(music)
                checkDurationMediaPlayer!!.dispose()
                checkMusicDuration(musics)
            }
        }
    }

    fun setMusicNow(){
        if(observableList.isEmpty()){
            return
        }

        if(musicSelected != null && (musicPlaying != musicSelected || musicPlaying == null)){
            mPlayer?.stop()
            mPlayer?.dispose()
            mPlayer = MediaPlayer(musicSelected?.getMedia())
            musicSelected!!.setName("-> " + musicSelected!!.getName())
            if(musicPlaying != null) musicPlaying!!.setName(musicPlaying!!.getName().substringAfter(' '))
            musicPlaying = musicSelected
            musicSlider.value = 0.0
        }

        mPlayer?.play()
    }

    fun setNextMusic(){
        if(mPlayer == null) return
        musicSelected = observableList[(observableList.indexOf(musicPlaying) + 1) % observableList.count()]
        setMusicNow()
    }

    fun setPrevMusic(){
        if(mPlayer == null) return
        musicSelected = if(observableList.indexOf(musicPlaying) == 0)
            observableList[observableList.count() - 1]
        else
            observableList[(observableList.indexOf(musicPlaying) - 1)]

        setMusicNow()
    }

    fun deleteMusicNow(){
        if(mPlayer?.media == musicSelected?.getMedia()) {
            mPlayer?.stop()
            mPlayer?.dispose()
            mPlayer = null
            musicSlider.value = 0.0
        }

        observableList.remove(musicSelected)
    }

    fun updateSlider(){
        if(mPlayer != null){
            try{
                println(musicSlider.value)
                mPlayer!!.seek(Duration((mPlayer!!.stopTime.toMillis() / 100 * musicSlider.value)))
            }catch (e : MediaException){
                e.printStackTrace()
            }
        }
    }

    companion object {}
}