package Pages.Main

import FolderReader
import Model
import Music
import Pages.Settings.SettingsStage
import javafx.beans.Observable
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.event.EventHandler
import javafx.scene.chart.XYChart
import javafx.scene.media.AudioSpectrumListener
import javafx.scene.media.Media
import javafx.scene.media.MediaException
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import org.eclipse.fx.ui.controls.filesystem.DirectoryTreeView
import org.eclipse.fx.ui.controls.filesystem.ResourceItem
import java.nio.file.Paths
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.stage.Stage
import org.eclipse.fx.ui.controls.filesystem.ResourcePreview
import org.eclipse.fx.ui.controls.filesystem.IconSize
import org.eclipse.fx.ui.controls.filesystem.DirectoryView

class MainController : Model() {

    fun init(){
        musicTableTurnOn()

        for(i in 0 until numOfBars){ // Spectrum preparation. (Вынести отдельно)
            spectrumData.data.add(XYChart.Data<String, Number>(i.toString(), 0))
        }
        spectrumBarChart.data.add(spectrumData)

        Thread(Runnable {
            while (true) {
                if (player.isInitialized()) {
                    val currentTime = player.getCurrentTime().toSeconds()
                    val allTime = player.getEndTime().toSeconds()

                    musicTimer.text = "${(currentTime / 60).toInt()}.${(currentTime % 60).toInt()} / ${player.playingMusic.getDuration()}"
                    musicName.text = "${player.playingMusic.getName()}"
                    musicSlider.value = currentTime * 100.0 / allTime
                }else{
                    musicName.text = "Music name..."
                    musicTimer.text = "Timer..."
                    musicSlider.value = 0.0
                }

                try {
                    Thread.sleep(1000)
                    tableViewMusic.refresh()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()

        player.mediaPlayerSetUp = {mediaPlayer ->
            volumeSliderDragged()

            mediaPlayer.audioSpectrumInterval = 0.02
            mediaPlayer.audioSpectrumNumBands = numOfBars
            mediaPlayer.audioSpectrumListener = AudioSpectrumListener { d, d2, magnitudes, phases -> // Spectrum listener.
                for(i in 0 until numOfBars){
                    var newValue = (magnitudes[i].toDouble() - mediaPlayer.audioSpectrumThreshold) * mediaPlayer.volume
                    if(spectrumData.data[i].yValue.toDouble() < newValue) {
                        spectrumData.data[i].yValue = newValue
                    }
                    else {
                        spectrumData.data[i].yValue = spectrumData.data[i].yValue.toDouble() - 0.4
                    }
                }

                spectrumBarChart.lookupAll(".default-color0.chart-bar").forEachIndexed { i, node ->
                    node.style =
                        if(i < numOfBars)
                            "-fx-bar-fill: rgb(${(spectrumData.data[i].yValue.toInt() * 255) / 60}, 0, 255);"
                        else
                            ""
                }
            }

            mediaPlayer.onEndOfMedia = Runnable {
                player.setNextMusic()
            }
        }
    }

    fun addNewFile() {
        selectedFile = fileChooser.showOpenDialog(primaryStage)
        if (selectedFile != null) {
            var url = selectedFile!!.toURI()
            println(url.toString())
            var listNewMusic : ArrayList<Music> = arrayListOf()
            listNewMusic.add(addNewMusic(url.toString()))
            checkMusicDuration(listNewMusic)
        }
    }

    fun addNewFolder() {
        selectedFile = folderChooser.showDialog(primaryStage)
        Thread {
            if (selectedFile != null) {
                var uri = selectedFile!!.toURI().toString()
                var paths = FolderReader(uri.substringAfter('/').replace("%20", " ")).read()
                var listNewMusic: ArrayList<Music> = arrayListOf()

                fromPathsToMusics(uri, paths, listNewMusic)
                checkMusicDuration(listNewMusic)
            }
        }.start()
    }

    fun settingsShow(){
        SettingsStage(settings).showAndWait()
    }

    fun musicSliderClick(){
        player.seek(Duration((player.getEndTime().toMillis() / 100) * musicSlider.value))
    }

    fun volumeSliderDragged(){
        player.setVolume(volumeSlider.value / 100)
    }

    private fun addNewMusic(url:String) : Music{
        var music = Music(url)
        player.addNewMusic(music)
        return music
    }

    private fun checkMusicDuration(musics: ArrayList<Music>){
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

    private fun fromPathsToMusics(uri : String, musicsPaths : ArrayList<String>, listNewMusic: ArrayList<Music>){
        if(musicsPaths.isEmpty()){
            return
        }

        var path = musicsPaths.first()
        var expansion =  path.substringAfterLast('.')
        var newMusic : Music
        if(expansion == "mp3" || expansion == "wav") {
            newMusic = addNewMusic((uri + path.substringAfterLast('\\')).replace(" ", "%20"))
            listNewMusic.add(newMusic)
        }
        musicsPaths.remove(path)
        fromPathsToMusics(uri, musicsPaths, listNewMusic)
    }

    fun setSelectedMusic(){
        player.setSelectedMusic()
    }

    fun setNextMusic(){
        player.setNextMusic()
    }

    fun setPrevMusic(){
        player.setPrevMusic()
    }

    fun pausePlayer(){
        player.pause()
    }

    fun stopPlayer(){
        player.stop()
    }

//    fun deleteMusicNow(){
//        if(mPlayer?.media == musicSelected?.getMedia()) {
//            mPlayer?.stop()
//            mPlayer?.dispose()
//            mPlayer = null
//            musicSlider.value = 0.0
//        }
//
//        observableList.remove(musicSelected)
//    }

    fun spectrumPageTurnOn(){
        mainBlock.children.clear()
        mainBlock.center = spectrumBarChart;
    }

    fun musicTableTurnOn(){
        mainBlock.children.clear()
        mainBlock.center = tableViewMusic;
    }

    fun directoryTreeTurnOn(){
        var rootDirItem = ResourceItem.createObservedPath(
            Paths.get(settings.mainMusicDirectory.get())
        )

        val tv = DirectoryTreeView()
        tv.iconSize = IconSize.SMALL
        tv.rootDirectories = FXCollections.observableArrayList(rootDirItem)

        val v = DirectoryView()
        v.iconSize = IconSize.SMALL

        tv.selectedItems.addListener { o: Observable ->
            if (!tv.selectedItems.isEmpty()) {
                v.dir = tv.selectedItems[0]
            } else {
                v.dir = null
            }
        }

        v.selectedItems.addListener { change: ListChangeListener.Change<out ResourceItem>? ->
            if(v.selectedItems.isNotEmpty()){
                println("${v.selectedItems[0].uri}")
            }
        }

        val p = SplitPane(tv, v)
        p.setDividerPositions(0.3, 0.8)

        mainBlock.children.clear()
        mainBlock.center = p;
    }
}
