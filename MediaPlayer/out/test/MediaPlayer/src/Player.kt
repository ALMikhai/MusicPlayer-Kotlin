package src

import Music
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.time.Duration
import kotlin.time.measureTime

class Player {
    public lateinit var player : MediaPlayer
    public val playList : ObservableList<Music> = FXCollections.observableArrayList()
    public lateinit var selectedMusic : Music
    public lateinit var playingMusic : Music

    public var mediaPlayerSetUp = {mediaPlayer : MediaPlayer ->} // When created new media player, doing this set up.

    public fun setVolume(n : Double){ // Player.
        player.volume = n
    }

    public fun getMusicNow() : Music {
        if (::playingMusic.isInitialized) {
            return playingMusic
        } else {
            return Music("")
        }
    }

    public fun setSelectedMusic(){
        if(playList.isEmpty()){
            return
        }
        if(::player.isInitialized){
            if (player.media != selectedMusic.getMedia()) {
                player.stop()
                player.dispose()
                player = MediaPlayer(selectedMusic.getMedia())
                mediaPlayerSetUp(player)
            }
        }else{
            player = MediaPlayer(selectedMusic.getMedia())
            mediaPlayerSetUp(player)
        }
        playingMusic = selectedMusic
        play()
    }

    public fun setNextMusic(){
        selectNext()
        setSelectedMusic()
        play()
    }

    public fun setPrevMusic(){
        selectPrev()
        setSelectedMusic()
        play()
    }

    public fun play(){
        if(::player.isInitialized){
            player.play()
        }
    }

    public fun pause(){
        if(::player.isInitialized){
            player.pause()
        }
    }

    public fun stop(){
        if(::player.isInitialized){
            player.stop()
        }
    }

    public fun seek(duration: javafx.util.Duration){
        if(::player.isInitialized){
            player.seek(duration)
        }
    }

    public fun isInitialized() : Boolean{
        return (::player.isInitialized)
    }

    public fun getEndTime() : javafx.util.Duration {
        return player.stopTime
    }
    public fun getCurrentTime() : javafx.util.Duration {
        return player.currentTime
    } // Player.

    public fun addNewMusic(music: Music){ // Playlist.
        playList.add(music)
    }

    public fun selectNext(){
        if(!::playingMusic.isInitialized) return
        selectedMusic = playList[(playList.indexOf(playingMusic) + 1) % playList.size]
    }

    public fun selectPrev(){
        if(!::playingMusic.isInitialized) return

        var index = playList.indexOf(playingMusic) - 1

        index = if (index < 0)
            playList.size - 1
        else
            index

        selectedMusic = playList[index]
    } // PlayList.
}