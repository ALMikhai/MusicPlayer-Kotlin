package Pages.Main

import FolderReader
import Model
import Music
import Pages.Settings.SettingsStage
import javafx.scene.chart.XYChart
import javafx.scene.media.AudioSpectrumListener
import javafx.scene.media.MediaException
import javafx.scene.media.MediaPlayer
import javafx.util.Duration

class MainController : Model() {

    fun init(){
        mainBlock.children.add(tableViewMusic)

        for(i in 0..127){ // Spectrum preparation.
            spectrumData.data.add(XYChart.Data<String, Number>(i.toString(), 0))
        }
        spectrumBarChart.data.add(spectrumData)

        Thread(Runnable {
            while (true) {
                if ( mPlayer != null) {
                    try {
                        val currentTime = mPlayer?.currentTime!!.toSeconds()
                        val allTime = mPlayer?.stopTime!!.toSeconds()

                        musicTimer.text = "${(currentTime / 60).toInt()}.${(currentTime % 60).toInt()} / ${musicPlaying?.getDuration()}"
                        musicName.text = "${musicPlaying?.getName()}"
                        musicSlider.value = currentTime * 100.0 / allTime
                    }catch (e : MediaException){
                        mPlayer?.dispose()
                        mPlayer = null
                    }
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
        SettingsStage(this).showAndWait()
    }

    fun musicSliderClick(){
        updateSlider()
    }

    fun volumeSliderDragged(){
        mPlayer?.volume  = volumeSlider.value / 100
    }

    private fun addNewMusic(url:String) : Music{
        var music = Music(url)
        observableList.add(music)
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

    fun setMusicNow(){
        if(observableList.isEmpty()){
            return
        }

        if(musicSelected != null && (musicPlaying != musicSelected || musicPlaying == null)){
            mPlayer?.stop()
            mPlayer?.dispose()
            mPlayer = MediaPlayer(musicSelected?.getMedia())

            mPlayer?.audioSpectrumInterval = 0.005
            mPlayer?.audioSpectrumListener = AudioSpectrumListener { d, d2, magnitudes, phases -> // Spectrum listener.
                for(i in 0..127){
                    var newValue = (magnitudes[i].toDouble() - mPlayer?.audioSpectrumThreshold!!) * mPlayer?.volume!!
                    if(spectrumData.data[i].yValue.toDouble() < newValue) {
                        spectrumData.data[i].yValue = newValue
                    }
                    else {
                        spectrumData.data[i].yValue = spectrumData.data[i].yValue.toDouble() - 0.1
                    }
                }
            }

            musicSelected!!.setName("-> " + musicSelected!!.getName())
            if(musicPlaying != null) musicPlaying!!.setName(musicPlaying!!.getName().substringAfter(' '))
            musicPlaying = musicSelected
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

    fun pausePlayer(){
        mPlayer?.pause()
    }

    fun stopPlayer(){
        mPlayer?.stop()
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

    private fun updateSlider(){
        if(mPlayer != null){
            try{
                println(musicSlider.value)
                mPlayer!!.seek(Duration((mPlayer!!.stopTime.toMillis() / 100 * musicSlider.value)))
            }catch (e : MediaException){
                e.printStackTrace()
            }
        }
    }

    fun spectrumPageTurnOn(){
        mainBlock.children.clear()
        mainBlock.children.add(spectrumBarChart)
    }

    fun musicTableTurnOn(){
        mainBlock.children.clear()
        mainBlock.children.add(tableViewMusic)
    }
}
