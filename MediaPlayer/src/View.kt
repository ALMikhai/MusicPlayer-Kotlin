import UI.MenuBarGeneration
import UI.PanelControlButtonsGeneration
import UI.SliderBoxGeneration
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.MediaException
import javafx.stage.FileChooser
import javafx.event.EventHandler

class View() : Model() {
    @Throws(Exception::class)
    fun start() {
        val root = object : BorderPane() {
            init {
                fileChooser.title = "Open File"
                fileChooser.extensionFilters.addAll(
                    FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3")
                )

                folderChooser.title = "Open Folder"

                val mainPage = object : VBox() {
                    init {
                        children.add(Label("Media control"))
                        children.add(PanelControlButtonsGeneration.init(this@View))
                        children.add(Label("Playlist"))
                        children.add(tableViewMusic)
                    }
                }
                center = mainPage
                top =  MenuBarGeneration.init(this@View)
                bottom = SliderBoxGeneration.init(this@View)

                onKeyReleased = controller.keyPressEvent
            }
        }

        Controller.installControl(this)

        Thread(Runnable {
            while (true) {
                if (mPlayer != null) {
                    try {
                        val currentTime = mPlayer?.currentTime!!.toSeconds()

                        var allTime = mPlayer?.stopTime!!.toSeconds()

                        var timeNow = currentTime * 100.0 / allTime

                        musicTimer.text =
                            (currentTime / 60).toInt().toString() + "." + (currentTime % 60).toInt().toString() + " / " + musicPlaying?.getDuration() + " " + musicPlaying?.getName()

                        musicSlider.value = timeNow
                        volumeSlider.value = mPlayer!!.volume
                        println("Cur time $timeNow")
                    }catch (e : MediaException){
                        mPlayer?.dispose()
                        mPlayer = null
                    }
                }else{
                    musicTimer.text = ""
                }

                try {
                    Thread.sleep(1000)
                    tableViewMusic.refresh()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start() // TODO вынести в отдельный класс(типо главный поток).

        val scene = Scene(root, windowWidth, windowHeight)
        primaryStage.scene = scene
        primaryStage.show()
    }
}