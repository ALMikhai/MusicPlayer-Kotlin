// 1)Добавить возможность открытия нескольких музыкальных файлов
// 2)Добавить возможность добавлять новые музыкальные файлы
// 3)Добавить компонент таблицы для отображения списка музыкальных файлов
// -1 столбец - номер трека
// -2 столбец - название файла
// -3 столбец - длина файла в секундах
// -По двойному клику на файл из списка должно начаться воспроизведение выбранного трека
// TODO -Трек который играет должен визуально отличаться в списке
// TODO 4)Добавить кнопки “Next” и “Prev” для переключения треков
// TODO 5)Добавить возможность перемотки треков, нажатием на слайдер
// TODO 6)Добавить слайдер для изменения громкости
// TODO 7)Подгрузка музыки из папки

import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage

import java.io.File

class Music(path : String, list : ObservableList<Music>){
    private lateinit var Path : SimpleStringProperty
    private var musicMedia : Media = Media(path)
    private var mPlayer : MediaPlayer = MediaPlayer(musicMedia)
    private lateinit var Duration : SimpleStringProperty

    init {
        mPlayer.onReady = Runnable {
            var allTime : Double = mPlayer.stopTime.toSeconds()
            Path = SimpleStringProperty(path.replace("%20", " "))
            Duration = SimpleStringProperty((allTime / 60).toInt().toString() + "." + (allTime % 60).toInt().toString())

            list.add(this)
        }
    }

    fun getPath() : String{
        return Path.get()
    }

    fun getDuration() : String{
        return Duration.get()
    }

    fun getMedia() : Media{
        return musicMedia
    }
}

class MPlayer : Application() {
    internal var musicNow : Music? = null
    internal var listMusic : ObservableList<Music> = FXCollections.observableArrayList()
    internal var tableViewMusic : TableView<Music> = TableView(listMusic)

    internal var selectedFile: File? = null
    internal var mplayer: MediaPlayer? = null

    internal var musicTimer : Text = Text()
    internal var musicSlider: Slider = Slider()

    fun setMusicNow(){
        if(mplayer!!.media != musicNow?.getMedia()) {
            mplayer?.stop()
            mplayer = MediaPlayer(musicNow?.getMedia())
            musicSlider.min = 0.0;
            musicSlider.max = 100.0;
        }

        if(musicNow != null) mplayer?.play()
    }

    fun deleteMusicNow(){
        if(musicNow?.getMedia() == mplayer?.media) {
            mplayer?.stop()
            mplayer = null
            musicSlider.min = 0.0;
            musicSlider.max = 100.0;
        }

        listMusic.remove(musicNow)
    }

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = object : BorderPane() {
            init {
                val filenameLabel = Label("Media control")
                val fileChooser = FileChooser()
                fileChooser.title = "Open File"
                fileChooser.extensionFilters.addAll(
                    ExtensionFilter("Audio Files", "*.wav", "*.mp3")
                )

                val vbox = object : VBox() {
                    init {
                        children.add(filenameLabel)
                        val hbox = object : HBox() {
                            init {
                                val playButton = Button("Play")
                                val pauseButton = Button("Pause")

                                playButton.setOnAction { e ->
                                    setMusicNow()
                                }

                                pauseButton.setOnAction { e -> mplayer?.pause() }

                                val stopButton = object : Button("Stop") {
                                    init {
                                        setOnAction { e -> mplayer?.stop() }
                                    }
                                }

                                val deleteButton = object : Button("Delete"){
                                    init {
                                        setOnAction { e ->
                                            deleteMusicNow()
                                        }
                                    }
                                }
                                children.addAll(playButton, pauseButton, stopButton, deleteButton)
                            }
                        }
                        children.add(hbox)

                        val musicSelectionModel = tableViewMusic.selectionModel

                        musicSelectionModel.selectedItemProperty().addListener { changed, oldValue, newValue ->
                            musicNow = newValue
                            println(musicNow)
                        }

                        children.add(Label("Playlist"))

                        var pathColumn : TableColumn<Music, String> = TableColumn("Path")
                        pathColumn.minWidth = 350.0
                        pathColumn.cellValueFactory = PropertyValueFactory<Music, String>("Path")
                        tableViewMusic.columns.add(pathColumn)

                        var durationColumn : TableColumn<Music, String> = TableColumn("Duration")
                        durationColumn.cellValueFactory = PropertyValueFactory<Music, String>("Duration")
                        tableViewMusic.columns.add(durationColumn)

                        tableViewMusic.setOnMouseClicked {e ->
                            if(e.clickCount == 2){
                                setMusicNow()
                            }
                        }

                        tableViewMusic.rowFactory

                        children.add(tableViewMusic)
                    }
                }
                center = vbox

                val menubar = object : MenuBar() {
                    init {
                        val menu = object : Menu("File") {
                            init {
                                val selectMenuItem = object : MenuItem("Add media") {
                                    init {
                                        setOnAction { e ->
                                            selectedFile = fileChooser.showOpenDialog(primaryStage)
                                            if (selectedFile != null) {
                                                var url = selectedFile!!.toURI()
                                                println(url.toString())
                                                Music(url.toString(), listMusic)
                                            }
                                        }
                                    }
                                }
                                val pauseMenuItem = MenuItem("Pause")
                                val playMenuItem = MenuItem("Play")
                                val stopMenuItem = MenuItem("Stop")

                                items.addAll(selectMenuItem, playMenuItem, pauseMenuItem, stopMenuItem)
                            }
                        }
                        menus.add(menu)
                    }
                }
                top = menubar

                val sliderBox = object : VBox() {
                    init {
                        children.add(musicTimer)
                        children.add(musicSlider)
                    }
                }

                bottom = sliderBox
            }
        }

        Thread(Runnable {
            while (true) {
                if (mplayer != null) {
                    val currentTime = mplayer?.currentTime!!.toSeconds()
                    val allTime =  mplayer?.stopTime!!.toSeconds()

                    var timeNow = currentTime * 100.0 / allTime

                    musicTimer.text = (currentTime / 60).toInt().toString() + "." + (currentTime % 60).toInt().toString() + " / " + (allTime / 60).toInt().toString() + "." + (allTime % 60).toInt().toString()

                    musicSlider.value = timeNow
                    println("Cur time " + timeNow)
                }
                try {
                    Thread.sleep(900)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()

        val scene = Scene(root, 500.0, 700.0)

        primaryStage.scene = scene
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(MPlayer::class.java)
        }
    }
}